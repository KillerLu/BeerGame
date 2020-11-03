package com.jnu.service;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnu.common.GameContext;
import com.jnu.common.ListAndCount;
import com.jnu.constant.GameConstant;
import com.jnu.dao.*;
import com.jnu.exception.ServiceException;
import com.jnu.handler.GameProcessHandler;
import com.jnu.util.CloneUtil;
import com.jnu.util.RoomNumUtil;
import com.jnu.util.SpringContextUtil;
import com.jnu.util.TokenUtil;
import com.jnu.view.*;
import com.jnu.vo.GameInformationVo;
import com.jnu.vo.GameStatisticsVo;
import com.jnu.vo.GameVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.sql.rowset.serial.SerialException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ：Killer
 * @date ：Created in 20-10-21 下午5:43
 * @description：${description}
 * @modified By：
 * @version: version
 */
@Service
public class GameService {
    
    @Autowired
    private GameDao gameDao;

    @Autowired
    private GameUserDao gameUserDao;

    @Autowired
    private GameRoundDao gameRoundDao;

    @Autowired
    private GameInformationDao gameInformationDao;

    @Autowired
    private GameConfigDao gameConfigDao;

    @Autowired
    private RoomNumUtil roomNumUtil;

    @Autowired
    private GameStatisticsDao gameStatisticsDao;

    public GameContext getGameContext(Long gameId) {
        GameContext context=new GameContext();
        //当前游戏信息
        context.setGame(gameDao.selectById(gameId));
        GameRound gameRound = gameRoundDao.getCurrentGameRound(gameId);
        //当前游戏的回合信息
        context.setGameRound(gameRound);
        //当前游戏的信息(当前用户的)
        if (gameRound != null) {
            //这里确保了一个用户一个回合只有一条信息
            GameInformation gameInformation = gameInformationDao.selectOne(new QueryWrapper<GameInformation>().eq("round_id", gameRound.getId()).eq("user_id", TokenUtil.getCurrentUserId()));
            context.setGameInformation(gameInformation);
        }
        //当前游戏的用户信息
        GameUser gameUser = getCurrentGameUser(gameId);
        context.setGameUser(gameUser);
        return context;
    }


    @Transactional
    public Game createGame() {
        //要先离开其它房间
        leaveOtherGame();
        Game game=addGame();
        addDefaultGameUser(game.getId());
        return game;
    }

    private void leaveOtherGame() {
        //实际上应该只有一个
        List<GameUser> gameUsers = gameUserDao.listUnEndGameUserByUserId(TokenUtil.getCurrentUserId());
        if (!CollectionUtils.isEmpty(gameUsers)) {
            for (GameUser gameUser : gameUsers) {
                //查询对应的游戏
                Game game = gameDao.selectById(gameUser.getGameId());
                if (game.getStatus() == GameConstant.RUNNING && StringUtils.equals(GameConstant.OB, gameUser.getUserRole())) {
                    throw new ServiceException("你无法退出正在进行中的游戏,gameId:" + game.getId() + ",房间号:" + game.getRoomNo());
                }
                //直接退出游戏
                gameUserDao.deleteById(gameUser);
            }
        }
    }

    public Game addGame() {
        Game game=new Game();
        //设置游戏状态
        game.setStatus(GameConstant.UNSTART);
        //设置ownerId
        game.setOwnerId(TokenUtil.getCurrentUserId());
        //设置房间号
        game.setRoomNo(roomNumUtil.getRoomNum());
        gameDao.insert(game);
        return game;
    }

    public GameUser addDefaultGameUser(Long gameId){
        //查询该用户是否已经加入游戏
        Integer selectCount = gameUserDao.selectCount(new QueryWrapper<GameUser>().eq("game_id", gameId).eq("user_id", TokenUtil.getCurrentUserId()));
        if (selectCount > 0) {
            throw new ServiceException("您已加入该游戏");
        }
        //增加游戏用户角色，创建者默认零售商角色
        GameUser gameUser=new GameUser();
        //游戏id
        gameUser.setGameId(gameId);
        //设置游戏角色(默认ob角色)
        gameUser.setUserRole(GameConstant.OB);
        //用户id
        gameUser.setUserId(TokenUtil.getCurrentUserId());
        gameUserDao.insert(gameUser);
        return gameUser;
    }

