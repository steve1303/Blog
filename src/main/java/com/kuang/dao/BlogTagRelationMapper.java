package com.kuang.dao;

import com.kuang.pojo.BlogTagRelation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BlogTagRelationMapper {

    // crud
    // 查询全部
    List<BlogTagRelation> getAllBlogTags();

    // 通过博客id获取标签id
    List<BlogTagRelation> getBlogTagById(Integer id);

    // 新增
    int addBlogTag(BlogTagRelation blogTagRelation);

    // 修改
    int updateBlogTag(BlogTagRelation blogTagRelation);

    // 通过id删除
    int deleteBlogTagById(Integer id);

}
