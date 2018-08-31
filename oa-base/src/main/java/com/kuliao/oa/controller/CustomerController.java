package com.kuliao.oa.controller;

import com.kuliao.oa.db.model.Customer;
import com.kuliao.oa.domain.RetCode;
import com.kuliao.oa.domain.RetEntity;
import com.kuliao.oa.exception.BusinessException;
import com.kuliao.oa.services.CustomerService;
import com.kuliao.oa.utils.TokenUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
/**
 * @author : Mingyue.Zhan
 * @version : 1.0
 * @date : 2018/7/20
 * @since : 1.5
 */

/**
 * 在这里注册了一个客户类的控制器。
 */
@RequestMapping("/${spring.application.name}/customer")
@RestController
public class CustomerController {
    private final static Logger log = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseEntity<RetEntity<List<Customer>>> queryAll() {
        List<Customer> list = customerService.selectAllCustomer();
        if (log.isDebugEnabled()) {
            log.debug("获取到客户列表信息" + list);
        }
        RetEntity<List<Customer>> retEntity = new RetEntity<>(RetCode.SUCCESS.getRetCode());
        retEntity.setMsg("获取客户列表成功");
        retEntity.setData(list);
        return new ResponseEntity<RetEntity<List<Customer>>>(retEntity, HttpStatus.OK);
    }
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<RetEntity> save(@RequestBody @Valid Customer customer) {
        if (customer == null || TokenUtils.empty(customer.getCname())) {
            throw new IllegalArgumentException("新增客户参数有误");
        }
        customerService.insertCustomer(customer);
        RetEntity retEntity = new RetEntity<>(RetCode.SUCCESS.getRetCode());
        retEntity.setMsg("保存客户成功");
        return new ResponseEntity<RetEntity>(retEntity, HttpStatus.OK);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public ResponseEntity<RetEntity> modify(@RequestBody @Valid Customer customer) {
        if (customer == null || TokenUtils.empty(customer.getCname()) || customer.getId() == null) {
            throw new IllegalArgumentException("修改客户参数有误");
        }
        Integer num = customerService.updateCustomer(customer);
        RetEntity<Customer> retEntity = new RetEntity<>(RetCode.SUCCESS.getRetCode());
        if (num == null) {
            retEntity.setMsg("修改客户失败");
            return new ResponseEntity<RetEntity>(retEntity, HttpStatus.BAD_REQUEST);
        } else {
            retEntity.setMsg("修改客户成功");
            return new ResponseEntity<RetEntity>(retEntity, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<RetEntity> delCustomer(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("删除客户参数有误");
        }
        customerService.deleteCustomer(id);
        RetEntity<Customer> retEntity = new RetEntity<>(RetCode.SUCCESS.getRetCode());
        retEntity.setMsg("删除客户成功");
        return new ResponseEntity<>(retEntity, HttpStatus.OK);
    }

    @RequestMapping(value = "/getCustomerById", method = RequestMethod.POST)
    public ResponseEntity<RetEntity<Customer>> getCustomer(Integer id) {
        if (log.isDebugEnabled()) {
            log.debug("将要获取id为{}" + id);
        }
        if (id == null) {
            throw new IllegalArgumentException("获取客户参数有误");
        }
        Customer customer = customerService.selectCustomerById(id);
        if (customer == null) {
            throw new BusinessException("103002", "客户不存在");
        }
        RetEntity<Customer> retEntity = new RetEntity<>(RetCode.SUCCESS.getRetCode());
        retEntity.setMsg("获取成功");
        retEntity.setData(customer);
        return new ResponseEntity<RetEntity<Customer>>(retEntity, HttpStatus.OK);
    }

    @RequestMapping(value = "/more", method = RequestMethod.POST)
    public ResponseEntity<RetEntity<List<Customer>>> getCustomerBySomeCondition(@Param("id") Integer id, @Param("cname") String cname, @Param("ctype") String ctype) {
        if (log.isDebugEnabled()) {
            log.debug("模糊查询开始");
        }
        if (log.isDebugEnabled()) {
            log.debug("获取到参数id={},cname={},ctype={}", id, cname, ctype);
        }
        List<Customer> customerList = customerService.fuzzyQueryCustomer(id, cname, ctype);
        RetEntity<List<Customer>> listRetEntity = new RetEntity<>(RetCode.SUCCESS.getRetCode());
        listRetEntity.setMsg("获取成功");
        listRetEntity.setData(customerList);
        return new ResponseEntity<>(listRetEntity, HttpStatus.OK);
    }

    /**
     * 默认异常处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity exception(MethodArgumentNotValidException e) {
        StringBuilder message = new StringBuilder();
        MethodArgumentNotValidException exception = e;
        exception.getParameter();
        exception.getBindingResult().getAllErrors().forEach(v -> {
            message.append("<br>" + v.getDefaultMessage());
        });
        RetEntity ret = new RetEntity(RetCode.DEFAULT_ERR.getRetCode());
        ret.setMsg(message.toString());
        return new ResponseEntity(ret, HttpStatus.BAD_REQUEST);
    }
}
