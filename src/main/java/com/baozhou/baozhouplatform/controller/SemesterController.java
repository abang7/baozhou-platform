package com.baozhou.baozhouplatform.controller;

import com.baozhou.baozhouplatform.entity.Semester;
import com.baozhou.baozhouplatform.service.ISemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学期Controller
 */
@RestController
@RequestMapping("/api/semester")
public class SemesterController {

    @Autowired
    private ISemesterService semesterService;

    /**
     * 新增学期
     *
     * POST http://localhost:8080/api/semester
     * Body: {"semesterName": "2025寒假班", "semesterType": "寒假班", "startDate": "2025-01-15", "endDate": "2025-02-15"}
     */
    @PostMapping
    public boolean save(@RequestBody Semester semester) {
        return semesterService.save(semester);
    }

    /**
     * 获取当前学期
     *
     * GET http://localhost:8080/api/semester/current
     */
    @GetMapping("/current")
    public Semester getCurrentSemester() {
        return semesterService.getCurrentSemester();
    }

    /**
     * 根据类型获取学期
     *
     * GET http://localhost:8080/api/semester/type/寒假班
     */
    @GetMapping("/type/{type}")
    public List<Semester> getByType(@PathVariable String type) {
        return semesterService.getSemestersByType(type);
    }

    /**
     * 获取所有学期
     *
     * GET http://localhost:8080/api/semester
     */
    @GetMapping
    public List<Semester> list() {
        return semesterService.list();
    }
}
