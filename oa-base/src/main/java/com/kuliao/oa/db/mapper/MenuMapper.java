package com.kuliao.oa.db.mapper;

import com.kuliao.oa.db.model.Menu;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;


public interface MenuMapper {
    @Delete({
        "delete from MENU",
        "where menu_id = #{menuId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer menuId);

    @Insert({
        "insert into MENU (menu_id, icon, ",
        "menu_name, parent_id, ",
        "index)",
        "values (#{menuId,jdbcType=INTEGER}, #{icon,jdbcType=VARCHAR}, ",
        "#{menuName,jdbcType=VARCHAR}, #{parentId,jdbcType=INTEGER}, ",
        "#{index,jdbcType=VARCHAR})"
    })
    int insert(Menu record);

    @Select({
        "select",
        "menu_id, icon, menu_name, parent_id, 'index'",
        "from MENU",
        "where menu_id = #{menuId,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="menu_id", property="menuId", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="icon", property="icon", jdbcType=JdbcType.VARCHAR),
        @Result(column="menu_name", property="menuName", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_id", property="parentId", jdbcType=JdbcType.INTEGER),
        @Result(column="index", property="index", jdbcType=JdbcType.VARCHAR)
    })
    Menu selectByPrimaryKey(Integer menuId);

    @Select({
        "select",
        "menu_id, icon, menu_name, parent_id, 'index'",
        "from MENU"
    })
    @Results({
        @Result(column="menu_id", property="menuId", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="icon", property="icon", jdbcType=JdbcType.VARCHAR),
        @Result(column="menu_name", property="menuName", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_id", property="parentId", jdbcType=JdbcType.INTEGER),
        @Result(column="index", property="index", jdbcType=JdbcType.VARCHAR)
    })
    List<Menu> selectAll();

    @Select({
            "select menu_id,icon,menu_name,parent_id,`index`",
            "from MENU where menu_id in",
            "(select menu_id from ROLE_MENU",
            "where role_id in",
            "(select role_id from USER_ROLE where user_id = (SELECT user_id from `USER`",
            "where emp_no = #{empNo,jdbcType=VARCHAR})))"
    })
    @Results({
            @Result(column="menu_id", property="menuId", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="icon", property="icon", jdbcType=JdbcType.VARCHAR),
            @Result(column="menu_name", property="menuName", jdbcType=JdbcType.VARCHAR),
            @Result(column="parent_id", property="parentId", jdbcType=JdbcType.INTEGER),
            @Result(column="index", property="index", jdbcType=JdbcType.VARCHAR)
    })
    List<Menu> selectMenuByEmpno(String empNo);

    @Update({
        "update MENU",
        "set icon = #{icon,jdbcType=VARCHAR},",
          "menu_name = #{menuName,jdbcType=VARCHAR},",
          "parent_id = #{parentId,jdbcType=INTEGER},",
          "index = #{index,jdbcType=VARCHAR}",
        "where menu_id = #{menuId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Menu record);
}