/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80044
 Source Host           : localhost:3306
 Source Schema         : baozhou_platform

 Target Server Type    : MySQL
 Target Server Version : 80044
 File Encoding         : 65001

 Date: 22/01/2026 18:43:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for class_students
-- ----------------------------
DROP TABLE IF EXISTS `class_students`;
CREATE TABLE `class_students`  (
  `relation_id` int NOT NULL AUTO_INCREMENT,
  `class_id` int NOT NULL COMMENT '班级ID',
  `student_id` int NOT NULL COMMENT '学生ID',
  `join_date` date NOT NULL DEFAULT 'curdate()' COMMENT '加入日期',
  `status` enum('在读','请假','停课','退班') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '在读' COMMENT '状态',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`relation_id`) USING BTREE,
  UNIQUE INDEX `uk_class_student`(`class_id`, `student_id`) USING BTREE,
  INDEX `idx_student`(`student_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学生-班级关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of class_students
-- ----------------------------
INSERT INTO `class_students` VALUES (1, 1, 1, '2026-01-22', '在读', '2026-01-22 18:11:46');
INSERT INTO `class_students` VALUES (2, 2, 2, '2026-01-22', '在读', '2026-01-22 18:12:01');
INSERT INTO `class_students` VALUES (3, 2, 3, '2026-01-22', '在读', '2026-01-22 18:12:08');

-- ----------------------------
-- Table structure for classes
-- ----------------------------
DROP TABLE IF EXISTS `classes`;
CREATE TABLE `classes`  (
  `class_id` int NOT NULL AUTO_INCREMENT COMMENT '班级ID',
  `teacher_id` int NOT NULL COMMENT '教师ID',
  `weekday` enum('周一','周二','周三','周四','周五','周六','周日') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `time_slot` enum('07:40-09:40','09:40-11:40','12:30-14:30','14:30-16:30','16:30-18:30','18:50-20:50') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `subject` enum('数学','英语','语文','物理','化学','政治','历史','地理') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '数学',
  `class_type` enum('一对一','一对三') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '班级类型',
  `max_students` int NOT NULL COMMENT '最大学生数',
  `current_students` int NULL DEFAULT 0 COMMENT '当前学生数',
  `is_active` tinyint(1) NULL DEFAULT 1 COMMENT '是否生效',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`class_id`) USING BTREE,
  UNIQUE INDEX `uk_teacher_slot`(`teacher_id`, `weekday`, `time_slot`, `is_active`) USING BTREE,
  INDEX `idx_time_slot`(`time_slot`) USING BTREE,
  INDEX `idx_active`(`is_active`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '班级表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of classes
-- ----------------------------
INSERT INTO `classes` VALUES (1, 1, '周六', '07:40-09:40', '数学', '一对一', 1, 1, 1, '2026-01-22 18:05:52', '2026-01-22 18:05:52');
INSERT INTO `classes` VALUES (2, 1, '周六', '12:30-14:30', '数学', '一对三', 3, 2, 1, '2026-01-22 18:08:43', '2026-01-22 18:37:33');

-- ----------------------------
-- Table structure for students
-- ----------------------------
DROP TABLE IF EXISTS `students`;
CREATE TABLE `students`  (
  `student_id` int NOT NULL AUTO_INCREMENT COMMENT '学生ID',
  `student_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学生姓名',
  `sex` enum('男','女') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `birthday` date NULL DEFAULT NULL COMMENT '出生日期',
  `grade` int NOT NULL COMMENT '年级',
  `level` enum('小学','初中','高中') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学段',
  `school_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '所在学校',
  `parent_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '家长姓名',
  `parent_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '家长手机号',
  `emergency_contact` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '紧急联系人电话',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '家庭住址',
  `enrollment_date` date NULL DEFAULT NULL COMMENT '入学日期',
  `status` enum('在读','停课','毕业','退费') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '在读' COMMENT '在读状态',
  `source` enum('自然到访','转介绍','市场活动','网络咨询') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '来源渠道',
  `consultant_id` int NULL DEFAULT NULL COMMENT '课程顾问ID',
  `head_teacher_id` int NULL DEFAULT NULL COMMENT '班主任ID',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`student_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学生基本信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of students
-- ----------------------------
INSERT INTO `students` VALUES (1, '孔祥菲', '女', NULL, 10, '高中', NULL, '孔父', '13800010001', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (2, '张博彦', '男', NULL, 10, '高中', NULL, '张父', '13800010002', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (3, '董轩', '男', NULL, 10, '高中', NULL, '董父', '13800010003', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (4, '金奕歆', '女', NULL, 10, '高中', NULL, '金父', '13800010004', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (5, '曹祎劼', '男', NULL, 10, '高中', NULL, '曹父', '13800010005', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (6, '赵米佳', '女', NULL, 10, '高中', NULL, '赵父', '13800010006', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (7, '何煦瑞', '男', NULL, 10, '高中', NULL, '何父', '13800010007', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (8, '邢易晨', '女', NULL, 10, '高中', NULL, '邢父', '13800010008', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (9, '李梦瑶', '女', NULL, 10, '高中', NULL, '李父', '13800010009', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (10, '贺语轩', '女', NULL, 10, '高中', NULL, '贺父', '13800010010', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (11, '俞凯宸', '男', NULL, 11, '高中', NULL, '俞父', '13800020001', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (12, '顾佳琳', '女', NULL, 11, '高中', NULL, '顾父', '13800020002', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (13, '王赫', '男', NULL, 11, '高中', NULL, '王父', '13800020003', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (14, '陈睿阳', '男', NULL, 11, '高中', NULL, '陈父', '13800020004', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (15, '沈奕辰', '女', NULL, 11, '高中', NULL, '沈父', '13800020005', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (16, '黄怡恬', '女', NULL, 11, '高中', NULL, '黄父', '13800020006', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (17, '刘子丹', '男', NULL, 11, '高中', NULL, '刘父', '13800020007', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (18, '刘梓渝', '男', NULL, 12, '高中', NULL, '刘父', '13800030001', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (19, '陆王丹', '女', NULL, 12, '高中', NULL, '陆父', '13800030002', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (20, '高泽楦', '男', NULL, 12, '高中', NULL, '高父', '13800030003', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (21, '陈郑皖龙', '男', NULL, 12, '高中', NULL, '陈父', '13800030004', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (22, '葛焱鑫', '男', NULL, 12, '高中', NULL, '葛父', '13800030005', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (23, '张博寓', '男', NULL, 12, '高中', NULL, '张父', '13800030006', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (24, '杨泽昱', '男', NULL, 12, '高中', NULL, '杨父', '13800030007', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (25, '仇浩宇', '男', NULL, 12, '高中', NULL, '仇父', '13800030008', NULL, NULL, NULL, '在读', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:43:23');
INSERT INTO `students` VALUES (26, '黄昕晨', '男', NULL, 12, '高中', NULL, '黄父', '13800030009', NULL, NULL, NULL, '毕业', NULL, NULL, NULL, NULL, '2026-01-22 17:43:23', '2026-01-22 17:47:16');

-- ----------------------------
-- Table structure for teacher_groups
-- ----------------------------
DROP TABLE IF EXISTS `teacher_groups`;
CREATE TABLE `teacher_groups`  (
  `group_id` int NOT NULL AUTO_INCREMENT,
  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `leader_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teacher_groups
-- ----------------------------
INSERT INTO `teacher_groups` VALUES (1, '高数组', NULL);

-- ----------------------------
-- Table structure for teachers
-- ----------------------------
DROP TABLE IF EXISTS `teachers`;
CREATE TABLE `teachers`  (
  `teacher_id` int NOT NULL AUTO_INCREMENT COMMENT '教师ID',
  `teacher_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '教师姓名',
  `employee_number` int NOT NULL COMMENT '教职工编号',
  `group_id` int NOT NULL COMMENT '学科小组ID',
  `classroom` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所在教室',
  `floor` enum('5楼','7楼','12楼') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '楼层',
  PRIMARY KEY (`teacher_id`) USING BTREE,
  UNIQUE INDEX `employee_number`(`employee_number`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teachers
-- ----------------------------
INSERT INTO `teachers` VALUES (1, '胡俸齐', 1, 1, 'A06', '12楼');

SET FOREIGN_KEY_CHECKS = 1;
