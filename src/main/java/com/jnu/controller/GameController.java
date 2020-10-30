package com.jnu.controller;

import com.jnu.common.ListAndCount;
import com.jnu.common.ResponseBuilder;
import com.jnu.service.GameService;
import com.jnu.util.CloneUtil;
import com.jnu.view.Game;
import com.jnu.view.GameUser;
import com.jnu.vo.GameInformationVo;
import com.jnu.vo.GameUserVo;
import com.jnu.vo.GameVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：Killer
 * @date ：Created in 20-10-21 下午5:51
 * @description：${description}
 * @modified By：
 * @version: version
 */
@RestController
@RequestMapping("game")
public class GameController {

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/createGame", method = RequestMethod.POST)
    @ApiOperation(value = "新建游戏", httpMethod = "POST", notes = "新建游戏",response = GameVo.class)
    public Object addOrder()
    {
        Game game=gameService.createGame();
        GameVo vo = CloneUtil.clone(game, GameVo.class);
        vo.setCurrentRound(0);
        return new ResponseBuilder().success().data(vo).build();
    }

    @RequestMapping(value = "/joinGame", method = RequestMethod.POST)
    @ApiOperation(value = "加入游戏", httpMethod = "POST", notes = "加入游戏",response = GameUserVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomNo", value = "房间编号",  dataType = "string", paramType = "query")
    })
    public Object joinGame(String roomNo) {
        GameUser gameUser=gameService.joinGame(roomNo);
        GameUserVo vo = CloneUtil.clone(gameUser, GameUserVo.class);
        vo.setRoomNo(roomNo);
        return new ResponseBuilder().success().data(vo).build();
    }

    @RequestMapping(value = "/chooseGameRole", method = RequestMethod.POST)
    @ApiOperation(value = "选择游戏角色", httpMethod = "POST", notes = "选择游戏角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameUserId", value = "游戏用户id",  dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "userRole", value = "玩家角色 RETAILER:零售商 WHOLESALER:批发商 RESELLER:分销商 MANUFACTURER:厂商 OB:观看者",  dataType = "long", paramType = "query")
    })
    public Object chooseGameRole(Long gameUserId, String userRole) {
        gameService.chooseGameRole(gameUserId, userRole);
        return new ResponseBuilder().success().build();
    }

    @RequestMapping(value = "/startGame", method = RequestMethod.POST)
    @ApiOperation(value = "开始游戏", httpMethod = "POST", notes = "开始游戏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId", value = "游戏id",  dataType = "long", paramType = "query")
    })
    public Object startGame(Long gameId) {
        gameService.startGame(gameId);
        return new ResponseBuilder().success().build();
    }

    @RequestMapping(value = "/getGameDetail", method = RequestMethod.GET)
    @ApiOperation(value = "获取当前游戏详情", httpMethod = "GET", notes = "获取当前游戏详情",response = GameVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "游戏id",  dataType = "long", paramType = "query")
    })
    public Object getGameDetail(Long id) {
        GameVo vo = gameService.getGameDetail(id);
        return new ResponseBuilder().success().data(vo).build();
    }


    @RequestMapping(value = "/getUserGameInformation", method = RequestMethod.GET)
    @ApiOperation(value = "获取某角色当前游戏信息，游戏开始后(前端按四个位置已满人判断)可以定时调用或手动刷新", httpMethod = "GET", notes = "获取某角色当前游戏信息，游戏开始后(前端按四个位置已满人判断)可以定时调用或手动刷新",response = GameInformationVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId", value = "游戏id",  dataType = "long", paramType = "query")
    })
    public Object getUserGameInformation(Long gameId) {
        GameInformationVo vo = gameService.getUserGameInformation(gameId);
        return new ResponseBuilder().success().data(vo).build();
    }

    @RequestMapping(value = "/sendGoods", method = RequestMethod.POST)
    @ApiOperation(value = "对下游供应商进行发货", httpMethod = "POST", notes = "对下游供应商进行发货")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId", value = "游戏id",  dataType = "long", paramType = "query")
    })
    public Object sendGoods(Long gameId) {
        gameService.sendGoods(gameId);
        return new ResponseBuilder().success().build();
    }


    @RequestMapping(value = "/orderGoods", method = RequestMethod.POST)
    @ApiOperation(value = "向上游供应商进行订货", httpMethod = "POST", notes = "向上游供应商进行订货")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId", value = "游戏id",  dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "orderNum", value = "订货数",  dataType = "int", paramType = "query")
    })
    public Object orderGoods(Long gameId,Integer orderNum) {
        gameService.orderGoods(gameId,orderNum);
        return new ResponseBuilder().success().build();
    }

    @RequestMapping(value = "/leaveRoom", method = RequestMethod.POST)
    @ApiOperation(value = "离开房间", httpMethod = "POST", notes = "离开房间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId", value = "游戏id",  dataType = "long", paramType = "query")
    })
    public Object leaveRoom(Long gameId) {
        gameService.leaveRoom(gameId);
        return new ResponseBuilder().success().build();
    }

    @RequestMapping(value = "/terminateGame", method = RequestMethod.POST)
    @ApiOperation(value = "终止游戏", httpMethod = "POST", notes = "终止游戏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId", value = "游戏id",  dataType = "long", paramType = "query")
    })
    public Object terminateGame(Long gameId) {
        gameService.terminateGame(gameId);
        return new ResponseBuilder().success().build();
    }


    @RequestMapping(value = "/getUserHistoryGame", method = RequestMethod.GET)
    @ApiOperation(value = "获取玩家最近的已正常完成游戏记录", httpMethod = "GET", notes = "获取玩家最近的已正常完成游戏记录",response = GameVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码, -1: 返回所有数据", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageLength", value = "每页长度, 默认为10", dataType = "int", paramType = "query")
    })
    public Object getUserHistoryGame( @RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "pageLength", defaultValue = "10") Integer pageLength) {
        ListAndCount<Game> listAndCount=gameService.getUserHistoryGame(page,pageLength);
        List<GameVo> vos = new ArrayList<GameVo>();
        if (!CollectionUtils.isEmpty(listAndCount.getList())) {
            for (Game game : listAndCount.getList()) {
                GameVo vo=new GameVo();
                vo.setId(game.getId());
                vos.add(vo);
            }
        }
        return new ResponseBuilder().success().data(vos).add("total", listAndCount.getCount()).build();
    }

}
