package com.baozhou.baozhouplatform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baozhou.baozhouplatform.entity.Teacher;
import com.baozhou.baozhouplatform.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Teacher控制器层
 */
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private ITeacherService teacherService;

    /**
     * 新增教师
     *
     * POST http://localhost:8080/api/teacher
     * Body: {"teacherName": "张老师", "employeeNumber": 1001, "groupId": 1, "classroom": "A06", "floor": "5楼"}
     */
    @PostMapping
    public boolean save(@RequestBody Teacher teacher) {
        return teacherService.save(teacher);
    }

    /**
     * 根据ID删除教师
     *
     * DELETE http://localhost:8080/api/teacher/1
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return teacherService.removeById(id);
    }

    /**
     * 更新教师信息
     *
     * PUT http://localhost:8080/api/teacher
     * Body: {"teacherId": 1, "teacherName": "李老师", "classroom": "B01"}
     */
    @PutMapping
    public boolean update(@RequestBody Teacher teacher) {
        return teacherService.updateById(teacher);
    }

    /**
     * 根据ID查询教师
     *
     * GET http://localhost:8080/api/teacher/1
     */
    @GetMapping("/{id}")
    public Teacher getById(@PathVariable Integer id) {
        return teacherService.getById(id);
    }

    /**
     * 查询所有教师
     *
     * GET http://localhost:8080/api/teacher
     */
    @GetMapping
    public List<Teacher> list() {
        return teacherService.list();
    }

    /**
     * 分页查询教师
     *
     * GET http://localhost:8080/api/teacher/page?page=1&size=10
     */
    @GetMapping("/page")
    public Page<Teacher> page(@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer size) {
        return teacherService.page(new Page<>(page, size));
    }
}
