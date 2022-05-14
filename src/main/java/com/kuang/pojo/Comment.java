package com.kuang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private BigInteger commentId;

    private String commentContent;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commentCreateTime;
    private BigInteger parentCommentId;

    private Integer commentLikeCount;
    private Integer commentStatus;
    private Integer isDeleted;

    private Integer userId; // 外键 说明评论属于那个博客
    private Integer blogId; // 外键 说明评论属于哪个用户


    private User user;
}