    private boolean checkGameUserReady(Long gameId){
        //查询该游戏所有的游戏用户
        List<GameUser> gameUsers = gameUserDao.selectList(new QueryWrapper<GameUser>().eq("game_id", gameId));
        List<String> existRoles = gameUsers.stream().map(GameUser::getUserRole).collect(Collectors.toList());
        return existRoles.contains(GameConstant.RETAILER) && existRoles.contains(GameConstant.WHOLESALER) && existRoles.contains(GameConstant.RESELLER) && existRoles.contains(GameConstant.MANUFACTURER);
    }

    @Transactional
    public void startGame(Long gameId){
        Game game = gameDao.selectById(gameId);
        if (game == null||game.getStatus()!=GameConstant.UNSTART) {
            throw new ServiceException("游戏不存在或状态不正确");
        }
        //查看是否四个角色都已经就位
        if (!checkGameUserReady(gameId)) {
            throw new ServiceException("角色没有完全就位，不能开始游戏");
        }
        //更新游戏状态
        game.setStatus(GameConstant.RUNNING);
        gameDao.updateById(game);
        nextRound(gameId);
    }


    /**
     * 游戏进入下一回合
     * @param id
     */
    public void nextRound(Long id) {
        Game game = gameDao.selectById(id);
        //新的游戏轮次
        GameRound newRound=null;
        //查询目前轮次
        GameRound currentGameRound = gameRoundDao.getCurrentGameRound(id);
        if (currentGameRound == null) {
            //如果目前轮次为空,则代表是新游戏,添加一条游戏回合记录
            newRound=new GameRound();
            newRound.setGameId(id);
            //当前回合为第一回合
            newRound.setRound(1);
            //回合一开始就默认进入零售商回合
            newRound.setProgress(GameConstant.RETAILER_TURN);
            addOrUpdateGameRound(newRound);
        }else{
            //判断是否达到最大轮次
            if (currentGameRound.getRound() >= gameConfigDao.getMaxRound()) {
                //已经达到最大轮次,则游戏结束
                game.setStatus(GameConstant.FINISH);
                addOrUpdateGame(game);
                //同时要回收房间号
                roomNumUtil.reUse(game.getRoomNo());
                //保存游戏统计信息
                saveGameStatistics(game.getId());
                return;
            }
            //否则要新增加一条游戏轮次记录
            newRound = CloneUtil.clone(game, GameRound.class);
            newRound.setRound(currentGameRound.getRound() + 1);
            newRound.setId(null);
            newRound.setProgress(GameConstant.RETAILER_TURN);
            newRound.setGameId(game.getId());
            addOrUpdateGameRound(newRound);
        }
        //查询该房间的所有玩家
        List<GameUser> gameUsers = listGameUserByGameId(id, false);
        //根据目前轮次创建对应的游戏信息
        if (!CollectionUtils.isEmpty(gameUsers)) {
            for (GameUser gameUser : gameUsers) {
                getGameProcessHandler(gameUser).initGameInformation(currentGameRound, newRound, gameUser);
            }
        }
    }

