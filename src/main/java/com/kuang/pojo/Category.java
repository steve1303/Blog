package com.kuang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private Integer categoryId;
    private String categoryName;
    private String categoryDescription;

    private int isDeleted;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date categoryCreateTime;

    private Integer userId;
}
