package com.kuliao.oa.db.mapper;

import com.kuliao.oa.db.model.Passmanage;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface PassmanageMapper {
    @Delete({
        "delete from PASSMANAGE",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into PASSMANAGE (id, user_id, ",
        "password, retry)",
        "values (#{id,jdbcType=INTEGER}, #{userId,jdbcType=INTEGER}, ",
        "#{password,jdbcType=VARCHAR}, #{retry,jdbcType=TINYINT})"
    })
    int insert(Passmanage record);

    @Select({
        "select",
        "id, user_id, password, retry",
        "from PASSMANAGE",
        "where user_id = #{userId,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
        @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
        @Result(column="expired", property="expired", jdbcType=JdbcType.TINYINT)
    })
    Passmanage selectByUserId(Integer userId);

    @Select({
        "select",
        "id, user_id, password, expired",
        "from PASSMANAGE"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
        @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
        @Result(column="retry", property="expired", jdbcType=JdbcType.TINYINT)
    })
    List<Passmanage> selectAll();

    @Update({
        "update PASSMANAGE",
        "set user_id = #{userId,jdbcType=INTEGER},",
          "password = #{password,jdbcType=VARCHAR},",
          "expired = #{retry,jdbcType=TINYINT}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int update(Passmanage record);

    @Update({
            "update PASSMANAGE",
            "SET password = #{password,jdbcType=VARCHAR},",
            "retry = '0'",
            "where user_id =",
            "(select user_id from USER where emp_no = #{empNo,jdbcType=VARCHAR})"
    })
    void modifyByUser(@Param("empNo") String empNo,@Param("password") String password);

  }