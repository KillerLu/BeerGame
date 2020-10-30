package com.jnu.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnu.constant.GameConstant;
import com.jnu.exception.ServiceException;
import com.jnu.service.GameService;
import com.jnu.view.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author ：Killer
 * @date ：Created in 20-10-22 上午10:37
 * @description：${description}
 * @modified By：
 * @version: version
 */
@Component
public class RoomNumUtil {

    @Autowired
    private GameService gameService;

    private Set<String> rooms = new HashSet<String>();

    //初始化启动所有的Job
    @PostConstruct
    public void initialize() {
        //查询所有进行中的游戏
        List<Game> games = gameService.listGame(new QueryWrapper<Game>().ne("status", GameConstant.FINISH));
        if (!CollectionUtils.isEmpty(games)) {
            for (Game game : games) {
                rooms.add(game.getRoomNo());
            }
        }
    }

    private Random r = new Random();


    public static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);// 左补0
                // sb.append(str).append("0");//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }

    // 随机生成一个4位数字
    public String getRoomNum() {
        //这里做个判断，不能留太多，避免内存溢出
        if (rooms.size() >= 5000) {
            throw new ServiceException("当前玩家人数过多，请稍后再进行游戏");
        }
        int n = r.nextInt(10000);
        String roomNum = addZeroForNum(String.valueOf(n), 4);
        if (rooms.contains(roomNum)) {
           return getRoomNum();
        }
        rooms.add(roomNum);
        return roomNum;
    }

    // 回收一个数字
    public void reUse(String roomNum) {
        rooms.remove(roomNum);
    }

    public static void main(String[] args) {
        RoomNumUtil util=new RoomNumUtil();
        for(int i=0;i<=9999;i++) {
            System.out.println(util.getRoomNum());

        }
    }
}



