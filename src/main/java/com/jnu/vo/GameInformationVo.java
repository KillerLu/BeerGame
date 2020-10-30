package com.jnu.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ：Killer
 * @date ：Created in 20-10-30 上午9:40
 * @description：${description}
 * @modified By：
 * @version: version
 */
@ApiModel("游戏信息")
public class GameInformationVo {


    @ApiModelProperty("收到下游供应商的需求数")
    private Integer requestNum;

    @ApiModelProperty("库存数")
    private Integer stockNum;

    @ApiModelProperty("在途1数量")
    private Integer intransitOneNum;

    @ApiModelProperty("在途2数量")
    private Integer intransitTwoNum;

    @ApiModelProperty("缺货数")
    private Integer shortSupplyNum;

    @ApiModelProperty("向上游供应商的订货量")
    private Integer newOrderNum;

    @ApiModelProperty("当前游戏轮数")
    private Integer round;

    @ApiModelProperty("游戏id")
    private Long gameId;

    @ApiModelProperty("玩家角色 RETAILER:零售商 WHOLESALER:批发商 RESELLER:分销商 MANUFACTURER:厂商 OB:观看者")
    private String userRole;

    @ApiModelProperty("该回合该角色的进度 0:刚开始 1:收到下游供应商订货请求 2:已发货给下游供应商 3:已向上游供应商发出订货请求 4:已收到上游供应商的发货")
    private Integer progress;

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Integer getRequestNum() {
        return requestNum;
    }

    public void setRequestNum(Integer requestNum) {
        this.requestNum = requestNum;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }

    public Integer getIntransitOneNum() {
        return intransitOneNum;
    }

    public void setIntransitOneNum(Integer intransitOneNum) {
        this.intransitOneNum = intransitOneNum;
    }

    public Integer getIntransitTwoNum() {
        return intransitTwoNum;
    }

    public void setIntransitTwoNum(Integer intransitTwoNum) {
        this.intransitTwoNum = intransitTwoNum;
    }

    public Integer getShortSupplyNum() {
        return shortSupplyNum;
    }

    public void setShortSupplyNum(Integer shortSupplyNum) {
        this.shortSupplyNum = shortSupplyNum;
    }

    public Integer getNewOrderNum() {
        return newOrderNum;
    }

    public void setNewOrderNum(Integer newOrderNum) {
        this.newOrderNum = newOrderNum;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}
