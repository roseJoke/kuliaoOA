package com.kuliao.oa.db.mapper;

import com.kuliao.oa.db.model.Customer;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

@Component(value = "customerMapper")
public interface CustomerMapper {
    @Delete({
        "delete from customer",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into customer (id, cname, ",
        "cwork, cphone, cemail, ",
        "conmunacation, beizhu, ",
        "ctype)",
        "values (#{id,jdbcType=INTEGER}, #{cname,jdbcType=VARCHAR}, ",
        "#{cwork,jdbcType=VARCHAR}, #{cphone,jdbcType=VARCHAR}, #{cemail,jdbcType=VARCHAR}, ",
        "#{conmunacation,jdbcType=VARCHAR}, #{beizhu,jdbcType=VARCHAR}, ",
        "#{ctype,jdbcType=VARCHAR})"
    })
    int insert(Customer record);

    @Select({
        "select",
        "id, cname, cwork, cphone, cemail, conmunacation, beizhu, ctype",
        "from customer",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="cname", property="cname", jdbcType=JdbcType.VARCHAR),
        @Result(column="cwork", property="cwork", jdbcType=JdbcType.VARCHAR),
        @Result(column="cphone", property="cphone", jdbcType=JdbcType.VARCHAR),
        @Result(column="cemail", property="cemail", jdbcType=JdbcType.VARCHAR),
        @Result(column="conmunacation", property="conmunacation", jdbcType=JdbcType.VARCHAR),
        @Result(column="beizhu", property="beizhu", jdbcType=JdbcType.VARCHAR),
        @Result(column="ctype", property="ctype", jdbcType=JdbcType.VARCHAR)
    })
    Customer selectByPrimaryKey(Integer id);

    @Select({
        "select",
        "id, cname, cwork, cphone, cemail, conmunacation, beizhu, ctype",
        "from customer"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="cname", property="cname", jdbcType=JdbcType.VARCHAR),
        @Result(column="cwork", property="cwork", jdbcType=JdbcType.VARCHAR),
        @Result(column="cphone", property="cphone", jdbcType=JdbcType.VARCHAR),
        @Result(column="cemail", property="cemail", jdbcType=JdbcType.VARCHAR),
        @Result(column="conmunacation", property="conmunacation", jdbcType=JdbcType.VARCHAR),
        @Result(column="beizhu", property="beizhu", jdbcType=JdbcType.VARCHAR),
        @Result(column="ctype", property="ctype", jdbcType=JdbcType.VARCHAR)
    })
    List<Customer> selectAll();

    @Update({
        "update customer",
        "set cname = #{cname,jdbcType=VARCHAR},",
          "cwork = #{cwork,jdbcType=VARCHAR},",
          "cphone = #{cphone,jdbcType=VARCHAR},",
          "cemail = #{cemail,jdbcType=VARCHAR},",
          "conmunacation = #{conmunacation,jdbcType=VARCHAR},",
          "beizhu = #{beizhu,jdbcType=VARCHAR},",
          "ctype = #{ctype,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Customer record);

   @Select({"<script>",
            "SELECT * FROM customer ",
            "<where> 1= 1",
            "<if test='id!=null and id!=\"\"'>",
            "and id = #{id}",
            "</if>",
           "<if test='cname!=null and cname!=\"\"'>",
           "and cname like CONCAT('%', #{cname}, '%')",
           "</if>",
           "<if test='ctype!=null and ctype!=\"\"'>",
           "and ctype=#{ctype}",
           "</if>",
           "</where>",
            "</script>"})
   @Results({
           @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
           @Result(column="cname", property="cname", jdbcType=JdbcType.VARCHAR),
           @Result(column="cwork", property="cwork", jdbcType=JdbcType.VARCHAR),
           @Result(column="cphone", property="cphone", jdbcType=JdbcType.VARCHAR),
           @Result(column="cemail", property="cemail", jdbcType=JdbcType.VARCHAR),
           @Result(column="conmunacation", property="conmunacation", jdbcType=JdbcType.VARCHAR),
           @Result(column="beizhu", property="beizhu", jdbcType=JdbcType.VARCHAR),
           @Result(column="ctype", property="ctype", jdbcType=JdbcType.VARCHAR)
   })
    List<Customer> selectCustomerByCondition(@Param("id") Integer id, @Param("cname") String cname, @Param("ctype") String ctype);

}