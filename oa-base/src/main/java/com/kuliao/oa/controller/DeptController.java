package com.kuliao.oa.controller;

import com.kuliao.oa.db.model.Dept;
import com.kuliao.oa.domain.*;
import com.kuliao.oa.services.DeptService;
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
@RequestMapping("/${spring.application.name}/dept")
public class DeptController {
    private final static Logger log = LoggerFactory.getLogger(DeptController.class);
    @Autowired
    private DeptService deptService;

    @RequestMapping("/generate")
    public ResponseEntity<RetEntity<List<MyDept>>> generateDepts(){
        List<MyDept> depts = deptService.generateDepts();
        log.debug("成功获取到部门列表"+depts);
        RetEntity<List<MyDept>> ret = new RetEntity<List<MyDept>>(RetCode.SUCCESS.getRetCode());
        ret.setMsg("获取部门成功");
        ret.setData(depts);
        return new ResponseEntity<RetEntity<List<MyDept>>>(ret, HttpStatus.OK);
    }

    @RequestMapping("/getDeptById")
    public ResponseEntity<RetEntity<MyDept>> getDeptById(Integer deptId){
        log.debug("使用部门ID：{}获取部门信息",deptId);
        if(deptId==null){
            throw new IllegalArgumentException("获取部门ID错误");
        }
        MyDept dept = deptService.getDeptById(deptId);
        log.debug("获取到部门"+dept);
        RetEntity<MyDept> ret = new RetEntity<MyDept>(RetCode.SUCCESS.getRetCode());
        ret.setMsg("获取部门成功");
        ret.setData(dept);
        return new ResponseEntity<RetEntity<MyDept>>(ret, HttpStatus.OK);
    }

    @RequestMapping("/selectAll")
    public ResponseEntity<RetEntity<List<Dept>>> selectAll(){
        List<Dept> depts = deptService.selectAll();
        log.debug("不包含子部门的部门列表获取{}",depts);
        RetEntity<List<Dept>> ret = new RetEntity<List<Dept>>(RetCode.SUCCESS.getRetCode());
        ret.setMsg("获取不包含子部门的部门列表成功");
        ret.setData(depts);

        return new ResponseEntity<RetEntity<List<Dept>>>(ret, HttpStatus.OK);
    }

    @RequestMapping("/selectDeptEmps")
    public ResponseEntity<RetEntity<List<DeptEmps>>> selectDeptEmps(){
        List<DeptEmps> deptEmpsList = deptService.selectDeptEmps();
        log.debug("获取部门-员工树列表{}",deptEmpsList);
        RetEntity<List<DeptEmps>> ret = new RetEntity<List<DeptEmps>>(RetCode.SUCCESS.getRetCode());
        ret.setMsg("获取部门-员工树列表成功");
        ret.setData(deptEmpsList);

        return new ResponseEntity<RetEntity<List<DeptEmps>>>(ret, HttpStatus.OK);
    }


    @RequestMapping("/userInDept")
    public ResponseEntity<RetEntity<List<UserInfo>>> getUserByDeptId(Integer deptId){
        log.debug("controller根据部门ID：{}获取员工",deptId);
        if(deptId==null){
            throw new IllegalArgumentException("根据部门ID获取员工参数为空");
        }
        List<UserInfo> users = deptService.getUserByDeptId(deptId);
        log.debug("根据部门获取到用户列表",users);
        RetEntity<List<UserInfo>> ret = new RetEntity<List<UserInfo>>(RetCode.SUCCESS.getRetCode());
        ret.setMsg("根据部门获取用户成功");
        ret.setData(users);

        return new ResponseEntity<RetEntity<List<UserInfo>>>(ret, HttpStatus.OK);
    }

    @RequestMapping("/setDeptForUser")
    public ResponseEntity<RetEntity> setDeptForUser(String empNo,Integer deptId){
        log.debug("将要给员工{}设置部门{}",empNo,deptId);
        if(empNo==null||deptId==null){
            throw new IllegalArgumentException("调整部门参数为空");
        }

        deptService.setDeptForUser(empNo,deptId);
        RetEntity ret = new RetEntity(RetCode.SUCCESS.getRetCode());
        ret.setMsg("调整部门成功");

        return new ResponseEntity<RetEntity>(ret, HttpStatus.OK);
    }

    @RequestMapping("/deleteEmp")
    public ResponseEntity<RetEntity> deleteEmp(String empNo){
        if(empNo==null){
            throw new IllegalArgumentException("删除员工编号为空！");
        }
        deptService.deleteEmp(empNo);
        RetEntity ret = new RetEntity(RetCode.SUCCESS.getRetCode());
        ret.setMsg("移除人员成功!");
        return new ResponseEntity<RetEntity>(ret, HttpStatus.OK);
    }

    @RequestMapping("/create")
    public ResponseEntity<RetEntity> createDept(@Valid Dept dept, BindingResult result){
        ValidateUtil.validata(result);
        deptService.createDept(dept);
        RetEntity ret = new RetEntity(RetCode.SUCCESS.getRetCode());
        ret.setMsg("新增部门成功");

        return new ResponseEntity<RetEntity>(ret, HttpStatus.OK);
    }

    @RequestMapping("/update")
    public ResponseEntity<RetEntity> updateDept(@Valid Dept dept, BindingResult result){
        ValidateUtil.validata(result);
        if(dept.getDeptId()==null){
            throw new IllegalArgumentException("修改部门参数错误！");
        }
        deptService.updateDept(dept);
        RetEntity ret = new RetEntity(RetCode.SUCCESS.getRetCode());
        ret.setMsg("修改部门成功");
        return new ResponseEntity<RetEntity>(ret, HttpStatus.OK);
    }

    @RequestMapping("/delete")
    public ResponseEntity<RetEntity> deleteDept(Integer deptId){
        if(deptId==null){
            throw new IllegalArgumentException("删除部门参数错误！");
        }

        deptService.deleteDept(deptId);

        RetEntity ret = new RetEntity(RetCode.SUCCESS.getRetCode());
        ret.setMsg("删除成功");
        return new ResponseEntity<RetEntity>(ret, HttpStatus.OK);
    }


}
