package com.kuliao.oa.controller;

import com.kuliao.oa.domain.*;
import com.kuliao.oa.exception.BusinessException;
import com.kuliao.oa.services.DeptService;
import com.kuliao.oa.services.MenuService;
import com.kuliao.oa.services.TokenService;
import com.kuliao.oa.services.UserService;
import com.kuliao.oa.utils.TokenUtils;
import com.kuliao.oa.utils.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController

@RequestMapping("${spring.application.name}/user")
public class OaBaseController {
    private final static Logger log = LoggerFactory.getLogger(OaBaseController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private DeptService deptService;

    @Autowired
    private TokenService tokenService;


    @RequestMapping(value = "/register")
    public ResponseEntity<RetEntity> register(
            @RequestParam(value = "empNo")String empNo,
            @RequestParam(value = "username")String username,
            @RequestParam(value = "code")String code,
            @RequestParam(value = "deptId")Integer deptId){
        if (log.isDebugEnabled()) {
            log.debug("开始注册员工，编号为{}，用户名{}，code{}，部门ID{}", empNo,username,code,deptId);
        }
        //员工编号是否存在

        if (userService.getUserAuthByEmpNo(empNo) != null) {
            throw new IllegalArgumentException("员工编号已存在！");
        }

        int status = userService.registerUser(empNo, username, code, deptId);
        if(status!=1){
            throw new BusinessException("100010","插入用户失败");
        }
        RetEntity ret = new RetEntity(RetCode.SUCCESS.getRetCode());
        ret.setMsg("创建用户成功");
        return new ResponseEntity<RetEntity>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = "/getWorkmates")
    public ResponseEntity<RetEntity<List<UserInfo>>> getWorkmates(String empNo) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("获取员工{}的同事列表",empNo);
        }


        if (TokenUtils.empty(empNo)) {
            throw new IllegalArgumentException("员工编号为空");
        }

        List<UserInfo> workmates  = userService.getWorkmates(empNo);

        RetEntity<List<UserInfo>> ret = new RetEntity<List<UserInfo>>(RetCode.SUCCESS.getRetCode());
        ret.setMsg("登陆成功");
        ret.setData(workmates);

        return new ResponseEntity<RetEntity<List<UserInfo>>>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = "/login")
    public ResponseEntity<RetEntity<UserAuth>> login(String empNo, String password) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("login auth...");
        }

        if (TokenUtils.empty(empNo) || TokenUtils.empty(password)) {
            throw new IllegalArgumentException("用户名或密码为空！");
        }
        UserAuth user = userService.login(empNo, password);
        RetEntity<UserAuth> ret = new RetEntity<UserAuth>(RetCode.SUCCESS.getRetCode());
        ret.setMsg("登陆成功");
        ret.setData(user);

        return new ResponseEntity<RetEntity<UserAuth>>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = {"/lockUser"})
    public ResponseEntity<RetEntity> modifyPwd(String empNo) {
        String msg = userService.lockUser(empNo);
        RetEntity ret = new RetEntity(RetCode.SUCCESS.getRetCode());
        ret.setMsg(msg);
        return new ResponseEntity<RetEntity>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = {"/getAllMenu"})
    public ResponseEntity<RetEntity<List<MenuOperate>>> selectAllMenus() {
        if (log.isDebugEnabled()) {
            log.debug("开始获取所有菜单集合");
        }

        List<MenuOperate> menus = menuService.selectAllMenus();
        RetEntity<List<MenuOperate>> ret = new RetEntity<List<MenuOperate>>(RetCode.SUCCESS.getRetCode());
        ret.setMsg("获取成功");
        ret.setData(menus);
        return new ResponseEntity<RetEntity<List<MenuOperate>>>(ret, HttpStatus.OK);
    }


    @RequestMapping(value = {"/menu"})
    public ResponseEntity<RetEntity<List<MenuOperate>>> getMenuOperation(String empNo) {
        if (log.isDebugEnabled()) {
            log.debug("get menu...");
        }
        if (TokenUtils.empty(empNo)) {
            throw new IllegalArgumentException("员工编号为空！");
        }
        UserAuth user = userService.getUserAuthByEmpNo(empNo);
        List<MenuOperate> menuOperates = menuService.getMenuOByUser(user);
        RetEntity<List<MenuOperate>> ret = new RetEntity<List<MenuOperate>>(RetCode.SUCCESS.getRetCode());
        ret.setMsg("获取成功");
        ret.setData(menuOperates);
        return new ResponseEntity<RetEntity<List<MenuOperate>>>(ret, HttpStatus.OK);
    }


    @RequestMapping(value = {"/query"})
    public ResponseEntity<RetEntity<List<UserInfo>>> selectEmp(@Valid UserInfo user, BindingResult result) {
        if (log.isDebugEnabled()) {
            log.debug("query emp...");
            log.debug("传入的用户信息为{},{},{}", user.getEmpNo(), user.getUsername(), user.getDeptName());
        }
        ValidateUtil.validata(result);


        List<UserInfo> userInfos = userService.selectEmp(user);
        log.debug("查询到用户列表"+userInfos);
        RetEntity<List<UserInfo>> ret = new RetEntity<List<UserInfo>>(RetCode.SUCCESS.getRetCode());
        ret.setMsg("获取成功");
        ret.setData(userInfos);
        return new ResponseEntity<RetEntity<List<UserInfo>>>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = "/generate")
    public ResponseEntity<RetEntity<String>> generateToken(String empNo, HttpServletRequest req) {

        if (log.isDebugEnabled()) {
            log.debug("generate token ...");
        }

        if (TokenUtils.empty(empNo)) {
            throw new IllegalArgumentException("员工编号为空！");
        }

        String referer = req.getHeader("Referer");
        Pattern pattern = Pattern.compile(":\\d+");
        Matcher matcher = pattern.matcher(referer.substring(referer.lastIndexOf(":")));
        if (!matcher.matches()) {
            throw new IllegalArgumentException("来源错误，请重试");
        }

        RetEntity<String> ret = new RetEntity<String>(RetCode.SUCCESS.getRetCode());
        String ip = TokenUtils.getIpAddress(req);

        String token = tokenService.generateToken(empNo, ip, 3600000L);

        ret.setMsg("获取token成功");
        ret.setData(token);
        return new ResponseEntity<RetEntity<String>>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = "/auth")
    public ResponseEntity<RetEntity> tokenAuth(HttpServletRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("token auth...");
        }
        String token = request.getHeader("Authorization");
        String ip = TokenUtils.getIpAddress(request);
        tokenService.tokenAuth(token, ip);
        RetEntity ret = new RetEntity(RetCode.SUCCESS.getRetCode());
        ret.setMsg("验证成功");
        return new ResponseEntity<RetEntity>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = "/getUserById")
    public ResponseEntity<RetEntity<UserInfo>> getUserById(Integer userId) {
        if (log.isDebugEnabled()) {
            log.debug("开始查询ID为{}的用户", userId);
        }
        if (userId == null) {
            throw new IllegalArgumentException("用户Id为空");
        }
        UserInfo user = userService.getUserById(userId);
        RetEntity<UserInfo> ret = new RetEntity<UserInfo>(RetCode.SUCCESS.getRetCode());
        ret.setMsg("获取成功");
        ret.setData(user);


        return new ResponseEntity<RetEntity<UserInfo>>(ret, HttpStatus.OK);
    }
}
