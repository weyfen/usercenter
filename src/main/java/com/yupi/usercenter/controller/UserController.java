package com.yupi.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.usercenter.common.BaseResponse;
import com.yupi.usercenter.common.ErrorCode;
import com.yupi.usercenter.common.ResultUtils;
import com.yupi.usercenter.exception.BusinessException;
import com.yupi.usercenter.model.domain.User;
import com.yupi.usercenter.model.domain.UserLoginRequest;
import com.yupi.usercenter.model.domain.UserRegisterRequest;
import com.yupi.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.yupi.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.yupi.usercenter.constant.UserConstant.USER_LOGIN_STATE;


@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /*
     *
     * @description:实现用户注册功能
     * @return: java.lang.Long
     **/
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        //Controller层的校验是对参数进行校验，不关注业务的逻辑
        //Service层的校验需要关注业务的逻辑，而Service也可能不被Controller调用
        //可能会被另一个Service调用
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,planetCode)) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"参数不能为空");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        return ResultUtils.success(result);
    }

    /*
     *
     * @description:实现用户登录功能
     * @return: java.lang.Long
     **/
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        //Controller层的校验是对参数进行校验，不关注业务的逻辑
        //Service层的校验需要关注业务的逻辑，而Service也可能不被Controller调用
        //可能会被另一个Service调用
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"参数不能为空");
        }
        User result = userService.userLogin(userAccount, userPassword, httpServletRequest);
        return ResultUtils.success(result);
    }

    /*
     *
     * @description:实现用户注销功能
     **/
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest httpServletRequest) {
        if(httpServletRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        int result = userService.userLogout(httpServletRequest);
        return ResultUtils.success(result);
    }

    //获取用户的登录态
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest httpServletRequest){
        Object userObj = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        if(userObj == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User user = (User)userObj;
        Long id = user.getId();
        //如果一个用户的相关信息(比如积分)更新的比较快
        //那么我们最好再从数据库中去获取一次该对象
        User safetyUser = userService.safetyUser(userService.getById(id));
        return ResultUtils.success(safetyUser);
    }

    //对用户进行模糊查询(仅管理员可查询)
    //查询的时候一般使用get请求
    @GetMapping("search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest httpServletRequest) {
//        Object userObj = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
//        User user = (User) userObj;
//        if (user == null || user.getRole() != ADMIN_ROLE) {
//            return new ArrayList<>();
//        }
        if(!isAdmin(httpServletRequest)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> result = userService.list(queryWrapper);
        return ResultUtils.success(result);
    }

    //完成删除用户功能
    @PostMapping("delete")
    public BaseResponse<Boolean> deleteUsers(@RequestBody long id,HttpServletRequest httpServletRequest) {
//        Object userObj = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
//        User user = (User) userObj;
//        if (user == null || user.getRole() != ADMIN_ROLE) {
//            return false;
//        }
        if(!isAdmin(httpServletRequest)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //这里的删除是逻辑删除
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    //对鉴权代码进行封装
    public boolean isAdmin(HttpServletRequest httpServletRequest){
        Object userObj = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user == null || user.getUserRole() != ADMIN_ROLE) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return true;
    }
}
