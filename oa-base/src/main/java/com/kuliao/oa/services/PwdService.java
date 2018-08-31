package com.kuliao.oa.services;

import com.kuliao.oa.db.mapper.PassmanageMapper;
import com.kuliao.oa.utils.Coder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PwdService {
    private final static Logger log = LoggerFactory.getLogger(PwdService.class);

    @Autowired
    private PassmanageMapper pmMapper;

    /**
     * 以111111重置用户密码
     * @param empNo 需要重置用户的员工编号
     */
    public void resetPwd(String empNo){
        log.debug("将要重置员工编号为{}的密码",empNo);
        //将密码用3des加密，并写入数据库，秘钥为applicationName
        String password = Coder.encode("111111",Coder.getAppName());
        pmMapper.modifyByUser(empNo,password);
    }


    /**
     * 修改密码
     * @param empNo 需要修改用户的员工编号
     * @param password 需要修改后的密码
     */
    public void modifyPwd(String empNo,String password){
        log.debug("将要修改员工编号为{}的密码为{}",empNo,password);
        //将密码用3des加密，并写入数据库，秘钥为applicationName
        password = Coder.encode(password,Coder.getAppName());
        pmMapper.modifyByUser(empNo,password);
    }



}
