package com.baozhou.baozhouplatform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baozhou.baozhouplatform.entity.StudentLeave;
import com.baozhou.baozhouplatform.mapper.StudentLeaveMapper;
import com.baozhou.baozhouplatform.service.IStudentLeaveService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * 学生请假Service实现
 */
@Service
public class StudentLeaveServiceImpl extends ServiceImpl<StudentLeaveMapper, StudentLeave>
        implements IStudentLeaveService {

    @Override
    public boolean hasLeaveOnDate(Integer studentId, LocalDate date) {
        // 查询学生在指定日期是否有请假（待审核或已通过）
        long count = this.lambdaQuery()
                .eq(StudentLeave::getStudentId, studentId)
                .in(StudentLeave::getStatus, Arrays.asList("待审核", "已通过"))
                .le(StudentLeave::getStartDate, date)
                .ge(StudentLeave::getEndDate, date)
                .count();

        return count > 0;
    }

    @Override
    public List<StudentLeave> getLeavesInRange(Integer studentId, LocalDate startDate, LocalDate endDate) {
        return this.lambdaQuery()
                .eq(StudentLeave::getStudentId, studentId)
                .in(StudentLeave::getStatus, Arrays.asList("待审核", "已通过"))
                .le(StudentLeave::getStartDate, endDate)
                .ge(StudentLeave::getEndDate, startDate)
                .list();
    }
}
