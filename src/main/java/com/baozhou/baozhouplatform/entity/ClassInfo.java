package com.baozhou.baozhouplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 班级表实体类
 * 对应数据库中的 classes 表
 *
 * 注意：类名用 ClassInfo 而不是 Class
 * 因为 class 是 Java 的关键字，不能作为类名
 *
 * 学习要点：
 * 1. 避免使用Java关键字
 * 2. 枚举字段的处理
 * 3. 布尔值的处理（tinyint(1)）
 */
@Data
@TableName("classes")
public class ClassInfo {

    /**
     * 班级ID - 主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer classId;

    /**
     * 教师ID
     *
     * 设计说明：
     * - int，必填
     * - 外键，关联到 teachers 表
     * - 表示这个班级的任课老师
     */
    private Integer teacherId;

    /**
     * 星期几
     *
     * 设计说明：
     * - enum('周一','周二','周三','周四','周五','周六','周日')
     * - String类型，必填
     * - 表示班级上课是在星期几
     */
    private String weekday;

    /**
     * 时段
     *
     * 设计说明：
     * - enum('07:40-09:40','09:40-11:40','12:30-14:30','14:30-16:30','16:30-18:30','18:50-20:50')
     * - String类型，必填
     * - 表示班级上课的具体时间段
     *
     * 为什么用枚举？
     * - 固定的上课时间段，方便排课
     * - 避免输入格式混乱（比如 9:40 和 09:40）
     */
    private String timeSlot;

    /**
     * 学科
     *
     * 设计说明：
     * - enum('数学','英语','语文','物理','化学','政治','历史','地理')
     * - String类型，默认'数学'
     * - 表示班级教授的学科
     */
    private String subject;

    /**
     * 班级类型
     *
     * 设计说明：
     * - enum('一对一','一对三')
     * - String类型，必填
     * - 表示班级的教学模式
     *
     * 业务含义：
     * - 一对一：1个老师教1个学生
     * - 一对三：1个老师教最多3个学生
     */
    private String classType;

    /**
     * 最大学生数
     *
     * 设计说明：
     * - int，必填
     * - 根据班级类型决定：
     *   - 一对一：max_students = 1
     *   - 一对三：max_students = 3
     */
    private Integer maxStudents;

    /**
     * 当前学生数
     *
     * 设计说明：
     * - int，默认0
     * - 当前班级中实际的学生数量
     * - 不能超过 maxStudents
     *
     * 注意：
     * - 这个字段由应用程序维护
     * - 每次添加或删除学生时需要更新
     */
    private Integer currentStudents;

    /**
     * 是否生效
     *
     * 设计说明：
     * - tinyint(1)，默认1
     * - Java中使用 Boolean 或 Integer
     * - 这里用 Boolean 更符合语义
     * - 1 = true（生效），0 = false（失效）
     *
     * 业务场景：
     * - true：班级正常进行中
     * - false：班级已结束或取消
     *
     * 为什么需要这个字段？
     * - 不是简单地删除班级记录
     * - 保留历史数据，但标记为不再生效
     * - 可以统计历史班级数据
     */
    private Boolean isActive;

    /**
     * 创建时间
     *
     * 设计说明：
     * - timestamp，自动生成
     * - 班级创建的时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     *
     * 设计说明：
     * - timestamp，自动更新
     * - 班级信息最后修改的时间
     */
    private LocalDateTime updatedAt;
}
