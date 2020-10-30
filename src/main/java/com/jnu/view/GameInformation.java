package com.jnu.view;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * 游戏信息表
 *
 * @author killer
 * @date 2020-10-23
 */
@TableName("game_information")
public class GameInformation extends BaseView {


    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 游戏回合id
     */
    private Long roundId;

    /**
     * 玩家id
     */
    private Long userId;

    /**
     * 订单需求量
     */
    private Integer requestNum;

    /**
     * 库存量
     */
    private Integer stockNum;

    /**
     * 在途1数量
     */
    private Integer intransitOneNum;

    /**
     * 在途2数量
     */
    private Integer intransitTwoNum;

    /**
     * 缺货量
     */
    private Integer shortSupplyNum;

    /**
     * 订货量
     */
    private Integer newOrderNum;

    /**
     * 该角色在该回合的进度,包括未开始,已发货,已收订货
     */
    private Integer progress;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public Long getRoundId() {
        return this.roundId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setRequestNum(Integer requestNum) {
        this.requestNum = requestNum;
    }

    public Integer getRequestNum() {
        return this.requestNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }

    public Integer getStockNum() {
        return this.stockNum;
    }

    public void setIntransitOneNum(Integer intransitOneNum) {
        this.intransitOneNum = intransitOneNum;
    }

    public Integer getIntransitOneNum() {
        return this.intransitOneNum;
    }

    public void setIntransitTwoNum(Integer intransitTwoNum) {
        this.intransitTwoNum = intransitTwoNum;
    }

    public Integer getIntransitTwoNum() {
        return this.intransitTwoNum;
    }

    public void setShortSupplyNum(Integer shortSupplyNum) {
        this.shortSupplyNum = shortSupplyNum;
    }

    public Integer getShortSupplyNum() {
        return this.shortSupplyNum;
    }

    public void setNewOrderNum(Integer newOrderNum) {
        this.newOrderNum = newOrderNum;
    }

    public Integer getNewOrderNum() {
        return this.newOrderNum;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getProgress() {
        return this.progress;
    }

}
