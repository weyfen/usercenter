package com.yupi.usercenter.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 该类用于封装用户注册时的信息
 **/
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -2972261021336901724L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;
}
