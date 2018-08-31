package com.kuliao.oa.db.mapper;

import com.kuliao.oa.db.model.User;
import com.kuliao.oa.domain.UserAuth;
import com.kuliao.oa.domain.UserInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;


public interface UserMapper {
    @Delete({
        "delete from USER",
        "where user_id = #{userId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer userId);

    @Insert({
        "insert into USER (user_id, emp_no, ",
        "code, user_name, ",
        "supervisor_id, dept_id)",
        "values (#{userId,jdbcType=INTEGER}, #{empNo,jdbcType=VARCHAR}, ",
        "#{code,jdbcType=VARCHAR}, #{userName,jdbcType=VARCHAR}, ",
        "#{supervisorId,jdbcType=INTEGER}, #{deptId,jdbcType=INTEGER})"
    })
    int insert(User record);

    @Select({
            "select u.user_id,u.emp_no,u.code,u.user_name,d.dept_name",
            "from `USER` u INNER JOIN  DEPT d",
            "on u.dept_id = d.dept_id",
            "where u.user_id = #{userId,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
            @Result(column="emp_no", property="empNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
            @Result(column="user_name", property="username", jdbcType=JdbcType.VARCHAR),
            @Result(column="dept_name" , property = "deptName",jdbcType = JdbcType.VARCHAR)
    })
    UserInfo selectByPrimaryKey(Integer userId);

    @Select({
            "select",
            "user_id, emp_no, code, user_name, supervisor_id, dept_id",
            "from USER",
            "where emp_no = #{empNo,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="emp_no", property="empNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
            @Result(column="user_name", property="userName", jdbcType=JdbcType.VARCHAR),
            @Result(column="supervisor_id", property="supervisorId", jdbcType=JdbcType.INTEGER),
            @Result(column="dept_id", property="deptId", jdbcType=JdbcType.INTEGER)
    })
    User selectByEmpNo(String empNo);

    @Select({
            "select u.user_id,u.emp_no,u.code,u.user_name,d.dept_name",
            "from `USER` u INNER JOIN  DEPT d",
            "on u.dept_id = d.dept_id",
            "where u.user_id in",
            "(select user_id from USER_ROLE where role_id = #{roleId,jdbcType=INTEGER})"
    })
    @Results({
            @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
            @Result(column="emp_no", property="empNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
            @Result(column="user_name", property="username", jdbcType=JdbcType.VARCHAR),
            @Result(column="dept_name" , property = "deptName",jdbcType = JdbcType.VARCHAR)
    })
    List<UserInfo> selectByRoleId(Integer roleId);

    @Select({
            "select u.user_id,u.emp_no,u.code,u.user_name,d.dept_name",
            "from `USER` u INNER JOIN  DEPT d",
            "on u.dept_id = d.dept_id",
            "where u.user_id not in",
            "(select user_id from USER_ROLE where role_id = #{roleId,jdbcType=INTEGER})"
    })
    @Results({
            @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
            @Result(column="emp_no", property="empNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
            @Result(column="user_name", property="username", jdbcType=JdbcType.VARCHAR),
            @Result(column="dept_name" , property = "deptName",jdbcType = JdbcType.VARCHAR)
    })
    List<UserInfo> selectNotInRole(Integer roleId);

    @Select({
            "select",
            "u.user_id,u.emp_no,u.user_name,u.is_block,p.password,p.retry,p.last_login from",
            "(select * from `USER` where emp_no = #{empNo,jdbcType=VARCHAR}) u INNER JOIN PASSMANAGE p on u.user_id = p.user_id"
    })
    @Results({
            @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
            @Result(column="emp_no", property="empNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="user_name", property="username", jdbcType=JdbcType.VARCHAR),
            @Result(column="is_block", property="isBlock", jdbcType=JdbcType.TINYINT),
            @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
            @Result(column="retry", property="retry", jdbcType=JdbcType.TINYINT),
            @Result(column="last_login" , property = "lastLogin",jdbcType = JdbcType.VARCHAR)
    })
    UserAuth selectAuthByEmpNo(String empNo);


    @Select({
            "<script>",
            "select u.user_id,u.emp_no,u.code,u.user_name,d.dept_name",
            "from `USER` u INNER JOIN DEPT d",
            "on u.dept_id = d.dept_id",
            "<where>",
            "<if test='empNo != null'>"+
            "and emp_no like CONCAT(CONCAT('%',#{empNo}),'%')</if>",
            "<if test='username != null'>" +
                    "AND user_name like CONCAT(CONCAT('%',#{username}),'%')</if>",
            "<if test='deptName != null'>" +
                    "AND dept_name like CONCAT(CONCAT('%',#{deptName}),'%')</if>",
            "</where>",
            "</script>"
          })
    @Results({
            @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
            @Result(column="emp_no", property="empNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
            @Result(column="user_name", property="username", jdbcType=JdbcType.VARCHAR),
            @Result(column="dept_name" , property = "deptName",jdbcType = JdbcType.VARCHAR)
    })
    List<UserInfo> selectEmp(UserInfo userInfo);

    @Select({
        "select",
        "user_id, emp_no, code, user_name, supervisor_id, dept_id",
        "from USER"
    })
    @Results({
        @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="emp_no", property="empNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_name", property="userName", jdbcType=JdbcType.VARCHAR),
        @Result(column="supervisor_id", property="supervisorId", jdbcType=JdbcType.INTEGER),
        @Result(column="dept_id", property="deptId", jdbcType=JdbcType.INTEGER)
    })
    List<User> selectAll();

    @Update({
        "update USER",
        "set emp_no = #{empNo,jdbcType=VARCHAR},",
          "code = #{code,jdbcType=VARCHAR},",
          "user_name = #{userName,jdbcType=VARCHAR},",
          "supervisor_id = #{supervisorId,jdbcType=INTEGER},",
          "dept_id = #{deptId,jdbcType=INTEGER}",
          "is_block = #{isBlock,jdbcType=TINYINT}",
        "where user_id = #{userId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(User record);

    @Update({
            "update PASSMANAGE",
            "set retry = #{retry,jdbcType=TINYINT},",
            "last_login = now()",
            "where user_id = #{userId,jdbcType=INTEGER}"
    })
    void retryUpdate(@Param("retry") Byte retry,@Param("userId") Integer userId);

    @Update({
            "update `USER` u  JOIN PASSMANAGE p",
            "on u.user_id = p.user_id",
            "set u.is_block = #{isBlock,jdbcType=TINYINT},",
            "p.retry = #{retry,jdbcType=TINYINT}",
            "where u.emp_no= #{empNo,jdbcType=VARCHAR}"
    })
    void lockUser(@Param("isBlock") Byte isBlock,@Param("retry") Byte retry,@Param("empNo")String empNo);
}