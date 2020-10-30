package com.jnu.dao;

import com.jnu.view.GameUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 游戏玩家表 mapper
 * 
 * @author killer
 * @date 2020-10-22
 */
public interface GameUserDao extends BaseMapper<GameUser> {

    /**
     * 根据用户id查询对应的非结束的游戏用户
     * @param userId
     * @return
     */
    List<GameUser> listUnEndGameUserByUserId(@Param("userId") Long userId);
}
