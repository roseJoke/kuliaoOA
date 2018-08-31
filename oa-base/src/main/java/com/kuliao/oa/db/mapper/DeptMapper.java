package com.kuliao.oa.db.mapper;

import com.kuliao.oa.db.model.Dept;
import com.kuliao.oa.domain.MyDept;
import com.kuliao.oa.domain.UserInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface DeptMapper {
    @Delete({
        "delete from DEPT",
        "where dept_id = #{deptId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer deptId);

    @Insert({
        "insert into DEPT (dept_id, dept_name, ",
        "dept_p_id, user_id)",
        "values (#{deptId,jdbcType=INTEGER}, #{deptName,jdbcType=VARCHAR}, ",
        "#{deptPId,jdbcType=INTEGER}, #{userId,jdbcType=INTEGER})"
    })
    int insert(Dept record);

    @Select({
            "select dept_id,dept_name,user_id,dept_p_id",
            "from DEPT",
            "where dept_id = #{deptId,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="dept_id",jdbcType= JdbcType.INTEGER,property="deptId"),
            @Result(column="dept_name",jdbcType= JdbcType.VARCHAR,property="deptName"),
            @Result(column="user_id",jdbcType=JdbcType.VARCHAR,property="userId"),
            @Result(column="dept_p_id",jdbcType= JdbcType.INTEGER,property="deptPId"),
            @Result(column="dept_id",property = "subs",many = @Many(
                    select = "com.kuliao.oa.db.mapper.DeptMapper.selectDeptByParentId"
            ))

    })
    MyDept selectByPrimaryKey(Integer deptId);



    @Select({
        "select",
        "dept_id, dept_name,user_id,dept_p_id",
        "from DEPT"
    })
    @Results({
        @Result(column="dept_id", property="deptId", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="dept_name", property="deptName", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
        @Result(column="dept_p_id",jdbcType= JdbcType.INTEGER,property="deptPId"),
    })
    List<Dept> selectAll();

    @Select({
            "select dept_id,dept_name,user_id,dept_p_id",
            "from DEPT",
            "where dept_p_id is null"
    })
    @Results({
            @Result(column="dept_id",jdbcType= JdbcType.INTEGER,property="deptId"),
            @Result(column="dept_name",jdbcType= JdbcType.VARCHAR,property="deptName"),
            @Result(column="user_id",jdbcType=JdbcType.VARCHAR,property="userId"),
            @Result(column="dept_p_id",jdbcType= JdbcType.INTEGER,property="deptPId"),
            @Result(column="dept_id",property = "subs",many = @Many(
                    select = "com.kuliao.oa.db.mapper.DeptMapper.selectDeptByParentId"
            ))

    })
    List<MyDept> getAllDept();

    @Select({
            "select dept_id,dept_name,user_id,dept_p_id",
            "from DEPT",
            "where dept_p_id = #{deptPId,jdbcType=INTEGER}"
    })

    @Results({
            @Result(column="dept_id",jdbcType= JdbcType.INTEGER,property="deptId"),
            @Result(column="dept_name",jdbcType= JdbcType.VARCHAR,property="deptName"),
            @Result(column="user_id",jdbcType=JdbcType.VARCHAR,property="userId"),
            @Result(column="dept_p_id",jdbcType= JdbcType.INTEGER,property="deptPId"),
            @Result(column="dept_id",property = "subs",many = @Many(
                    select = "com.kuliao.oa.db.mapper.DeptMapper.selectDeptByParentId"
            ))

    })
    MyDept selectDeptByParentId(int deptPId);

    @Select({
            "select u.user_id,u.emp_no,u.code,u.user_name,d.dept_name",
            "from `USER` u INNER JOIN  DEPT d",
            "on u.dept_id = d.dept_id",
            "where u.dept_id = #{deptId,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
            @Result(column="emp_no", property="empNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
            @Result(column="user_name", property="username", jdbcType=JdbcType.VARCHAR),
            @Result(column="dept_name" , property = "deptName",jdbcType = JdbcType.VARCHAR)
    })
    List<UserInfo> selectByDeptId(int deptId);

    @Update({
        "update DEPT",
        "set dept_name = #{deptName,jdbcType=VARCHAR},",
          "dept_p_id = #{deptPId,jdbcType=INTEGER},",
          "user_id = #{userId,jdbcType=INTEGER}",
        "where dept_id = #{deptId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Dept record);


    @Update({
            "update `USER`",
            "set dept_id = null",
            "where emp_no = #{empNo,jdbcType=INTEGER}"
    })
    int deleteDeptByUser(String empNo);

    @Update({
            "update `USER`",
            "set dept_id = #{param2}",
            "where emp_no = #{param1}"
    })
    int setDeptForUser(String empNo,Integer deptId);
}