package com.baozhou.baozhouplatform.service.impl;

import com.baozhou.baozhouplatform.entity.Course;
import com.baozhou.baozhouplatform.mapper.CourseMapper;
import com.baozhou.baozhouplatform.service.ICourseService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 课程表Service实现类
 * 负责具体的业务逻辑处理
 */
@Service  // 告诉Spring这是一个Service组件，需要被管理
public class CourseServiceImpl implements ICourseService {

    /**
     * 注入CourseMapper
     * @Autowired 表示自动装配，Spring会自动把CourseMapper注入进来
     * 这样我们就可以使用mapper来操作数据库了
     */
    @Autowired
    private CourseMapper courseMapper;

    /**
     * 查询所有课程
     */
    @Override
    public List<Course> getAllCourses() {
        // selectList(null) 表示查询所有记录，null表示没有查询条件
        return courseMapper.selectList(null);
    }

    /**
     * 根据ID查询课程
     */
    @Override
    public Course getCourseById(Long id) {
        // selectById 根据主键查询
        return courseMapper.selectById(id);
    }

    /**
     * 添加新课程
     */
    @Override
    public boolean addCourse(Course course) {
        // insert 插入一条记录
        // 返回值是影响的行数，如果>0表示成功
        return courseMapper.insert(course) > 0;
    }

    /**
     * 更新课程信息
     */
    @Override
    public boolean updateCourse(Course course) {
        // updateById 根据主键更新
        // 只会更新非null的字段
        return courseMapper.updateById(course) > 0;
    }

    /**
     * 根据ID删除课程
     */
    @Override
    public boolean deleteCourseById(Long id) {
        // deleteById 根据主键删除
        return courseMapper.deleteById(id) > 0;
    }
}
