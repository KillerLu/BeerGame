package com.jnu.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnu.common.GameContext;
import com.jnu.constant.GameConstant;
import com.jnu.service.GameService;
import com.jnu.util.CloneUtil;
import com.jnu.view.Game;
import com.jnu.view.GameInformation;
import com.jnu.view.GameRound;
import com.jnu.view.GameUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author ：Killer
 * @date ：Created in 20-10-23 上午10:58
 * @description：${description}
 * @modified By：
 * @version: version
 */
public abstract class GameProcessHandler {

    @Autowired
    protected GameService gameService;

    public void sendGoods(GameContext context){
        //进行发货操作
        Integer totalSend = doSendGood(context);
        //更新下游供应商
        updateLowerSupplier(context, totalSend);
    }

    public void orderGoods(GameContext context,Integer orderNum) {
        //进行订货操作
        doOrderGoods(context, orderNum);
        //更新上游供应商
        updateUpperSupplier(context, orderNum);
        //订货后的后置操作
        postOrderGoods(context);
    }

    protected void postOrderGoods(GameContext context) {
    }


    protected void updateUpperSupplier(GameContext context, Integer orderNum) {
        //查询上游供应商
        GameInformation upperSupplierGameInformation = getUpperSupplierInformation(context);
        if (upperSupplierGameInformation == null) {
            return;
        }
        upperSupplierGameInformation.setRequestNum(orderNum);
        upperSupplierGameInformation.setProgress(GameConstant.HAVE_RECEIVE_ORDER);
        gameService.addOrUpdateGameInformation(upperSupplierGameInformation);
    }

    /**
     * 获取上游供应商信息
     * @param context
     * @return
     */
    protected GameInformation getUpperSupplierInformation(GameContext context){
        //查询其该游戏的下游角色
        String userRole=getUpperUserRole();
        if (StringUtils.isBlank(userRole)) {
            return null;
        }
        return getRelatedSupplierInformation(context, userRole);
    }

    /**
     * 根据角色查询当前回合对应的游戏信息
     * @param context
     * @param userRole
     * @return
     */
    private GameInformation getRelatedSupplierInformation(GameContext context, String userRole) {
        //获取对应的game
        Game game = context.getGame();
        //获取对应的gameInformation
        GameInformation gameInformation=context.getGameInformation();
        //这里的下游角色也是确保唯一的
        List<GameUser> gameUsers = gameService.listGameUser(new QueryWrapper<GameUser>().eq("user_role", userRole).eq("game_id", game.getId()));
        if (CollectionUtils.isEmpty(gameUsers)) {
            return null;
        }
        QueryWrapper<GameInformation> wrapper = new QueryWrapper<GameInformation>().eq("round_id", gameInformation.getRoundId())
                .eq("user_id", gameUsers.get(0).getUserId());
        List<GameInformation> gameInformations = gameService.listGameInformation(wrapper);
        return CollectionUtils.isEmpty(gameInformations)?null:gameInformations.get(0);
    }

    protected abstract String getUpperUserRole();

    protected void doOrderGoods(GameContext context, Integer orderNum) {
        GameInformation gameInformation=context.getGameInformation();
        gameInformation.setProgress(GameConstant.HAVE_ORDER_GOODS);
        gameInformation.setNewOrderNum(orderNum);
        gameService.addOrUpdateGameInformation(gameInformation);
    }

    protected void updateLowerSupplier(GameContext context,Integer totalSend){
        //查询下游供应商
        GameInformation lowerSupplierGameInformation = getLowerSupplierInformation(context);
        if (lowerSupplierGameInformation == null) {
            return;
        }
        //下游供应商会收到上游供应商的发货
        lowerSupplierGameInformation.setProgress(GameConstant.HAVE_RECEIVED_GOODS);
        //更新到在途2中
        lowerSupplierGameInformation.setIntransitTwoNum(lowerSupplierGameInformation.getIntransitTwoNum() + totalSend);
        gameService.addOrUpdateGameInformation(lowerSupplierGameInformation);
    }

    /**
     * 获取下游供应商信息
     * @param context
     * @return
     */
    protected GameInformation getLowerSupplierInformation(GameContext context){
        //查询其该游戏的下游角色
        String userRole=getLowerUserRole();
        if (StringUtils.isBlank(userRole)) {
            return null;
        }
        return getRelatedSupplierInformation(context, userRole);
    }

