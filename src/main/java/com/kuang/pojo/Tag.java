package com.kuang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    private Integer tagId;
    private String tagName;

    private Integer isDeleted;
    private Date tagCreateTime;
}
