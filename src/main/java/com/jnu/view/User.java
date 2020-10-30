package com.jnu.view;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 *
 * @author killer
 * @date 2020-10-28
 */
@TableName("user")
public class User extends BaseView {


    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 微信openId
     */
    private String openId;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOpenId() {
        return this.openId;
    }

}
