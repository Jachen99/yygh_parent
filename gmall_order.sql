/*
 Navicat Premium Data Transfer

 Source Server         : mysql8-192.168.253.137
 Source Server Type    : MySQL
 Source Server Version : 80027 (8.0.27)
 Source Host           : 192.168.253.137:3308
 Source Schema         : gmall_order

 Target Server Type    : MySQL
 Target Server Version : 80027 (8.0.27)
 File Encoding         : 65001

 Date: 11/06/2023 21:52:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart_info
-- ----------------------------
DROP TABLE IF EXISTS `cart_info`;
CREATE TABLE `cart_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `sku_id` bigint NULL DEFAULT NULL COMMENT 'skuid',
  `cart_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '放入购物车时价格',
  `sku_num` int NULL DEFAULT NULL COMMENT '数量',
  `img_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片文件',
  `sku_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'sku名称 (冗余)',
  `is_checked` int NULL DEFAULT NULL,
  `is_ordered` bigint NULL DEFAULT NULL COMMENT '是否已经下单',
  `order_time` datetime NULL DEFAULT NULL COMMENT '下单时间',
  `source_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源类型',
  `source_id` bigint NULL DEFAULT NULL COMMENT '来源编号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '购物车表 用户登录系统时更新冗余' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart_info
-- ----------------------------

-- ----------------------------
-- Table structure for order_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` bigint NULL DEFAULT NULL COMMENT '订单编号',
  `sku_id` bigint NULL DEFAULT NULL COMMENT 'sku_id',
  `sku_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'sku名称（冗余)',
  `img_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片名称（冗余)',
  `order_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '购买价格(下单时sku价格）',
  `sku_num` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '购买个数',
  `source_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源类型',
  `source_id` bigint NULL DEFAULT NULL COMMENT '来源编号',
  `split_total_amount` decimal(10, 2) NULL DEFAULT NULL,
  `split_activity_amount` decimal(10, 2) NULL DEFAULT NULL,
  `split_coupon_amount` decimal(10, 2) NULL DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 77 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_detail
-- ----------------------------
INSERT INTO `order_detail` VALUES (1, 1, 4, 'Redmi 10X 4G Helio G85游戏芯 4800万超清四摄 5020mAh大电量 小孔全面屏 128GB大存储 4GB+128GB 冰雾白 游戏智能手机 小米 红米', 'http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rgJqAHPnoAAF9hoDNfsc505.jpg', 999.00, '1', '2401', 4, 999.00, 0.00, 0.00, '2021-08-13 15:18:40', '2021-08-13 15:18:40', 0);
INSERT INTO `order_detail` VALUES (2, 1, 5, 'Redmi 10X 4G Helio G85游戏芯 4800万超清四摄 5020mAh大电量 小孔全面屏 128GB大存储 4GB+128GB 明月灰 游戏智能手机 小米 红米', 'http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rgJqAHPnoAAF9hoDNfsc505.jpg', 999.00, '4', '2401', 5, 3874.00, 122.00, 0.00, '2021-08-13 15:18:40', '2021-08-13 15:18:40', 0);
INSERT INTO `order_detail` VALUES (3, 2, 2, '小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 12GB+256GB 陶瓷黑 游戏手机', 'http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rfvmAcbl2AAFopp2WGBQ404.jpg', 6999.00, '1', '2401', 2, 6999.00, 0.00, 0.00, '2021-08-13 15:18:40', '2021-08-13 15:18:40', 0);
INSERT INTO `order_detail` VALUES (4, 3, 5, 'Redmi 10X 4G Helio G85游戏芯 4800万超清四摄 5020mAh大电量 小孔全面屏 128GB大存储 4GB+128GB 明月灰 游戏智能手机 小米 红米', 'http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rgJqAHPnoAAF9hoDNfsc505.jpg', 999.00, '1', '2401', 5, 999.00, 0.00, 0.00, '2021-08-13 15:18:40', '2021-08-13 15:18:40', 0);
INSERT INTO `order_detail` VALUES (64, 36, 34, '华为荣耀 V30 PRO  幻夜星河 8G+256G 6.8英寸', 'http://jachen.com:9000/gmall/1678444869202de69d8ab-533f-487c-b402-986d8550db5f', 7999.00, '2', NULL, NULL, NULL, NULL, NULL, '2023-03-20 11:44:47', '2023-03-20 11:44:47', 0);
INSERT INTO `order_detail` VALUES (65, 36, 27, '小米CC9 手机  仙女渐变色  8G+256G', 'http://jachen.com:9000/gmall/16781083311953b008216-80d0-40c7-90f6-5c71e2219e6f', 8888.00, '1', NULL, NULL, NULL, NULL, NULL, '2023-03-20 11:44:47', '2023-03-20 11:44:47', 0);
INSERT INTO `order_detail` VALUES (66, 36, 22, ' 小米CC9 手机  仙女渐变色  8G+128G', 'http://jachen.com:9000/gmall/16781083311981b81c84a-0c5e-49c0-ae2f-ef630f14ed03', 5999.00, '1', NULL, NULL, NULL, NULL, NULL, '2023-03-20 11:44:47', '2023-03-20 11:44:47', 0);
INSERT INTO `order_detail` VALUES (67, 37, 22, ' 小米CC9 手机  仙女渐变色  8G+128G', 'http://jachen.com:9000/gmall/16781083311981b81c84a-0c5e-49c0-ae2f-ef630f14ed03', 4999.00, '1', NULL, NULL, NULL, NULL, NULL, '2023-03-20 12:52:32', '2023-03-20 12:52:32', 0);
INSERT INTO `order_detail` VALUES (68, 38, 28, '小米CC9 手机  蓝色宝石  8G+128G  6.17英寸', 'http://jachen.com:9000/gmall/16781083251629b5252d4-5fa6-4bb3-8019-20ad7abd38c9', 3333.00, '1', NULL, NULL, NULL, NULL, NULL, '2023-03-20 13:18:53', '2023-03-20 13:18:53', 0);
INSERT INTO `order_detail` VALUES (69, 38, 22, ' 小米CC9 手机  仙女渐变色  8G+128G', 'http://jachen.com:9000/gmall/16781083311981b81c84a-0c5e-49c0-ae2f-ef630f14ed03', 4999.00, '1', NULL, NULL, NULL, NULL, NULL, '2023-03-20 13:18:53', '2023-03-20 13:18:53', 0);
INSERT INTO `order_detail` VALUES (70, 39, 28, '小米CC9 手机  蓝色宝石  8G+128G  6.17英寸', 'http://jachen.com:9000/gmall/16781083251629b5252d4-5fa6-4bb3-8019-20ad7abd38c9', 3333.00, '2', NULL, NULL, NULL, NULL, NULL, '2023-03-20 13:20:49', '2023-03-20 13:20:49', 0);
INSERT INTO `order_detail` VALUES (71, 39, 22, ' 小米CC9 手机  仙女渐变色  8G+128G', 'http://jachen.com:9000/gmall/16781083311981b81c84a-0c5e-49c0-ae2f-ef630f14ed03', 4999.00, '1', NULL, NULL, NULL, NULL, NULL, '2023-03-20 13:20:49', '2023-03-20 13:20:49', 0);
INSERT INTO `order_detail` VALUES (72, 40, 22, ' 小米CC9 手机  仙女渐变色  8G+128G', 'http://jachen.com:9000/gmall/16781083311981b81c84a-0c5e-49c0-ae2f-ef630f14ed03', 4999.00, '1', NULL, NULL, NULL, NULL, NULL, '2023-03-22 12:55:27', '2023-03-22 12:55:27', 0);
INSERT INTO `order_detail` VALUES (73, 41, 22, ' 小米CC9 手机  仙女渐变色  8G+128G', 'http://jachen.com:9000/gmall/16781083311981b81c84a-0c5e-49c0-ae2f-ef630f14ed03', 4999.00, '1', NULL, NULL, NULL, NULL, NULL, '2023-03-22 13:09:58', '2023-03-22 13:09:58', 0);
INSERT INTO `order_detail` VALUES (74, 41, 34, '华为荣耀 V30 PRO  幻夜星河 8G+256G 6.8英寸', 'http://jachen.com:9000/gmall/1678444869202de69d8ab-533f-487c-b402-986d8550db5f', 7999.00, '1', NULL, NULL, NULL, NULL, NULL, '2023-03-22 13:09:58', '2023-03-22 13:09:58', 0);
INSERT INTO `order_detail` VALUES (75, 42, 28, '小米CC9 手机  蓝色宝石  8G+128G  6.17英寸', 'http://jachen.com:9000/gmall/16781083251629b5252d4-5fa6-4bb3-8019-20ad7abd38c9', 3333.00, '1', NULL, NULL, NULL, NULL, NULL, '2023-03-22 13:13:16', '2023-03-22 13:13:16', 0);
INSERT INTO `order_detail` VALUES (76, 43, 28, '小米CC9 手机  蓝色宝石  8G+128G  6.17英寸', 'http://jachen.com:9000/gmall/16781083251629b5252d4-5fa6-4bb3-8019-20ad7abd38c9', 3.00, '1', NULL, NULL, NULL, NULL, NULL, '2023-03-25 09:11:12', '2023-03-25 09:11:12', 0);

-- ----------------------------
-- Table structure for order_detail_activity
-- ----------------------------
DROP TABLE IF EXISTS `order_detail_activity`;
CREATE TABLE `order_detail_activity`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` bigint NULL DEFAULT NULL COMMENT '订单id',
  `order_detail_id` bigint NULL DEFAULT NULL COMMENT '订单明细id',
  `activity_id` bigint NULL DEFAULT NULL COMMENT '活动ID',
  `activity_rule` bigint NULL DEFAULT NULL COMMENT '活动规则',
  `sku_id` bigint NULL DEFAULT NULL COMMENT 'skuID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单明细购物券表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_detail_activity
-- ----------------------------
INSERT INTO `order_detail_activity` VALUES (1, 1, 2, 8, 68, 5, '2021-08-13 15:18:40', '2021-08-13 15:18:40', 0);

-- ----------------------------
-- Table structure for order_detail_coupon
-- ----------------------------
DROP TABLE IF EXISTS `order_detail_coupon`;
CREATE TABLE `order_detail_coupon`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` bigint NULL DEFAULT NULL COMMENT '订单id',
  `order_detail_id` bigint NULL DEFAULT NULL COMMENT '订单明细id',
  `coupon_id` bigint NULL DEFAULT NULL COMMENT '购物券ID',
  `sku_id` bigint NULL DEFAULT NULL COMMENT 'skuID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单明细购物券表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_detail_coupon
-- ----------------------------

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `consignee` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人',
  `consignee_tel` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人电话',
  `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '总金额',
  `order_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单状态',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `payment_way` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '付款方式',
  `delivery_address` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '送货地址',
  `order_comment` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `out_trade_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单交易编号（第三方支付用)',
  `trade_body` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单描述(第三方支付用)',
  `operate_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '失效时间',
  `process_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '进度状态',
  `tracking_no` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流单编号',
  `parent_order_id` bigint NULL DEFAULT NULL COMMENT '父订单编号',
  `img_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片路径',
  `province_id` int NULL DEFAULT NULL COMMENT '地区',
  `activity_reduce_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '促销金额',
  `coupon_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券',
  `original_total_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '原价金额',
  `feight_fee` decimal(16, 2) NULL DEFAULT NULL COMMENT '运费',
  `feight_fee_reduce` decimal(10, 2) NULL DEFAULT NULL COMMENT '减免运费',
  `refundable_time` datetime NULL DEFAULT NULL COMMENT '可退款日期（签收后30天）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表 订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES (35, 'admin', '2', 14887.00, 'UNPAID', 1, 'ONLINE', '北京市昌平区2', '', 'gmall6d4cef98-188f-4de5-9e2b-c6d80ddbfb24', '小米CC9 手机  仙女渐变色  8G+256G 小米CC9 手机  仙女渐变色  8G+128G', NULL, '2023-03-21 19:17:59', 'UNPAID', NULL, NULL, NULL, NULL, NULL, 0.00, 14887.00, NULL, NULL, NULL, '2023-03-20 19:17:59', '2023-03-20 11:17:59', 0);
INSERT INTO `order_info` VALUES (36, 'admin', '2', 30885.00, 'UNPAID', 1, 'ONLINE', '北京市昌平区2', '', 'gmall898680c4-e1a3-4bd4-a6b7-00e34a65ffcb', '华为荣耀 V30 PRO  幻夜星河 8G+256G 6.8英寸小米CC9 手机  仙女渐变色  8', NULL, '2023-03-21 19:44:47', 'UNPAID', NULL, NULL, NULL, NULL, NULL, 0.00, 30885.00, NULL, NULL, NULL, '2023-03-20 19:44:47', '2023-03-20 11:44:47', 0);
INSERT INTO `order_info` VALUES (37, 'admin', '2', 4999.00, 'UNPAID', 1, 'ONLINE', '北京市昌平区2', '', 'gmall73644b20-c210-40ea-ad9f-0591a7f491a9', ' 小米CC9 手机  仙女渐变色  8G+128G', NULL, '2023-03-21 20:52:32', 'UNPAID', NULL, NULL, NULL, NULL, NULL, 0.00, 4999.00, NULL, NULL, NULL, '2023-03-20 20:52:32', '2023-03-20 12:52:32', 0);
INSERT INTO `order_info` VALUES (38, 'admin', '2', 8332.00, 'UNPAID', 1, 'ONLINE', '北京市昌平区2', '', 'gmallfd7188f9-d5df-4462-be1b-49e113363e4a', '小米CC9 手机  蓝色宝石  8G+128G  6.17英寸 小米CC9 手机  仙女渐变色  8', NULL, '2023-03-21 21:18:53', 'UNPAID', NULL, NULL, NULL, NULL, NULL, 0.00, 8332.00, NULL, NULL, NULL, '2023-03-20 21:18:53', '2023-03-20 13:18:53', 0);
INSERT INTO `order_info` VALUES (39, 'admin', '2', 11665.00, 'UNPAID', 1, 'ONLINE', '北京市昌平区2', '', 'gmalldb3f9d16-af09-4572-8e2d-17f1061fc762', '小米CC9 手机  蓝色宝石  8G+128G  6.17英寸 小米CC9 手机  仙女渐变色  8', NULL, '2023-03-21 21:20:49', 'UNPAID', NULL, NULL, NULL, NULL, NULL, 0.00, 11665.00, NULL, NULL, NULL, '2023-03-20 21:20:49', '2023-03-20 13:20:49', 0);
INSERT INTO `order_info` VALUES (40, 'admin', '2', 4999.00, 'UNPAID', 1, 'ONLINE', '北京市昌平区2', '', 'gmall6efd155d-19c0-4f9b-b426-72d2df22c1fc', ' 小米CC9 手机  仙女渐变色  8G+128G', NULL, '2023-03-23 20:55:28', 'UNPAID', NULL, NULL, NULL, NULL, NULL, 0.00, 4999.00, NULL, NULL, NULL, '2023-03-22 20:55:28', '2023-03-22 12:55:27', 0);
INSERT INTO `order_info` VALUES (41, 'admin', '2', 12998.00, 'UNPAID', 1, 'ONLINE', '北京市昌平区2', '', 'gmall911460cc-4acf-44c2-a37c-474e6f063987', ' 小米CC9 手机  仙女渐变色  8G+128G华为荣耀 V30 PRO  幻夜星河 8G+256', NULL, '2023-03-23 21:09:59', 'UNPAID', NULL, NULL, NULL, NULL, NULL, 0.00, 12998.00, NULL, NULL, NULL, '2023-03-22 21:09:59', '2023-03-22 13:09:58', 0);
INSERT INTO `order_info` VALUES (42, 'admin', '2', 3333.00, 'UNPAID', 1, 'ONLINE', '北京市昌平区2', '', 'gmall578947bd-bf2f-4b90-857e-a18a20322b8c', '小米CC9 手机  蓝色宝石  8G+128G  6.17英寸', NULL, '2023-03-23 21:13:17', 'UNPAID', NULL, NULL, NULL, NULL, NULL, 0.00, 3333.00, NULL, NULL, NULL, '2023-03-22 21:13:17', '2023-03-22 13:13:16', 0);
INSERT INTO `order_info` VALUES (43, 'admin', '2', 3.00, 'PAID', 1, 'ONLINE', '北京市昌平区2', '', 'gmalla6e7b307-3160-4b88-b268-564c002422c2', '小米CC9 手机  蓝色宝石  8G+128G  6.17英寸', NULL, '2023-03-26 17:11:13', 'NOTIFIED_WARE', NULL, NULL, NULL, NULL, NULL, 0.00, 3.00, NULL, NULL, NULL, '2023-03-25 17:11:13', '2023-03-25 09:12:13', 0);

-- ----------------------------
-- Table structure for order_refund_info
-- ----------------------------
DROP TABLE IF EXISTS `order_refund_info`;
CREATE TABLE `order_refund_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `order_id` bigint NULL DEFAULT NULL COMMENT '订单id',
  `sku_id` bigint NULL DEFAULT NULL COMMENT 'skuid',
  `refund_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退款类型（1：退款 2：退货）',
  `refund_num` bigint NULL DEFAULT NULL COMMENT '退货件数',
  `refund_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '退款金额',
  `refund_reason_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原因类型',
  `refund_reason_txt` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原因内容',
  `refund_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退款状态（0：待审批 1：已退款）',
  `approve_operator_id` bigint NULL DEFAULT NULL COMMENT '审批人',
  `approve_time` datetime NULL DEFAULT NULL COMMENT '审批时间',
  `approve_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审批备注',
  `tracking_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退货物流单号',
  `tracking_time` datetime NULL DEFAULT NULL COMMENT '退货单号录入时间',
  `recieve_operator_id` bigint NULL DEFAULT NULL COMMENT '签收人',
  `recieve_time` datetime NULL DEFAULT NULL COMMENT '签收时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '退单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_refund_info
-- ----------------------------

-- ----------------------------
-- Table structure for order_status_log
-- ----------------------------
DROP TABLE IF EXISTS `order_status_log`;
CREATE TABLE `order_status_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NULL DEFAULT NULL,
  `order_status` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `operate_time` datetime NULL DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_status_log
-- ----------------------------

-- ----------------------------
-- Table structure for payment_info
-- ----------------------------
DROP TABLE IF EXISTS `payment_info`;
CREATE TABLE `payment_info`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `out_trade_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对外业务编号',
  `order_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `payment_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付类型（微信 支付宝）',
  `trade_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易编号',
  `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `subject` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易内容',
  `payment_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付状态',
  `callback_time` datetime NULL DEFAULT NULL COMMENT '回调时间',
  `callback_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '回调信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment_info
-- ----------------------------
INSERT INTO `payment_info` VALUES (18, 'gmall911460cc-4acf-44c2-a37c-474e6f063987', '41', 1, 'ALIPAY', '2023032222001419921438824740', 12998.00, ' 小米CC9 手机  仙女渐变色  8G+128G华为荣耀 V30 PRO  幻夜星河 8G+256', 'CLOSED', '2023-03-22 21:10:19', '{gmt_create=2023-03-22 21:10:09, charset=utf-8, gmt_payment=2023-03-22 21:10:17, notify_time=2023-03-22 21:10:17, subject= 小米CC9 手机  仙女渐变色  8G+128G华为荣耀 V30 PRO  幻夜星河 8G+256, buyer_id=2088022035519922, invoice_amount=0.01, version=1.0, notify_id=2023032201222211017019921411925228, fund_bill_list=[{\"amount\":\"0.01\",\"fundChannel\":\"ALIPAYACCOUNT\"}], notify_type=trade_status_sync, out_trade_no=gmall911460cc-4acf-44c2-a37c-474e6f063987, total_amount=0.01, trade_status=TRADE_SUCCESS, trade_no=2023032222001419921438824740, auth_app_id=2021001163617452, receipt_amount=0.01, point_amount=0.00, buyer_pay_amount=0.01, app_id=2021001163617452, seller_id=2088831489324244}', '2023-03-22 21:10:04', '2023-03-22 13:15:36', 0);
INSERT INTO `payment_info` VALUES (19, 'gmall578947bd-bf2f-4b90-857e-a18a20322b8c', '42', 1, 'ALIPAY', '2023032222001419921436787643', 3333.00, '小米CC9 手机  蓝色宝石  8G+128G  6.17英寸', 'CLOSED', '2023-03-22 21:13:38', '{gmt_create=2023-03-22 21:13:25, charset=utf-8, gmt_payment=2023-03-22 21:13:36, notify_time=2023-03-22 21:13:36, subject=小米CC9 手机  蓝色宝石  8G+128G  6.17英寸, buyer_id=2088022035519922, invoice_amount=0.01, version=1.0, notify_id=2023032201222211336019921412612731, fund_bill_list=[{\"amount\":\"0.01\",\"fundChannel\":\"ALIPAYACCOUNT\"}], notify_type=trade_status_sync, out_trade_no=gmall578947bd-bf2f-4b90-857e-a18a20322b8c, total_amount=0.01, trade_status=TRADE_SUCCESS, trade_no=2023032222001419921436787643, auth_app_id=2021001163617452, receipt_amount=0.01, point_amount=0.00, buyer_pay_amount=0.01, app_id=2021001163617452, seller_id=2088831489324244}', '2023-03-22 21:13:21', '2023-03-22 13:15:28', 0);
INSERT INTO `payment_info` VALUES (20, 'gmalla6e7b307-3160-4b88-b268-564c002422c2', '43', 1, 'ALIPAY', NULL, 3.00, '小米CC9 手机  蓝色宝石  8G+128G  6.17英寸', 'UNPAID', NULL, NULL, '2023-03-25 17:11:22', '2023-03-25 09:11:21', 0);

SET FOREIGN_KEY_CHECKS = 1;
