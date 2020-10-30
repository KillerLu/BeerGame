package com.jnu.view;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * 游戏表
 *
 * @author killer
 * @date 2020-10-23
 */
@TableName("game")
public class Game extends BaseView {


    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 房间号
     */
    private String roomNo;

    /**
     * 游戏状态
     */
    private Integer status;

    /**
     * 房主id
     */
    private Long ownerId;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomNo() {
        return this.roomNo;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

}
