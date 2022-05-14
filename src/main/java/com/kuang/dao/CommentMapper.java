package com.kuang.dao;

import com.kuang.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {

    /**
     * 查询全部
     * */
    List<Comment> getAllComments();

    // 通过评论id获取
    Comment getCommentById(Integer id);

    // 新增
    int addComment(Comment comment);

    // 修改
    int updateComment(Comment comment);

    // 通过id删除
    int deleteCommentById(Integer id);



    // 根据博客id查询该博客文章下的所有评论 不包括回复
    List<Comment> getAllCommentsByBlogId(Integer id);

    // 根据博客id查询该博客文章下的所有回复
    List<Comment> getAllRepliesByBlogId(Integer id);


    // 逻辑删除 评论
    int removeCommentLogic(Integer id);

    // 查询 评论总数
    int getCommentCount();
}
