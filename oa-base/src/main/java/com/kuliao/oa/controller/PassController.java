package com.kuliao.oa.controller;

import com.kuliao.oa.domain.RetCode;
import com.kuliao.oa.domain.RetEntity;
import com.kuliao.oa.services.PwdService;
import com.kuliao.oa.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${spring.application.name}/pwd")
public class PassController {
    @Autowired
    private PwdService pwdService;

    @RequestMapping("/reset")
    public ResponseEntity<RetEntity> resetPwd(String empNo){
        if(TokenUtils.empty(empNo)){
            throw new IllegalArgumentException("员工编号为空");
        }
        pwdService.resetPwd(empNo);
        RetEntity ret = new RetEntity(RetCode.SUCCESS.getRetCode());
        ret.setMsg("重置成功");
        return new ResponseEntity<RetEntity>(ret, HttpStatus.OK);
    }

    @RequestMapping("/modify")
    public ResponseEntity<RetEntity> modifyPwd(String empNo,String password){

        if(TokenUtils.empty(empNo)||TokenUtils.empty(password)){
            throw new IllegalArgumentException("员工编号或者密码为空!");
        }
        pwdService.modifyPwd(empNo,password);
        RetEntity ret = new RetEntity(RetCode.SUCCESS.getRetCode());
        ret.setMsg("修改密码成功！");
        return new ResponseEntity<RetEntity>(ret, HttpStatus.OK);
    }

}