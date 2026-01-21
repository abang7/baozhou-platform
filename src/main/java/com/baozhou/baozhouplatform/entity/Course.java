package com.baozhou.baozhouplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 课程表实体类
 * 对应数据库中的 course 表
 */
@Data  // Lombok注解，自动生成getter/setter、toString等方法
@TableName("course")  // 指定对应的数据库表名
public class Course {

    /**
     * 课程ID - 主键，自增
     */
    @TableId(type = IdType.AUTO)  // 表示主键自动递增
    private Long id;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 上课地点
     */
    private String location;

    /**
     * 星期几（1-7，代表周一到周日）
     */
    private Integer weekDay;

    /**
     * 开始节次（比如第1节课、第2节课）
     */
    private Integer startSection;

    /**
     * 结束节次
     */
    private Integer endSection;
}