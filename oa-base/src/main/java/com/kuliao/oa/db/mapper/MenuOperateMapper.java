package com.kuliao.oa.db.mapper;

import com.kuliao.oa.domain.MenuOperate;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;


public interface MenuOperateMapper {

    @Select({
        "select rmom.menu_id,rmom.icon,rmom.menu_name,rmom.index,rmom.parent_id,rmo.`create`,rmo.`delete`,rmo.`select`,rmo.`update`",
        "from ROLE_MENU_OPERATION rmo INNER JOIN",
        "(select m.menu_id,m.icon,m.menu_name,m.parent_id,m.index,rm.id",
        "from MENU m INNER JOIN (select id,menu_id from ROLE_MENU",
        "where role_id in (select role_id from USER_ROLE",
        "where user_id = (SELECT user_id from `USER` where emp_no = #{empNo,jdbcType=VARCHAR}))) rm",
        "on rm.menu_id=m.menu_id) rmom on rmom.id = rmo.role_menu_id"

    })
    @Results({
            @Result(column="menu_id",jdbcType= JdbcType.INTEGER,property="menuId"),
            @Result(column="menu_name",jdbcType= JdbcType.VARCHAR,property="menuName"),
            @Result(column="index",jdbcType=JdbcType.VARCHAR,property="index"),
            @Result(column="icon",jdbcType=JdbcType.VARCHAR,property="icon"),
            @Result(column="parent_id",jdbcType= JdbcType.INTEGER,property="parentId"),
            @Result(column="menu_id",property = "subs",many = @Many(
                    select = "com.kuliao.oa.db.mapper.MenuOperateMapper.selectMenuByParentId"

            )),
            @Result(column="create", property="create", jdbcType=JdbcType.TINYINT),
            @Result(column="delete", property="delete", jdbcType=JdbcType.TINYINT),
            @Result(column="update", property="update", jdbcType=JdbcType.TINYINT),
            @Result(column="select", property="select", jdbcType=JdbcType.TINYINT)
    })
    List<MenuOperate> selectMenuOperateByEmpNo(String empNo);

    @Select({
           "select menu_id,menu_name,icon,`index`,parent_id from MENU",
            "where parent_id = #{parentId,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="menu_id",jdbcType= JdbcType.INTEGER,property="menuId"),
            @Result(column="menu_name",jdbcType= JdbcType.VARCHAR,property="menuName"),
            @Result(column="index",jdbcType=JdbcType.VARCHAR,property="index"),
            @Result(column="icon",jdbcType=JdbcType.VARCHAR,property="icon"),
            @Result(column="parent_id",jdbcType= JdbcType.INTEGER,property="parentId"),
            @Result(column="menu_id",property = "subs",many = @Many(
                    select = "com.kuliao.oa.db.mapper.MenuOperateMapper.selectMenuByParentId"
            ))

    })
    List<MenuOperate> selectMenuByParentId(Integer parentId);

    @Select({
            "select menu_id,menu_name,icon,`index`,parent_id from MENU",
            "where parent_id is null"
    })
    @Results({
            @Result(column="menu_id",jdbcType= JdbcType.INTEGER,property="menuId"),
            @Result(column="menu_name",jdbcType= JdbcType.VARCHAR,property="menuName"),
            @Result(column="index",jdbcType=JdbcType.VARCHAR,property="index"),
            @Result(column="icon",jdbcType=JdbcType.VARCHAR,property="icon"),
            @Result(column="parent_id",jdbcType= JdbcType.INTEGER,property="parentId"),
            @Result(column="menu_id",property = "subs",many = @Many(
                    select = "com.kuliao.oa.db.mapper.MenuOperateMapper.selectMenuByParentId"
            ))

    })
    List<MenuOperate> selectAllMenus();

}

