/*
 Navicat Premium Data Transfer

 Source Server         : oj-server
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : 10.66.47.16:3306
 Source Schema         : zuccoj

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 21/02/2021 18:19:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for contest
-- ----------------------------
DROP TABLE IF EXISTS `contest`;
CREATE TABLE `contest`  (
  `contest_id` int(11) NOT NULL AUTO_INCREMENT,
  `contest_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `begin_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `freeze_time` datetime NULL DEFAULT NULL,
  `unfreeze_time` datetime NULL DEFAULT NULL,
  `is_public` tinyint(1) NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contest_type` int(11) NOT NULL,
  PRIMARY KEY (`contest_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of contest
-- ----------------------------

-- ----------------------------
-- Table structure for contest_member
-- ----------------------------
DROP TABLE IF EXISTS `contest_member`;
CREATE TABLE `contest_member`  (
  `contest_id` int(11) NOT NULL,
  `username` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sign_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`contest_id`, `username`) USING BTREE,
  INDEX `fk_member_user`(`username`) USING BTREE,
  CONSTRAINT `fk_member_contest` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`contest_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_member_user` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of contest_member
-- ----------------------------

-- ----------------------------
-- Table structure for contest_problem
-- ----------------------------
DROP TABLE IF EXISTS `contest_problem`;
CREATE TABLE `contest_problem`  (
  `contest_id` int(11) NOT NULL,
  `problem_order` int(11) NOT NULL,
  `problem_id` int(11) NOT NULL,
  PRIMARY KEY (`contest_id`, `problem_order`) USING BTREE,
  INDEX `fk_problem_id`(`problem_id`) USING BTREE,
  INDEX `fk_contest_id`(`contest_id`) USING BTREE,
  CONSTRAINT `fk_contest_id` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`contest_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_problem_id` FOREIGN KEY (`problem_id`) REFERENCES `problems` (`problem_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of contest_problem
-- ----------------------------

-- ----------------------------
-- Table structure for contest_type_table
-- ----------------------------
DROP TABLE IF EXISTS `contest_type_table`;
CREATE TABLE `contest_type_table`  (
  `contest_type_code` int(11) NOT NULL AUTO_INCREMENT,
  `contest_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`contest_type_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of contest_type_table
-- ----------------------------
INSERT INTO `contest_type_table` VALUES (1, 'ACM/ICPC');
INSERT INTO `contest_type_table` VALUES (2, 'OI');
INSERT INTO `contest_type_table` VALUES (3, 'IOI');
INSERT INTO `contest_type_table` VALUES (4, 'Codeforces');

-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback`  (
  `feedback_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `unread` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`feedback_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of feedback
-- ----------------------------

-- ----------------------------
-- Table structure for judgehost
-- ----------------------------
DROP TABLE IF EXISTS `judgehost`;
CREATE TABLE `judgehost`  (
  `judgehost_username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `judgehost_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`judgehost_username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of judgehost
-- ----------------------------
INSERT INTO `judgehost` VALUES ('Archer', '0Y0umYU2K9UriG+zypYDVg==', '2021-02-21 07:34:38');
INSERT INTO `judgehost` VALUES ('Koluta', '1paEwIYu65OC94DYcqmAOw==', '2021-02-21 08:50:28');

-- ----------------------------
-- Table structure for judgehost_log
-- ----------------------------
DROP TABLE IF EXISTS `judgehost_log`;
CREATE TABLE `judgehost_log`  (
  `judgehost_username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `action_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `source_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of judgehost_log
-- ----------------------------

-- ----------------------------
-- Table structure for lang_table
-- ----------------------------
DROP TABLE IF EXISTS `lang_table`;
CREATE TABLE `lang_table`  (
  `lang_code` int(11) NOT NULL AUTO_INCREMENT,
  `lang_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`lang_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lang_table
-- ----------------------------
INSERT INTO `lang_table` VALUES (1, 'C');
INSERT INTO `lang_table` VALUES (2, 'C++');
INSERT INTO `lang_table` VALUES (3, 'Java');

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `news_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `visible` tinyint(1) NOT NULL,
  `priority` int(11) NOT NULL DEFAULT 1,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`news_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of news
-- ----------------------------

-- ----------------------------
-- Table structure for problems
-- ----------------------------
DROP TABLE IF EXISTS `problems`;
CREATE TABLE `problems`  (
  `problem_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `input` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `output` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `hint` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `time_limit` int(11) NOT NULL,
  `memory_limit` int(11) NOT NULL,
  `spj` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `visible` tinyint(1) NOT NULL DEFAULT 0,
  `tags` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `samples` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`problem_id`) USING BTREE,
  UNIQUE INDEX `idx_problem_id`(`problem_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of problems
-- ----------------------------
INSERT INTO `problems` VALUES (1, 'A+B Problem', '给定两个整数 $A$ 和 $B$， 计算 $A+B$ 的结果。', '输入包含两个用空格隔开的整数 $A$ 和 $B$，两个数都在 `int` 范围内。', '输出 $A+B$ 的结果', 'Enjoy ZUCC Online Judge β！ :)', 1000, 262144, NULL, 0, '[\"入门\"]', '[{\"input\":\"1 1\",\"output\":\"2\"},{\"input\":\"2 2\",\"output\":\"4\"}]');

-- ----------------------------
-- Table structure for result_table
-- ----------------------------
DROP TABLE IF EXISTS `result_table`;
CREATE TABLE `result_table`  (
  `result_id` int(11) NOT NULL AUTO_INCREMENT,
  `result_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `result_short_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`result_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of result_table
-- ----------------------------
INSERT INTO `result_table` VALUES (-1, 'Pending', 'PD');
INSERT INTO `result_table` VALUES (1, 'Compile Error', 'CE');
INSERT INTO `result_table` VALUES (2, 'Time Limit Exceed', 'TLE');
INSERT INTO `result_table` VALUES (3, 'Memory Limit Exceed', 'MLE');
INSERT INTO `result_table` VALUES (4, 'Output Limit Exceed', 'OLE');
INSERT INTO `result_table` VALUES (5, 'Runtime Error', 'RE');
INSERT INTO `result_table` VALUES (6, 'Wrong Answer', 'WA');
INSERT INTO `result_table` VALUES (7, 'Accepted', 'AC');
INSERT INTO `result_table` VALUES (8, 'Presentation Error', 'PE');
INSERT INTO `result_table` VALUES (9, 'System Error', 'SE');

-- ----------------------------
-- Table structure for scoreboard
-- ----------------------------
DROP TABLE IF EXISTS `scoreboard`;
CREATE TABLE `scoreboard`  (
  `contest_id` int(11) NOT NULL,
  `scoreboard_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`contest_id`) USING BTREE,
  CONSTRAINT `fk_scoreboard_contest_id` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`contest_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of scoreboard
-- ----------------------------

-- ----------------------------
-- Table structure for solutions
-- ----------------------------
DROP TABLE IF EXISTS `solutions`;
CREATE TABLE `solutions`  (
  `solution_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `problem_id` int(11) NOT NULL,
  `username` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `submit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `result` int(11) NOT NULL,
  `memory_used` int(11) NULL DEFAULT NULL,
  `time_used` int(11) NULL DEFAULT NULL,
  `code_length` int(11) NOT NULL,
  `lang` int(11) NOT NULL,
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `judgehost` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contest_id` int(11) NOT NULL DEFAULT 0,
  `score` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`solution_id`) USING BTREE,
  INDEX `fk_solution_problem`(`problem_id`) USING BTREE,
  INDEX `fk_solution_user`(`username`) USING BTREE,
  INDEX `fk_solution_result`(`result`) USING BTREE,
  INDEX `fk_solution_lang`(`lang`) USING BTREE,
  INDEX `contest_id`(`contest_id`) USING BTREE,
  CONSTRAINT `fk_solution_lang` FOREIGN KEY (`lang`) REFERENCES `lang_table` (`lang_code`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_solution_problem` FOREIGN KEY (`problem_id`) REFERENCES `problems` (`problem_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_solution_result` FOREIGN KEY (`result`) REFERENCES `result_table` (`result_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_solution_user` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of solutions
-- ----------------------------

-- ----------------------------
-- Table structure for testcases
-- ----------------------------
DROP TABLE IF EXISTS `testcases`;
CREATE TABLE `testcases`  (
  `testcase_id` int(11) NOT NULL AUTO_INCREMENT,
  `problem_id` int(11) NOT NULL,
  `input_filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `output_filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `input_md5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `output_md5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `input_size` bigint(20) NOT NULL,
  `output_size` bigint(20) NOT NULL,
  PRIMARY KEY (`testcase_id`) USING BTREE,
  INDEX `idx_problem_id`(`problem_id`) USING BTREE,
  CONSTRAINT `testcases_ibfk_1` FOREIGN KEY (`problem_id`) REFERENCES `problems` (`problem_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of testcases
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `access_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `reg_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `school` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` int(11) NOT NULL COMMENT '0 nomal user\r\n-1 forbidden user\r\n999 admin',
  `signature` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('admin', 'zuccoj@zucc.edu.cn', '10.69.255.122', '2021-02-21 10:07:02', '1998-10-11 19:30:00', 'admin', 'ZUCC Online Judge β', '9v3/5IyQjesPTDvTbAMucg==', 999, '');

-- ----------------------------
-- View structure for contest_submitter
-- ----------------------------
DROP VIEW IF EXISTS `contest_submitter`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `contest_submitter` AS select count(distinct `solutions`.`username`) AS `count`,`solutions`.`contest_id` AS `contest_id` from `solutions` group by `solutions`.`contest_id`;

SET FOREIGN_KEY_CHECKS = 1;
