package com.baozhou.baozhouplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 课程安排表实体类
 * 对应数据库中的 course_schedules 表
 *
 * 表设计说明：
 * - 这是实际的排课信息表，记录"哪位老师、在哪间教室、什么时间、上哪门课"
 * - 通过多个外键关联其他表，体现表之间的关系
 *
 * 业务场景举例：
 * - "周三第2节课，张老师在201教室上初中数学"
 * - course_id -> courses表（初中数学）
 * - teacher_id -> teachers表（张老师）
 * - classroom_id -> classrooms表（201教室）
 * - week_day=3, period=2（周三第2节）
 */
@Data
@TableName("course_schedules")
public class CourseSchedule {

    /**
     * 排课ID - 主键，自增
     *
     * 设计说明：
     * - 使用 @TableId(type = IdType.AUTO) 自动递增
     * - 每条排课记录有唯一ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 课程ID - 外键，关联courses表
     *
     * 设计说明：
     * - 指向 courses.course_id
     * - 表示这条排课记录是上哪门课
     * - 比如：course_id=1 表示"初中数学"
     *
     * 为什么存ID而不是对象？
     * - 数据库设计原则：关系型数据库通过ID关联表
     * - 节省空间：只存一个数字，而不是重复存储课程名称
     * - 数据一致性：课程信息修改时，只需要改courses表
     *
     * Java中如何获取完整课程信息？
     * - 可以通过 courseService.getById(courseId) 查询
     * - 或者在查询时使用连表查询（LEFT JOIN）
     */
    private Integer courseId;

    /**
     * 教师ID - 外键，关联teachers表
     *
     * 设计说明：
     * - 指向 teachers.teacher_id
     * - 表示这条排课记录是哪位老师上课
     * - 比如：teacher_id=5 表示"张老师"
     *
     * 业务含义：
     * - 同一门课可能由多个老师教
     * - 比如初中数学，张老师教1班，李老师教2班
     * - 通过不同的teacher_id区分
     */
    private Integer teacherId;

    /**
     * 教室ID - 外键，关联classrooms表（注：你的SQL中还没创建这个表）
     *
     * 设计说明：
     * - 指向 classrooms.class_id（假设的教室表）
     * - 表示在哪个教室上课
     * - 比如：classroom_id=101 表示"201教室"
     *
     * 为什么需要教室表？
     * - 避免教室名称重复存储
     * - 便于管理教室容量、设备等信息
     * - 检查教室冲突：同一时间不能有两门课在同一个教室
     *
     * 提示：你可能需要创建 classrooms 表
     */
    private Integer classroomId;

    /**
     * 星期几
     *
     * 设计说明：
     * - int 类型，值为 1-7
     * - 1=周一，2=周二，...，7=周日
     * - 表示这门课在周几上
     *
     * 为什么用数字不用字符串？
     * - 节省存储空间（int占4字节，varchar占更多）
     * - 便于计算（比如计算下周的时间）
     * - 可以用枚举或常量提高可读性
     *
     * 扩展：可以定义常量
     * - public static final int MONDAY = 1;
     * - public static final int TUESDAY = 2;
     */
    private Integer weekDay;

    /**
     * 节次
     *
     * 设计说明：
     * - int 类型，表示第几节课
     * - 比如：1=第1节，2=第2节，...，8=第8节
     * - 不同学校节次安排可能不同
     *
     * 业务场景：
     * - week_day=3, period=2 表示"周三第2节"
     * - 结合起来确定具体的上课时间
     *
     * 注意：
     * - 有的课程可能连上2节（period=1和period=2）
     * - 需要在业务逻辑中处理连续节次的情况
     */
    private Integer period;

    /**
     * 周模式
     *
     * 设计说明：
     * - varchar(10) 类型
     * - 表示这门课是每周都上，还是单周/双周上
     * - 可能的值：'每周'、'单周'、'双周'
     *
     * 业务含义：
     * - '每周'：每个星期都上这门课（最常见）
     * - '单周'：只在单数学周上（第1、3、5...周）
     * - '双周'：只在双数学周上（第2、4、6...周）
     *
     * 为什么需要这个字段？
     * - 有些课程不是每周都上
     - 比如实验课可能两周上一次
     */
    private String weekPattern;

    /**
     * 学期ID - 外键，关联terms表（注：你的SQL中还没创建这个表）
     *
     * 设计说明：
     * - 指向 terms.term_id（假设的学期表）
     * - 表示这条排课记录属于哪个学期
     * - 比如：term_id=1 表示"2024-2025第一学期"
     *
     * 为什么需要学期概念？
     * - 课程表每学期都不同
     * - 需要区分不同学期的排课
     * - 便于历史数据查询和管理
     *
     * 提示：你可能需要创建 terms 表
     * - term_id: 学期ID
     * - term_name: 学期名称（如"2024-2025第一学期"）
     * - start_date: 开始日期
     * - end_date: 结束日期
     */
    private Integer termId;
}