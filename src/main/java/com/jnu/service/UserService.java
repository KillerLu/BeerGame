package com.jnu.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnu.dao.UserDao;
import com.jnu.exception.ServiceException;
import com.jnu.util.HttpUtil;
import com.jnu.util.TokenUtil;
import com.jnu.view.Game;
import com.jnu.view.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：Killer
 * @date ：Created in 20-10-22 上午11:13
 * @description：${description}
 * @modified By：
 * @version: version
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Value("${auth.wechat.sessionHost}")
    private String sessionHost;

    @Value("${auth.wechat.appId}")
    private String appId;

    @Value("${auth.wechat.secret}")
    private String secret;

    @Value("${auth.wechat.grantType}")
    private String grantType;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserDao userDao;

    public Map login(String code) {
        String openId = getUserOpenId(code);
        //查询该openId对应的用户是否存在  这里必须是确保唯一
        User user = userDao.selectOne(new QueryWrapper<User>().eq("open_id", openId));
        if (user == null) {
            //如果用户为空，则代表第一次登录，为它创建一个用户信息
            user=new User();
            user.setOpenId(openId);
            userDao.insert(user);
        }

        //执行login操作
        String token=doLogin(user);
        //查询该用户所在的游戏
        Game game=gameService.getUserCurrentGame(user.getId());
        Map<String,Object> retMap=new HashMap<String,Object>();
        retMap.put("token", token);
        retMap.put("roomNo", game.getRoomNo());
        return retMap;
    }

    private String doLogin(User user) {
        String token = TokenUtil.createToken(user.getId(), user.getName());
        logger.info("当前登录用户id:"+user.getId()+",签发的token为:"+token);
        return token;
    }


    public String getUserOpenId(String code) {
        String url = sessionHost+"?appid="+appId+"&secret="+secret+"&js_code="+code+"&grant_type="+grantType;
        String resp = HttpUtil.get(url);
        JSONObject ret = JSONObject.parseObject(resp);
        if (ret.get("errcode") != null) {
            String errorMsg = null;
            switch (ret.get("errcode")+"") {
                case "-1":
                    errorMsg = "系统繁忙，此时请开发者稍候再试";
                    break;
                case "40029":
                    errorMsg = "code 无效";
                    break;
                case "40163":
                    errorMsg = "code 已被使用过";
                    break;
                case "45011":
                    errorMsg = "频率限制，每个用户每分钟100次";
                    break;
            }
            logger.error("getUserInfoByMiniprogram ERROR, resp: {}",errorMsg);
            throw new ServiceException("00050069", "微信小程序获取用户信息失败："+errorMsg);
        }
        return ret.getString("openid");
    }


}
