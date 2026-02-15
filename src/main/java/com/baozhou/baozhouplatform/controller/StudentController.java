package com.baozhou.baozhouplatform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baozhou.baozhouplatform.entity.Student;
import com.baozhou.baozhouplatform.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Student控制器层
 *
 * 学习要点：
 * 1. @RestController：标记这是控制器，返回JSON数据
 * 2. @RequestMapping：指定URL路径前缀
 * 3. @Autowired：自动注入Service
 *
 * RESTful API设计：
 * - POST   /api/student       新增学生
 * - DELETE /api/student/{id}  删除学生
 * - PUT    /api/student       更新学生
 * - GET    /api/student/{id}  查询单个学生
 * - GET    /api/student       查询学生列表（可分页）
 */
@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private IStudentService studentService;

    /**
     * 新增学生
     *
     * POST http://localhost:8080/api/student
     * Body: {"studentName": "张三", "grade": 10}
     */
    @PostMapping
    public boolean save(@RequestBody Student student) {
        return studentService.save(student);
    }

    /**
     * 根据ID删除学生
     *
     * DELETE http://localhost:8080/api/student/1
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return studentService.removeById(id);
    }

    /**
     * 更新学生信息
     *
     * PUT http://localhost:8080/api/student
     * Body: {"studentId": 1, "studentName": "李四", "grade": 11}
     */
    @PutMapping
    public boolean update(@RequestBody Student student) {
        return studentService.updateById(student);
    }

    /**
     * 根据ID查询学生
     *
     * GET http://localhost:8080/api/student/1
     */
    @GetMapping("/{id}")
    public Student getById(@PathVariable Integer id) {
        return studentService.getById(id);
    }

    /**
     * 查询所有学生
     *
     * GET http://localhost:8080/api/student
     */
    @GetMapping
    public List<Student> list() {
        return studentService.list();
    }

    /**
     * 分页查询学生
     *
     * GET http://localhost:8080/api/student/page?page=1&size=10
     * page: 当前页码
     * size: 每页大小
     */
    @GetMapping("/page")
    public Page<Student> page(@RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer size) {
        return studentService.page(new Page<>(page, size));
    }
}
