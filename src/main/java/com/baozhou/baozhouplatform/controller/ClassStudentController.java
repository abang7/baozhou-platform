package com.baozhou.baozhouplatform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baozhou.baozhouplatform.entity.ClassInfo;
import com.baozhou.baozhouplatform.entity.ClassStudent;
import com.baozhou.baozhouplatform.service.IClassInfoService;
import com.baozhou.baozhouplatform.service.IClassStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassStudent控制器层
 *
 * API说明：
 * - 用于管理学生和班级的关联关系
 * - 主要操作：学生加入班级、退出班级、查询学生在哪些班级等
 */
@RestController
@RequestMapping("/api/class-student")
public class ClassStudentController {

    @Autowired
    private IClassStudentService classStudentService;

    @Autowired
    private IClassInfoService classInfoService;

    /**
     * 学生加入班级
     *
     * POST http://localhost:8080/api/class-student
     * Body: {"classId": 1, "studentId": 2, "status": "在读"}
     */
    @PostMapping
    public Map<String, Object> save(@RequestBody ClassStudent classStudent) {
        Map<String, Object> result = new HashMap<>();

        // 1. 检查班级是否存在
        ClassInfo classInfo = classInfoService.getById(classStudent.getClassId());
        if (classInfo == null) {
            result.put("success", false);
            result.put("message", "班级不存在");
            return result;
        }

        // 2. 检查班级是否已满
        if (classInfo.getCurrentStudents() >= classInfo.getMaxStudents()) {
            result.put("success", false);
            result.put("message", "班级人数已满");
            return result;
        }

        // 3. 检查学生是否已在该班级
        long count = classStudentService.lambdaQuery()
                .eq(ClassStudent::getClassId, classStudent.getClassId())
                .eq(ClassStudent::getStudentId, classStudent.getStudentId())
                .eq(ClassStudent::getStatus, "在读")
                .count();

        if (count > 0) {
            result.put("success", false);
            result.put("message", "该学生已在此班级中");
            return result;
        }

        // 4. 保存关联
        boolean saved = classStudentService.save(classStudent);

        if (saved) {
            // 5. 更新班级的学生数量
            classInfo.setCurrentStudents(classInfo.getCurrentStudents() + 1);
            classInfoService.updateById(classInfo);
        }

        result.put("success", saved);
        result.put("message", saved ? "添加成功" : "添加失败");
        result.put("data", classStudent);

        return result;
    }

    /**
     * 根据ID删除关联记录（学生退出班级）
     *
     * DELETE http://localhost:8080/api/class-student/1
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return classStudentService.removeById(id);
    }

    /**
     * 更新关联信息（比如修改状态为"退班"）
     *
     * PUT http://localhost:8080/api/class-student
     * Body: {"relationId": 1, "status": "退班"}
     */
    @PutMapping
    public boolean update(@RequestBody ClassStudent classStudent) {
        return classStudentService.updateById(classStudent);
    }

    /**
     * 根据ID查询关联记录
     *
     * GET http://localhost:8080/api/class-student/1
     */
    @GetMapping("/{id}")
    public ClassStudent getById(@PathVariable Integer id) {
        return classStudentService.getById(id);
    }

    /**
     * 查询所有关联记录
     *
     * GET http://localhost:8080/api/class-student
     */
    @GetMapping
    public List<ClassStudent> list() {
        return classStudentService.list();
    }

    /**
     * 分页查询关联记录
     *
     * GET http://localhost:8080/api/class-student/page?page=1&size=10
     */
    @GetMapping("/page")
    public Page<ClassStudent> page(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        return classStudentService.page(new Page<>(page, size));
    }
}
