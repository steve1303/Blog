package com.kuang.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer userId;
    private String userName;
    private String userPassword;
    private String userPhoto;
    private String userPhone;
    private String userEmail;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date userCreatTime;  // 注册时间 自动创建
    private String userPerms; // 权限

    //一个用户 拥有多个博客
    private List<Blog> blogs;
    private List<Category> categories;
    private List<Comment> comments;

}
