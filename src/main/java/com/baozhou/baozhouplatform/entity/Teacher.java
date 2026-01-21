package com.baozhou.baozhouplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 教师表实体类
 * 对应数据库中的 teachers 表
 *
 * 实体类设计原则：
 * 1. 每个字段对应数据库表的一列
 * 2. 使用注解标注主键、表名等映射关系
 * 3. 使用Lombok简化代码（自动生成getter/setter）
 */
@Data  // Lombok注解：自动生成getter、setter、toString、equals、hashCode方法
@TableName("teachers")  // 指定对应的数据库表名是 teachers
public class Teacher {

    /**
     * 教师ID - 主键，自增
     *
     * 设计说明：
     * - @TableId 表示这是主键字段
     * - type = IdType.AUTO 表示主键自动递增（对应SQL的AUTO_INCREMENT）
     * - 使用Long类型而不是int，因为Long能表示更大的数值范围，且允许为null
     */
    @TableId(type = IdType.AUTO)
    private Integer teacherId;

    /**
     * 教师姓名
     *
     * 设计说明：
     * - SQL中是 varchar(50)，Java中对应String类型
     * - String是引用类型，可以处理null值
     * - 长度限制(50)通常在数据库层面验证，Java层可加@Length注解
     */
    private String teacherName;

    /**
     * 教职工编号
     *
     * 设计说明：
     * - SQL中是 int类型，Java中使用Integer包装类
     * - 为什么用Integer而不是int？
     *   1. int是基本类型，默认值是0，无法区分"没设置"和"设置为0"
     *   2. Integer可以为null，更灵活
     * - employee_number在SQL中有UNIQUE约束，表示每个教师的编号唯一
     */
    private Integer employeeNumber;

    /**
     * 学科小组ID
     *
     * 设计说明：
     * - 这是一个外键字段，关联到 teacher_groups 表
     * - 在Java实体中，我们通常只存储ID值
     * - 后续可以通过关联查询获取完整的 TeacherGroup 对象
     *
     * 为什么这样设计？
     * - 数据库中使用外键ID而不是对象，是为了减少数据冗余
     * - Java中可以有 teacherGroup 对象属性用于关联查询（进阶用法）
     */
    private Integer groupId;
}