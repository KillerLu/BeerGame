package com.jnu.dao;

import com.jnu.view.GameInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 游戏信息表 mapper
 * 
 * @author killer
 * @date 2020-10-23
 */
public interface GameInformationDao extends BaseMapper<GameInformation> {

    /**
     * 查询某游戏的所有游戏信息
     * @param gameId
     * @param userId
     * @return
     */
    List<GameInformation> listGameInformation(@Param("gameId") Long gameId, @Param("userId") Long userId);
}
