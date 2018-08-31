package com.kuliao.oa.controller;

import com.kuliao.oa.db.model.Role;
import com.kuliao.oa.db.model.UserRole;
import com.kuliao.oa.domain.RetCode;
import com.kuliao.oa.domain.RetEntity;
import com.kuliao.oa.domain.UserInfo;
import com.kuliao.oa.services.MenuService;
import com.kuliao.oa.services.RoleService;
import com.kuliao.oa.utils.TokenUtils;
import com.kuliao.oa.utils.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/${spring.application.name}/role")
public class RoleController {
    private final static Logger log = LoggerFactory.getLogger(RoleController.class);
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;

    @RequestMapping("/generate")
    public ResponseEntity<RetEntity<List<Role>>> generateRoles(){
        List<Role> roles = roleService.generateRoles();
        log.debug("成功获取到角色列表"+roles);
        RetEntity<List<Role>> ret = new RetEntity<List<Role>>(RetCode.SUCCESS.getRetCode());
        ret.setMsg("获取角色成功");
        ret.setData(roles);
        return new ResponseEntity<RetEntity<List<Role>>>(ret, HttpStatus.OK);
    }

    @RequestMapping("/userInRole")
    public ResponseEntity<RetEntity<List<UserInfo>>> selectByRoleId(Integer roleId){
        log.debug("controller根据角色ID{}获取用户"+roleId);
        if (roleId==null) {
            throw new IllegalArgumentException("角色ID为空");
        }
        List<UserInfo> users = roleService.selectByRoleId(roleId);
        log.debug("根据角色获取到用户列表"+users);
        RetEntity<List<UserInfo>> ret = new RetEntity<List<UserInfo>>(RetCode.SUCCESS.getRetCode());
        ret.setMsg("根据角色获取用户成功");
        ret.setData(users);

        return new ResponseEntity<RetEntity<List<UserInfo>>>(ret, HttpStatus.OK);
    }

    @RequestMapping("/userNotInRole")
    public ResponseEntity<RetEntity<List<UserInfo>>> selectNotInRole(Integer roleId){
        log.debug("controller根据角色ID{}获取不是当前角色用户"+roleId);
        if (roleId==null) {
            throw new IllegalArgumentException("角色ID为空");
        }
        List<UserInfo> users = roleService.selectNotInRole(roleId);
        log.debug("获取无当前角色用户列表"+users);
        RetEntity<List<UserInfo>> ret = new RetEntity<List<UserInfo>>(RetCode.SUCCESS.getRetCode());
        ret.setMsg("获取无当前角色用户成功");
        ret.setData(users);

        return new ResponseEntity<RetEntity<List<UserInfo>>>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = {"/setMenuAuth"})
    public ResponseEntity<RetEntity> setMenuAuth(Integer roleId,Integer menuId,Byte create,Byte delete,Byte update,Byte select) {
        if (log.isDebugEnabled()) {
            log.debug("开始为角色{},菜单{}设置权限,增加{},删除{},修改{},查看{}",roleId,menuId,create,delete,update,select);
        }
        if(roleId == null || menuId == null
                || create == null || delete ==null || update == null || select == null){
            throw new IllegalArgumentException("设置菜单权限参数错误！");
        }


        menuService.setMenuAuth(roleId,menuId,create,delete,update,select,true);

        RetEntity ret = new RetEntity(RetCode.SUCCESS.getRetCode());
        ret.setMsg("设置权限成功");
        return new ResponseEntity<RetEntity>(ret,HttpStatus.OK);
    }

    @RequestMapping("/setRoleForUser")
    public ResponseEntity<RetEntity> setRoleForUser(UserRole userRole){
        if(userRole==null||userRole.getUserId()==null||userRole.getRoleId()==null){
            throw new IllegalArgumentException("新增角色参数错误！");
        }
        roleService.setRoleForUser(userRole);
        RetEntity ret = new RetEntity(RetCode.SUCCESS.getRetCode());
        ret.setMsg("设置角色成功");

        return new ResponseEntity<RetEntity>(ret, HttpStatus.OK);
    }

    @RequestMapping("/deleteEmp")
    public ResponseEntity<RetEntity> deleteEmp(String empNo,Integer roleId){
        log.debug("controller删除用户{}的角色{}",empNo,roleId);
        if (TokenUtils.empty(empNo)||roleId==null) {
            throw new IllegalArgumentException("删除角色中员工参数错误");
        }
        roleService.deleteEmp(empNo,roleId);

        RetEntity ret = new RetEntity(RetCode.SUCCESS.getRetCode());
        ret.setMsg("移除角色成功");


        return new ResponseEntity<RetEntity>(ret, HttpStatus.OK);
    }

    @RequestMapping("/create")
    public ResponseEntity<RetEntity> createRole(@Valid Role role, BindingResult result){
        ValidateUtil.validata(result);

        roleService.createRole(role);
        RetEntity ret = new RetEntity(RetCode.SUCCESS.getRetCode());
        ret.setMsg("新增角色成功");
        return new ResponseEntity<RetEntity>(ret, HttpStatus.OK);
    }


    @RequestMapping("/update")
    public ResponseEntity<RetEntity> updateRole(@Valid Role role, BindingResult result){
        ValidateUtil.validata(result);
        roleService.updateRole(role);
        RetEntity ret = new RetEntity(RetCode.SUCCESS.getRetCode());
        ret.setMsg("修改角色成功");

        return new ResponseEntity<RetEntity>(ret, HttpStatus.OK);
    }

    @RequestMapping("/delete")
    public ResponseEntity<RetEntity> deleteRole(Integer roleId){
        if(roleId==null){
            throw new IllegalArgumentException("删除角色参数错误！");
        }

        roleService.deleteRole(roleId);

        RetEntity ret = new RetEntity(RetCode.SUCCESS.getRetCode());
        ret.setMsg("删除角色成功");

        return new ResponseEntity<RetEntity>(ret, HttpStatus.OK);
    }


}
