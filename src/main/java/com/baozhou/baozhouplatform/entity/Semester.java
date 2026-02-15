package com.baozhou.baozhouplatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学期实体类
 */
@Data
@TableName("semesters")
public class Semester {

    /**
     * 学期ID
     */
    @TableId(type = IdType.AUTO)
    private Integer semesterId;

    /**
     * 学期名称
     */
    private String semesterName;

    /**
     * 学期类型：春季班、暑假班、秋季班、寒假班
     */
    private String semesterType;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 状态：未开始、进行中、已结束、已关闭
     */
    private String status;

    /**
     * 报名开始
     */
    private LocalDate registrationStart;

    /**
     * 报名截止
     */
    private LocalDate registrationEnd;

    /**
     * 说明
     */
    private String description;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
