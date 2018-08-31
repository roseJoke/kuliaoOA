package com.kuliao.oa.db.mapper;

import com.kuliao.oa.db.model.RoleMenuOperation;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;


public interface RoleMenuOperationMapper {
    @Delete({
        "delete from ROLE_MENU_OPERATION",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Delete({
            "delete from ROLE_MENU_OPERATION",
            "where role_menu_id in",
            "(select id from ROLE_MENU where role_id = #{roleId,jdbcType=INTEGER})"
    })
    void deleteByRoleId(Integer roleId);


    @Insert({
        "insert into ROLE_MENU_OPERATION (id, role_menu_id, ",
        "`create`, `delete`, ",
        "`update`, `select`)",
        "values (#{id,jdbcType=INTEGER}, #{roleMenuId,jdbcType=INTEGER}, ",
        "#{create,jdbcType=TINYINT}, #{delete,jdbcType=TINYINT}, ",
        "#{update,jdbcType=TINYINT}, #{select,jdbcType=TINYINT})"
    })
    int insert(RoleMenuOperation record);

    @Select({
        "select",
        "id, role_menu_id, create, delete, update, select",
        "from ROLE_MENU_OPERATION",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="role_menu_id", property="roleMenuId", jdbcType=JdbcType.INTEGER),
        @Result(column="create", property="create", jdbcType=JdbcType.TINYINT),
        @Result(column="delete", property="delete", jdbcType=JdbcType.TINYINT),
        @Result(column="update", property="update", jdbcType=JdbcType.TINYINT),
        @Result(column="select", property="select", jdbcType=JdbcType.TINYINT)
    })
    RoleMenuOperation selectByPrimaryKey(Integer id);

    @Select({
            "select",
            "id, role_menu_id, `create`, `delete`, `update`, `select`",
            "from ROLE_MENU_OPERATION",
            "where role_menu_id = #{roleMenuId,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="role_menu_id", property="roleMenuId", jdbcType=JdbcType.INTEGER),
            @Result(column="create", property="create", jdbcType=JdbcType.TINYINT),
            @Result(column="delete", property="delete", jdbcType=JdbcType.TINYINT),
            @Result(column="update", property="update", jdbcType=JdbcType.TINYINT),
            @Result(column="select", property="select", jdbcType=JdbcType.TINYINT)
    })
    RoleMenuOperation selectByRoleMenuId(Integer roleMenuId);


    @Select({
        "select",
        "id, role_menu_id, create, delete, update, select",
        "from ROLE_MENU_OPERATION"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="role_menu_id", property="roleMenuId", jdbcType=JdbcType.INTEGER),
        @Result(column="create", property="create", jdbcType=JdbcType.TINYINT),
        @Result(column="delete", property="delete", jdbcType=JdbcType.TINYINT),
        @Result(column="update", property="update", jdbcType=JdbcType.TINYINT),
        @Result(column="select", property="select", jdbcType=JdbcType.TINYINT)
    })
    List<RoleMenuOperation> selectAll();

    @Select({
        "select rmo.id,rmo.role_menu_id,rmo.`create`,rmo.`delete`,rmo.`select`,rmo.`update`",
        "from ROLE_MENU_OPERATION rmo",
        "INNER JOIN (select id,menu_id from ROLE_MENU",
        "where role_id in",
        "(select role_id from USER_ROLE where user_id =",
        "(select user_id from `USER` where emp_no = #{empNo,jdbcType=VARCHAR}))) rm on rm.id=rmo.role_menu_id"

    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="role_menu_id", property="roleMenuId", jdbcType=JdbcType.INTEGER),
            @Result(column="create", property="create", jdbcType=JdbcType.TINYINT),
            @Result(column="delete", property="delete", jdbcType=JdbcType.TINYINT),
            @Result(column="update", property="update", jdbcType=JdbcType.TINYINT),
            @Result(column="select", property="select", jdbcType=JdbcType.TINYINT)
    })
    List<RoleMenuOperation> selectByEmpNo(String empNo);

    @Update({
        "update ROLE_MENU_OPERATION",
        "set role_menu_id = #{roleMenuId,jdbcType=INTEGER},",
          "`create` = #{create,jdbcType=TINYINT},",
          "`delete` = #{delete,jdbcType=TINYINT},",
          "`update` = #{update,jdbcType=TINYINT},",
          "`select` = #{select,jdbcType=TINYINT}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(RoleMenuOperation record);
}