package com.baozhou.baozhouplatform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baozhou.baozhouplatform.entity.Teacher;
import com.baozhou.baozhouplatform.mapper.TeacherMapper;
import com.baozhou.baozhouplatform.service.ITeacherService;
import org.springframework.stereotype.Service;

/**
 * Teacher服务层实现类
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {
}