    private void saveGameStatistics(Long gameId) {
        //查询游戏用户
        List<GameUser> gameUsers = listGameUserByGameId(gameId, false);
        //查询该游戏所有的游戏信息
        List<GameInformation> gameInformations = listGameInformation(gameId, null);
        Map<Long, List<GameInformation>> gameInformationMap = gameInformations.stream().collect(Collectors.groupingBy(GameInformation::getUserId));
        //为每个用户保存统计信息
        if (!CollectionUtils.isEmpty(gameUsers)) {
            for (GameUser gameUser : gameUsers) {
                GameStatistics statistics=new GameStatistics();
                statistics.setGameId(gameId);
                statistics.setUserId(gameUser.getUserId());
                statistics.setUserRole(gameUser.getUserRole());
                Integer inventoryCost=0;
                Integer shortageCost=0;
                List<GameInformation> userGameInformations = gameInformationMap.get(gameUser.getUserId());
                if (!CollectionUtils.isEmpty(userGameInformations)) {
                    for (GameInformation userGameInformation : userGameInformations) {
                        //库存成本
                        inventoryCost=inventoryCost+userGameInformation.getStockNum();
                        //缺货成本
                        shortageCost=inventoryCost+3*userGameInformation.getShortSupplyNum();
                    }
                }
                statistics.setInventoryCost(inventoryCost);
                statistics.setShortageCost(shortageCost);
                statistics.setTotalCost(inventoryCost+shortageCost);
                gameStatisticsDao.insert(statistics);
            }
        }
    }


    public List<GameUser> listGameUserByGameId(Long gameId, boolean includeOb) {
        QueryWrapper<GameUser> wrapper = new QueryWrapper<GameUser>().eq("game_id", gameId);
        if (!includeOb) {
            wrapper.ne("user_role", GameConstant.OB);
        }
        return gameUserDao.selectList(wrapper);
    }


    /**
     * 根据某个回合关联查询对应的Game
     * @param roundId
     * @return
     */
    public Game getGameByRoundId(Long roundId) {
        return gameDao.getGameByRoundId(roundId);
    }

    /**
     * 发货
     */
    @Transactional
    public void sendGoods(Long id){
        GameContext context = getGameContext(id);
        GameProcessHandler gameProcessHandler = getGameProcessHandler(context.getGameUser());
        gameProcessHandler.sendGoods(context);
    }

    private GameProcessHandler getGameProcessHandler(GameUser gameUser) {
        Map<String, GameProcessHandler> handlerMap = SpringContextUtil.getApplicationContext().getBeansOfType(GameProcessHandler.class);
        for (Map.Entry<String, GameProcessHandler> entry : handlerMap.entrySet()) {
            if (entry.getValue().support(gameUser)) {
                return entry.getValue();
            }
        }
        throw new ServiceException("没有为" + gameUser.getUserRole() + "角色找到相关执行器");
    }

    /**
     * 订货
     */
    @Transactional
    public void orderGoods(Long id,Integer orderNum){
        GameContext context = getGameContext(id);
        GameProcessHandler gameProcessHandler = getGameProcessHandler(context.getGameUser());
        gameProcessHandler.orderGoods(context,orderNum);
    }


    public void addOrUpdateGame(Game game){
        if (game.getId() == null) {
            //id为空代表是新增
            gameDao.insert(game);
        }else{
            //先查询旧的
            Game oldGame = gameDao.selectById(game.getId());
            //赋予新的值
            CloneUtil.copyPropertiesIgnoreNull(game, oldGame);
            gameDao.updateById(oldGame);
        }
    }

    public void addOrUpdateGameInformation(GameInformation gameInformation){
        if (gameInformation.getId() == null) {
            //id为空代表是新增
            gameInformationDao.insert(gameInformation);
        }else{
            //先查询旧的
            GameInformation oldGameInformation = gameInformationDao.selectById(gameInformation.getId());
            //赋予新的值
            CloneUtil.copyPropertiesIgnoreNull(gameInformation, oldGameInformation);
            gameInformationDao.updateById(oldGameInformation);
        }
    }


    public void addOrUpdateGameRound(GameRound gameRound){
        if (gameRound.getId() == null) {
            //id为空代表是新增
            gameRoundDao.insert(gameRound);
        }else{
            //先查询旧的
            GameRound oldGameRound = gameRoundDao.selectById(gameRound.getId());
            //赋予新的值
            CloneUtil.copyPropertiesIgnoreNull(gameRound, oldGameRound);
            gameRoundDao.updateById(oldGameRound);
        }
    }

