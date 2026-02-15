package com.baozhou.baozhouplatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 班级排课实体类
 */
@Data
@TableName("class_schedules")
public class ClassSchedule {

    /**
     * 排课ID
     */
    @TableId(type = IdType.AUTO)
    private Integer scheduleId;

    /**
     * 班级ID
     */
    private Integer classId;

    /**
     * 学期ID
     */
    private Integer semesterId;

    /**
     * 上课日期
     */
    private LocalDate courseDate;

    /**
     * 星期几
     */
    private String weekday;

    /**
     * 开始时间
     */
    private LocalTime startTime;

    /**
     * 结束时间
     */
    private LocalTime endTime;

    /**
     * 源课表（来自周六还是周日的课）
     */
    private String sourceWeekday;

    /**
     * 教室
     */
    private String classroom;

    /**
     * 状态：正常、取消、已调课
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
