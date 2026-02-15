package com.baozhou.baozhouplatform.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程表数据传输对象
 *
 * 学习要点：
 * 1. DTO（Data Transfer Object）：数据传输对象
 * 2. 用于组合多个表的数据，方便返回给前端
 * 3. 避免实体类直接暴露给前端
 */
@Data
public class ScheduleDTO {

    /**
     * 班级ID
     */
    private Integer classId;

    /**
     * 班级类型（一对一、一对三）
     */
    private String classType;

    /**
     * 学科
     */
    private String subject;

    /**
     * 星期几
     */
    private String weekday;

    /**
     * 时段
     */
    private String timeSlot;

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
     * 当前学生数
     */
    private Integer currentStudents;

    /**
     * 最大学生数
     */
    private Integer maxStudents;

    /**
     * 是否生效
     */
    private Boolean isActive;

    /**
     * 班级中的学生列表
     */
    private List<StudentInfo> students;

    /**
     * 学生信息（内部类）
     */
    @Data
    public static class StudentInfo {
        private Integer studentId;
        private String studentName;
        private Integer grade;
        private String status;

        public StudentInfo(Integer studentId, String studentName, Integer grade, String status) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.grade = grade;
            this.status = status;
        }
    }
}
