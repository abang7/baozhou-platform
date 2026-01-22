package com.baozhou.baozhouplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 教师小组表实体类
 * 对应数据库中的 teacher_groups 表
 *
 * 学习要点：
 * 1. 自增主键的使用（和Teacher一样）
 * 2. 如何处理枚举类型字段
 */
@Data
@TableName("teacher_groups")
public class TeacherGroup {

    /**
     * 小组ID - 主键，自增
     *
     * 设计说明：
     * - 和 teacher_id 一样，使用 AUTO_INCREMENT 自增
     * - @TableId(type = IdType.AUTO) 表示主键自动递增
     * - 插入数据时不需要设置这个值，数据库会自动分配
     */
    @TableId(type = IdType.AUTO)
    private Integer groupId;

    /**
     * 小组名称
     *
     * 设计说明：
     * - varchar(50) 对应 String
     * - 比如：高数组、初数组、小学组等
     */
    private String groupName;

    /**
     * 组长ID
     *
     * 设计说明：
     * - SQL中是 int NULL，表示可以为空
     * - Java中对应 Integer（可以为null）
     * - 这是一个可选字段，表示该小组的组长是哪位教师
     * - 如果为null，说明该小组还没有指定组长
     *
     * 为什么设计为可空？
     * - 业务上有些小组可能刚成立还没选组长
     * - 灵活性更高，不是所有小组都必须有组长
     * - 通过外键关联到 teachers 表的 teacher_id
     */
    private Integer leaderId;
}