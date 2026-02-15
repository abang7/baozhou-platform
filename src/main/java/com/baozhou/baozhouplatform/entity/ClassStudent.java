package com.baozhou.baozhouplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生-班级关联表实体类
 * 对应数据库中的 class_students 表
 *
 * 学习要点：
 * 1. 多对多关系的处理
 * 2. 唯一约束（UNIQUE INDEX）的含义
 * 3. 关联表的设计思路
 *
 * 表结构说明：
 * - relation_id: 主键
 * - class_id: 班级ID（外键）
 * - student_id: 学生ID（外键）
 * - 唯一约束: uk_class_student(class_id, student_id)
 *   表示：同一个学生不能重复加入同一个班级
 */
@Data
@TableName("class_students")
public class ClassStudent {

    /**
     * 关联ID - 主键，自增
     *
     * 设计说明：
     * - 这是关联表自己的主键
     * - 虽然也可以用 (class_id + student_id) 作为联合主键
     * - 但使用独立主键更灵活，便于扩展
     */
    @TableId(type = IdType.AUTO)
    private Integer relationId;

    /**
     * 班级ID
     *
     * 设计说明：
     * - int，必填
     * - 外键，关联到 classes 表的 class_id
     * - 表示学生加入的班级
     */
    private Integer classId;

    /**
     * 学生ID
     *
     * 设计说明：
     * - int，必填
     * - 外键，关联到 students 表的 student_id
     * - 表示加入班级的学生
     */
    private Integer studentId;

    /**
     * 加入日期
     *
     * 设计说明：
     * - date类型，默认当天
     * - 学生加入班级的日期
     * - 为什么需要这个字段？
     *   - 记录学生何时开始上课
     *   - 计算学习时长
     *   - 统计班级学生增长情况
     */
    private LocalDate joinDate;

    /**
     * 状态
     *
     * 设计说明：
     * - enum('在读','请假','停课','退班')
     * - String类型，默认'在读'
     *
     * 业务含义：
     * - 在读：学生正常上课
     * - 请假：暂时不上课（会保留名额）
     * - 停课：暂停课程（可能后续恢复）
     * - 退班：已退出班级（不再上课）
     *
     * 为什么需要状态？
     * - 学生加入班级后，状态可能会变化
     * - 不删除记录，保留历史
     * - 方便统计和分析
     */
    private String status;

    /**
     * 创建时间
     *
     * 设计说明：
     * - timestamp，自动生成
     * - 记录创建这条关联记录的时间
     */
    private LocalDateTime createdAt;

    /**
     * 设计思路讲解：
     *
     * 1. 为什么需要关联表？
     *    - 学生和班级是多对多关系：一个学生可以上多个班级，一个班级有多个学生
     *    - 如果在 students 表加 class_id 字段：一个学生只能加入一个班级（不对）
     *    - 如果在 classes 表加 student_id 字段：需要存储多个学生ID（不规范）
     *    - 解决：用关联表记录学生和班级的关系
     *
     * 2. 唯一约束的作用：
     *    - UNIQUE INDEX `uk_class_student`(`class_id`, `student_id`)
     *    - 防止同一个学生重复加入同一个班级
     *    - 比如不能插入两条 (class_id=1, student_id=1) 的记录
     *
     * 3. 索引设计：
     *    - idx_student: 经常查询某个学生加入了哪些班级
     *    - idx_status: 经常查询某个状态下的学生（比如所有"在读"的学生）
     *
     * 4. 业务场景：
     *    - 学生报名：插入新记录到 class_students
     *    - 学生退班：更新 status 为 '退班'
     *    - 查询班级学生：SELECT * FROM class_students WHERE class_id = ?
     *    - 查询学生班级：SELECT * FROM class_students WHERE student_id = ?
     */
}
