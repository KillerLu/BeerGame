package com.jnu.controller;

import com.jnu.common.ResponseBuilder;
import com.jnu.util.TokenUtil;
import com.jnu.view.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：Killer
 * @date ：Created in 20-10-22 上午11:16
 * @description：${description}
 * @modified By：
 * @version: version
 */
@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Object test()
    {
        System.out.println(TokenUtil.getCurrentUserId());
        return "ok";
    }
}
