package com.yupi.usercenter.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 该类用于封装用户登录时的信息
 **/
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -4258748142008588486L;

    private String userAccount;

    private String userPassword;

}
