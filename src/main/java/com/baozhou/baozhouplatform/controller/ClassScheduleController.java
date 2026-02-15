package com.baozhou.baozhouplatform.controller;

import com.baozhou.baozhouplatform.dto.StudentCalendarDTO;
import com.baozhou.baozhouplatform.dto.TeacherScheduleDTO;
import com.baozhou.baozhouplatform.entity.ClassSchedule;
import com.baozhou.baozhouplatform.service.IClassScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 班级排课Controller
 */
@RestController
@RequestMapping("/api/class-schedule")
public class ClassScheduleController {

    @Autowired
    private IClassScheduleService classScheduleService;

    /**
     * 生成寒假排课
     *
     * POST http://localhost:8080/api/class-schedule/generate/1
     *
     * @param semesterId 学期ID
     * @return 生成的排课数量
     */
    @PostMapping("/generate/{semesterId}")
    public Map<String, Object> generateWinterSchedule(@PathVariable Integer semesterId) {
        int count = classScheduleService.generateWinterSchedule(semesterId);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("count", count);
        result.put("message", "成功生成 " + count + " 节课");

        return result;
    }

    /**
     * 获取学期的所有排课
     *
     * GET http://localhost:8080/api/class-schedule/semester/1
     */
    @GetMapping("/semester/{semesterId}")
    public List<ClassSchedule> getSemesterSchedules(@PathVariable Integer semesterId) {
        return classScheduleService.getSemesterSchedules(semesterId);
    }

    /**
     * 获取教师在指定日期的课程
     *
     * GET http://localhost:8080/api/class-schedule/teacher/1?date=2025-01-20
     */
    @GetMapping("/teacher/{teacherId}")
    public List<ClassSchedule> getTeacherSchedule(
            @PathVariable Integer teacherId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return classScheduleService.getTeacherScheduleByDate(teacherId, date);
    }

    /**
     * 获取教室在指定日期的使用情况
     *
     * GET http://localhost:8080/api/class-schedule/classroom/A06?date=2025-01-20
     */
    @GetMapping("/classroom/{classroom}")
    public List<ClassSchedule> getClassroomSchedule(
            @PathVariable String classroom,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return classScheduleService.getClassroomScheduleByDate(classroom, date);
    }

    /**
     * 获取教师的排课数据（包含学生信息）
     *
     * GET http://localhost:8080/api/class-schedule/teacher-data/1?semesterId=1
     *
     * @param teacherId 教师ID
     * @param semesterId 学期ID（可选）
     * @return 排课数据列表
     */
    @GetMapping("/teacher-data/{teacherId}")
    public List<TeacherScheduleDTO> getTeacherScheduleData(
            @PathVariable Integer teacherId,
            @RequestParam(required = false) Integer semesterId) {
        return classScheduleService.getTeacherScheduleData(teacherId, semesterId);
    }

    /**
     * 获取学生的日历课程数据
     *
     * GET http://localhost:8080/api/class-schedule/student-data/1?semesterId=1
     *
     * @param studentId 学生ID
     * @param semesterId 学期ID
     * @return 学生日历数据列表
     */
    @GetMapping("/student-data/{studentId}")
    public List<StudentCalendarDTO> getStudentCalendarData(
            @PathVariable Integer studentId,
            @RequestParam Integer semesterId) {
        return classScheduleService.getStudentCalendarData(studentId, semesterId);
    }
}
