package com.jnu.view;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * 游戏配置表
 *
 * @author killer
 * @date 2020-10-30
 */
@TableName("game_config")
public class GameConfig extends BaseView {


    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 客户需求数
     */
    private Integer requestNum;

    /**
     * 回合数
     */
    private Integer round;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setRequestNum(Integer requestNum) {
        this.requestNum = requestNum;
    }

    public Integer getRequestNum() {
        return this.requestNum;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public Integer getRound() {
        return this.round;
    }

}
