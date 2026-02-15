package com.baozhou.baozhouplatform.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 学生日历数据传输对象
 */
@Data
public class StudentCalendarDTO {

    /**
     * 排课ID
     */
    private Integer scheduleId;

    /**
     * 班级ID
     */
    private Integer classId;

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
     * 学科
     */
    private String subject;

    /**
     * 班级类型
     */
    private String classType;

    /**
     * 教师ID
     */
    private Integer teacherId;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 教室
     */
    private String classroom;

    /**
     * 楼层
     */
    private String floor;

    /**
     * 状态（正常、请假、缺课）
     */
    private String status;

    /**
     * 是否请假
     */
    private Boolean isOnLeave;

    /**
     * 请假ID（如果已请假）
     */
    private Integer leaveId;

    /**
     * 请假原因
     */
    private String leaveReason;
}
