package com.jnu.dao;

import com.jnu.view.GameConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 游戏配置表 mapper
 * 
 * @author killer
 * @date 2020-10-30
 */
public interface GameConfigDao extends BaseMapper<GameConfig> {
    public Integer getMaxRound();
}
