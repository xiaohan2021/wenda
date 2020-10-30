package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Description:
 * @Author: 小韩同学
 * @Date: 2020/10/30
 */
@Mapper
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, create_date, entity_id, entity_type, status ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    @Insert({ "insert into", TABLE_NAME, "(" , INSERT_FIELDS,
            ") values(#{userId}, #{content}, #{createDate}, #{entityId}, #{entityType}, #{status})"})
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELDS,  " from ", TABLE_NAME,
    " where entity_id = #{entityId} and entity_type = #{entityType} order by create_date desc"})
    List<Comment> getCommentsByEntity(@Param("entityId") int entityId,
                                         @Param("entityType") int entityType );

    @Select({"select count(id) from ", TABLE_NAME, " where entity_id = #{entityId} and entity_type = #{entityType}"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({"update comment set status = #{status} where id = #{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);
}
