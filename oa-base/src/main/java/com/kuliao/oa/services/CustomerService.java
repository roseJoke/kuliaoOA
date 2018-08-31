package com.kuliao.oa.services;

import com.kuliao.oa.db.mapper.CustomerMapper;
import com.kuliao.oa.db.model.Customer;
import com.kuliao.oa.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * // * @author : Mingyue.Zhan
 * // * @version : 1.0
 * // * @date : 2018/7/20
 * // * @since : 1.5
 * //
 */

@Service
public class CustomerService {
    private final static Logger log = LoggerFactory.getLogger(CustomerService.class);
    @Autowired
    private CustomerMapper customerMapper;

    /**
     * 列出所有的客户信息
     *
     * @return
     */
    public List<Customer> selectAllCustomer() {
        return customerMapper.selectAll();
    }

    /**
     * 保存客户信息
     *
     * @param customer 客户所有信息
     */
    public void insertCustomer(Customer customer) {
        if (log.isDebugEnabled()) {
            log.debug("新增客户" + customer);
        }
        try {
            int result =  customerMapper.insert(customer);
        }catch (Exception e){
          throw  new BusinessException("103002","客户已存在");
        }
    }

    /**
     * 根据客户编号拿到客户信息
     *
     * @param id 客户编号
     * @return 客户对象
     */
    public Customer selectCustomerById(Integer id) {
        Customer customer = customerMapper.selectByPrimaryKey(id);
        return customer;
    }

    /**
     * 根据客户编号删除客户信息，
     *
     * @param id 客户编号
     */
    public void deleteCustomer(Integer id) {
        if (log.isDebugEnabled()) {
            log.debug("将要删除cid为{}的客户信息",id);
        }
        int i = customerMapper.deleteByPrimaryKey(id);
        if (i != 1) {
            throw new BusinessException("103004", "删除客户失败");
        }
    }

    /**
     * 修改客户信息。
     *
     * @param customer 修改后的客户所有信息
     */
    public int updateCustomer(Customer customer) {
        if (log.isDebugEnabled()) {
            log.debug("将要修改客户信息为" + customer);
        }
        int i = customerMapper.updateByPrimaryKey(customer);
        if (i != 1) {
            throw new BusinessException("103003", "修改客户信息失败");
        }
        return i;
    }

    /**
     * 根据客户的编号，姓名，类型。查找客户。
     *
     * @param id    客户编号
     * @param cname 客户姓名
     * @param ctype 客户类型
     * @return
     */
    public List<Customer> fuzzyQueryCustomer(Integer id, String cname, String ctype) {
        if (log.isDebugEnabled()){
            log.debug("将要开始搜索客户，客户编号{}，客户姓名{}，客户单位{}",id,cname,ctype);
        }
        List<Customer> customerList = customerMapper.selectCustomerByCondition(id, cname, ctype);
        return customerList;
    }
}
