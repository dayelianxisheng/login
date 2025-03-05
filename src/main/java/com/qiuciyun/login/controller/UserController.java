package com.qiuciyun.login.controller;

import com.qiuciyun.login.common.Result;
import com.qiuciyun.login.entity.User;
import com.qiuciyun.login.model.LoginRequest; // 导入 LoginRequest
import com.qiuciyun.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/api/login")
    public Result<User> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("用户名或密码错误");
        }
    }
}