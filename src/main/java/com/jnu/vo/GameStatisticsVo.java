package com.jnu.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ：Killer
 * @date ：Created in 20-11-2 上午10:30
 * @description：${description}
 * @modified By：
 * @version: version
 */
@ApiModel("游戏统计")
public class GameStatisticsVo {

    @ApiModelProperty("游戏id")
    private Long gameId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("玩家角色 RETAILER:零售商 WHOLESALER:批发商 RESELLER:分销商 MANUFACTURER:厂商 OB:观看者")
    private String userRole;

    @ApiModelProperty("库存成本")
    private Integer inventoryCost;

    @ApiModelProperty("缺货成本")
    private Integer shortageCost;

    @ApiModelProperty("总成本")
    private Integer totalCost;

    @ApiModelProperty("前百分之多少，小数返回")
    private Double rank;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Integer getInventoryCost() {
        return inventoryCost;
    }

    public void setInventoryCost(Integer inventoryCost) {
        this.inventoryCost = inventoryCost;
    }

    public Integer getShortageCost() {
        return shortageCost;
    }

    public void setShortageCost(Integer shortageCost) {
        this.shortageCost = shortageCost;
    }

    public Integer getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }

    public Double getRank() {
        return rank;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }
}
