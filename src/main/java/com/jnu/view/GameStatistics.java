package com.jnu.view;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * 游戏统计表
 *
 * @author killer
 * @date 2020-11-02
 */
@TableName("game_statistics")
public class GameStatistics extends BaseView {


    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 游戏id
     */
    private Long gameId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 游戏角色
     */
    private String userRole;

    /**
     * 库存成本
     */
    private Integer inventoryCost;

    /**
     * 缺货成本
     */
    private Integer shortageCost;

    /**
     * 总成本
     */
    private Integer totalCost;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getGameId() {
        return this.gameId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserRole() {
        return this.userRole;
    }

    public void setInventoryCost(Integer inventoryCost) {
        this.inventoryCost = inventoryCost;
    }

    public Integer getInventoryCost() {
        return this.inventoryCost;
    }

    public void setShortageCost(Integer shortageCost) {
        this.shortageCost = shortageCost;
    }

    public Integer getShortageCost() {
        return this.shortageCost;
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }

    public Integer getTotalCost() {
        return this.totalCost;
    }

}
