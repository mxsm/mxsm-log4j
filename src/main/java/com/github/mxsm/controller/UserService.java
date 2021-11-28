package com.github.mxsm.controller;

import com.github.mxsm.log.annotation.Log;
import org.springframework.stereotype.Service;

/**
 * @author mxsm
 * @date 2021/11/28 15:45
 * @Since 1.0.0
 */
@Service
public class UserService {

    @Log(template = "${#user.name}")
    public boolean addUser(User user){

        System.out.println(user);
        return true;
    }

}
