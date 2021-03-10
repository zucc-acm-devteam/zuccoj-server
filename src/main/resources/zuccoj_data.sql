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
-- Records of contest_type_table
-- ----------------------------
INSERT INTO `contest_type_table` VALUES (1, 'ACM/ICPC');
INSERT INTO `contest_type_table` VALUES (2, 'OI');
INSERT INTO `contest_type_table` VALUES (3, 'IOI');
INSERT INTO `contest_type_table` VALUES (4, 'Codeforces');

-- ----------------------------
-- Records of judgehost
-- ----------------------------
INSERT INTO `judgehost` VALUES ('Archer', '0Y0umYU2K9UriG+zypYDVg==', '2021-02-21 07:34:38');
INSERT INTO `judgehost` VALUES ('Koluta', '1paEwIYu65OC94DYcqmAOw==', '2021-02-21 08:50:28');

-- ----------------------------
-- Records of lang_table
-- ----------------------------
INSERT INTO `lang_table` VALUES (1, 'C');
INSERT INTO `lang_table` VALUES (2, 'C++');
INSERT INTO `lang_table` VALUES (3, 'Java');

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
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('admin', 'zuccoj@zucc.edu.cn', NULL, NULL, '1998-10-11 19:30:00', 'admin', 'ZUCC Online Judge β', '9v3/5IyQjesPTDvTbAMucg==', 999, '');


SET FOREIGN_KEY_CHECKS = 1;
