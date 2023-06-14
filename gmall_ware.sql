/*
 Navicat Premium Data Transfer

 Source Server         : mysql8-192.168.253.137
 Source Server Type    : MySQL
 Source Server Version : 80027 (8.0.27)
 Source Host           : 192.168.253.137:3308
 Source Schema         : gmall_ware

 Target Server Type    : MySQL
 Target Server Version : 80027 (8.0.27)
 File Encoding         : 65001

 Date: 11/06/2023 21:52:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ware_info
-- ----------------------------
DROP TABLE IF EXISTS `ware_info`;
CREATE TABLE `ware_info`  (
  `id` bigint NOT NULL,
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `areacode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ware_info
-- ----------------------------
INSERT INTO `ware_info` VALUES (1, '北京大兴仓库', '北京大兴', '110000', '2021-08-13 15:18:42', '2021-08-13 15:18:42', 0);
INSERT INTO `ware_info` VALUES (2, '北京昌平区仓库', '北京昌平', '110100', '2021-08-13 15:18:42', '2021-08-13 15:18:42', 0);

-- ----------------------------
-- Table structure for ware_order_task
-- ----------------------------
DROP TABLE IF EXISTS `ware_order_task`;
CREATE TABLE `ware_order_task`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` bigint NULL DEFAULT NULL COMMENT '订单编号',
  `consignee` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人',
  `consignee_tel` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人电话',
  `delivery_address` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '送货地址',
  `order_comment` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `payment_way` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '付款方式 1:在线付款 2:货到付款',
  `task_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作单状态',
  `order_body` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单描述',
  `tracking_no` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流单号',
  `ware_id` bigint NULL DEFAULT NULL COMMENT '仓库编号',
  `task_comment` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作单备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '库存工作单表 库存工作单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ware_order_task
-- ----------------------------
INSERT INTO `ware_order_task` VALUES (1, 1, 'qigntiqny', '15099999999', '订个蛋糕梵蒂冈电饭锅地方', '', '2', 'SPLIT', '红米3 小米77一代 小米MIX2S  ', NULL, NULL, NULL, '2021-08-13 15:18:42', '2021-08-13 15:18:42', 0);
INSERT INTO `ware_order_task` VALUES (2, 2, 'qigntiqny', '15099999999', '订个蛋糕梵蒂冈电饭锅地方', '', '2', 'DEDUCTED', '小米77一代 小米MIX2S  ', NULL, 1, NULL, '2021-08-13 15:18:42', '2021-08-13 15:18:42', 0);
INSERT INTO `ware_order_task` VALUES (3, 3, 'qigntiqny', '15099999999', '订个蛋糕梵蒂冈电饭锅地方', '', '2', 'DEDUCTED', '红米3 ', NULL, 2, NULL, '2021-08-13 15:18:42', '2021-08-13 15:18:42', 0);
INSERT INTO `ware_order_task` VALUES (4, 4, 'qigntiqny', '15099999999', '订个蛋糕梵蒂冈电饭锅地方', '', '2', 'DEDUCTED', '小米77一代 ', NULL, NULL, NULL, '2021-08-13 15:18:42', '2021-08-13 15:18:42', 0);
INSERT INTO `ware_order_task` VALUES (5, 5, 'qigntiqny', '15099999999', '订个蛋糕梵蒂冈电饭锅地方', '', '2', 'DEDUCTED', '小米77一代 ', NULL, 1, NULL, '2021-08-13 15:18:42', '2021-08-13 15:18:42', 0);
INSERT INTO `ware_order_task` VALUES (6, 22, 'Administrator', '15099999999', '北京市昌平区宏福科技园', '', '2', 'DEDUCTED', '结婚买房买车买手机.', NULL, NULL, NULL, '2021-09-29 14:43:21', '2021-09-29 06:43:21', 0);
INSERT INTO `ware_order_task` VALUES (7, 23, 'Administrator', '15099999999', '北京市昌平区宏福科技园', '', '2', 'SPLIT', '结婚买房买车买手机.', NULL, NULL, NULL, '2021-09-29 15:56:51', '2021-09-29 07:56:52', 0);
INSERT INTO `ware_order_task` VALUES (8, 24, 'Administrator', '15099999999', '北京市昌平区宏福科技园', '', '2', 'DEDUCTED', '结婚买房买车买手机.', NULL, 1, NULL, '2021-09-29 15:56:52', '2021-09-29 07:56:52', 0);
INSERT INTO `ware_order_task` VALUES (9, 25, 'Administrator', '15099999999', '北京市昌平区宏福科技园', '', '2', 'DEDUCTED', '结婚买房买车买手机.', NULL, 2, NULL, '2021-09-29 15:56:52', '2021-09-29 07:56:52', 0);

-- ----------------------------
-- Table structure for ware_order_task_detail
-- ----------------------------
DROP TABLE IF EXISTS `ware_order_task_detail`;
CREATE TABLE `ware_order_task_detail`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `sku_id` bigint NULL DEFAULT NULL COMMENT 'sku_id',
  `sku_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'sku名称',
  `sku_num` int NULL DEFAULT NULL COMMENT '购买个数',
  `task_id` bigint NULL DEFAULT NULL COMMENT '工作单编号',
  `refund_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '库存工作单明细表 库存工作单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ware_order_task_detail
-- ----------------------------
INSERT INTO `ware_order_task_detail` VALUES (1, 42, '红米3', 1, 1, NULL, '2021-08-13 15:18:42', '2021-08-13 15:18:42', 0);
INSERT INTO `ware_order_task_detail` VALUES (2, 27, '小米77一代', 1, 1, NULL, '2021-08-13 15:18:42', '2021-08-13 15:18:42', 0);
INSERT INTO `ware_order_task_detail` VALUES (3, 25, '小米MIX2S ', 1, 1, NULL, '2021-08-13 15:18:42', '2021-08-13 15:18:42', 0);
INSERT INTO `ware_order_task_detail` VALUES (4, 27, '小米77一代', 1, 2, NULL, '2021-08-13 15:18:42', '2021-08-13 15:18:42', 0);
INSERT INTO `ware_order_task_detail` VALUES (5, 25, '小米MIX2S ', 1, 2, NULL, '2021-08-13 15:18:42', '2021-08-13 15:18:42', 0);
INSERT INTO `ware_order_task_detail` VALUES (6, 42, '红米3', 1, 3, NULL, '2021-08-13 15:18:42', '2021-08-13 15:18:42', 0);
INSERT INTO `ware_order_task_detail` VALUES (7, 27, '小米77一代', 1, 4, NULL, '2021-08-13 15:18:42', '2021-08-13 15:18:42', 0);
INSERT INTO `ware_order_task_detail` VALUES (8, 27, '小米77一代', 1, 5, NULL, '2021-08-13 15:18:42', '2021-08-13 15:18:42', 0);
INSERT INTO `ware_order_task_detail` VALUES (9, 19, '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 1, 6, NULL, '2021-09-29 06:43:21', '2021-09-29 06:43:21', 0);
INSERT INTO `ware_order_task_detail` VALUES (10, 20, '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 2, 6, NULL, '2021-09-29 06:43:21', '2021-09-29 06:43:21', 0);
INSERT INTO `ware_order_task_detail` VALUES (11, 19, '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 1, 7, NULL, '2021-09-29 07:56:51', '2021-09-29 07:56:51', 0);
INSERT INTO `ware_order_task_detail` VALUES (12, 20, '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 2, 7, NULL, '2021-09-29 07:56:51', '2021-09-29 07:56:51', 0);
INSERT INTO `ware_order_task_detail` VALUES (13, 19, '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 1, 8, NULL, '2021-09-29 07:56:52', '2021-09-29 07:56:52', 0);
INSERT INTO `ware_order_task_detail` VALUES (14, 20, '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 2, 9, NULL, '2021-09-29 07:56:52', '2021-09-29 07:56:52', 0);

-- ----------------------------
-- Table structure for ware_sku
-- ----------------------------
DROP TABLE IF EXISTS `ware_sku`;
CREATE TABLE `ware_sku`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `sku_id` bigint NULL DEFAULT NULL COMMENT 'skuid',
  `warehouse_id` bigint NULL DEFAULT NULL COMMENT '仓库id',
  `stock` int NULL DEFAULT NULL COMMENT '库存数',
  `stock_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存货名称',
  `stock_locked` int NULL DEFAULT NULL COMMENT '锁定库存数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'sku与仓库关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ware_sku
-- ----------------------------
INSERT INTO `ware_sku` VALUES (20, 22, 1, 100, ' 小米CC9 手机  仙女渐变色  8G+128G', 1, '2023-03-20 10:36:11', '2023-03-20 12:35:19', 0);
INSERT INTO `ware_sku` VALUES (21, 23, 2, 100, '小米CC9 手机  蓝色宝石  8G+256G', 1, '2023-03-20 10:36:14', '2023-03-20 12:35:19', 0);
INSERT INTO `ware_sku` VALUES (22, 26, 1, 100, '小米CC9 手机 蓝色宝石 8G+256G', 1, '2023-03-20 10:36:15', '2023-03-20 12:35:19', 0);
INSERT INTO `ware_sku` VALUES (23, 27, 1, 100, '小米CC9 手机  仙女渐变色  8G+256G', 1, '2023-03-20 10:36:16', '2023-03-20 12:35:19', 0);
INSERT INTO `ware_sku` VALUES (24, 28, 1, 100, '小米CC9 手机  蓝色宝石  8G+128G  6.17英寸', 1, '2023-03-20 10:36:18', '2023-03-20 12:35:19', 0);
INSERT INTO `ware_sku` VALUES (25, 29, 2, 100, '华为荣耀 V30 PRO  冰岛幻境  6G+128G  5.4英寸', 1, '2023-03-20 10:36:20', '2023-03-20 12:35:19', 0);
INSERT INTO `ware_sku` VALUES (26, 30, 1, 100, '华为荣耀 V30 PRO  冰岛幻境  6G+128G  6.8英寸', 1, '2023-03-20 12:32:49', '2023-03-20 12:35:19', 0);
INSERT INTO `ware_sku` VALUES (27, 31, 1, 100, '华为荣耀 V30 PRO  冰岛幻境  8G+256G 6.8英寸', 1, '2023-03-20 12:32:50', '2023-03-20 12:35:19', 0);
INSERT INTO `ware_sku` VALUES (28, 32, 1, 100, '华为荣耀 V30 PRO  冰岛幻境  8G+256G 5.4英寸', 1, '2023-03-20 12:32:51', '2023-03-20 12:35:19', 0);
INSERT INTO `ware_sku` VALUES (29, 33, 2, 100, '华为荣耀 V30 PRO  幻夜星河 8G+256G 5.4英寸', 1, '2023-03-20 12:33:07', '2023-03-20 12:35:19', 0);
INSERT INTO `ware_sku` VALUES (30, 34, 1, 100, '华为荣耀 V30 PRO  幻夜星河 8G+256G 6.8英寸', 1, '2023-03-20 12:33:08', '2023-03-20 12:35:19', 0);
INSERT INTO `ware_sku` VALUES (31, 35, 1, 100, '华为荣耀 V30 PRO  幻夜星河 6G+128G 6.8英寸', 1, '2023-03-20 12:33:09', '2023-03-20 12:35:20', 0);
INSERT INTO `ware_sku` VALUES (32, 36, 2, 100, '华为荣耀 V30 PRO  幻夜星河  6G+128G 5.4英寸', 1, '2023-03-20 12:33:11', '2023-03-20 12:35:20', 0);

SET FOREIGN_KEY_CHECKS = 1;
