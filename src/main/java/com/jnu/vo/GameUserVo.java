package com.jnu.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ：Killer
 * @date ：Created in 20-10-29 下午4:55
 * @description：${description}
 * @modified By：
 * @version: version
 */
@ApiModel("游戏用户")
public class GameUserVo {

    @ApiModelProperty("游戏用户id")
    private Long id;

    @ApiModelProperty("玩家用户id")
    private Long userId;

    @ApiModelProperty("玩家角色 RETAILER:零售商 WHOLESALER:批发商 RESELLER:分销商 MANUFACTURER:厂商 OB:观看者")
    private String userRole;

    @ApiModelProperty("游戏id")
    private Long gameId;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("游戏用户名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }
}
