package com.kuang.dao;

import com.kuang.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    // crud
    // 查询全部
    List<User> getAllUsers();

    // 通过id获取
    User getUserById(Integer id);

    // 新增
    int addUser(User user);

    // 修改
    int updateUser(User user);

    // 通过id删除
    int deleteUserById(Integer id);


    /*shiro*/
    // 根据用户名查询用户
    User queryUserByName(@Param(value = "userName") String name);

    // 查询全部用户
    List<User> getAllUsersIF(@Param(value = "searchParam") String name);

    // check 参看个人信息
    int selectIdByName(String name);

    // 修改用户密码
    int updateUserPassword(@Param("userId") Integer id, @Param("new_password") String password);


    // 根据用户id查询博客
    List getUserBlogs(Integer id);


}
