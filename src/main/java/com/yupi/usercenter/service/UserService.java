package com.yupi.usercenter.service;

import com.yupi.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 *  该类负责完成用户相关业务
 */
public interface UserService extends IService<User> {
    //实现用户注册功能
    long userRegister(String userAccount,String userPassword,String checkPassword,String planetCode);

    //实现用户登录功能
    User userLogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest);

    //实现用户注销功能
    int userLogout(HttpServletRequest httpServletRequest);

    //返回脱敏后的用户信息
    User safetyUser(User user);
}
