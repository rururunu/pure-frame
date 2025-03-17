/*
 Navicat Premium Dump SQL

 Source Server         : my
 Source Server Type    : MySQL
 Source Server Version : 80039 (8.0.39)
 Source Host           : localhost:3306
 Source Schema         : pure_frame_database

 Target Server Type    : MySQL
 Target Server Version : 80039 (8.0.39)
 File Encoding         : 65001

 Date: 17/03/2025 10:59:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for power
-- ----------------------------
DROP TABLE IF EXISTS `power`;
CREATE TABLE `power`  (
  `power_id` int NOT NULL AUTO_INCREMENT,
  `power_sup_id` int NULL DEFAULT 0 COMMENT '父级权限id',
  `power_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限编码',
  `power_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限昵称',
  `is_delete` int NOT NULL DEFAULT 0 COMMENT '是否删除 0 未 1 删除',
  PRIMARY KEY (`power_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of power
-- ----------------------------
INSERT INTO `power` VALUES (1, 17, 'user', '用户', 0);
INSERT INTO `power` VALUES (2, 1, 'user:add', '用户添加', 0);
INSERT INTO `power` VALUES (3, 1, 'user:set', '用户修改', 0);
INSERT INTO `power` VALUES (4, 1, 'user:del', '用户删除', 0);
INSERT INTO `power` VALUES (5, 17, 'role', '角色', 0);
INSERT INTO `power` VALUES (6, 5, 'role:add', '角色添加', 0);
INSERT INTO `power` VALUES (7, 5, 'role:set', '角色修改', 0);
INSERT INTO `power` VALUES (8, 5, 'role:del', '角色删除', 0);
INSERT INTO `power` VALUES (9, 17, 'power', '权限', 0);
INSERT INTO `power` VALUES (10, 9, 'power:add', '权限添加', 0);
INSERT INTO `power` VALUES (11, 9, 'power:set', '权限修改', 0);
INSERT INTO `power` VALUES (12, 9, 'power:del', '权限删除', 0);
INSERT INTO `power` VALUES (17, 0, 'system', '系统管理', 0);
INSERT INTO `power` VALUES (18, 9, 'power:look', '权限查看1', 1);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `role_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码',
  `note` varchar(555) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
  `is_delete` int NOT NULL DEFAULT 0 COMMENT '是否删除 0 未 1 删除',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'admin', 'admin', '管理员', 0);

-- ----------------------------
-- Table structure for role_power
-- ----------------------------
DROP TABLE IF EXISTS `role_power`;
CREATE TABLE `role_power`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `power_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_power
-- ----------------------------
INSERT INTO `role_power` VALUES (13, '1', '17');
INSERT INTO `role_power` VALUES (14, '1', '1');
INSERT INTO `role_power` VALUES (15, '1', '2');
INSERT INTO `role_power` VALUES (16, '1', '3');
INSERT INTO `role_power` VALUES (17, '1', '4');
INSERT INTO `role_power` VALUES (18, '1', '5');
INSERT INTO `role_power` VALUES (19, '1', '6');
INSERT INTO `role_power` VALUES (20, '1', '7');
INSERT INTO `role_power` VALUES (21, '1', '8');
INSERT INTO `role_power` VALUES (22, '1', '9');
INSERT INTO `role_power` VALUES (23, '1', '10');
INSERT INTO `role_power` VALUES (24, '1', '11');
INSERT INTO `role_power` VALUES (25, '1', '12');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户编号',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `user_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `user_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户密码',
  `user_new_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  `user_set_date` datetime NULL DEFAULT NULL COMMENT '用户信息修改时间',
  `user_new_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户的用户名称',
  `user_is_delete` int NOT NULL DEFAULT 0 COMMENT '用户是否被删除 1:删除 0:正常',
  `user_head` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `user_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户电话号码',
  `user_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `idx_user_account`(`user_account` ASC) USING BTREE,
  INDEX `idx_user_is_delete`(`user_is_delete` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '管理员', 'admin', '$2a$10$7G/iTgCM6no52PCdy1Wa.ONF5XUINhCA8rbSodVkBsC5t0p9guKm6', '2025-02-25 15:13:54', NULL, '管理员', 0, NULL, NULL, NULL);
INSERT INTO `user` VALUES ('255546755560448000', 'user1', 'user1', '$2a$10$iIEiWcBFS1p4TJSFraK4TOhhgSYCc2OUf37ryMQ/iCHS8em1ek8ju', '2025-03-07 17:12:34', '2025-03-14 14:26:31', '管理员', 0, NULL, '17520146146', 'user@123.com');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ifx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `ifx_role_id`(`role_id` ASC) USING BTREE,
  CONSTRAINT `ifx_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `ifx_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, '1', '1');

SET FOREIGN_KEY_CHECKS = 1;
