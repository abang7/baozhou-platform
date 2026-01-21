package com.baozhou.baozhouplatform.controller;

import com.baozhou.baozhouplatform.entity.Course;
import com.baozhou.baozhouplatform.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程表Controller
 * 负责处理HTTP请求，接收前端发来的请求并返回响应
 */
@RestController  // @RestController = @Controller + @ResponseBody
                 // 表示这个类的所有方法都返回JSON数据
@RequestMapping("/api/course")  // 所有接口的URL都以 /api/course 开头
public class CourseController {

    /**
     * 注入Service
     */
    @Autowired
    private ICourseService courseService;

    /**
     * 查询所有课程
     * GET请求：http://localhost:8080/api/course/list
     */
    @GetMapping("/list")
    public List<Course> getAllCourses() {
        // 直接返回列表，Spring会自动转换成JSON格式
        return courseService.getAllCourses();
    }

    /**
     * 根据ID查询课程
     * GET请求：http://localhost:8080/api/course/1
     * {id} 是路径参数，会被@PathVariable注解获取
     */
    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) {
        // @PathVariable("id") 表示从URL路径中获取id值
        // 比如 /api/course/1，id就是1
        return courseService.getCourseById(id);
    }

    /**
     * 添加新课程
     * POST请求：http://localhost:8080/api/course
     * @RequestBody 表示从请求体中获取JSON数据，自动转换成Course对象
     */
    @PostMapping
    public boolean addCourse(@RequestBody Course course) {
        // 前端发送的JSON数据会自动转换成Course对象
        // 比如：{"courseName":"Java", "teacherName":"张老师", ...}
        return courseService.addCourse(course);
    }

    /**
     * 更新课程信息
     * PUT请求：http://localhost:8080/api/course
     */
    @PutMapping
    public boolean updateCourse(@RequestBody Course course) {
        return courseService.updateCourse(course);
    }

    /**
     * 删除课程
     * DELETE请求：http://localhost:8080/api/course/1
     */
    @DeleteMapping("/{id}")
    public boolean deleteCourse(@PathVariable Long id) {
        return courseService.deleteCourseById(id);
    }
}