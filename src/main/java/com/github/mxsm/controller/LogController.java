package com.github.mxsm.controller;

import com.github.mxsm.log.annotation.Log;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mxsm
 * @date 2021/11/26 12:20
 * @Since 1.0.0
 */
@RestController
@RequestMapping("/log")
public class LogController {

    private UserService service;

    @Log(template = "#name")
    @GetMapping("")
    public long currentTime(@RequestParam(value = "name",required = false)String name){
        return System.currentTimeMillis();
    }

    //@Log(template = "用户${#name}信息：${#user.name}")
    @PostMapping("/user")
    public long currentTime1(@RequestParam(value = "name",required = false)String name,
        @RequestBody User user){
        service.addUser(user);
        return System.currentTimeMillis();
    }

    @Autowired
    public void setService(UserService service) {
        this.service = service;
    }
}
