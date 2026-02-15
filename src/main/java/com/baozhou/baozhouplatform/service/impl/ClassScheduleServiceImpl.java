package com.baozhou.baozhouplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baozhou.baozhouplatform.dto.StudentCalendarDTO;
import com.baozhou.baozhouplatform.dto.TeacherScheduleDTO;
import com.baozhou.baozhouplatform.entity.ClassInfo;
import com.baozhou.baozhouplatform.entity.ClassSchedule;
import com.baozhou.baozhouplatform.entity.ClassStudent;
import com.baozhou.baozhouplatform.entity.Semester;
import com.baozhou.baozhouplatform.entity.Student;
import com.baozhou.baozhouplatform.entity.StudentLeave;
import com.baozhou.baozhouplatform.entity.Teacher;
import com.baozhou.baozhouplatform.mapper.ClassScheduleMapper;
import com.baozhou.baozhouplatform.service.IClassInfoService;
import com.baozhou.baozhouplatform.service.IClassScheduleService;
import com.baozhou.baozhouplatform.service.IClassStudentService;
import com.baozhou.baozhouplatform.service.IStudentLeaveService;
import com.baozhou.baozhouplatform.service.IStudentService;
import com.baozhou.baozhouplatform.service.ISemesterService;
import com.baozhou.baozhouplatform.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 班级排课Service实现
 */