    @Transactional
    public GameUser joinGame(String roomNum) {
        //正在进行中是保证唯一的
        Game game=gameDao.selectOne(new QueryWrapper<Game>().eq("room_no", roomNum).ne("status", GameConstant.FINISH));
        if (game == null) {
            throw new ServiceException("房间不存在或者该游戏已结束");
        }
        //要先离开其它房间
        leaveOtherGame();
        //进去后默认是OB角色
        GameUser gameUser=addDefaultGameUser(game.getId());
        return gameUser;
    }

    @Transactional
    public synchronized void chooseGameRole(Long gameUserId, String userRole) {
        //查询该游戏角色
        GameUser gameUser = gameUserDao.selectById(gameUserId);
        if (gameUser == null) {
            throw new ServiceException("该游戏角色不存在");
        }
        //查询对应的游戏
        Game game = gameDao.selectById(gameUser.getGameId());
        if (game == null || game.getStatus() == GameConstant.FINISH) {
            throw new ServiceException("该游戏不存在或者已结束");
        }
        //查询对应的用户角色
        if (!StringUtils.equals(GameConstant.OB, userRole)) {
            Integer selectCount = gameUserDao.selectCount(new QueryWrapper<GameUser>().eq("user_role", userRole).eq("game_id", game.getId()));
            if (selectCount > 0) {
                throw new ServiceException("该游戏已经存在该角色了");
            }
        }
        gameUser.setUserRole(userRole);
        gameUserDao.updateById(gameUser);
    }


    public GameInformationVo getUserGameInformation(Long gameId) {
        //查询当前游戏
        Game game = gameDao.selectById(gameId);
        if (game == null || game.getStatus() == GameConstant.FINISH) {
            throw new ServiceException("当前游戏不存在或游戏已结束");
        }
        //判断当前game是否未开始
        if (game.getStatus() == GameConstant.UNSTART) {
            //未开始就没有游戏信息
            return null;
        }
        //查找当前游戏进行的回合数
        GameRound gameRound = gameRoundDao.getCurrentGameRound(gameId);
        //gameRound为空代表未开始
        if (gameRound == null) {
            return null;
        }
        //查询当前游戏用户
        GameUser gameUser = getCurrentGameUser(gameId);
        if (gameUser == null) {
            throw new ServiceException("该游戏用户不存在");
        }
        //查询当前游戏信息
        GameInformation gameInformation = gameInformationDao.selectOne(new QueryWrapper<GameInformation>().eq("round_id", gameRound.getId()).eq("user_id", TokenUtil.getCurrentUserId()));
        if (gameInformation == null) {
            return null;
        }
        GameInformationVo vo = CloneUtil.clone(gameInformation, GameInformationVo.class);
        vo.setGameId(gameId);
        vo.setRound(gameRound.getRound());
        vo.setUserRole(gameUser.getUserRole());
        return vo;
    }

    public GameVo getGameDetail(Long gameId) {
        Game game = gameDao.selectById(gameId);
        if (game == null) {
            throw new ServiceException("当前游戏不存在");
        }
        GameVo vo = CloneUtil.clone(game, GameVo.class);
        GameRound gameRound = gameRoundDao.getCurrentGameRound(gameId);
        vo.setCurrentRound(gameRound == null ? 0 : gameRound.getRound());
        return vo;
    }

    @Transactional
    public void leaveRoom(Long gameId) {
        Game game = gameDao.selectById(gameId);
        if (game == null) {
            throw new ServiceException("游戏不存在");
        }
        //查询该游戏用户
        GameUser gameUser = getCurrentGameUser(gameId);
        if (gameUser == null) {
            throw new ServiceException("当前游戏用户不存在");
        }
        if (game.getStatus() == GameConstant.RUNNING&&!StringUtils.equals(GameConstant.OB, gameUser.getUserRole())) {
            throw new ServiceException("游戏正在进行中，玩家无法退出游戏");
        }
        gameUserDao.delete(new UpdateWrapper<GameUser>().eq("game_id", gameId).eq("user_id", TokenUtil.getCurrentUserId()));
    }

