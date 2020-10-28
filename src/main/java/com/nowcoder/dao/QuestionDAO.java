package com.nowcoder.dao;

import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Description:
 * @Author: 小韩同学
 * @Date: 2020/10/24
 */
@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    @Insert({ "insert into", TABLE_NAME, "(" , INSERT_FIELDS,
            ") values(#{title}, #{content}, #{createdDate}, #{userId}, #{commentCount})"})
    int addQuestion(Question question);

    List<Question> selectLatestQuestions(@Param("userId") int userId,
                              @Param("offset") int offset,
                              @Param("limit") int limit);


    @Update({"update ", TABLE_NAME, " set comment_count=#{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);
}
