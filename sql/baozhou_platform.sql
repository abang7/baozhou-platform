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

 Date: 21/01/2026 19:46:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for course_schedules
-- ----------------------------
DROP TABLE IF EXISTS `course_schedules`;
CREATE TABLE `course_schedules`  (
  `id` int NOT NULL,
  `course_id` int NULL DEFAULT NULL,
  `teacher_id` int NULL DEFAULT NULL,
  `classroom_id` int NULL DEFAULT NULL,
  `week_day` int NULL DEFAULT NULL,
  `period` int NULL DEFAULT NULL,
  `week_pattern` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `term_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_schedules
-- ----------------------------

-- ----------------------------
-- Table structure for courses
-- ----------------------------
DROP TABLE IF EXISTS `courses`;
CREATE TABLE `courses`  (
  `course_id` int NOT NULL AUTO_INCREMENT COMMENT '课程ID，主键自增',
  `course_name` enum('英语','数学','语文','物理','化学','政治','历史','地理') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学科',
  `level` enum('小学','初中','高中') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '年级',
  PRIMARY KEY (`course_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '课程信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of courses
-- ----------------------------

-- ----------------------------
-- Table structure for teacher_groups
-- ----------------------------
DROP TABLE IF EXISTS `teacher_groups`;
CREATE TABLE `teacher_groups`  (
  `group_id` int NOT NULL,
  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `level` enum('小学','初中','高中') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `leader_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`group_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teacher_groups
-- ----------------------------

-- ----------------------------
-- Table structure for teachers
-- ----------------------------
DROP TABLE IF EXISTS `teachers`;
CREATE TABLE `teachers`  (
  `teacher_id` int NOT NULL AUTO_INCREMENT COMMENT '教师ID',
  `teacher_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '教师姓名',
  `employee_number` int NOT NULL COMMENT '教职工编号',
  `group_id` int NOT NULL COMMENT '学科小组ID',
  PRIMARY KEY (`teacher_id`) USING BTREE,
  UNIQUE INDEX `employee_number`(`employee_number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teachers
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
