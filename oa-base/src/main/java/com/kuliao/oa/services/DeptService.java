package com.kuliao.oa.services;

import com.kuliao.oa.db.mapper.DeptMapper;
import com.kuliao.oa.db.model.Dept;
import com.kuliao.oa.domain.DeptEmps;
import com.kuliao.oa.domain.MyDept;
import com.kuliao.oa.domain.UserAuth;
import com.kuliao.oa.domain.UserInfo;
import com.kuliao.oa.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeptService {
    private final static Logger log = LoggerFactory.getLogger(DeptService.class);

    @Autowired
    private DeptMapper deptMapper;

//    @Autowired
//    private UserMapper userMapper;
//
//    @Autowired
//    private RoleMenuMapper rmMapper;
//
//    @Autowired
//    private RoleMenuOperationMapper rmoMapper;

    public List<MyDept> generateDepts() {

        return deptMapper.getAllDept();
    }

    public MyDept getDeptById(Integer deptId){
        return deptMapper.selectByPrimaryKey(deptId);
    }

    public List<Dept> selectAll(){
        return deptMapper.selectAll();
    }

    /**
     * 获取部门-员工列表
     * 为了方便页面读取，用DEPT对象存储员工信息。
     * @return
     */
    public List<DeptEmps> selectDeptEmps(){
        List<DeptEmps> deptEmpsList = new ArrayList<DeptEmps>();
        //查询到不分层级的部门列表
        List<Dept> depts = deptMapper.selectAll();

        for(Dept dept:depts){
            if(dept.getDeptPId()!=null) {
                DeptEmps deptEmps = new DeptEmps();
                deptEmps.setDeptId(dept.getDeptId());
                deptEmps.setDeptName(dept.getDeptName());
                //查到部门中所有的用户列表
                List<UserInfo> users = deptMapper.selectByDeptId(dept.getDeptId());
                List<Dept> empDepts = new ArrayList<Dept>();
                //把员工信息放在Dept对象中
                for(UserInfo ui:users){
                    Dept empDept = new Dept();
                    empDept.setDeptId(ui.getUserId());
                    empDept.setDeptName(ui.getUsername());
                    empDepts.add(empDept);
                }
                deptEmps.setDeptEmps(empDepts);
                deptEmpsList.add(deptEmps);
            }
        }
        //返回部门及所属员工列表
        return deptEmpsList;
    }

    public int setDeptForUser(String empNo,Integer deptId){
        log.debug("将要把用户{}的部门改为{}",empNo,deptId);
        return deptMapper.setDeptForUser(empNo,deptId);
    }

    public int deleteEmp(String empNo){
        log.debug("将要删除{}的部门",empNo);
        int status = deptMapper.deleteDeptByUser(empNo);
        if(status!=1){
            throw new BusinessException("102001","从部门删除用户失败");
        }
        return status;
    }

    public List<UserInfo> getUserByDeptId(Integer deptId) {
        return deptMapper.selectByDeptId(deptId);
    }

    public int createDept(Dept dept) {
        log.debug("将要新增部门"+dept);
        int status = deptMapper.insert(dept);
        if(status!=1){
            throw new BusinessException("102002","新增部门失败");
        }
        return status;
    }

    public int updateDept(Dept dept) {
        log.debug("将要修改部门为"+dept);
        int status = deptMapper.updateByPrimaryKey(dept);
        if(status!=1){
            throw new BusinessException("102003","修改部门失败！");
        }
        return status;
    }

    public int deleteDept(Integer deptId) {
        log.debug("将要删除id为{}的部门",deptId);
        //查出该角色下的所有用户，如果为空才能删除
        List<UserInfo> users = deptMapper.selectByDeptId(deptId);

        if (users.size() != 0) {
            throw new BusinessException("102004", "删除部门下还有用户存在！");
        }

        return deptMapper.deleteByPrimaryKey(deptId);
    }


}
