package com.jnu.dao;

import com.jnu.view.Game;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 游戏表 mapper
 * 
 * @author killer
 * @date 2020-10-22
 */
public interface GameDao extends BaseMapper<Game> {

    /**
     * 根据回合id关联查询对应的游戏
     * @return
     */
    public Game getGameByRoundId(@Param("roundId") Long roundId);

    /**
     * 查询用户游戏记录(只查询已完成的)
     * @param currentUserId
     * @param page
     * @param pageLength
     * @return
     */
    public List<Game> getUserHistoryGame(@Param("userId") Long userId,
                                         @Param("offset")long offset,
                                         @Param("length")long length);

    long countUserHistoryGame(@Param("userId") Long userId);
}
