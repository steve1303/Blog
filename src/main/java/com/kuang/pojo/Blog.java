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
public class Blog {

    private Integer blogId;
    private String blogTitle;
    private String blogContent;
    private Integer blogViews;
    private Integer blogCommentCount;
    private String blogStatus;
    private String blogCategory;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date blogAlterTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date blogCreatDate;
    //一个用户有多个博客 一对多
    private Integer userId; // 外键

    // 多对一
    private BlogTagRelation blogTagRelation;

    // 一对多
    private List<Comment> comments;

}
