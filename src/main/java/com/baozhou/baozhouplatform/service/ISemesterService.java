package com.baozhou.baozhouplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baozhou.baozhouplatform.entity.Semester;

import java.util.List;

/**
 * 学期Service
 */
public interface ISemesterService extends IService<Semester> {

    /**
     * 获取当前学期
     *
     * @return 当前学期
     */
    Semester getCurrentSemester();

    /**
     * 根据类型获取学期
     *
     * @param type 学期类型（如：寒假班）
     * @return 学期列表
     */
    List<Semester> getSemestersByType(String type);
}
