package com.kuang.dao;

import com.kuang.pojo.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BlogMapper {

    // crud
    // 查询全部
    List<Blog> getAllBlogs();

    // 通过id获取
    Blog getBlogById(Integer id);

    // 新增
    int addBlog(Blog blog);

    // 修改
    int updateBlog(Blog blog);

    // 通过id删除
    int deleteBlogById(Integer id);



    // 移动至回收站 逻辑删除
    int removeBlogById(Integer id);

    // 查询 标签总数
    int getBlogCount();

    // 查询最新的10条博客
    List<Blog> getBlogsNew();

    // 查询最热的10条博客
    List<Blog> getBlogsHot();

    // 根据标签名查询博客 三表联查
    List<Blog> getBlogsByTagName(String tagName);

    // 多字段模糊查询
    List<Blog> getAllBlogsIfFore(@Param(value = "keyword") String keyword);

}