    protected abstract String getLowerUserRole();

    protected Integer doSendGood(GameContext context) {
        GameInformation gameInformation=context.getGameInformation();
        Integer totalSend=0;
        //计算当前库存与需求的差
        Integer remains=gameInformation.getStockNum()-gameInformation.getRequestNum();
        //判断库存是否足够  对比库存
        if (remains>=0) {
            totalSend=totalSend+gameInformation.getRequestNum();
            //如果库存足够,则直接减掉库存即可
            gameInformation.setStockNum(remains);
            //扣掉后还要看之前是否有缺货,如果有缺货还要发之前缺少的
            Integer finalRemains=remains-gameInformation.getShortSupplyNum();
            //判断是否够满足之前缺货的
            if (finalRemains >= 0) {
                //先记录发货数
                totalSend=totalSend+gameInformation.getShortSupplyNum();
                //如果还能满足,则缺货直接置0
                gameInformation.setShortSupplyNum(0);
                //同时更新库存
                gameInformation.setStockNum(finalRemains);
            }else{
                //先记录发货数
                totalSend=totalSend+remains;
                //不够的话则库存清0
                gameInformation.setStockNum(0);
                //同时更新缺货数
                gameInformation.setShortSupplyNum(gameInformation.getShortSupplyNum()-remains);
            }

        }else{
            //先记录了发货数
            totalSend=totalSend+gameInformation.getStockNum();
            //不够的话，则讲缺少的记录到缺货数里面
            gameInformation.setShortSupplyNum(gameInformation.getRequestNum()-gameInformation.getStockNum());
            //同事库存清0
            gameInformation.setStockNum(0);
        }
        //更新进度
        gameInformation.setProgress(GameConstant.HAVE_SEND_GOODS);
        gameService.addOrUpdateGameInformation(gameInformation);
        return totalSend;
    }


    public void initGameInformation(GameRound currentRound,GameRound newRound, GameUser gameUser) {
        if (currentRound==null) {
            //如果为空,则代表是第一轮
            initFirstRoundGameInformation(newRound, gameUser);
        }else{
            initNextRoundGameInformation(currentRound,newRound,gameUser);
        }
    }

    protected void initNextRoundGameInformation(GameRound currentRound, GameRound newRound, GameUser gameUser) {
        //先查询本轮的游戏信息
        QueryWrapper<GameInformation> wrapper = new QueryWrapper<GameInformation>().eq("round_id", currentRound.getId()).eq("user_id", gameUser.getUserId());
        List<GameInformation> gameInformations = gameService.listGameInformation(wrapper);
        if (!CollectionUtils.isEmpty(gameInformations)) {
            //不为空则代表并不是第一轮
            GameInformation oldGameInformation = gameInformations.get(0);
            GameInformation nextGameInformation = CloneUtil.clone(oldGameInformation, GameInformation.class);
            //先将id置空
            nextGameInformation.setId(null);
            //上一轮的在途2会去到在途1,在途1会去到库存里去
            nextGameInformation.setIntransitTwoNum(0);
            nextGameInformation.setIntransitOneNum(oldGameInformation.getIntransitTwoNum());
            nextGameInformation.setStockNum(oldGameInformation.getIntransitOneNum());
            nextGameInformation.setRoundId(newRound.getId());
            //重置newOrderNum
            nextGameInformation.setNewOrderNum(0);
            //设置进度
            nextGameInformation.setProgress(GameConstant.INIT);
            gameService.addOrUpdateGameInformation(nextGameInformation);
        }
    }

    /**
     * 初始化第一轮的游戏信息数据
     * @param gameRound
     * @param gameUser
     */
    protected void initFirstRoundGameInformation(GameRound gameRound, GameUser gameUser) {
        GameInformation gameInformation=new GameInformation();
        gameInformation.setRoundId(gameRound.getId());
        gameInformation.setUserId(gameUser.getUserId());
        //需求数
        gameInformation.setRequestNum(0);
        gameInformation.setStockNum(10);
        gameInformation.setIntransitOneNum(0);
        gameInformation.setIntransitTwoNum(0);
        gameInformation.setShortSupplyNum(0);
        gameInformation.setNewOrderNum(0);
        gameInformation.setProgress(GameConstant.INIT);
        gameService.addOrUpdateGameInformation(gameInformation);
    }

    public abstract boolean support(GameUser gameUser);
}
