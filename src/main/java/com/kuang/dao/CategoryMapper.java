package com.kuang.dao;

import com.kuang.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryMapper {

    // crud
    // 查询全部
    List<Category> getAllCategories();

    // 通过id获取
    Category getCategoryById(Integer id);

    // 新增
    int addCategory(Category category);

    // 修改
    int updateCategory(Category category);

    // 通过id删除
    int deleteCategoryById(Integer id);


    // 根据用户id查询博客分类
    List<Category> getAllCategoryByUserId(@Param(value = "userId") Integer userId);

    // 查询 标签总数
    int getCategoryCount();
    
}
