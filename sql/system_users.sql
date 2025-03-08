/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3306
 Source Schema         : login

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 08/03/2025 19:26:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for system_users
-- ----------------------------
DROP TABLE IF EXISTS `system_users`;
CREATE TABLE `system_users`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户账号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '密码',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户昵称',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `post_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '岗位编号数组',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` tinyint NULL DEFAULT 0 COMMENT '用户性别',
  `avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '头像地址',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '帐号状态（0正常 1停用）',
  `login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 144 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_users
-- ----------------------------

INSERT INTO `system_users` VALUES (3, 'test', '123456', '测试账号', '测试专用账号', 104, '[2]', 'test@company.com', '13800138002', 2, 'https://example.com/avatar/test.jpg', 0, '127.0.0.1', '2024-02-25 11:00:00', 'admin', '2024-02-25 11:00:00', 'test', '2025-02-25 20:14:01', b'1', 1);
INSERT INTO `system_users` VALUES (5, 'qc', '$2a$10$1PKp1wk0dVBokYxkD44pLuFZQ/iBTpBPPmSlBi0D4POVdH8jq8fvS', '质检专员', '质量控制人员', 105, '[3]', 'qc@company.com', '13800138003', 1, 'https://example.com/avatar/qc.jpg', 0, '127.0.0.1', '2024-02-25 12:00:00', 'admin', '2024-02-25 12:00:00', 'qc', '2024-02-25 12:00:00', b'0', 1);
INSERT INTO `system_users` VALUES (6, 'qcqc', '$2a$10$hX9XKyROX0kWECPLdtuqu.9zn1yJyDO59bEQocpLedXp725C8xbOy', '质检主管', '质量控制主管', 105, '[3,4]', 'qcqc@company.com', '13800138004', 1, 'https://example.com/avatar/qcqc.jpg', 0, '127.0.0.1', '2024-02-25 13:00:00', 'admin', '2024-02-25 13:00:00', 'qcqc', '2024-02-25 13:00:00', b'0', 1);
INSERT INTO `system_users` VALUES (10, '1', '1', '用户一', '测试用户1', 106, '[4]', 'user1@company.com', '13800138005', 2, 'https://example.com/avatar/user1.jpg', 0, '127.0.0.1', '2024-02-25 14:00:00', 'admin', '2024-02-25 14:00:00', '1', '2025-02-25 17:21:31', b'1', 1);
INSERT INTO `system_users` VALUES (140, '111111111', '11111111', '1111', '111', 103, '[1]', '111@qq.com', '17783519713', 1, NULL, 0, '', NULL, NULL, '2025-02-25 17:51:49', NULL, '2025-02-25 20:14:05', b'1', 1);
INSERT INTO `system_users` VALUES (141, 'qiuci', '123456', 'qc', '1', 103, '1', 'qc@qq.com', '17783519713', 1, NULL, 0, '', NULL, NULL, '2025-02-25 20:08:18', NULL, '2025-02-25 20:08:18', b'0', 1);
INSERT INTO `system_users` VALUES (142, 'qqqq', '$2a$10$grzczdWNw61IjLKJu2dYl.K6tDf2FpF05OR5fOiHu4G6on4vHp3Ya', '123', '', 103, '1', '123@qq.com', '17783519713', 1, NULL, 0, '', NULL, NULL, '2025-02-25 20:13:09', NULL, '2025-02-25 20:13:09', b'0', 1);
INSERT INTO `system_users` VALUES (143, 'admin', '$2a$10$3jQjXtBuLywOBRsoUeYg3.jp.1ZIXzhRATeQ4vuCHMhIDguy4I4SC', '管理员', '', 103, '1', 'admin@qq.com', '17788888888', 1, '/uploads/avatars/af0015b6-93c8-4eb2-a679-6e09aeb94aeb.png', 0, '', NULL, NULL, '2025-02-25 20:14:51', NULL, '2025-02-25 21:50:09', b'0', 1);

SET FOREIGN_KEY_CHECKS = 1;
