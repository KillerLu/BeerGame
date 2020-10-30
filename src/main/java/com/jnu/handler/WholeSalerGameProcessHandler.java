package com.jnu.handler;

import com.alibaba.druid.util.StringUtils;
import com.jnu.common.GameContext;
import com.jnu.constant.GameConstant;
import com.jnu.view.Game;
import com.jnu.view.GameRound;
import com.jnu.view.GameUser;
import org.springframework.stereotype.Component;

/**
 * @author ：Killer
 * @date ：Created in 20-10-23 上午10:59
 * @description：${description}
 * @modified By：
 * @version: version
 */
@Component
public class WholeSalerGameProcessHandler extends GameProcessHandler{

    @Override
    protected String getUpperUserRole() {
        return GameConstant.RESELLER;
    }

    @Override
    protected String getLowerUserRole() {
        return GameConstant.RETAILER;
    }

    @Override
    public boolean support(GameUser gameUser) {
        return StringUtils.equals(gameUser.getUserRole(), GameConstant.WHOLESALER);
    }

    @Override
    protected void postOrderGoods(GameContext context) {
        //将当前游戏进度改为到了批发商回合
        GameRound gameRound=context.getGameRound();
        gameRound.setProgress(GameConstant.RESELLER_TURN);
        gameService.addOrUpdateGameRound(gameRound);
    }
}
