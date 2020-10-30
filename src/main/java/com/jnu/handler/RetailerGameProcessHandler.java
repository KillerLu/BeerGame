package com.jnu.handler;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnu.common.GameContext;
import com.jnu.constant.GameConstant;
import com.jnu.service.GameService;
import com.jnu.util.CloneUtil;
import com.jnu.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author ：Killer
 * @date ：Created in 20-10-23 上午10:58
 * @description：${description}
 * @modified By：
 * @version: version
 */
@Component
public class RetailerGameProcessHandler extends GameProcessHandler{


    @Override
    protected String getUpperUserRole() {
        return GameConstant.WHOLESALER;
    }

    @Override
    protected String getLowerUserRole() {
        return null;
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
            //零售商直接就是已收到订单需求
            nextGameInformation.setProgress(GameConstant.HAVE_RECEIVE_ORDER);
            gameService.addOrUpdateGameInformation(nextGameInformation);
        }
    }

    /**
     * 初始化第一轮的游戏信息数据
     * @param gameRound
     * @param gameUser
     */
    protected void initFirstRoundGameInformation(GameRound gameRound, GameUser gameUser) {
        //零售商第一轮的需求是根据配置
        GameConfig gameConfig = gameService.getGameConfigByRound(gameRound.getRound());
        if (gameConfig == null) {
            throw new RuntimeException("请先配置第"+gameRound.getRound()+"轮的需求数");
        }
        GameInformation gameInformation=new GameInformation();
        gameInformation.setRoundId(gameRound.getId());
        gameInformation.setUserId(gameUser.getUserId());
        gameInformation.setRequestNum(gameConfig.getRequestNum());
        gameInformation.setStockNum(10);
        gameInformation.setIntransitOneNum(0);
        gameInformation.setIntransitTwoNum(0);
        gameInformation.setShortSupplyNum(0);
        gameInformation.setNewOrderNum(0);
        //零售商直接就是已收到订单需求
        gameInformation.setProgress(GameConstant.HAVE_RECEIVE_ORDER);
        gameService.addOrUpdateGameInformation(gameInformation);
    }

    @Override
    public boolean support(GameUser gameUser) {
        return StringUtils.equals(gameUser.getUserRole(), GameConstant.RETAILER);
    }

    @Override
    protected void postOrderGoods(GameContext context) {
        //将当前游戏进度改为到了批发商回合
        GameRound gameRound=context.getGameRound();
        gameRound.setProgress(GameConstant.WHOLESALER_TURN);
        gameService.addOrUpdateGameRound(gameRound);
    }
}
