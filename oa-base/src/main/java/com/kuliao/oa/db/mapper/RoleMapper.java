package com.kuliao.oa.db.mapper;

import com.kuliao.oa.db.model.Role;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;


public interface RoleMapper {
    @Delete({
        "delete from ROLE",
        "where role_id = #{roleId,jdbcType=INTEGER}"
    })
    int deleteById(Integer roleId);

    @Insert({
        "insert into ROLE (role_id, role_name)",
        "values (#{roleId,jdbcType=INTEGER}, #{roleName,jdbcType=VARCHAR})"
    })
    int insert(Role record);


    @Update({
        "update ROLE",
        "set role_name = #{roleName,jdbcType=VARCHAR}",
        "where role_id = #{roleId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Role record);

    @Select({
            "select",
            "role_id, role_name",
            "from ROLE",
            "where role_id = #{roleId,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="role_id", property="roleId", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="role_name", property="roleName", jdbcType=JdbcType.VARCHAR)
    })
    Role selectByPrimaryKey(Integer roleId);

    @Select({
            "select",
            "role_id, role_name",
            "from ROLE"
    })
    @Results({
            @Result(column="role_id", property="roleId", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="role_name", property="roleName", jdbcType=JdbcType.VARCHAR)
    })
    List<Role> selectAll();
}