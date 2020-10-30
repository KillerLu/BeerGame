package com.jnu.controller;

import com.jnu.common.ResponseBuilder;
import com.jnu.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：Killer
 * @date ：Created in 20-10-22 上午11:37
 * @description：${description}
 * @modified By：
 * @version: version
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "登录", httpMethod = "POST", notes = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "微信code",  dataType = "string", paramType = "query")
    })
    public Object login(String code)
    {
        String token=userService.login(code);
        System.out.println(token);
        return new ResponseBuilder().success().add("token", token).build();
    }
}
