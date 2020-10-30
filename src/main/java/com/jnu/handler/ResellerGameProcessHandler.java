package com.jnu.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnu.common.GameContext;
import com.jnu.constant.GameConstant;
import com.jnu.service.GameService;
import com.jnu.view.Game;
import com.jnu.view.GameInformation;
import com.jnu.view.GameRound;
import com.jnu.view.GameUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author ：Killer
 * @date ：Created in 20-10-23 上午10:59
 * @description：${description}
 * @modified By：
 * @version: version
 */
@Component
public class ResellerGameProcessHandler extends GameProcessHandler{

    @Override
    protected String getUpperUserRole() {
        return GameConstant.MANUFACTURER;
    }

    @Override
    protected String getLowerUserRole() {
        return GameConstant.WHOLESALER;
    }

    @Override
    public boolean support(GameUser gameUser) {
        return StringUtils.equals(gameUser.getUserRole(), GameConstant.RESELLER);
    }

    @Override
    protected void postOrderGoods(GameContext context) {
        //将当前游戏进度改为到了批发商回合
        GameRound gameRound=context.getGameRound();
        gameRound.setProgress(GameConstant.MANUFACTURER_TURN);
        gameService.addOrUpdateGameRound(gameRound);
    }
}
