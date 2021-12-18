package com.github.mxsm.log.test.nolog;

import com.github.mxsm.log.annotation.Log;
import com.github.mxsm.log.test.User;
import org.springframework.stereotype.Service;

/**
 * @author mxsm
 * @date 2021/12/18 17:33
 * @Since 1.0.0
 */
@Service
public class UserServiceImplNoLog {

    @Log(template = "新增用户-名称为:${#user.name}  用户地址：${#user.address}, 年龄:${#user.age}, 用户的信息：${@userServiceImplTrue.getUserInfo(#user)}")
    public boolean addUser(User user){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 1999; ++i){
            sb.append(i);
        }
        return true;
    }

    private String getUserInfo(User use){
        return use.toString();
    }

}
