package com.kuang.dao;

import com.kuang.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TagMapper {

    // crud
    // 查询全部
    List<Tag> getAllTags();

    // 通过id获取
    Tag getTagById(Integer id);

    // 新增
    int addTag(Tag Tag);

    // 修改
    int updateTag(Tag Tag);

    // 通过id删除
    int deleteTagById(Integer id);



    // 查询 标签总数
    int getTagCount();

}
