package com.cjw.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjw.common.lang.RpcResult;
import com.cjw.entity.User;
import com.cjw.entity.UserDto;
import com.cjw.service.UserService;
import com.cjw.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author cjw
 * @since 2021-08-08
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    @RequiresAuthentication
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public RpcResult index() {
        User user = userService.getById(1L);
        return RpcResult.succ(user);
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RpcResult save(@Validated @RequestBody User user) {
        return RpcResult.succ(user);
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public RpcResult login(@RequestBody UserDto userDto, HttpServletResponse response) {
        User user = userService.getOne(new QueryWrapper<User>().eq("username", userDto.getUsername()));
        Assert.notNull(user, "用户不存在！");
        if (!user.getPassword().equals(SecureUtil.md5(userDto.getPassword()))) {
            return RpcResult.fail("密码错误！");
        }
        String token = jwtUtils.generateToken(user.getId());
        response.setHeader("Authorization", token);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        return RpcResult.succ(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("avatar", user.getAvatar())
                .put("email", user.getEmail())
                .map());
    }

    @RequiresAuthentication
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public RpcResult logout(){
        SecurityUtils.getSubject().logout();
        return RpcResult.succ("注销成功");
    }
}
