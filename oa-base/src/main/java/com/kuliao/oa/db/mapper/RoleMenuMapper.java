package com.kuliao.oa.db.mapper;

import com.kuliao.oa.db.model.RoleMenu;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;


public interface RoleMenuMapper {
    @Delete({
        "delete from ROLE_MENU",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Delete("delete from ROLE_MENU where role_id = #{roleId,jdbcType=INTEGER}")
    int deleteByRoleId(Integer roleId);

    @Insert({
        "insert into ROLE_MENU (id, role_id, ",
        "menu_id)",
        "values (#{id,jdbcType=INTEGER}, #{roleId,jdbcType=INTEGER}, ",
        "#{menuId,jdbcType=INTEGER})"
    })
    int insert(RoleMenu record);

    @Select({
        "select",
        "id, role_id, menu_id",
        "from ROLE_MENU",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.INTEGER),
        @Result(column="menu_id", property="menuId", jdbcType=JdbcType.INTEGER)
    })
    RoleMenu selectByPrimaryKey(Integer id);

    @Select({
        "select",
        "id, role_id, menu_id",
        "from ROLE_MENU"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.INTEGER),
        @Result(column="menu_id", property="menuId", jdbcType=JdbcType.INTEGER)
    })
    List<RoleMenu> selectAll();

    @Update({
        "update ROLE_MENU",
        "set role_id = #{roleId,jdbcType=INTEGER},",
          "menu_id = #{menuId,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(RoleMenu record);

    @Select({
            "select",
            "id, role_id, menu_id",
            "from ROLE_MENU",
            "where role_id = #{roleId,jdbcType=INTEGER}",
            "and menu_id = #{menuId,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="role_id", property="roleId", jdbcType=JdbcType.INTEGER),
            @Result(column="menu_id", property="menuId", jdbcType=JdbcType.INTEGER)
    })
    RoleMenu isExist(RoleMenu roleMenu);
}