@Service
public class ClassScheduleServiceImpl extends ServiceImpl<ClassScheduleMapper, ClassSchedule>
        implements IClassScheduleService {

    @Autowired
    private ISemesterService semesterService;

    @Autowired
    private IClassInfoService classInfoService;

    @Autowired
    private IStudentLeaveService studentLeaveService;

    @Autowired
    private ITeacherService teacherService;

    @Autowired
    private IClassStudentService classStudentService;

    @Autowired
    private IStudentService studentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int generateWinterSchedule(Integer semesterId) {
        // 1. 获取学期信息
        Semester semester = semesterService.getById(semesterId);
        if (semester == null) {
            throw new RuntimeException("学期不存在");
        }

        LocalDate startDate = semester.getStartDate();
        LocalDate endDate = semester.getEndDate();

        // 2. 清除该学期的旧排课数据
        this.lambdaUpdate()
                .eq(ClassSchedule::getSemesterId, semesterId)
                .remove();

        int count = 0;
        LocalDate currentDate = startDate;
        int sundayCount = 0; // 记录第几个周日

        // 3. 遍历寒假每一天
        while (!currentDate.isAfter(endDate)) {
            if (currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                // 周日：轮流使用周六和周日课表
                sundayCount++;
                String sourceWeekday = (sundayCount % 2 == 1) ? "周六" : "周日"; // 奇数用周六，偶数用周日
                count += generateScheduleForDay(currentDate, sourceWeekday, semesterId);
            } else {
                // 非周日：计算是寒假的第几天
                int dayNumber = calculateDayNumber(currentDate, startDate);

                // 根据天数规则决定使用周六还是周日的课
                String sourceWeekday = determineSourceWeekday(dayNumber);

                // 为该源课表的所有班级生成排课
                count += generateScheduleForDay(currentDate, sourceWeekday, semesterId);
            }

            currentDate = currentDate.plusDays(1);
        }

        return count;
    }

    /**
     * 计算是寒假的第几天
     */
    private int calculateDayNumber(LocalDate date, LocalDate startDate) {
        // 计算从startDate到date的天数差，加1（第1天）
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, date);
        return (int) days + 1;
    }

    /**
     * 根据天数决定使用周六还是周日的课
     * 规则：一三五用周六课表，二四六用周日课表
     */
    private String determineSourceWeekday(int dayNumber) {
        int remainder = dayNumber % 7;
        if (remainder == 1 || remainder == 3 || remainder == 5) {
            return "周六";
        } else if (remainder == 2 || remainder == 4 || remainder == 6 || remainder == 0) {
            return "周日";
        } else {
            return null;
        }
    }

    /**
     * 为指定日期生成排课
     */
    private int generateScheduleForDay(LocalDate courseDate, String sourceWeekday, Integer semesterId) {
        if (sourceWeekday == null) {
            return 0;
        }

        // 获取该源课表的所有生效班级
        List<ClassInfo> allClasses = classInfoService.lambdaQuery()
                .eq(ClassInfo::getWeekday, sourceWeekday)
                .eq(ClassInfo::getIsActive, true)
                .list();

        int count = 0;
        for (ClassInfo classInfo : allClasses) {
            // ===== 关键修改：只为有学生的班级生成排课 =====
            // 检查该班级是否有在读的学生
            long studentCount = classStudentService.lambdaQuery()
                    .eq(ClassStudent::getClassId, classInfo.getClassId())
                    .eq(ClassStudent::getStatus, "在读")
                    .count();

            // 如果该班级没有学生，跳过
            if (studentCount == 0) {
                continue;
            }

            ClassSchedule schedule = new ClassSchedule();
            schedule.setClassId(classInfo.getClassId());
            schedule.setSemesterId(semesterId);
            schedule.setCourseDate(courseDate);
            schedule.setWeekday(getWeekdayName(courseDate.getDayOfWeek()));
            schedule.setSourceWeekday(sourceWeekday);

            // 解析时段（如 "07:40-09:40"）
            String[] times = classInfo.getTimeSlot().split("-");
            schedule.setStartTime(LocalTime.parse(times[0]));
            schedule.setEndTime(LocalTime.parse(times[1]));

            // 从教师信息获取教室
            Teacher teacher = teacherService.getById(classInfo.getTeacherId());
            if (teacher != null) {
                schedule.setClassroom(teacher.getClassroom());
            }
            schedule.setStatus("正常");

            this.save(schedule);
            count++;
        }

        return count;
    }

    /**
     * 将DayOfWeek转换为中文名称
     */
    private String getWeekdayName(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return "周一";
            case TUESDAY: return "周二";
            case WEDNESDAY: return "周三";
            case THURSDAY: return "周四";
            case FRIDAY: return "周五";
            case SATURDAY: return "周六";
            case SUNDAY: return "周日";
            default: return "";
        }
    }

    @Override
    public List<ClassSchedule> getTeacherScheduleByDate(Integer teacherId, LocalDate date) {
        // 需要通过class_id关联查询教师
        return this.lambdaQuery()
                .eq(ClassSchedule::getCourseDate, date)
                .list()
                .stream()
                .filter(schedule -> {
                    ClassInfo classInfo = classInfoService.getById(schedule.getClassId());
                    return classInfo != null && classInfo.getTeacherId().equals(teacherId);
                })
                .toList();
    }

    @Override
    public List<ClassSchedule> getClassroomScheduleByDate(String classroom, LocalDate date) {
        return this.lambdaQuery()
                .eq(ClassSchedule::getClassroom, classroom)
                .eq(ClassSchedule::getCourseDate, date)
                .orderByAsc(ClassSchedule::getStartTime)
                .list();
    }

    @Override
    public List<ClassSchedule> getSemesterSchedules(Integer semesterId) {
        return this.lambdaQuery()
                .eq(ClassSchedule::getSemesterId, semesterId)
                .orderByAsc(ClassSchedule::getCourseDate)
                .list();
    }

    @Override
    public List<TeacherScheduleDTO> getTeacherScheduleData(Integer teacherId, Integer semesterId) {
        // 1. 查询该教师的班级排课
        LambdaQueryWrapper<ClassSchedule> queryWrapper = new LambdaQueryWrapper<>();

        // 先查询该教师的所有班级ID
        List<Integer> classIds = classInfoService.lambdaQuery()
                .eq(ClassInfo::getTeacherId, teacherId)
                .list()
                .stream()
                .map(ClassInfo::getClassId)
                .toList();

        if (classIds.isEmpty()) {
            return List.of();
        }

        // 查询这些班级的排课
        queryWrapper.in(ClassSchedule::getClassId, classIds);
        if (semesterId != null) {
            queryWrapper.eq(ClassSchedule::getSemesterId, semesterId);
        }
        queryWrapper.orderByAsc(ClassSchedule::getCourseDate)
                    .orderByAsc(ClassSchedule::getStartTime);

        List<ClassSchedule> schedules = this.list(queryWrapper);

        // 2. 组装DTO数据
        List<TeacherScheduleDTO> result = new ArrayList<>();

        for (ClassSchedule schedule : schedules) {
            // 获取班级信息
            ClassInfo classInfo = classInfoService.getById(schedule.getClassId());
            if (classInfo == null) {
                continue;
            }

            TeacherScheduleDTO dto = new TeacherScheduleDTO();
            dto.setScheduleId(schedule.getScheduleId());
            dto.setClassId(schedule.getClassId());
            dto.setSemesterId(schedule.getSemesterId());
            dto.setCourseDate(schedule.getCourseDate());
            dto.setWeekday(schedule.getWeekday());
            dto.setStartTime(schedule.getStartTime());
            dto.setEndTime(schedule.getEndTime());
            dto.setSourceWeekday(schedule.getSourceWeekday());
            dto.setSubject(classInfo.getSubject());
            dto.setClassType(classInfo.getClassType());
            dto.setStatus(schedule.getStatus());

            // 获取教师信息（教室、楼层）
            Teacher teacher = teacherService.getById(classInfo.getTeacherId());
            if (teacher != null) {
                dto.setClassroom(teacher.getClassroom());
                dto.setFloor(teacher.getFloor());
            }

            // 获取班级中的学生
            List<ClassStudent> classStudents = classStudentService.lambdaQuery()
                    .eq(ClassStudent::getClassId, schedule.getClassId())
                    .eq(ClassStudent::getStatus, "在读")
                    .list();

            // 组装学生信息
            List<TeacherScheduleDTO.StudentInfo> studentInfos = classStudents.stream()
                    .map(cs -> {
                        Student student = studentService.getById(cs.getStudentId());
                        if (student != null) {
                            TeacherScheduleDTO.StudentInfo info = new TeacherScheduleDTO.StudentInfo();
                            info.setStudentId(student.getStudentId());
                            info.setStudentName(student.getStudentName());
                            info.setGrade(student.getGrade());
                            info.setStatus(cs.getStatus());

                            // 检查该学生是否请假
                            boolean hasLeave = studentLeaveService.hasLeaveOnDate(student.getStudentId(), schedule.getCourseDate());
                            info.setIsOnLeave(hasLeave);

                            // 如果请假，获取请假原因
                            if (hasLeave) {
                                List<StudentLeave> leaves = studentLeaveService.getLeavesInRange(
                                        student.getStudentId(),
                                        schedule.getCourseDate(),
                                        schedule.getCourseDate()
                                );
                                if (!leaves.isEmpty()) {
                                    info.setLeaveReason(leaves.get(0).getReason());
                                }
                            }

                            return info;
                        }
                        return null;
                    })
                    .filter(info -> info != null)
                    .toList();

            dto.setStudents(studentInfos);
            result.add(dto);
        }

        return result;
    }

    @Override
    public List<StudentCalendarDTO> getStudentCalendarData(Integer studentId, Integer semesterId) {
        // 1. 查询学生的所有班级关联
        List<ClassStudent> classStudents = classStudentService.lambdaQuery()
                .eq(ClassStudent::getStudentId, studentId)
                .eq(ClassStudent::getStatus, "在读")
                .list();

        if (classStudents.isEmpty()) {
            return List.of();
        }

        // 获取所有班级ID
        List<Integer> classIds = classStudents.stream()
                .map(ClassStudent::getClassId)
                .toList();

        // 2. 查询这些班级的排课
        LambdaQueryWrapper<ClassSchedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ClassSchedule::getClassId, classIds);
        if (semesterId != null) {
            queryWrapper.eq(ClassSchedule::getSemesterId, semesterId);
        }
        queryWrapper.orderByAsc(ClassSchedule::getCourseDate)
                    .orderByAsc(ClassSchedule::getStartTime);

        List<ClassSchedule> schedules = this.list(queryWrapper);

        // 3. 组装DTO数据
        List<StudentCalendarDTO> result = new ArrayList<>();

        for (ClassSchedule schedule : schedules) {
            // 获取班级信息
            ClassInfo classInfo = classInfoService.getById(schedule.getClassId());
            if (classInfo == null) {
                continue;
            }

            StudentCalendarDTO dto = new StudentCalendarDTO();
            dto.setScheduleId(schedule.getScheduleId());
            dto.setClassId(schedule.getClassId());
            dto.setCourseDate(schedule.getCourseDate());
            dto.setWeekday(schedule.getWeekday());
            dto.setStartTime(schedule.getStartTime());
            dto.setEndTime(schedule.getEndTime());
            dto.setSubject(classInfo.getSubject());
            dto.setClassType(classInfo.getClassType());
            dto.setStatus(schedule.getStatus());

            // 获取教师信息
            Teacher teacher = teacherService.getById(classInfo.getTeacherId());
            if (teacher != null) {
                dto.setTeacherId(teacher.getTeacherId());
                dto.setTeacherName(teacher.getTeacherName());
                dto.setClassroom(teacher.getClassroom());
                dto.setFloor(teacher.getFloor());
            }

            // 检查该日期是否有请假
            boolean hasLeave = studentLeaveService.hasLeaveOnDate(studentId, schedule.getCourseDate());
            dto.setIsOnLeave(hasLeave);

            // 如果有请假，获取请假详情
            if (hasLeave) {
                List<StudentLeave> leaves = studentLeaveService.getLeavesInRange(
                        studentId,
                        schedule.getCourseDate(),
                        schedule.getCourseDate()
                );
                if (!leaves.isEmpty()) {
                    StudentLeave leave = leaves.get(0);
                    dto.setLeaveId(leave.getLeaveId());
                    dto.setLeaveReason(leave.getReason());
                }
            }

            result.add(dto);
        }

        return result;
    }
}
