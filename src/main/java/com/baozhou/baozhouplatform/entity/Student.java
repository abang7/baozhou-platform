package com.baozhou.baozhouplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生基本信息表实体类
 * 对应数据库中的 students 表
 *
 * 学习要点：
 * 1. 如何处理日期类型（LocalDate、LocalDateTime）
 * 2. 如何处理大量可选字段
 * 3. 业务字段的设计思路
 */
@Data
@TableName("students")
public class Student {

    /**
     * 学生ID - 主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer studentId;

    /**
     * 学生姓名
     *
     * 设计说明：
     * - varchar(50)，必填
     * - 学生真实姓名
     */
    private String studentName;

    /**
     * 性别
     *
     * 设计说明：
     * - SQL中是 enum('男','女')
     * - 可以为空，String类型存储
     */
    private String sex;

    /**
     * 出生日期
     *
     * 设计说明：
     * - SQL中是 date 类型
     * - Java中使用 LocalDate（不包含时间）
     * - 可以为空
     *
     * LocalDate vs Date：
     * - LocalDate：Java 8+新API，推荐使用，更安全
     * - Date：老式API，有线程安全问题
     */
    private LocalDate birthday;

    /**
     * 年级
     *
     * 设计说明：
     * - int类型，必填
     * - 比如：10表示高一，11表示高二，12表示高三
     */
    private Integer grade;

    /**
     * 学段
     *
     * 设计说明：
     * - enum('小学','初中','高中')
     * - String类型，可以为空
     */
    private String level;

    /**
     * 所在学校
     *
     * 设计说明：
     * - varchar(100)，可选
     * - 学生就读的学校名称
     */
    private String schoolName;

    /**
     * 家长姓名
     *
     * 设计说明：
     * - varchar(50)，可选
     * - 主要联系人（通常是父亲或母亲）
     */
    private String parentName;

    /**
     * 家长手机号
     *
     * 设计说明：
     * - varchar(20)，可选
     * - 存储家长联系电话
     */
    private String parentPhone;

    /**
     * 紧急联系人电话
     *
     * 设计说明：
     * - varchar(20)，可选
     * - 备用联系电话
     */
    private String emergencyContact;

    /**
     * 家庭住址
     *
     * 设计说明：
     * - varchar(200)，可选
     * - 学生家庭住址
     */
    private String address;

    /**
     * 入学日期
     *
     * 设计说明：
     * - date类型，可选
     * - 学生首次来校的日期
     */
    private LocalDate enrollmentDate;

    /**
     * 在读状态
     *
     * 设计说明：
     * - enum('在读','停课','毕业','退费')
     * - String类型，默认值'在读'
     */
    private String status;

    /**
     * 来源渠道
     *
     * 设计说明：
     * - enum('自然到访','转介绍','市场活动','网络咨询')
     * - String类型，可选
     * - 记录学生如何了解到机构
     */
    private String source;

    /**
     * 课程顾问ID
     *
     * 设计说明：
     * - int，可选
     * - 外键，关联到对应的销售人员表
     */
    private Integer consultantId;

    /**
     * 班主任ID
     *
     * 设计说明：
     * - int，可选
     * - 外键，关联到教师表
     */
    private Integer headTeacherId;

    /**
     * 备注
     *
     * 设计说明：
     * - text类型，可选
     * - text用于存储大量文本
     * - 可以记录特殊情况、注意事项等
     */
    private String remark;

    /**
     * 创建时间
     *
     * 设计说明：
     * - timestamp类型，自动生成
     * - Java中使用 LocalDateTime
     * - 由数据库自动设置为当前时间
     *
     * 为什么不手动设置？
     * - 使用 DEFAULT CURRENT_TIMESTAMP 让数据库自动处理
     * - 避免应用服务器时间不一致的问题
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     *
     * 设计说明：
     * - timestamp类型，自动更新
     * - 每次记录更新时，数据库自动更新为当前时间
     * - ON UPDATE CURRENT_TIMESTAMP
     */
    private LocalDateTime updatedAt;
}
