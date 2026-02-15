package com.baozhou.baozhouplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baozhou.baozhouplatform.dto.ScheduleDTO;
import com.baozhou.baozhouplatform.entity.ClassInfo;

import java.util.List;

/**
 * ClassInfo服务层接口
 */
public interface IClassInfoService extends IService<ClassInfo> {

    /**
     * 获取完整的课程表数据
     * 包括：班级信息、教师信息、学生信息
     *
     * @return 课程表列表
     */
    List<ScheduleDTO> getScheduleList();

    /**
     * 检查是否有重复的课程
     * 重复定义：同一个教师、同一个星期几、同一个时间段
     *
     * @param teacherId 教师ID
     * @param weekday 星期几
     * @param timeSlot 时间段
     * @param excludeClassId 排除的班级ID（用于更新时排除自身）
     * @return true 表示有重复，false 表示没有重复
     */
    boolean hasDuplicateClass(Integer teacherId, String weekday, String timeSlot, Integer excludeClassId);
}
