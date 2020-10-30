package com.jnu.view;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * 游戏玩家表
 *
 * @author killer
 * @date 2020-10-22
 */
@TableName("game_user")
public class GameUser extends BaseView {


    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 玩家id
     */
    private Long userId;

    /**
     * 玩家角色
     */
    private String userRole;

    /**
     * 游戏id
     */
    private Long gameId;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
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

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getGameId() {
        return this.gameId;
    }

}
