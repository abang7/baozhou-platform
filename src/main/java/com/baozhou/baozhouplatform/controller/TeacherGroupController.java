package com.baozhou.baozhouplatform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baozhou.baozhouplatform.entity.TeacherGroup;
import com.baozhou.baozhouplatform.service.ITeacherGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TeacherGroup控制器层
 */
@RestController
@RequestMapping("/api/teacher-group")
public class TeacherGroupController {

    @Autowired
    private ITeacherGroupService teacherGroupService;

    /**
     * 新增教师小组
     *
     * POST http://localhost:8080/api/teacher-group
     * Body: {"groupName": "高数组", "leaderId": 1}
     */
    @PostMapping
    public boolean save(@RequestBody TeacherGroup teacherGroup) {
        return teacherGroupService.save(teacherGroup);
    }

    /**
     * 根据ID删除教师小组
     *
     * DELETE http://localhost:8080/api/teacher-group/1
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return teacherGroupService.removeById(id);
    }

    /**
     * 更新教师小组信息
     *
     * PUT http://localhost:8080/api/teacher-group
     * Body: {"groupId": 1, "groupName": "高数组", "leaderId": 2}
     */
    @PutMapping
    public boolean update(@RequestBody TeacherGroup teacherGroup) {
        return teacherGroupService.updateById(teacherGroup);
    }

    /**
     * 根据ID查询教师小组
     *
     * GET http://localhost:8080/api/teacher-group/1
     */
    @GetMapping("/{id}")
    public TeacherGroup getById(@PathVariable Integer id) {
        return teacherGroupService.getById(id);
    }

    /**
     * 查询所有教师小组
     *
     * GET http://localhost:8080/api/teacher-group
     */
    @GetMapping
    public List<TeacherGroup> list() {
        return teacherGroupService.list();
    }

    /**
     * 分页查询教师小组
     *
     * GET http://localhost:8080/api/teacher-group/page?page=1&size=10
     */
    @GetMapping("/page")
    public Page<TeacherGroup> page(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        return teacherGroupService.page(new Page<>(page, size));
    }
}
