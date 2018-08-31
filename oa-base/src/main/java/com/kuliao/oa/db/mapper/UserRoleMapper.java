package com.kuliao.oa.db.mapper;

import com.kuliao.oa.db.model.UserRole;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;


public interface UserRoleMapper {
    @Delete({
        "delete from USER_ROLE",
        "where role_id = #{param1}",
        "AND user_id = (select user_id from `USER` where emp_no = #{param2})"
    })
    int deleteByUserRole(Integer roleId,String empNo);

    @Insert({
        "insert into USER_ROLE (user_id, ",
        "role_id)",
        "values (#{userId,jdbcType=INTEGER}, ",
        "#{roleId,jdbcType=INTEGER})"
    })
    int insert(UserRole record);

    @Select({
        "select",
        "id, user_id, role_id",
        "from USER_ROLE",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.INTEGER)
    })
    UserRole selectByPrimaryKey(Integer id);

    @Select({
        "select",
        "id, user_id, role_id",
        "from USER_ROLE"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.INTEGER)
    })
    List<UserRole> selectAll();

    @Update({
        "update USER_ROLE",
        "set user_id = #{userId,jdbcType=INTEGER},",
          "role_id = #{roleId,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(UserRole record);
}