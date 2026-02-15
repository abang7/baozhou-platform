package com.baozhou.baozhouplatform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baozhou.baozhouplatform.entity.Semester;
import com.baozhou.baozhouplatform.mapper.SemesterMapper;
import com.baozhou.baozhouplatform.service.ISemesterService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 学期Service实现
 */
@Service
public class SemesterServiceImpl extends ServiceImpl<SemesterMapper, Semester>
        implements ISemesterService {

    @Override
    public Semester getCurrentSemester() {
        // 获取状态为"进行中"的学期
        return this.lambdaQuery()
                .eq(Semester::getStatus, "进行中")
                .last("LIMIT 1")
                .one();
    }

    @Override
    public List<Semester> getSemestersByType(String type) {
        return this.lambdaQuery()
                .eq(Semester::getSemesterType, type)
                .orderByDesc(Semester::getStartDate)
                .list();
    }
}
