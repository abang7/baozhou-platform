package com.baozhou.baozhouplatform.service;

import com.baozhou.baozhouplatform.entity.Course;
import java.util.List;

/**
 * 课程表Service接口
 * 定义业务逻辑方法的规范
 */
public interface ICourseService {

    /**
     * 查询所有课程
     * @return 课程列表
     */
    List<Course> getAllCourses();

    /**
     * 根据ID查询课程
     * @param id 课程ID
     * @return 课程对象
     */
    Course getCourseById(Long id);

    /**
     * 添加新课程
     * @param course 课程对象
     * @return 是否添加成功
     */
    boolean addCourse(Course course);

    /**
     * 更新课程信息
     * @param course 课程对象
     * @return 是否更新成功
     */
    boolean updateCourse(Course course);

    /**
     * 根据ID删除课程
     * @param id 课程ID
     * @return 是否删除成功
     */
    boolean deleteCourseById(Long id);
}
