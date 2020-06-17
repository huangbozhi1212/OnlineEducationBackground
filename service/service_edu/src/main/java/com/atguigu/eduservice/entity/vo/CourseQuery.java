package com.atguigu.eduservice.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CourseQuery implements Serializable {
    private static final long serialVersionUID = 1L;
    private String teacherId;
    private String title;
    private String status;
    private BigDecimal price;
}
