package com.jnu.dao;

import com.jnu.view.GameRound;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 游戏回合表 mapper
 * 
 * @author killer
 * @date 2020-10-22
 */
public interface GameRoundDao extends BaseMapper<GameRound> {

    public GameRound getCurrentGameRound(@Param("gameId") Long gameId);

}
