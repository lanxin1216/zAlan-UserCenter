package com.alan.usercenterservera.controller;

import com.alan.usercenterservera.common.enumeration.ErrorCode;
import com.alan.usercenterservera.common.response.BaseResponse;
import com.alan.usercenterservera.common.response.BaseResponseUtils;
import com.alan.usercenterservera.exception.BusinessException;
import com.alan.usercenterservera.model.domain.User;
import com.alan.usercenterservera.model.request.UserLoginRequest;
import com.alan.usercenterservera.model.request.UserRegisterRequest;
import com.alan.usercenterservera.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.alan.usercenterservera.constant.UserConstant.ADMIN_ROLE;
import static com.alan.usercenterservera.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author alan
 * 用户控制层
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 注册请求
     * @return 注册成功返回用户id
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        /* 获取数据 */
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return BaseResponseUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 登录请求
     * @param request          http请求
     * @return 返回用户脱敏后信息
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        /* 获取数据 */
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User resultUser = userService.userLogin(userAccount, userPassword, request);
        return BaseResponseUtils.success(resultUser);
    }

    /**
     * 用户注销登录
     *
     * @param request http请求
     * @return 返回登录结果
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return BaseResponseUtils.success(result);
    }

    /**
     * 搜索获取用户列表（根据用户名模糊搜索）
     *
     * @param userName 用户名
     * @param request  http请求
     * @return 用户列表
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String userName, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        List<User> userList = userService.searchUsers(userName);
        return BaseResponseUtils.success(userList);
    }

    /**
     * 删除用户
     *
     * @param userId  用户 id
     * @param request http请求
     * @return 返回删除结果
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long userId, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        boolean result = userService.deleteUser(userId);
        return BaseResponseUtils.success(result);
    }

    /**
     * 判断是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
