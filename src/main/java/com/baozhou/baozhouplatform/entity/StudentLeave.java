package com.baozhou.baozhouplatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生请假实体类
 */
@Data
@TableName("student_leave")
public class StudentLeave {

    /**
     * 请假ID
     */
    @TableId(type = IdType.AUTO)
    private Integer leaveId;

    /**
     * 学生ID
     */
    private Integer studentId;

    /**
     * 学期ID
     */
    private Integer semesterId;

    /**
     * 请假开始日期
     */
    private LocalDate startDate;

    /**
     * 请假结束日期
     */
    private LocalDate endDate;

    /**
     * 请假类型
     */
    private String leaveType;

    /**
     * 请假原因
     */
    private String reason;

    /**
     * 审核状态：待审核、已通过、已拒绝
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
