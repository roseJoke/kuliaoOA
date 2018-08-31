package com.kuliao.oa.services;

import com.kuliao.oa.db.mapper.*;
import com.kuliao.oa.db.model.Role;
import com.kuliao.oa.db.model.User;
import com.kuliao.oa.db.model.UserRole;
import com.kuliao.oa.domain.UserInfo;
import com.kuliao.oa.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final static Logger log = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper urMapper;

    @Autowired
    private RoleMenuMapper rmMapper;

    @Autowired
    private RoleMenuOperationMapper rmoMapper;

    public List<Role> generateRoles() {
        return roleMapper.selectAll();
    }

    public List<UserInfo> selectByRoleId(Integer roleId) {
        return userMapper.selectByRoleId(roleId);
    }

    public List<UserInfo> selectNotInRole(Integer roleId) {
        return userMapper.selectNotInRole(roleId);
    }



    public int createRole(Role role) {
        log.debug("将要新增角色"+role);
        int status = roleMapper.insert(role);
        if(status!=1){
            throw new BusinessException("101001","新增角色失败");
        }
        return status;
    }

    public int setRoleForUser(UserRole userRole){
        log.debug("将要给用户Id为{}设置角色为{}",userRole.getUserId(),userRole.getRoleId());
        int status = urMapper.insert(userRole);
        if(status!=1){
            throw new BusinessException("101002","为用户新增角色失败");
        }
        return status;
    }

    public int deleteEmp(String empNo,Integer roleId){
        log.debug("将要从角色ID{}删除用户{}",roleId,empNo);
        int status = urMapper.deleteByUserRole(roleId,empNo);
        if(status!=1){
            throw new BusinessException("101003","从角色删除用户失败");
        }
        return status;
    }

    public int updateRole(Role role) {
        log.debug("将要修改角色为"+role);
        int status = roleMapper.updateByPrimaryKey(role);
        if(status!=1){
            throw new BusinessException("101004","修改角色失败！");
        }
        return status;
    }

    public int deleteRole(Integer roleId) {
        log.debug("将要删除id为{}的角色",roleId);
        //查出该角色下的所有用户，如果为空才能删除
        List<UserInfo> users = userMapper.selectByRoleId(roleId);

        if (users.size() != 0) {
            throw new BusinessException("101005", "删除角色下还有用户存在！");
        }


        //删除该角色所有的权限配置
        rmoMapper.deleteByRoleId(roleId);
        rmMapper.deleteByRoleId(roleId);

        return roleMapper.deleteById(roleId);
    }


}
