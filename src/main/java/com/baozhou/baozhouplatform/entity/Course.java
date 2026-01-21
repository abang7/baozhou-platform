package com.baozhou.baozhouplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 课程表实体类
 * 对应数据库中的 courses 表
 *
 * 表设计说明：
 * - courses表：存储课程的基本信息（学科+年级），比如"初中数学"、"高中英语"
 * - course_schedules表：存储具体的排课信息（哪个老师、在哪个教室、什么时间上课）
 *
 * 为什么要分成两个表？
 * - 避免数据冗余：同一个课程可能在不同时间、由不同老师上多次
 * - 分离关注点：课程信息 vs 排课信息
 */
@Data
@TableName("courses")
public class Course {

    /**
     * 课程ID - 主键，自增
     *
     * 设计说明：
     * - 使用 @TableId(type = IdType.AUTO) 自动递增
     * - 这个ID会被 course_schedules 表引用（外键关联）
     */
    @TableId(type = IdType.AUTO)
    private Integer courseId;

    /**
     * 学科名称
     *
     * 设计说明：
     * - SQL中是 enum('英语','数学','语文','物理','化学','政治','历史','地理')
     * - Java中使用 String 存储，只能存储枚举中定义的8个学科之一
     * - 数据库层面保证了只能存这8个值，Java层不需要额外验证
     *
     * 业务含义：
     * - 表示这门课是什么学科
     * - 比如：数学、英语、物理等
     */
    private String courseName;

    /**
     * 年级级别
     *
     * 设计说明：
     * - SQL中是 enum('小学','初中','高中')
     * - Java中使用 String 存储
     * - 只能是：'小学'、'初中'、'高中' 三者之一
     *
     * 业务含义：
     * - 表示这门课属于哪个学段
     * - 比如：小学数学、初中数学、高中数学
     *
     * 组合起来：
     * - courseName='数学' + level='初中' = "初中数学"
     */
    private String level;
}