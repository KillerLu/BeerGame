package com.jnu.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ：Killer
 * @date ：Created in 20-10-29 下午4:32
 * @description：${description}
 * @modified By：
 * @version: version
 */
@ApiModel("游戏")
public class GameVo {


    @ApiModelProperty("游戏id")
    private Long id;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("游戏状态 0:未开始 1:游戏中 2:终止(未完成强行结束) 3:已结束")
    private Integer status;

    @ApiModelProperty("房主的用户id")
    private Long ownerId;

    @ApiModelProperty("当前游戏回合数(未开始为0)")
    private Integer currentRound;

    public Integer getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Integer currentRound) {
        this.currentRound = currentRound;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