    public List<GameInformation> listGameInformation(QueryWrapper wrapper) {
        return gameInformationDao.selectList(wrapper);
    }

    public List<Game> listGame(QueryWrapper wrapper) {
        return gameDao.selectList(wrapper);
    }

    public List<GameRound> listGameRound(QueryWrapper wrapper) {
        return gameRoundDao.selectList(wrapper);
    }

    public List<GameUser> listGameUser(QueryWrapper wrapper) {
        return gameUserDao.selectList(wrapper);
    }

    public GameConfig getGameConfigByRound(Integer round) {
        return gameConfigDao.selectOne(new QueryWrapper<GameConfig>().eq("round", round));
    }

    /**
     * 获取当前游戏的当前用户信息
     * @param gameId 游戏id
     * @return
     */
    public GameUser getCurrentGameUser(Long gameId) {
        //保证唯一
        return gameUserDao.selectOne(new QueryWrapper<GameUser>().eq("game_id", gameId).eq("user_id", TokenUtil.getCurrentUserId()));
    }

    @Transactional
    public void terminateGame(Long gameId) {
        Game game = gameDao.selectById(gameId);
        if (game == null||game.getStatus()==GameConstant.FINISH) {
            throw new ServiceException("游戏不存在或游戏已结束");
        }
        game.setStatus(GameConstant.TERMINATE);
        addOrUpdateGame(game);
        //同时要回收房间号
        roomNumUtil.reUse(game.getRoomNo());
    }

    public ListAndCount<Game> getUserHistoryGame(Integer page, Integer pageLength) {
        long offset = page!=null&&page > 0 ? (page - 1) * pageLength : 0;
        long length = page!=null&&page > 0 ? pageLength : Long.MAX_VALUE;
        ListAndCount<Game> listAndCount = new ListAndCount<Game>();
        List<Game> games = gameDao.getUserHistoryGame(TokenUtil.getCurrentUserId(), offset, length);
        long count = gameDao.countUserHistoryGame(TokenUtil.getCurrentUserId());
        listAndCount.setList(games);
        listAndCount.setCount(count);
        return listAndCount;
    }


    public List<GameInformation> listGameInformation(Long gameId, Long userId) {
        return gameInformationDao.listGameInformation(gameId, userId);
    }


    /**
     * 获取某用户某局游戏的统计信息
     * @param gameId
     * @return
     */
    public GameStatisticsVo getGameStatistics(Long gameId) {
        //查询该游戏用户
        GameUser gameUser=getCurrentGameUser(gameId);
        if (gameUser == null) {
            return null;
        }
        GameStatistics statistics = gameStatisticsDao.selectOne(new QueryWrapper<GameStatistics>().eq("game_id", gameId).eq("user_id", TokenUtil.getCurrentUserId()));
        if (statistics == null) {
            return null;
        }
        GameStatisticsVo vo = CloneUtil.clone(statistics, GameStatisticsVo.class);
        //查询有多少成本大于等于他的
        Integer highCostPlayer = gameStatisticsDao.selectCount(new QueryWrapper<GameStatistics>().ne("game_id", gameId).eq("user_role", gameUser.getUserRole()).ge("total_cost", statistics.getTotalCost()));
        //查询成本小于他的  排名比他高
        Integer lowerCostPlayer = gameStatisticsDao.selectCount(new QueryWrapper<GameStatistics>().ne("game_id", gameId).eq("user_role", gameUser.getUserRole()).lt("total_cost", statistics.getTotalCost()));
        vo.setRank((lowerCostPlayer+1)*1.0/(lowerCostPlayer+highCostPlayer+1));
        return vo;
    }

    public Game getUserCurrentGame(Long userId) {
        return gameDao.getUserCurrentGame(userId);
    }
}
