package com.baozhou.baozhouplatform.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 教师排课数据传输对象
 */
@Data
public class TeacherScheduleDTO {

    /**
     * 排课ID
     */
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
     * 学科
     */
    private String subject;

    /**
     * 班级类型（一对一、一对三）
     */
    private String classType;

    /**
     * 教室
     */
    private String classroom;

    /**
     * 楼层
     */
    private String floor;

    /**
     * 状态
     */
    private String status;

    /**
     * 学生列表
     */
    private List<StudentInfo> students;

    /**
     * 学生信息
     */
    @Data
    public static class StudentInfo {
        private Integer studentId;
        private String studentName;
        private Integer grade;
        private String status;
        /**
         * 是否请假
         */
        private Boolean isOnLeave;
        /**
         * 请假原因
         */
        private String leaveReason;
    }
}
