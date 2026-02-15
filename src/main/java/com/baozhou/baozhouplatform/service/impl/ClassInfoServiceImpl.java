package com.baozhou.baozhouplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baozhou.baozhouplatform.dto.ScheduleDTO;
import com.baozhou.baozhouplatform.entity.ClassInfo;
import com.baozhou.baozhouplatform.entity.ClassStudent;
import com.baozhou.baozhouplatform.entity.Student;
import com.baozhou.baozhouplatform.entity.Teacher;
import com.baozhou.baozhouplatform.mapper.ClassInfoMapper;
import com.baozhou.baozhouplatform.service.IClassInfoService;
import com.baozhou.baozhouplatform.service.IClassStudentService;
import com.baozhou.baozhouplatform.service.IStudentService;
import com.baozhou.baozhouplatform.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassInfo服务层实现类
 */
@Service
public class ClassInfoServiceImpl extends ServiceImpl<ClassInfoMapper, ClassInfo> implements IClassInfoService {

    @Autowired
    private ITeacherService teacherService;

    @Autowired
    private IClassStudentService classStudentService;

    @Autowired
    private IStudentService studentService;

    /**
     * 获取完整的课程表数据
     *
     * 实现思路：
     * 1. 查询所有班级信息
     * 2. 遍历每个班级，查询对应的教师信息
     * 3. 查询班级中的所有学生
     * 4. 组装成DTO返回
     */
    @Override
    public List<ScheduleDTO> getScheduleList() {
        // 1. 查询所有生效的班级
        List<ClassInfo> classList = this.lambdaQuery()
                .eq(ClassInfo::getIsActive, true)
                .orderByAsc(ClassInfo::getWeekday)
                .list();

        List<ScheduleDTO> scheduleList = new ArrayList<>();

        // 2. 遍历每个班级，组装完整信息
        for (ClassInfo classInfo : classList) {
            ScheduleDTO dto = new ScheduleDTO();

            // 基本信息
            dto.setClassId(classInfo.getClassId());
            dto.setClassType(classInfo.getClassType());
            dto.setSubject(classInfo.getSubject());
            dto.setWeekday(classInfo.getWeekday());
            dto.setTimeSlot(classInfo.getTimeSlot());
            dto.setCurrentStudents(classInfo.getCurrentStudents());
            dto.setMaxStudents(classInfo.getMaxStudents());
            dto.setIsActive(classInfo.getIsActive());

            // 查询教师信息
            Teacher teacher = teacherService.getById(classInfo.getTeacherId());
            if (teacher != null) {
                dto.setTeacherId(teacher.getTeacherId());
                dto.setTeacherName(teacher.getTeacherName());
                dto.setClassroom(teacher.getClassroom());
                dto.setFloor(teacher.getFloor());
            }

            // 查询班级中的学生（只查询在读的）
            List<ClassStudent> classStudents = classStudentService.lambdaQuery()
                    .eq(ClassStudent::getClassId, classInfo.getClassId())
                    .eq(ClassStudent::getStatus, "在读")
                    .list();

            // 组装学生信息
            List<ScheduleDTO.StudentInfo> studentInfos = classStudents.stream()
                    .map(cs -> {
                        Student student = studentService.getById(cs.getStudentId());
                        if (student != null) {
                            return new ScheduleDTO.StudentInfo(
                                    student.getStudentId(),
                                    student.getStudentName(),
                                    student.getGrade(),
                                    cs.getStatus()
                            );
                        }
                        return null;
                    })
                    .filter(info -> info != null)
                    .collect(Collectors.toList());

            dto.setStudents(studentInfos);

            scheduleList.add(dto);
        }

        return scheduleList;
    }

    @Override
    public boolean hasDuplicateClass(Integer teacherId, String weekday, String timeSlot, Integer excludeClassId) {
        LambdaQueryWrapper<ClassInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClassInfo::getTeacherId, teacherId)
                .eq(ClassInfo::getWeekday, weekday)
                .eq(ClassInfo::getTimeSlot, timeSlot)
                .eq(ClassInfo::getIsActive, true);

        // 如果是更新操作，排除当前班级本身
        if (excludeClassId != null) {
            queryWrapper.ne(ClassInfo::getClassId, excludeClassId);
        }

        long count = this.count(queryWrapper);
        return count > 0;
    }
}

