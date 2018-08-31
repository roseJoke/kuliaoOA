package com.kuliao.oa.services;

import com.kuliao.oa.db.mapper.DeptMapper;
import com.kuliao.oa.db.mapper.PassmanageMapper;
import com.kuliao.oa.db.mapper.UserMapper;
import com.kuliao.oa.db.model.Dept;
import com.kuliao.oa.db.model.Passmanage;
import com.kuliao.oa.db.model.User;
import com.kuliao.oa.domain.UserAuth;
import com.kuliao.oa.domain.UserInfo;
import com.kuliao.oa.exception.BusinessException;
import com.kuliao.oa.utils.Coder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private PassmanageMapper pmMapper;
    @Autowired
    private TokenService tokenService;
    /**
     * @param empNo    员工编号
     * @param password 密码
     * @return UserAuth 用户信息
     */
    public UserAuth login(String empNo, String password) {

        UserAuth user = userMapper.selectAuthByEmpNo(empNo);
        if (user == null) {
            throw new BusinessException("100001", "用户不存在");
        }

        if (log.isDebugEnabled()) {
            log.debug("通过员工编号获取到用户信息" + user);
        }
        //判断用户是否锁定
        if (user.getIsBlock() == 1) {
            throw new BusinessException("100002", "用户已过期");

        }
        if (user.getRetry() >= 3) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            long loginTime = 0;
            try {
                date = sdf.parse(user.getLastLogin());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(date!=null) {
                loginTime = System.currentTimeMillis() - date.getTime();
            }
            //用户登录失败超过30分钟则自动解除锁定，如果不够30分钟抛出异常
            if (loginTime <= (1000 * 60 * 30)) {
                long minute = loginTime / 1000 / 60;

                throw new BusinessException("100003", "用户密码错误3次已被锁定，还有" + (30 - minute) + "分钟");
            } else {
                //够30分钟，自动解除锁定
                userMapper.retryUpdate((byte) 0, user.getUserId());
            }
            //密码错误后，重试次数加1，并抛出业务异常
        }
        try {
            password = Coder.decode(password,tokenService.getTokenCache());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将密码加密
        password = Coder.encode(password, Coder.getAppName());
        if (!password.equals(user.getPassword())) {

            userMapper.retryUpdate((byte) (user.getRetry() + 1), user.getUserId());
            throw new BusinessException("100004", "密码错误");
        }


        return user;
    }

    public String lockUser(String empNo) {
        UserAuth user = userMapper.selectAuthByEmpNo(empNo);
        log.debug("获取用户成功，信息为：{}", user);
        Byte isBlock = user.getIsBlock();
        if (isBlock == 1) {
            log.debug("用户为锁定状态");
            userMapper.lockUser((byte) 0, (byte) 0, empNo);
            return "解锁用户成功！";
        } else if (isBlock == 0) {
            log.debug("即将锁定用户，编号" + empNo);
            userMapper.lockUser((byte) 1, (byte) 0, empNo);
            return "锁定用户成功！";
        }
        return null;
    }

    public int registerUser(String empNo,String username,String code,Integer deptId){
        int status = 0;
        if (userMapper.selectByEmpNo(empNo) != null) {
            throw new IllegalArgumentException("员工编号已存在！");
        }
        User user = new User();
        //查出对应部门的主管ID
        user.setSupervisorId(deptMapper.selectByPrimaryKey(deptId).getUserId());
        user.setDeptId(deptId);
        user.setEmpNo(empNo);
        user.setCode(code);
        user.setUserName(username);
        //插入员工信息
        status = userMapper.insert(user);
        if(status!=1){
            throw new BusinessException("100010","插入用户失败");
        }
        //插入密码信息
        Passmanage pm = new Passmanage();
        pm.setRetry((byte) 0);
        pm.setUserId(userMapper.selectByEmpNo(user.getEmpNo()).getUserId());
        //默认密码设置为111111
        pm.setPassword(Coder.encode("111111",Coder.getAppName()));
        status = pmMapper.insert(pm);
        return status;
    }

    public List<UserInfo> getWorkmates(String empNo){
        User user = userMapper.selectByEmpNo(empNo);
        log.debug("service根据编号{}获取到用户{}",empNo,user);
        List<UserInfo> workmates = deptMapper.selectByDeptId(user.getDeptId());
        log.debug("service获取到同事信息{}",workmates);
        return workmates;
    }

    public List<UserInfo> selectEmp(UserInfo user){

        return  userMapper.selectEmp(user);
    }
    public UserAuth getUserAuthByEmpNo(String empNo) {
        return userMapper.selectAuthByEmpNo(empNo);
    }



    public UserInfo getUserById(Integer userId){
        return userMapper.selectByPrimaryKey(userId);
    }

}
