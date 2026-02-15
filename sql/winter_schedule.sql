-- ========================================
-- 寒假班请假和排课系统
-- ========================================

-- 1. 学生请假表
DROP TABLE IF EXISTS `student_leave`;
CREATE TABLE `student_leave` (
  `leave_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '请假ID',
  `student_id` INT NOT NULL COMMENT '学生ID',
  `semester_id` INT NOT NULL COMMENT '学期ID',
  `start_date` DATE NOT NULL COMMENT '请假开始日期',
  `end_date` DATE NOT NULL COMMENT '请假结束日期',
  `leave_type` ENUM('旅游','探亲','生病','其他') DEFAULT '其他' COMMENT '请假类型',
  `reason` VARCHAR(200) COMMENT '请假原因',
  `status` ENUM('待审核','已通过','已拒绝') DEFAULT '待审核' COMMENT '审核状态',
  `remark` TEXT COMMENT '备注',
  `created_by` VARCHAR(50) COMMENT '创建人',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_student` (student_id),
  INDEX `idx_semester` (semester_id),
  INDEX `idx_date_range` (start_date, end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生请假表';

-- 2. 学期表（如果还没有）
DROP TABLE IF EXISTS `semesters`;
CREATE TABLE `semesters` (
  `semester_id` INT PRIMARY KEY AUTO_INCREMENT,
  `semester_name` VARCHAR(50) NOT NULL COMMENT '学期名称',
  `semester_type` ENUM('春季班','暑假班','秋季班','寒假班') NOT NULL COMMENT '学期类型',
  `start_date` DATE NOT NULL COMMENT '开始日期',
  `end_date` DATE NOT NULL COMMENT '结束日期',
  `status` ENUM('未开始','进行中','已结束','已关闭') DEFAULT '未开始',
  `registration_start` DATE COMMENT '报名开始',
  `registration_end` DATE COMMENT '报名截止',
  `description` TEXT COMMENT '说明',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学期表';

-- 3. 插入示例寒假学期
INSERT INTO `semesters` (`semester_name`, `semester_type`, `start_date`, `end_date`, `status`, `description`)
VALUES ('2025寒假班', '寒假班', '2025-01-15', '2025-02-15', '进行中', '寒假短期课程，一三五按周六课表，二四六按周日课表');

-- 4. 班级排课表（记录具体的上课日期）
DROP TABLE IF EXISTS `class_schedules`;
CREATE TABLE `class_schedules` (
  `schedule_id` INT PRIMARY KEY AUTO_INCREMENT,
  `class_id` INT NOT NULL COMMENT '班级ID',
  `semester_id` INT NOT NULL COMMENT '学期ID',
  `course_date` DATE NOT NULL COMMENT '上课日期',
  `weekday` ENUM('周一','周二','周三','周四','周五','周六','周日') COMMENT '星期几',
  `start_time` TIME NOT NULL COMMENT '开始时间',
  `end_time` TIME NOT NULL COMMENT '结束时间',
  `source_weekday` ENUM('周六','周日') COMMENT '源课表（来自周六还是周日的课）',
  `classroom` VARCHAR(50) COMMENT '教室',
  `status` ENUM('正常','取消','已调课') DEFAULT '正常',
  `remark` TEXT COMMENT '备注',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `uk_class_date` (class_id, course_date),
  INDEX `idx_semester` (semester_id),
  INDEX `idx_date` (course_date),
  INDEX `idx_classroom` (classroom, course_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级排课表';

-- 5. 学生出勤表（实际上课记录）
DROP TABLE IF EXISTS `student_attendance`;
CREATE TABLE `student_attendance` (
  `attendance_id` INT PRIMARY KEY AUTO_INCREMENT,
  `schedule_id` INT NOT NULL COMMENT '排课ID',
  `student_id` INT NOT NULL COMMENT '学生ID',
  `attendance_status` ENUM('正常','请假','缺课','补课') DEFAULT '正常',
  `homework_score` DECIMAL(5,2) COMMENT '作业成绩',
  `remark` TEXT COMMENT '备注',
  `recorded_by` VARCHAR(50) COMMENT '记录人',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `uk_schedule_student` (schedule_id, student_id),
  INDEX `idx_student` (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生出勤表';
