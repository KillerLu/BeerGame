package com.jnu.handler;

import com.alibaba.druid.util.StringUtils;
import com.jnu.common.GameContext;
import com.jnu.constant.GameConstant;
import com.jnu.view.Game;
import com.jnu.view.GameInformation;
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
public class ManufacturerGameProcessHandler extends GameProcessHandler{
    @Override
    protected String getLowerUserRole() {
        return GameConstant.RESELLER;
    }

    @Override
    protected String getUpperUserRole() {
        return null;
    }

    @Override
    protected void doOrderGoods(GameContext context, Integer orderNum) {
        GameInformation gameInformation=context.getGameInformation();
        //厂商已经没有上级供应商，因此状态直接变成已收到上游供应商发货
        gameInformation.setProgress(GameConstant.HAVE_RECEIVED_GOODS);
        gameInformation.setNewOrderNum(orderNum);
        //厂商已经没有上级供应商了,因此他如果订货将直接就是生产货物,就会在在途二中
        gameInformation.setIntransitTwoNum(gameInformation.getIntransitTwoNum() + orderNum);
        gameService.addOrUpdateGameInformation(gameInformation);
    }

    @Override
    public boolean support(GameUser gameUser) {
        return StringUtils.equals(gameUser.getUserRole(), GameConstant.MANUFACTURER);
    }


    @Override
    protected void postOrderGoods(GameContext context) {
        //厂商就没有下一个游戏角色，直接开始下一轮了
        gameService.nextRound(context.getGame().getId());
    }
}
