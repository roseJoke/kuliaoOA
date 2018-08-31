package com.kuliao.oa.services;

import com.kuliao.oa.db.mapper.MenuMapper;
import com.kuliao.oa.db.mapper.MenuOperateMapper;
import com.kuliao.oa.db.mapper.RoleMenuMapper;
import com.kuliao.oa.db.mapper.RoleMenuOperationMapper;
import com.kuliao.oa.db.model.Menu;
import com.kuliao.oa.db.model.RoleMenu;
import com.kuliao.oa.db.model.RoleMenuOperation;
import com.kuliao.oa.domain.MenuOperate;
import com.kuliao.oa.domain.UserAuth;
import com.kuliao.oa.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class MenuService {
    private final static Logger log = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private MenuOperateMapper moMapper;
    @Autowired
    private RoleMenuMapper rmMapper;
    @Autowired
    private RoleMenuOperationMapper rmoMapper;


    public List<MenuOperate> getMenuOByUser(UserAuth user) {
        if (log.isDebugEnabled()) {
            log.debug("用户{}开始查询菜单",user.getUsername());
        }

        //查出用户所有角色的菜单
        List<MenuOperate> menuOperates = moMapper.selectMenuOperateByEmpNo(user.getEmpNo());

       //去除重复菜单，处理重复菜单权限
        List<MenuOperate> noReMenuOperates = new ArrayList<MenuOperate>();

        for(MenuOperate mo:menuOperates) {

                    if (noReMenuOperates.size() == 0) {
                        noReMenuOperates.add(mo);
                    } else {
                        int isRepeat = 0;
                        for (MenuOperate nmo : noReMenuOperates) {
                            if (nmo.getMenuId().equals(mo.getMenuId())) {
                                isRepeat = 1;
                            }
                        }
                        if (isRepeat == 0) {
                            noReMenuOperates.add(mo);
                        }
                    }
        }
        //获得根菜单，并根据根菜单查询出子菜单
        List<MenuOperate> newMenuOperates = new ArrayList<MenuOperate>();
        for(MenuOperate mo:noReMenuOperates){
            if(mo.getParentId()==null){
                newMenuOperates.add(mo);
            }
        }
        return fixMenu(noReMenuOperates,newMenuOperates);
    }


    /**
     * 查询子菜单是否具有权限，删除没有权限的子菜单
     * @param parentMenu 有权限的菜单列表
     * @param menus 级联菜单集合
     * @return 删除后的级联菜单集合
     */
    public List<MenuOperate> fixMenu(List<MenuOperate> parentMenu,List<MenuOperate> menus){

            Iterator<MenuOperate> it = menus.iterator();
            while(it.hasNext()) {
                MenuOperate mo = it.next();
                int finded = 0;
                for (MenuOperate pmo : parentMenu) {
                    if (mo.getMenuId().equals(pmo.getMenuId())) {
                        finded =1;
                        mo.setCreate(pmo.getCreate());
                        mo.setDelete(pmo.getDelete());
                        mo.setUpdate(pmo.getUpdate());
                        mo.setSelect(pmo.getSelect());
                        break;
                    }
                }
                if(finded==0){
                    it.remove();
                }else if(mo.getSubs().size()!=0){
                    fixMenu(parentMenu,mo.getSubs());
                }


            }
            return menus;

    }

    public List<MenuOperate> selectAllMenus(){
        return moMapper.selectAllMenus();
    }

    /**
     * 设置菜单权限
     * @param roleId 设置的角色
     * @param menuId 设置的菜单
     * @param create 增加权限
     * @param delete 删除权限
     * @param update 修改权限
     * @param select 查看权限
     * @param isReset 属于更新权限？还是重置权限，更新指子菜单权限变动，导致的父菜单权限更新。false=更新权限，true重置权限
      * @return
     */
    public int setMenuAuth(Integer roleId,Integer menuId,Byte create,Byte delete,Byte update,Byte select,Boolean isReset){
        int status = 0;
        boolean flag = false;
        Menu menu = menuMapper.selectByPrimaryKey(menuId);
        if(create == 0 && delete == 0 && update == 0 && select == 0 && isReset){
            log.debug("正在设置菜单{}权限为空",menuId);
            flag = true;
        };

        RoleMenu roleMenu = new RoleMenu(roleId,menuId);
        //查询角色菜单是否已经关联
        RoleMenu newRoleMenu = rmMapper.isExist(roleMenu);
        log.debug("根据指定的用户、菜单查询到结果"+newRoleMenu);
        if(newRoleMenu==null){
            //如果角色菜单并未关联，则设置关联
                if(flag){
                    return 1;
                }
                rmMapper.insert(roleMenu);
                newRoleMenu = rmMapper.isExist(roleMenu);
                RoleMenuOperation rmo = new RoleMenuOperation(newRoleMenu.getId(), create, delete, update, select);
                log.debug("全新插入权限设置" + rmo);
                status = rmoMapper.insert(rmo);


        }else{
            //如果角色菜单已关联，查询是否设置权限
            RoleMenuOperation rmo = rmoMapper.selectByRoleMenuId(newRoleMenu.getId());
            log.debug("已经存在用户菜单关联，查询到权限设置为"+rmo);
            if(rmo==null){
                if(flag){
                    status = rmMapper.deleteByPrimaryKey(newRoleMenu.getId());
                    return status;
                }
                //如果未设置权限，则全新设置
                RoleMenuOperation newRmo = new RoleMenuOperation(newRoleMenu.getId(),create,delete,update,select);
                log.debug("全新设置权限为"+newRmo);
                status =rmoMapper.insert(newRmo);

            }else{
                if(flag){
                    rmoMapper.deleteByPrimaryKey(rmo.getId());
                    status = rmMapper.deleteByPrimaryKey(newRoleMenu.getId());
                    return status;
                }
                //判断是重置权限，还是子菜单影响父菜单权限更新
                if(isReset) {
                    log.debug("本次全新设置权限");
                    rmo.setCreate(create);
                    rmo.setDelete(delete);
                    rmo.setUpdate(update);
                    rmo.setSelect(select);
                }else{
                    log.debug("子菜单权限变更导致父级菜单更新");
                    if(rmo.getCreate()==0){
                        rmo.setCreate(create);
                    }
                    if(rmo.getDelete()==0){
                        rmo.setDelete(delete);
                    }
                    if(rmo.getUpdate()==0){
                        rmo.setUpdate(update);
                    }
                    if(rmo.getSelect()==0){
                        rmo.setSelect(select);
                    }
                }
                log.debug("修改权限设置为"+rmo);
                status = rmoMapper.updateByPrimaryKey(rmo);

            }
        }

        if(menu.getParentId()!=null){
            this.setMenuAuth(roleId,menu.getParentId(),create,delete,update,select,false);
        }
        if(status!=1){
            throw new BusinessException("100010","设置权限失败");
        }

        return status;
    }


}
