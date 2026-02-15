package com.baozhou.baozhouplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baozhou.baozhouplatform.entity.StudentLeave;

import java.time.LocalDate;
import java.util.List;

/**
 * 学生请假Service
 */
public interface IStudentLeaveService extends IService<StudentLeave> {

    /**
     * 检查学生在指定日期是否有请假
     *
     * @param studentId 学生ID
     * @param date 日期
     * @return 是否请假
     */
    boolean hasLeaveOnDate(Integer studentId, LocalDate date);

    /**
     * 获取学生在日期范围内的所有请假
     *
     * @param studentId 学生ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 请假列表
     */
    List<StudentLeave> getLeavesInRange(Integer studentId, LocalDate startDate, LocalDate endDate);
}
