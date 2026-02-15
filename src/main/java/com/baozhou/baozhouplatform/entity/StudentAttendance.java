package com.baozhou.baozhouplatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 学生出勤实体类
 */
@Data
@TableName("student_attendance")
public class StudentAttendance {

    /**
     * 出勤ID
     */
    @TableId(type = IdType.AUTO)
    private Integer attendanceId;

    /**
     * 排课ID
     */
    private Integer scheduleId;

    /**
     * 学生ID
     */
    private Integer studentId;

    /**
     * 出勤状态：正常、请假、缺课、补课
     */
    private String attendanceStatus;

    /**
     * 作业成绩
     */
    private BigDecimal homeworkScore;

    /**
     * 备注
     */
    private String remark;

    /**
     * 记录人
     */
    private String recordedBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
