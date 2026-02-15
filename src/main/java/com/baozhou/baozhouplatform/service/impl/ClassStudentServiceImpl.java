package com.baozhou.baozhouplatform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baozhou.baozhouplatform.entity.ClassStudent;
import com.baozhou.baozhouplatform.mapper.ClassStudentMapper;
import com.baozhou.baozhouplatform.service.IClassStudentService;
import org.springframework.stereotype.Service;

/**
 * ClassStudent服务层实现类
 */
@Service
public class ClassStudentServiceImpl extends ServiceImpl<ClassStudentMapper, ClassStudent> implements IClassStudentService {
}
