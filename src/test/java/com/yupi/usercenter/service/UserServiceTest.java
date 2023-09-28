package com.yupi.usercenter.service;

import java.util.Date;

import com.yupi.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @description:用户服务测试
 **/
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("yupi");
        user.setUserAccount("wyf");
        user.setAvatarUrl("dsnka");
        user.setUserPassword("123");
        user.setPhone("2323");
        user.setEmail("231");
        boolean save = userService.save(user);
    }

    @Test
    void userRegister() {
        String userAccount = "yupiyupiy";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        String planetCode = "123";
        long result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(14,result);
    }
}