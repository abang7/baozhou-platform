package com.baozhou.baozhouplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baozhou.baozhouplatform.dto.StudentCalendarDTO;
import com.baozhou.baozhouplatform.dto.TeacherScheduleDTO;
import com.baozhou.baozhouplatform.entity.ClassSchedule;

import java.time.LocalDate;
import java.util.List;

/**
 * 班级排课Service
 */
public interface IClassScheduleService extends IService<ClassSchedule> {

    /**
     * 为寒假学期生成排课
     *
     * 规则：
     * - 一三五（第1,3,5天）：使用周六的课表
     * - 二四六（第2,4,6天）：使用周日的课表
     * - 周日（第7天）：休息/轮流
     *
     * @param semesterId 学期ID
     * @return 生成的排课数量
     */
    int generateWinterSchedule(Integer semesterId);

    /**
     * 获取教师在指定日期的课程
     *
     * @param teacherId 教师ID
     * @param date 日期
     * @return 排课列表
     */
    List<ClassSchedule> getTeacherScheduleByDate(Integer teacherId, LocalDate date);

    /**
     * 获取教室在指定日期的使用情况
     *
     * @param classroom 教室
     * @param date 日期
     * @return 排课列表
     */
    List<ClassSchedule> getClassroomScheduleByDate(String classroom, LocalDate date);

    /**
     * 获取学期的所有排课
     *
     * @param semesterId 学期ID
     * @return 排课列表
     */
    List<ClassSchedule> getSemesterSchedules(Integer semesterId);

    /**
     * 获取教师的排课数据（包含学生信息）
     *
     * @param teacherId 教师ID
     * @param semesterId 学期ID（可选，不传则查询所有）
     * @return 排课数据列表
     */
    List<TeacherScheduleDTO> getTeacherScheduleData(Integer teacherId, Integer semesterId);

    /**
     * 获取学生的日历课程数据
     *
     * @param studentId 学生ID
     * @param semesterId 学期ID
     * @return 学生日历数据列表
     */
    List<StudentCalendarDTO> getStudentCalendarData(Integer studentId, Integer semesterId);
}
