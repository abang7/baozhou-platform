package com.baozhou.baozhouplatform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baozhou.baozhouplatform.entity.Student;
import com.baozhou.baozhouplatform.mapper.StudentMapper;
import com.baozhou.baozhouplatform.service.IStudentService;
import org.springframework.stereotype.Service;

/**
 * Student服务层实现类
 *
 * 学习要点：
 * 1. ServiceImpl实现了IService接口
 * 2. 需要指定泛型：<Mapper类型, 实体类型>
 * 3. @Service注解标记这是Spring的服务组件
 *
 * 架构分层：
 * Controller → Service → Mapper → Database
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    // ServiceImpl已经实现了IService的所有方法
    // 所以这里不需要写任何代码，就已经拥有了完整的CRUD功能

    // 如果需要自定义业务方法，在这里实现
    // 例如：
    // @Override
    // public List<Student> queryStudentsByStatus(String status) {
    //     return this.lambdaQuery()
    //         .eq(Student::getStatus, status)
    //         .list();
    // }
}
