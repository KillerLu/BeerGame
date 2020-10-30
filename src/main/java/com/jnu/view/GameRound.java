package com.jnu.view;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * 游戏回合表
 *
 * @author killer
 * @date 2020-10-23
 */
@TableName("game_round")
public class GameRound extends BaseView {


    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 当前回合数
     */
    private Integer round;

    /**
     * 游戏id
     */
    private Long gameId;

    /**
     * 当前进行到那个角色  1:零售商 2:批发商 3:分销商 4:厂商
     */
    private Integer progress;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public Integer getRound() {
        return this.round;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getGameId() {
        return this.gameId;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getProgress() {
        return this.progress;
    }

}
