package com.baozhou.baozhouplatform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baozhou.baozhouplatform.dto.ScheduleDTO;
import com.baozhou.baozhouplatform.entity.ClassInfo;
import com.baozhou.baozhouplatform.service.IClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassInfo控制器层
 */
@RestController
@RequestMapping("/api/class")
public class ClassInfoController {

    @Autowired
    private IClassInfoService classInfoService;

    /**
     * 新增班级
     *
     * POST http://localhost:8080/api/class
     * Body: {"teacherId": 1, "weekday": "周六", "timeSlot": "07:40-09:40", "subject": "数学", "classType": "一对一", "maxStudents": 1}
     */
    @PostMapping
    public Map<String, Object> save(@RequestBody ClassInfo classInfo) {
        Map<String, Object> result = new HashMap<>();

        // 检查是否有重复课程
        boolean hasDuplicate = classInfoService.hasDuplicateClass(
                classInfo.getTeacherId(),
                classInfo.getWeekday(),
                classInfo.getTimeSlot(),
                null
        );

        if (hasDuplicate) {
            result.put("success", false);
            result.put("message", "该教师在此时间段已有课程，请勿重复录入");
            return result;
        }

        // 保存课程
        boolean saved = classInfoService.save(classInfo);
        result.put("success", saved);
        result.put("message", saved ? "录入成功" : "录入失败");
        result.put("data", classInfo);

        return result;
    }

    /**
     * 根据ID删除班级
     *
     * DELETE http://localhost:8080/api/class/1
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return classInfoService.removeById(id);
    }

    /**
     * 更新班级信息
     *
     * PUT http://localhost:8080/api/class
     * Body: {"classId": 1, "teacherId": 1, "currentStudents": 2}
     */
    @PutMapping
    public boolean update(@RequestBody ClassInfo classInfo) {
        return classInfoService.updateById(classInfo);
    }

    /**
     * 根据ID查询班级
     *
     * GET http://localhost:8080/api/class/1
     */
    @GetMapping("/{id}")
    public ClassInfo getById(@PathVariable Integer id) {
        return classInfoService.getById(id);
    }

    /**
     * 查询所有班级
     *
     * GET http://localhost:8080/api/class
     */
    @GetMapping
    public List<ClassInfo> list() {
        return classInfoService.list();
    }

    /**
     * 分页查询班级
     *
     * GET http://localhost:8080/api/class/page?page=1&size=10
     */
    @GetMapping("/page")
    public Page<ClassInfo> page(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer size) {
        return classInfoService.page(new Page<>(page, size));
    }

    /**
     * 获取课程表数据
     *
     * GET http://localhost:8080/api/class/schedule
     *
     * 返回数据包含：
     * - 班级信息（班级类型、学科、时间）
     * - 教师信息（姓名、教室、楼层）
     * - 学生列表（在读的学生）
     */
    @GetMapping("/schedule")
    public List<ScheduleDTO> getSchedule() {
        return classInfoService.getScheduleList();
    }
}
