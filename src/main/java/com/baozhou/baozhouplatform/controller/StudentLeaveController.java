package com.baozhou.baozhouplatform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baozhou.baozhouplatform.entity.StudentLeave;
import com.baozhou.baozhouplatform.service.IStudentLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生请假Controller
 */
@RestController
@RequestMapping("/api/student-leave")
public class StudentLeaveController {

    @Autowired
    private IStudentLeaveService studentLeaveService;

    /**
     * 新增请假
     *
     * POST http://localhost:8080/api/student-leave
     * Body: {"studentId": 1, "semesterId": 1, "startDate": "2025-01-20", "endDate": "2025-01-25", "leaveType": "旅游", "reason": "全家旅游"}
     */
    @PostMapping
    public boolean save(@RequestBody StudentLeave studentLeave) {
        studentLeave.setStatus("待审核");
        return studentLeaveService.save(studentLeave);
    }

    /**
     * 审核请假
     *
     * PUT http://localhost:8080/api/student-leave/approve
     * Body: {"leaveId": 1, "status": "已通过"}
     */
    @PutMapping("/approve")
    public boolean approve(@RequestBody StudentLeave studentLeave) {
        return studentLeaveService.updateById(studentLeave);
    }

    /**
     * 删除请假记录
     *
     * DELETE http://localhost:8080/api/student-leave/1
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return studentLeaveService.removeById(id);
    }

    /**
     * 根据学生ID查询请假记录
     *
     * GET http://localhost:8080/api/student-leave/student/1
     */
    @GetMapping("/student/{studentId}")
    public List<StudentLeave> getByStudentId(@PathVariable Integer studentId) {
        return studentLeaveService.lambdaQuery()
                .eq(StudentLeave::getStudentId, studentId)
                .orderByDesc(StudentLeave::getCreatedAt)
                .list();
    }

    /**
     * 查询所有待审核的请假
     *
     * GET http://localhost:8080/api/student-leave/pending
     */
    @GetMapping("/pending")
    public List<StudentLeave> getPending() {
        return studentLeaveService.lambdaQuery()
                .eq(StudentLeave::getStatus, "待审核")
                .list();
    }
}
