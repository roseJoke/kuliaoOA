package com.kuliao.oa.db.mapper;

import com.kuliao.oa.db.model.Notice;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface NoticeMapper {
    @Delete({
        "delete from NOTICE",
        "where notice_id = #{noticeId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer noticeId);

    @Insert({
        "insert into NOTICE (notice_id, title, ",
        "content, publisher, ",
        "pub_time)",
        "values (#{noticeId,jdbcType=INTEGER}, #{title,jdbcType=VARCHAR}, ",
        "#{content,jdbcType=VARCHAR}, #{publisher,jdbcType=VARCHAR}, ",
        "#{pubTime,jdbcType=TIMESTAMP})"
    })
    int insert(Notice record);

    @Select({
        "select",
        "notice_id, title, content, publisher, pub_time",
        "from NOTICE",
        "where notice_id = #{noticeId,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="notice_id", property="noticeId", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="publisher", property="publisher", jdbcType=JdbcType.VARCHAR),
        @Result(column="pub_time", property="pubTime", jdbcType=JdbcType.TIMESTAMP)
    })
    Notice selectByPrimaryKey(Integer noticeId);

    @Select({
        "select",
        "notice_id, title, content, publisher, pub_time",
        "from NOTICE"
    })
    @Results({
        @Result(column="notice_id", property="noticeId", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="publisher", property="publisher", jdbcType=JdbcType.VARCHAR),
        @Result(column="pub_time", property="pubTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<Notice> selectAll();

    @Update({
        "update NOTICE",
        "set title = #{title,jdbcType=VARCHAR},",
          "content = #{content,jdbcType=VARCHAR},",
          "publisher = #{publisher,jdbcType=VARCHAR},",
          "pub_time = #{pubTime,jdbcType=TIMESTAMP}",
        "where notice_id = #{noticeId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Notice record);
}