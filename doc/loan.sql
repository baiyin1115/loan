/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3306
 Source Schema         : loan

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 24/01/2019 21:30:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sequence
-- ----------------------------
DROP TABLE IF EXISTS `sequence`;
CREATE TABLE `sequence`  (
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `current_value` bigint(20) UNSIGNED NOT NULL DEFAULT 0,
  `increment` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sequence
-- ----------------------------
INSERT INTO `sequence` VALUES ('ACCT', 10000005, 1);
INSERT INTO `sequence` VALUES ('CUSTOMER', 10000002, 1);

-- ----------------------------
-- Table structure for t_biz_acct
-- ----------------------------
DROP TABLE IF EXISTS `t_biz_acct`;
CREATE TABLE `t_biz_acct`  (
  `id` bigint(20) NOT NULL COMMENT '融资账户171开头,借款账户271开头,公司卡账户471开头,暂收371，暂付372，代偿370',
  `user_no` bigint(20) NOT NULL COMMENT '融资账户、借款人账户指的是客户编号\r\n如果是公司卡账户、代偿账户、暂收暂付账户，为15000000001',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账户名称',
  `available_balance` decimal(15, 2) NOT NULL COMMENT '可用余额,默认0.00',
  `freeze_balance` decimal(15, 2) NOT NULL COMMENT '冻结余额,默认0.00',
  `acct_type` bigint(20) NOT NULL COMMENT '1:公司卡账户,2:融资账户,3:借款人账户,4:代偿账户,5:暂收,6:暂付',
  `status` bigint(20) NOT NULL COMMENT '账户状态，有效:1,冻结:2,止用:3',
  `balance_type` bigint(20) NOT NULL COMMENT '余额性质,可透支:1,不可透支:2',
  `create_by` bigint(20) NOT NULL COMMENT '操作员,',
  `modified_by` bigint(20) NOT NULL COMMENT '修改操作员,',
  `create_at` datetime(0) DEFAULT NULL COMMENT '创建时间,',
  `update_at` datetime(0) DEFAULT NULL COMMENT '更新时间,默认为当前系统时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '账户表（借款人,融资,公司卡账户）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_biz_acct
-- ----------------------------
INSERT INTO `t_biz_acct` VALUES (17110000001, 15110000001, '张三', 9999999.55, 0.00, 2, 1, 2, 1, 1, '2019-01-24 18:16:49', '2019-01-24 18:16:49', '系统自动建立');
INSERT INTO `t_biz_acct` VALUES (27110000002, 25110000002, '李四', 0.00, 0.00, 3, 3, 2, 1, 1, '2019-01-24 18:23:54', '2019-01-24 20:59:56', '系统自动建立\n            ');
INSERT INTO `t_biz_acct` VALUES (37010000005, 15000000001, '代偿001', 0.00, 0.00, 4, 1, 2, 1, 1, '2019-01-24 21:16:06', '2019-01-24 21:16:06', '            ');
INSERT INTO `t_biz_acct` VALUES (37110000004, 15000000001, '公司卡账户001', 0.00, 0.00, 1, 1, 2, 1, 1, '2019-01-24 21:15:33', '2019-01-24 21:16:26', '            ');

-- ----------------------------
-- Table structure for t_biz_acct_record
-- ----------------------------
DROP TABLE IF EXISTS `t_biz_acct_record`;
CREATE TABLE `t_biz_acct_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '流水编号,自增',
  `group_no` bigint(20) NOT NULL COMMENT '组号,',
  `org_no` bigint(20) NOT NULL COMMENT '公司编号,',
  `voucher_no` bigint(20) NOT NULL COMMENT '凭证编号,借据编号,融资编号,收支编号，入账编号，\r\n出账编号',
  `acct_no` bigint(20) NOT NULL COMMENT '账户,公司卡账户,融资账户,借款人账\r\n户,代偿账户,暂收,暂付',
  `type` bigint(20) NOT NULL COMMENT '业务类型,放款:1,还款:2,服务费收取:3,\r\n服务费补偿:4,支出:5,融资:6,撤资:7,收入:8,资金登记:9,转账:10,提现:11,结转:12',
  `amt_type` bigint(20) NOT NULL COMMENT '资金类型,本金:1,利息:2,罚息:3,服务费:4,代偿:5,活期利息:6,资金:7',
  `acct_date` date NOT NULL COMMENT '业务日期,',
  `amt` decimal(15, 2) NOT NULL COMMENT '发生金额,',
  `bal_dir` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发生方向,‘+/-’,通过符号表示，相对当前账户来说，是增加还是减少',
  `status` bigint(20) NOT NULL COMMENT '交易状态,成功:1,失败:2',
  `create_by` bigint(20) NOT NULL COMMENT '操作员,',
  `modified_by` bigint(20) NOT NULL COMMENT '修改操作员,',
  `create_at` datetime(0) DEFAULT NULL COMMENT '创建时间,',
  `update_at` datetime(0) DEFAULT NULL COMMENT '更新时间,默认为当前系统时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '账户资金流水表（资金变动流水信息）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_biz_customer_info
-- ----------------------------
DROP TABLE IF EXISTS `t_biz_customer_info`;
CREATE TABLE `t_biz_customer_info`  (
  `id` bigint(20) NOT NULL COMMENT '客户编号,融资151开头,借款251开头',
  `cert_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '证件号码,',
  `cert_type` bigint(20) NOT NULL COMMENT '证件类型,1:身份证,2:护照,3:营业执照,4:组织机构代\r\n码,5:统一社会信用代码',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户姓名,',
  `sex` bigint(20) NOT NULL COMMENT '性别,1:男,2:女',
  `mobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '手机号,',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电话,',
  `email` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电子邮箱,',
  `type` bigint(20) NOT NULL COMMENT '客户类型,1:借款人账户,2:融资人账户',
  `status` bigint(20) NOT NULL COMMENT '客户状态,正常:1,黑名单:2,删除:3',
  `create_by` bigint(20) NOT NULL COMMENT '操作员,',
  `modified_by` bigint(20) NOT NULL COMMENT '修改操作员,',
  `create_at` datetime(0) DEFAULT NULL COMMENT '创建时间,',
  `update_at` datetime(0) DEFAULT NULL COMMENT '更新时间,默认为当前系统时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `cus_u_index`(`cert_no`, `cert_type`) USING BTREE COMMENT '证件号码、证件类型唯一'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '客户信息表（描述的是借款人信息）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_biz_customer_info
-- ----------------------------
INSERT INTO `t_biz_customer_info` VALUES (15110000001, '130803199701014011', 1, '张三', 1, '13910673518', '04713988057', 'msjfkg@baiqishi.com', 1, 1, 1, 1, '2019-01-24 18:16:49', '2019-01-24 18:16:49', ' 安全           ');
INSERT INTO `t_biz_customer_info` VALUES (25110000002, '130803199701014012', 1, '李四', 1, '13911673518', '047139880572', 'msjfkg@baiqishi.com', 2, 3, 1, 1, '2019-01-24 18:23:54', '2019-01-24 21:18:29', '李四\n            ');

-- ----------------------------
-- Table structure for t_biz_in_out_voucher_info
-- ----------------------------
DROP TABLE IF EXISTS `t_biz_in_out_voucher_info`;
CREATE TABLE `t_biz_in_out_voucher_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收支编号,自增',
  `acct_no` bigint(20) NOT NULL COMMENT '账户,指的是从哪个公司卡账户出入钱',
  `external_acct` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '外部账户,指的是从外部那个银行卡出入的,标记',
  `amt` decimal(15, 2) NOT NULL COMMENT '金额,',
  `type` bigint(20) NOT NULL COMMENT '用途,一般预算支出:1,办公支出:2,其他支出:3,股东分润:5,对公支出:6,对公收入:7,利息收入:8',
  `status` bigint(20) NOT NULL COMMENT '状态,成功:1,失败:2',
  `create_by` bigint(20) NOT NULL COMMENT '操作员,',
  `modified_by` bigint(20) NOT NULL COMMENT '修改操作员,',
  `create_at` datetime(0) DEFAULT NULL COMMENT '创建时间,',
  `update_at` datetime(0) DEFAULT NULL COMMENT '更新时间,默认为当前系统时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收支信息表（描述收支凭证信息）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_biz_invest_info
-- ----------------------------
DROP TABLE IF EXISTS `t_biz_invest_info`;
CREATE TABLE `t_biz_invest_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '融资编号,自增',
  `org_no` bigint(20) NOT NULL COMMENT '公司编号,',
  `user_no` bigint(20) NOT NULL COMMENT '用户编号,指的是融资',
  `in_acct_no` bigint(20) NOT NULL COMMENT '入账账户,指的是入账到哪个公司卡账户上',
  `external_acct` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '投资人出款账户,指的是融资的出资银行卡卡号',
  `prin` decimal(15, 2) NOT NULL COMMENT '本金,',
  `acct_date` date NOT NULL COMMENT '业务日期,',
  `begin_date` date NOT NULL COMMENT '起息开始日期,',
  `end_date` date DEFAULT NULL COMMENT '起息结束日期,一般指全额提现日或者结转日',
  `rate` decimal(15, 3) NOT NULL COMMENT '利率,',
  `term_no` bigint(20) DEFAULT NULL COMMENT '期数,',
  `cycle_interval` bigint(20) NOT NULL COMMENT '周期间隔,一期的天数，默认是30天',
  `status` bigint(20) NOT NULL COMMENT '状态,登记:1,计息中:2,已延期:3,已撤资:4,\r\n已结转:5',
  `dd_date` bigint(20) DEFAULT NULL COMMENT '计息日,每个月的第几天',
  `extension_no` bigint(20) DEFAULT NULL COMMENT '延期期数,',
  `extension_rate` decimal(15, 3) DEFAULT NULL COMMENT '延期利率,',
  `tot_schd_bigint` decimal(15, 2) DEFAULT NULL COMMENT '应回利息累计,',
  `tot_paid_prin` decimal(15, 2) DEFAULT NULL COMMENT '已提本金累计,',
  `tot_paid_bigint` decimal(15, 2) DEFAULT NULL COMMENT '已提利息累计,',
  `tot_wav_amt` decimal(15, 2) DEFAULT NULL COMMENT '收益调整金额累计,融资提现时增加或者扣除的\r\n金额',
  `create_by` bigint(20) NOT NULL COMMENT '操作员,',
  `modified_by` bigint(20) NOT NULL COMMENT '修改操作员,',
  `create_at` datetime(0) DEFAULT NULL COMMENT '创建时间,',
  `update_at` datetime(0) DEFAULT NULL COMMENT '更新时间,默认为当前系统时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '融资信息表（描述融资的融资信息）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_biz_invest_plan
-- ----------------------------
DROP TABLE IF EXISTS `t_biz_invest_plan`;
CREATE TABLE `t_biz_invest_plan`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '计划编号,自增',
  `invest_no` bigint(20) NOT NULL COMMENT '融资编号,',
  `org_no` bigint(20) NOT NULL COMMENT '公司编号,',
  `user_no` bigint(20) NOT NULL COMMENT '用户编号,',
  `term_no` bigint(20) NOT NULL COMMENT '期数,第几期',
  `dd_date` date DEFAULT NULL COMMENT '计息日期,',
  `rate` decimal(15, 3) NOT NULL COMMENT '利率,',
  `begin_date` date NOT NULL COMMENT '本期开始日期,',
  `end_date` date DEFAULT NULL COMMENT '本期结束日期,',
  `dd_num` bigint(20) NOT NULL COMMENT '计息天数,',
  `dd_prin` decimal(15, 2) DEFAULT NULL COMMENT '本期计息本金,',
  `chd_bigint` decimal(15, 2) DEFAULT NULL COMMENT '本期应收利息,',
  `paid_bigint` decimal(15, 2) DEFAULT NULL COMMENT '本期已回利息,',
  `status` bigint(20) NOT NULL COMMENT '回款状态,未计息:1,已计息:2,已结息:3,已终止:4',
  `create_by` bigint(20) NOT NULL COMMENT '操作员,',
  `modified_by` bigint(20) NOT NULL COMMENT '修改操作员,',
  `create_at` datetime(0) DEFAULT NULL COMMENT '创建时间,',
  `update_at` datetime(0) DEFAULT NULL COMMENT '更新时间,默认为当前系统时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '回款计划表（描述融资的回款计划信息）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_biz_loan_info
-- ----------------------------
DROP TABLE IF EXISTS `t_biz_loan_info`;
CREATE TABLE `t_biz_loan_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '借据编号,自增',
  `org_no` bigint(20) NOT NULL COMMENT '公司编号,',
  `product_no` bigint(20) NOT NULL COMMENT '产品编号,',
  `cust_no` bigint(20) NOT NULL COMMENT '客户编号,',
  `contr_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '原始合同编号,',
  `loan_type` bigint(20) NOT NULL COMMENT '贷款类型,抵押:1,网签:2,其他:3',
  `acct_date` date NOT NULL COMMENT '业务日期,',
  `begin_date` date NOT NULL COMMENT '借款开始日期,',
  `end_date` date DEFAULT NULL COMMENT '借款结束日期,',
  `prin` decimal(15, 2) NOT NULL COMMENT '本金,',
  `rate` decimal(15, 3) NOT NULL COMMENT '利率,按年百分比填写',
  `receive_bigint` decimal(15, 2) NOT NULL COMMENT '应收利息,',
  `repay_type` bigint(20) NOT NULL COMMENT '产品还款方式,等额本息:1,等额本金:2;\r\n先息后本:3,先息后本（上交息）:4,一次性还本付息:5',
  `term_no` bigint(20) DEFAULT NULL COMMENT '期数,',
  `lending_date` date DEFAULT NULL COMMENT '放款日期,',
  `lending_amt` decimal(15, 2) DEFAULT NULL COMMENT '放款金额,',
  `lending_acct` bigint(20) NOT NULL COMMENT '放款账户,指出金的公司卡账户信息',
  `external_acct` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收款账户,借款人收款的银行账户',
  `service_fee` decimal(15, 2) NOT NULL COMMENT '服务费,默认为0.00',
  `service_fee_type` bigint(20) DEFAULT NULL COMMENT '服务费收取方式,首期:1,按期:2',
  `dd_date` bigint(20) DEFAULT NULL COMMENT '约定还款日,每个月的第几天',
  `is_pen` bigint(20) DEFAULT NULL COMMENT '是否罚息,1:是,2:否',
  `pen_rate` decimal(15, 3) DEFAULT NULL COMMENT '罚息利率,按日千分比填写,默认0.000',
  `pen_number` bigint(20) DEFAULT NULL COMMENT '罚息基数,本金:1,未还金额:2',
  `extension_no` bigint(20) DEFAULT NULL COMMENT '展期期数,期数',
  `extension_rate` decimal(15, 3) DEFAULT NULL COMMENT '展期利息,',
  `schd_prin` decimal(15, 2) DEFAULT NULL COMMENT '应还本金,',
  `schd_bigint` decimal(15, 2) DEFAULT NULL COMMENT '应还利息,',
  `schd_serv_fee` decimal(15, 2) DEFAULT NULL COMMENT '应收服务费,',
  `schd_pen` decimal(15, 2) DEFAULT NULL COMMENT '逾期罚息累计,',
  `tot_paid_prin` decimal(15, 2) DEFAULT NULL COMMENT '已还本金累计,',
  `tot_paid_bigint` decimal(15, 2) DEFAULT NULL COMMENT '已还利息累计,',
  `tot_paid_serv_fee` decimal(15, 2) DEFAULT NULL COMMENT '已收服务费累计,',
  `tot_paid_pen` decimal(15, 2) DEFAULT NULL COMMENT '已还罚息累计,',
  `tot_wav_amt` decimal(15, 2) DEFAULT NULL COMMENT '减免金额累计,',
  `status` bigint(20) NOT NULL COMMENT '借据状态,登记:1,已放款:2,还款中:3,\r\n已逾期:4,已展期:5,已结清:6,已代偿:7,已终止:8',
  `create_by` bigint(20) NOT NULL COMMENT '操作员,',
  `modified_by` bigint(20) NOT NULL COMMENT '修改操作员,',
  `create_at` datetime(0) DEFAULT NULL COMMENT '创建时间,',
  `update_at` datetime(0) DEFAULT NULL COMMENT '更新时间,默认为当前系统时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '借据表（借款具体信息，描述具体的借款情况，整个借款的生命周期中，主要变更这个表）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_biz_loan_voucher_info
-- ----------------------------
DROP TABLE IF EXISTS `t_biz_loan_voucher_info`;
CREATE TABLE `t_biz_loan_voucher_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '凭证编号,自增',
  `loan_no` bigint(20) NOT NULL COMMENT '借据编号,',
  `type` bigint(20) NOT NULL COMMENT '凭证类型,身份证:1,电子合同:2,房本:3,解押手\r\n续:4',
  `path` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '凭证存储地址,',
  `create_by` bigint(20) NOT NULL COMMENT '操作员,',
  `modified_by` bigint(20) NOT NULL COMMENT '修改操作员,',
  `create_at` datetime(0) DEFAULT NULL COMMENT '创建时间,',
  `update_at` datetime(0) DEFAULT NULL COMMENT '更新时间,',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '借据凭证信息（描述的是贷款的原始凭证信息）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_biz_product_info
-- ----------------------------
DROP TABLE IF EXISTS `t_biz_product_info`;
CREATE TABLE `t_biz_product_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '产品编号,自增',
  `org_no` bigint(20) NOT NULL COMMENT '公司编号,',
  `product_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '产品名称,',
  `rate` decimal(15, 3) NOT NULL COMMENT '产品利率,按年百分比填写',
  `service_fee_scale` decimal(15, 3) NOT NULL COMMENT '服务费比例,默认0.000',
  `service_fee_type` bigint(20) DEFAULT NULL COMMENT '服务费收取方式,首期:1,按期:2',
  `pen_rate` decimal(15, 3) DEFAULT NULL COMMENT '罚息利率,按日千分比填写,默认0.000',
  `is_pen` bigint(20) DEFAULT NULL COMMENT '是否罚息,1:是,0:否',
  `pen_number` bigint(20) DEFAULT NULL COMMENT '罚息基数,本金:1,未还金额:2',
  `repay_type` bigint(20) NOT NULL COMMENT '产品还款方式,等额本息:1,等额本金:2;\r\n先息后本:3,先息后本（上交息）:4,一次性还本付息:5',
  `loan_type` bigint(20) NOT NULL COMMENT '贷款类型,抵押:1,网签:2,3:其他',
  `cycle_interval` bigint(20) NOT NULL COMMENT '周期间隔,一期的天数，默认是30天',
  `create_by` bigint(20) NOT NULL COMMENT '操作员,',
  `modified_by` bigint(20) NOT NULL COMMENT '修改操作员,',
  `create_at` datetime(0) DEFAULT NULL COMMENT '创建时间,',
  `update_at` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间,默认为当前系统时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '产品代码表（描述贷款产品信息，不同公司的产品编码不能一致）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_biz_product_info
-- ----------------------------
INSERT INTO `t_biz_product_info` VALUES (3, 28, '投资公司上交息001', 0.053, 0.000, 1, 0.020, 1, 1, 4, 1, 30, 1, 1, '2019-01-22 15:07:02', '2019-01-23 13:56:59', NULL);
INSERT INTO `t_biz_product_info` VALUES (25, 24, '典当行上交息002', 0.053, 0.000, 1, 0.020, 1, 1, 1, 1, 30, 1, 1, '2019-01-22 20:13:47', '2019-01-22 20:43:42', NULL);
INSERT INTO `t_biz_product_info` VALUES (26, 28, '投资公司上交息002', 0.053, 0.000, 1, 0.020, 1, 2, 1, 1, 30, 1, 1, '2019-01-22 20:44:05', '2019-01-23 13:56:41', NULL);

-- ----------------------------
-- Table structure for t_biz_repay_plan
-- ----------------------------
DROP TABLE IF EXISTS `t_biz_repay_plan`;
CREATE TABLE `t_biz_repay_plan`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '计划编号,自增',
  `loan_no` bigint(20) NOT NULL COMMENT '借据编号,',
  `org_no` bigint(20) NOT NULL COMMENT '公司编号,',
  `product_no` bigint(20) NOT NULL COMMENT '产品编号,',
  `cust_no` bigint(20) NOT NULL COMMENT '客户编号,',
  `acct_date` date NOT NULL COMMENT '业务日期,',
  `term_no` bigint(20) NOT NULL COMMENT '期数,第几期',
  `rate` decimal(15, 3) NOT NULL COMMENT '利率,',
  `begin_date` date NOT NULL COMMENT '本期开始日期,',
  `end_date` date DEFAULT NULL COMMENT '本期结束日期,',
  `dd_num` bigint(20) NOT NULL COMMENT '计息天数,',
  `dd_date` date DEFAULT NULL COMMENT '还款日期,',
  `external_acct` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '还款账户,借款人还款的银行账户',
  `in_acct_no` bigint(20) NOT NULL COMMENT '入账账户,还款的钱入到哪个公司卡账户上面',
  `ctd_prin` decimal(15, 2) DEFAULT NULL COMMENT '本期应还本金,',
  `ctd_bigint` decimal(15, 2) DEFAULT NULL COMMENT '本期应还利息,',
  `ctd_serv_fee` decimal(15, 2) DEFAULT NULL COMMENT '本期应收服务费,',
  `ctd_pen` decimal(15, 2) DEFAULT NULL COMMENT '本期应收罚息,',
  `paid_prin` decimal(15, 2) DEFAULT NULL COMMENT '本期已还本金,',
  `paid_bigint` decimal(15, 2) DEFAULT NULL COMMENT '本期已还利息,',
  `paid_serv_fee` decimal(15, 2) DEFAULT NULL COMMENT '本期已收服务费,',
  `paid_pen` decimal(15, 2) DEFAULT NULL COMMENT '本期已收罚息,',
  `wav_amt` decimal(15, 2) DEFAULT NULL COMMENT '本期减免,',
  `status` bigint(20) NOT NULL COMMENT '还款状态,待还:1,已还:2,已逾期:3,\r\n已代偿:4,已终止:5',
  `create_by` bigint(20) NOT NULL COMMENT '操作员,',
  `modified_by` bigint(20) NOT NULL COMMENT '修改操作员,',
  `create_at` datetime(0) DEFAULT NULL COMMENT '创建时间,',
  `update_at` datetime(0) DEFAULT NULL COMMENT '更新时间,默认为当前系统时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '还款计划表（客户的还款计划信息，根据借据的期数，生成多条记录）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_biz_transfer_voucher_info
-- ----------------------------
DROP TABLE IF EXISTS `t_biz_transfer_voucher_info`;
CREATE TABLE `t_biz_transfer_voucher_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '转账编号,自增',
  `in_acct_no` bigint(20) NOT NULL COMMENT '入账账户,',
  `out_acct_no` bigint(20) NOT NULL COMMENT '出账账户,',
  `amt` decimal(15, 2) NOT NULL COMMENT '金额,',
  `type` bigint(20) NOT NULL COMMENT '用途,账户调整:1,服务费补偿:2,融资人贴息:3,其他:4',
  `status` bigint(20) NOT NULL COMMENT '状态,成功:1,失败:2',
  `create_by` bigint(20) NOT NULL COMMENT '操作员,',
  `modified_by` bigint(20) NOT NULL COMMENT '修改操作员,',
  `create_at` datetime(0) DEFAULT NULL COMMENT '创建时间,',
  `update_at` datetime(0) DEFAULT NULL COMMENT '更新时间,默认为当前系统时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '转账凭证表（描述转账凭证信息）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_cfg
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_cfg`;
CREATE TABLE `t_sys_cfg`  (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `cfg_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '参数名',
  `cfg_value` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '参数值',
  `cfg_desc` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '参数描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统参数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_cfg
-- ----------------------------
INSERT INTO `t_sys_cfg` VALUES (1, 'JS_API_TICKET', 'test', '微信JSAPI_TICKET(produt:kgt8ON7yVITDhtdwci0qeYBa_xOxkaEccepDVZel0heq1M9pKgrfFWOlX2MfHEt122psCpElf4V5eePHPouJPg)');
INSERT INTO `t_sys_cfg` VALUES (2, 'ACCESS_TOKEN', 'test', '微信Token');
INSERT INTO `t_sys_cfg` VALUES (3, 'app_name', 'loan', '小贷登记系统web框架');

-- ----------------------------
-- Table structure for t_sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_dept`;
CREATE TABLE `t_sys_dept`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `num` int(11) DEFAULT NULL COMMENT '排序',
  `pid` int(11) DEFAULT NULL COMMENT '父部门id',
  `pids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '父级ids',
  `simplename` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '简称',
  `fullname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '全称',
  `tips` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '提示',
  `version` int(11) DEFAULT NULL COMMENT '版本（乐观锁保留字段）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_dept
-- ----------------------------
INSERT INTO `t_sys_dept` VALUES (24, 1, 0, '[0],', '典当行', '典当行', '', NULL);
INSERT INTO `t_sys_dept` VALUES (28, 2, 0, '[0],', '投资公司', '投资公司', '', NULL);

-- ----------------------------
-- Table structure for t_sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_dict`;
CREATE TABLE `t_sys_dict`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `num` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '排序',
  `pid` int(11) DEFAULT NULL COMMENT '父级字典',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '名称',
  `tips` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '提示',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 134 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_dict
-- ----------------------------
INSERT INTO `t_sys_dict` VALUES (16, '0', 0, '状态', NULL);
INSERT INTO `t_sys_dict` VALUES (17, '1', 16, '启用', NULL);
INSERT INTO `t_sys_dict` VALUES (18, '2', 16, '禁用', NULL);
INSERT INTO `t_sys_dict` VALUES (35, '0', 0, '账号状态', NULL);
INSERT INTO `t_sys_dict` VALUES (36, '1', 35, '启用', NULL);
INSERT INTO `t_sys_dict` VALUES (37, '2', 35, '冻结', NULL);
INSERT INTO `t_sys_dict` VALUES (38, '3', 35, '已删除', NULL);
INSERT INTO `t_sys_dict` VALUES (39, '0', 0, '银行卡类型', NULL);
INSERT INTO `t_sys_dict` VALUES (40, '0', 39, '借记卡', NULL);
INSERT INTO `t_sys_dict` VALUES (41, '1', 39, '信用卡', NULL);
INSERT INTO `t_sys_dict` VALUES (44, '0', 0, '联系人关系', NULL);
INSERT INTO `t_sys_dict` VALUES (45, '1', 44, '父子', NULL);
INSERT INTO `t_sys_dict` VALUES (46, '2', 44, '母子', NULL);
INSERT INTO `t_sys_dict` VALUES (47, '3', 44, '配偶', NULL);
INSERT INTO `t_sys_dict` VALUES (48, '4', 44, '朋友', NULL);
INSERT INTO `t_sys_dict` VALUES (49, '5', 44, '子女', NULL);
INSERT INTO `t_sys_dict` VALUES (50, '6', 44, '兄弟姐妹', NULL);
INSERT INTO `t_sys_dict` VALUES (56, '0', 0, '学历类型', NULL);
INSERT INTO `t_sys_dict` VALUES (57, '1', 56, '博士', NULL);
INSERT INTO `t_sys_dict` VALUES (58, '2', 56, '硕士', NULL);
INSERT INTO `t_sys_dict` VALUES (59, '3', 56, '本科', NULL);
INSERT INTO `t_sys_dict` VALUES (60, '4', 56, '专科', NULL);
INSERT INTO `t_sys_dict` VALUES (61, '5', 56, '高中及以下', NULL);
INSERT INTO `t_sys_dict` VALUES (68, '0', 0, '证件类型', NULL);
INSERT INTO `t_sys_dict` VALUES (69, '1', 68, '身份证', NULL);
INSERT INTO `t_sys_dict` VALUES (70, '2', 68, '护照', NULL);
INSERT INTO `t_sys_dict` VALUES (71, '3', 68, '营业执照', NULL);
INSERT INTO `t_sys_dict` VALUES (72, '4', 68, '组织机构代码', NULL);
INSERT INTO `t_sys_dict` VALUES (73, '5', 68, '统一社会信用代码', NULL);
INSERT INTO `t_sys_dict` VALUES (74, '0', 0, '性别', NULL);
INSERT INTO `t_sys_dict` VALUES (75, '1', 74, '男', NULL);
INSERT INTO `t_sys_dict` VALUES (76, '2', 74, '女', NULL);
INSERT INTO `t_sys_dict` VALUES (80, '0', 0, '是否', NULL);
INSERT INTO `t_sys_dict` VALUES (81, '1', 80, '是', NULL);
INSERT INTO `t_sys_dict` VALUES (82, '0', 80, '否', NULL);
INSERT INTO `t_sys_dict` VALUES (83, '0', 0, '服务费收取方式', NULL);
INSERT INTO `t_sys_dict` VALUES (84, '1', 83, '首期', NULL);
INSERT INTO `t_sys_dict` VALUES (85, '2', 83, '按期', NULL);
INSERT INTO `t_sys_dict` VALUES (88, '0', 0, '还款方式', NULL);
INSERT INTO `t_sys_dict` VALUES (89, '1', 88, '等额本息', NULL);
INSERT INTO `t_sys_dict` VALUES (90, '2', 88, '等额本金', NULL);
INSERT INTO `t_sys_dict` VALUES (91, '3', 88, '先息后本', NULL);
INSERT INTO `t_sys_dict` VALUES (92, '4', 88, '先息后本[上交息]', NULL);
INSERT INTO `t_sys_dict` VALUES (93, '5', 88, '一次性还本付息', NULL);
INSERT INTO `t_sys_dict` VALUES (94, '0', 0, '贷款类型', NULL);
INSERT INTO `t_sys_dict` VALUES (95, '1', 94, '抵押', NULL);
INSERT INTO `t_sys_dict` VALUES (96, '2', 94, '网签', NULL);
INSERT INTO `t_sys_dict` VALUES (97, '3', 94, '其它', NULL);
INSERT INTO `t_sys_dict` VALUES (104, '0', 0, '罚息基数', NULL);
INSERT INTO `t_sys_dict` VALUES (105, '1', 104, '本金', NULL);
INSERT INTO `t_sys_dict` VALUES (106, '2', 104, '未还金额', NULL);
INSERT INTO `t_sys_dict` VALUES (107, '0', 0, '客户类型', NULL);
INSERT INTO `t_sys_dict` VALUES (108, '1', 107, '借款人账户', NULL);
INSERT INTO `t_sys_dict` VALUES (109, '2', 107, '融资人账户', NULL);
INSERT INTO `t_sys_dict` VALUES (110, '0', 0, '客户状态', NULL);
INSERT INTO `t_sys_dict` VALUES (111, '1', 110, '正常', NULL);
INSERT INTO `t_sys_dict` VALUES (112, '2', 110, '黑名单', NULL);
INSERT INTO `t_sys_dict` VALUES (113, '3', 110, '删除', NULL);
INSERT INTO `t_sys_dict` VALUES (120, '0', 0, '余额性质', NULL);
INSERT INTO `t_sys_dict` VALUES (121, '1', 120, '可透支', NULL);
INSERT INTO `t_sys_dict` VALUES (122, '2', 120, '不可透支', NULL);
INSERT INTO `t_sys_dict` VALUES (123, '0', 0, '账户状态', NULL);
INSERT INTO `t_sys_dict` VALUES (124, '1', 123, '有效', NULL);
INSERT INTO `t_sys_dict` VALUES (125, '2', 123, '冻结', NULL);
INSERT INTO `t_sys_dict` VALUES (126, '3', 123, '止用', NULL);
INSERT INTO `t_sys_dict` VALUES (127, '0', 0, '账户类型', NULL);
INSERT INTO `t_sys_dict` VALUES (128, '1', 127, '公司卡账户', NULL);
INSERT INTO `t_sys_dict` VALUES (129, '2', 127, '融资账户', NULL);
INSERT INTO `t_sys_dict` VALUES (130, '3', 127, '借款人账户', NULL);
INSERT INTO `t_sys_dict` VALUES (131, '4', 127, '代偿账户', NULL);
INSERT INTO `t_sys_dict` VALUES (132, '5', 127, '暂收', NULL);
INSERT INTO `t_sys_dict` VALUES (133, '6', 127, '暂付', NULL);

-- ----------------------------
-- Table structure for t_sys_expense
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_expense`;
CREATE TABLE `t_sys_expense`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `money` decimal(20, 2) DEFAULT NULL COMMENT '报销金额',
  `desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '描述',
  `createtime` datetime(0) DEFAULT NULL,
  `state` int(11) DEFAULT NULL COMMENT '状态: 1.待提交  2:待审核   3.审核通过 4:驳回',
  `userid` int(11) DEFAULT NULL COMMENT '用户id',
  `processId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '流程定义id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '报销表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_login_log`;
CREATE TABLE `t_sys_login_log`  (
  `id` int(65) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `logname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '日志名称',
  `userid` int(65) DEFAULT NULL COMMENT '管理员id',
  `createtime` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `succeed` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '是否执行成功',
  `message` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '具体消息',
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '登录ip',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '登录记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_menu`;
CREATE TABLE `t_sys_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单编号',
  `pcode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单父编号',
  `pcodes` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '当前菜单的所有父菜单编号',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单名称',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单图标',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'url地址',
  `num` int(65) DEFAULT NULL COMMENT '菜单排序号',
  `levels` int(65) DEFAULT NULL COMMENT '菜单层级',
  `ismenu` int(11) DEFAULT NULL COMMENT '是否是菜单（1：是  0：不是）',
  `tips` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `status` int(65) DEFAULT NULL COMMENT '菜单状态 :  1:启用   0:不启用',
  `isopen` int(11) DEFAULT NULL COMMENT '是否打开:    1:打开   0:不打开',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 244 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_menu
-- ----------------------------
INSERT INTO `t_sys_menu` VALUES (105, 'system', '0', '[0],', '系统管理', 'fa-cog', '#', 4, 1, 1, NULL, 1, 1);
INSERT INTO `t_sys_menu` VALUES (106, 'mgr', 'system', '[0],[system],', '用户管理', '', '/mgr', 1, 2, 1, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (107, 'mgr_add', 'mgr', '[0],[system],[mgr],', '添加用户', '', '/mgr/add', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (108, 'mgr_edit', 'mgr', '[0],[system],[mgr],', '修改用户', NULL, '/mgr/edit', 2, 3, 0, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (109, 'mgr_delete', 'mgr', '[0],[system],[mgr],', '删除用户', NULL, '/mgr/delete', 3, 3, 0, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (110, 'mgr_reset', 'mgr', '[0],[system],[mgr],', '重置密码', NULL, '/mgr/reset', 4, 3, 0, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (111, 'mgr_freeze', 'mgr', '[0],[system],[mgr],', '冻结用户', NULL, '/mgr/freeze', 5, 3, 0, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (112, 'mgr_unfreeze', 'mgr', '[0],[system],[mgr],', '解除冻结用户', NULL, '/mgr/unfreeze', 6, 3, 0, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (113, 'mgr_setRole', 'mgr', '[0],[system],[mgr],', '分配角色', NULL, '/mgr/setRole', 7, 3, 0, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (114, 'role', 'system', '[0],[system],', '角色管理', NULL, '/role', 2, 2, 1, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (115, 'role_add', 'role', '[0],[system],[role],', '添加角色', NULL, '/role/add', 1, 3, 0, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (116, 'role_edit', 'role', '[0],[system],[role],', '修改角色', NULL, '/role/edit', 2, 3, 0, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (117, 'role_remove', 'role', '[0],[system],[role],', '删除角色', NULL, '/role/remove', 3, 3, 0, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (118, 'role_setAuthority', 'role', '[0],[system],[role],', '配置权限', NULL, '/role/setAuthority', 4, 3, 0, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (119, 'menu', 'system', '[0],[system],', '菜单管理', NULL, '/menu', 4, 2, 1, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (120, 'menu_add', 'menu', '[0],[system],[menu],', '添加菜单', NULL, '/menu/add', 1, 3, 0, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (121, 'menu_edit', 'menu', '[0],[system],[menu],', '修改菜单', NULL, '/menu/edit', 2, 3, 0, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (122, 'menu_remove', 'menu', '[0],[system],[menu],', '删除菜单', NULL, '/menu/remove', 3, 3, 0, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (128, 'log', 'system', '[0],[system],', '业务日志', NULL, '/log', 6, 2, 1, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (130, 'druid', 'system', '[0],[system],', '监控管理', NULL, '/druid', 7, 2, 1, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (131, 'dept', 'system', '[0],[system],', '公司管理', '', '/dept', 3, 2, 1, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (132, 'dict', 'system', '[0],[system],', '字典管理', NULL, '/dict', 4, 2, 1, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (133, 'loginLog', 'system', '[0],[system],', '登录日志', NULL, '/loginLog', 6, 2, 1, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (134, 'log_clean', 'log', '[0],[system],[log],', '清空日志', NULL, '/log/delLog', 3, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (135, 'dept_add', 'dept', '[0],[system],[dept],', '添加部门', NULL, '/dept/add', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (136, 'dept_update', 'dept', '[0],[system],[dept],', '修改部门', NULL, '/dept/update', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (137, 'dept_delete', 'dept', '[0],[system],[dept],', '删除部门', NULL, '/dept/delete', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (138, 'dict_add', 'dict', '[0],[system],[dict],', '添加字典', NULL, '/dict/add', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (139, 'dict_update', 'dict', '[0],[system],[dict],', '修改字典', NULL, '/dict/update', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (140, 'dict_delete', 'dict', '[0],[system],[dict],', '删除字典', NULL, '/dict/delete', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (141, 'notice', 'system', '[0],[system],', '通知管理', NULL, '/notice', 9, 2, 1, NULL, 0, NULL);
INSERT INTO `t_sys_menu` VALUES (142, 'notice_add', 'notice', '[0],[system],[notice],', '添加通知', NULL, '/notice/add', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (143, 'notice_update', 'notice', '[0],[system],[notice],', '修改通知', NULL, '/notice/update', 2, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (144, 'notice_delete', 'notice', '[0],[system],[notice],', '删除通知', NULL, '/notice/delete', 3, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (145, 'hello', '0', '[0],', '通知', 'fa-rocket', '/notice/hello', 1, 1, 1, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (148, 'code', '0', '[0],', '代码生成', 'fa-code', '/code', 3, 1, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (149, 'api_mgr', '0', '[0],', '接口文档', 'fa-leaf', '/swagger-ui.html', 2, 1, 1, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (150, 'to_menu_edit', 'menu', '[0],[system],[menu],', '菜单编辑跳转', '', '/menu/menu_edit', 4, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (151, 'menu_list', 'menu', '[0],[system],[menu],', '菜单列表', '', '/menu/list', 5, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (152, 'to_dept_update', 'dept', '[0],[system],[dept],', '修改部门跳转', '', '/dept/dept_update', 4, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (153, 'dept_list', 'dept', '[0],[system],[dept],', '部门列表', '', '/dept/list', 5, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (154, 'dept_detail', 'dept', '[0],[system],[dept],', '部门详情', '', '/dept/detail', 6, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (155, 'to_dict_edit', 'dict', '[0],[system],[dict],', '修改菜单跳转', '', '/dict/dict_edit', 4, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (156, 'dict_list', 'dict', '[0],[system],[dict],', '字典列表', '', '/dict/list', 5, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (157, 'dict_detail', 'dict', '[0],[system],[dict],', '字典详情', '', '/dict/detail', 6, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (158, 'log_list', 'log', '[0],[system],[log],', '日志列表', '', '/log/list', 2, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (159, 'log_detail', 'log', '[0],[system],[log],', '日志详情', '', '/log/detail', 3, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (160, 'del_login_log', 'loginLog', '[0],[system],[loginLog],', '清空登录日志', '', '/loginLog/delLoginLog', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (161, 'login_log_list', 'loginLog', '[0],[system],[loginLog],', '登录日志列表', '', '/loginLog/list', 2, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (162, 'to_role_edit', 'role', '[0],[system],[role],', '修改角色跳转', '', '/role/role_edit', 5, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (163, 'to_role_assign', 'role', '[0],[system],[role],', '角色分配跳转', '', '/role/role_assign', 6, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (164, 'role_list', 'role', '[0],[system],[role],', '角色列表', '', '/role/list', 7, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (165, 'to_assign_role', 'mgr', '[0],[system],[mgr],', '分配角色跳转', '', '/mgr/role_assign', 8, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (166, 'to_user_edit', 'mgr', '[0],[system],[mgr],', '编辑用户跳转', '', '/mgr/user_edit', 9, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (167, 'mgr_list', 'mgr', '[0],[system],[mgr],', '用户列表', '', '/mgr/list', 10, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (168, 'expense', '0', '[0],', '报销管理', 'fa-clone', '#', 5, 1, 1, NULL, 0, NULL);
INSERT INTO `t_sys_menu` VALUES (169, 'expense_fill', 'expense', '[0],[expense],', '报销申请', '', '/expense', 1, 2, 1, NULL, 0, NULL);
INSERT INTO `t_sys_menu` VALUES (170, 'expense_progress', 'expense', '[0],[expense],', '报销审批', '', '/process', 2, 2, 1, NULL, 0, NULL);
INSERT INTO `t_sys_menu` VALUES (199, 'cfg_add', 'cfg', '[0],[system],[cfg],', '添加系统参数', '', '/cfg/add', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (200, 'cfg_update', 'cfg', '[0],[system],[cfg],', '修改系统参数', '', '/cfg/update', 2, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (201, 'cfg_delete', 'cfg', '[0],[system],[cfg],', '删除系统参数', '', '/cfg/delete', 3, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (202, 'task', 'system', '[0],[system],', '任务管理', '', '/task', 11, 2, 1, '', 0, NULL);
INSERT INTO `t_sys_menu` VALUES (203, 'task_add', 'task', '[0],[system],[task],', '添加任务', '', '/task/add', 1, 3, 0, '', 1, NULL);
INSERT INTO `t_sys_menu` VALUES (204, 'task_update', 'task', '[0],[system],[task],', '修改任务', '', '/task/update', 2, 3, 0, '', 1, NULL);
INSERT INTO `t_sys_menu` VALUES (205, 'task_delete', 'task', '[0],[system],[task],', '删除任务', '', '/task/delete', 3, 3, 0, '', 1, NULL);
INSERT INTO `t_sys_menu` VALUES (206, 'biz', '0', '[0],', '业务管理', 'fa-shopping-cart', '#', 6, 1, 1, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (207, 'product', 'biz', '[0],[biz],', '产品管理', '', '/product', 1, 2, 1, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (208, 'account', 'biz', '[0],[biz],', '账户管理', '', '/account', 3, 2, 1, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (209, 'customer', 'biz', '[0],[biz],', '客户管理', '', '/customer', 2, 2, 1, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (210, 'loan', 'biz', '[0],[biz],', '贷款管理', '', '/loan', 4, 2, 1, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (212, 'txn', 'query', '[0],[biz],[query],', '交易流水查询', '', '/query/txn', 8, 3, 1, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (213, 'accountant', 'biz', '[0],[biz],', '账务处理', '', '/accountant', 7, 2, 1, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (214, 'query', 'biz', '[0],[biz],', '数据查询', '', '/query', 9, 2, 1, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (215, 'loan_account_serial', 'query', '[0],[biz],[query],', '贷款台账报表查询', '', '/query/loan_account_serial', 1, 3, 1, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (216, 'in_out_details', 'query', '[0],[biz],[query],', '收支明细报表查询', '', '/query/in_outDetails', 2, 3, 1, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (217, 'interest', 'query', '[0],[biz],[query],', '利润报表查询', '', '/query/interest', 3, 3, 1, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (218, 'mgr_setDept', 'mgr', '[0],[system],[mgr],', '分配部门', '', '/mgr/setDept', 11, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (219, 'mgr_depo_assign', 'mgr', '[0],[system],[mgr],', '分配部门跳转', '', '/mgr/depo_assign', 12, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (221, 'product_add', 'product', '[0],[biz],[product],', '添加产品', NULL, '/product/add', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (223, 'product_update', 'product', '[0],[biz],[product],', '修改产品', NULL, '/product/update', 2, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (224, 'product_delete', 'product', '[0],[biz],[product],', '删除产品', NULL, '/product/delete', 3, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (225, 'to_product_update', 'product', '[0],[biz],[product],', '修改产品跳转', '', '/product/product_update', 4, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (226, 'product_list', 'product', '[0],[biz],[product],', '产品列表', '', '/product/list', 5, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (227, 'product_detail', 'product', '[0],[biz],[product],', '产品详情', '', '/product/detail', 6, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (228, 'customer_add', 'customer', '[0],[biz],[customer],', '添加客户', NULL, '/customer/add', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (229, 'customer_update', 'customer', '[0],[biz],[customer],', '修改客户', NULL, '/customer/update', 2, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (230, 'customer_delete', 'customer', '[0],[biz],[customer],', '删除客户', '', '/customer/logic_delete', 3, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (231, 'to_customer_update', 'customer', '[0],[biz],[customer],', '修改客户跳转', '', '/customer/customer_update', 4, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (232, 'customer_list', 'customer', '[0],[biz],[customer],', '客户列表', '', '/customer/list', 5, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (233, 'customer_detail', 'customer', '[0],[biz],[customer],', '客户详情', '', '/customer/detail', 6, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (234, 'in_out', 'biz', '[0],[biz],', '收支管理', '', '/biz/in_out', 5, 2, 1, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (235, 'set_black_list', 'customer', '[0],[biz],[customer],', '设置黑名单', '', '/customer/set_black_list', 7, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (236, 'account_add', 'account', '[0],[biz],[account],', '添加账户', NULL, '/account/add', 1, 3, 0, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (237, 'account_update', 'account', '[0],[biz],[account],', '修改账户', '', '/account/update', 2, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (238, 'account_delete', 'account', '[0],[biz],[account],', '删除账户', NULL, '/account/logic_delete', 3, 3, 0, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (239, 'account_freeze', 'account', '[0],[biz],[account],', '冻结账户', NULL, '/account/freeze', 5, 3, 0, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (240, 'account_unfreeze', 'account', '[0],[biz],[account],', '解除冻结账户', NULL, '/account/unfreeze', 6, 3, 0, NULL, 1, 0);
INSERT INTO `t_sys_menu` VALUES (241, 'account_update', 'account', '[0],[biz],[account],', '编辑账户跳转', '', '/account/account_update', 9, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (242, 'account_list', 'account', '[0],[biz],[account],', '账户列表', '', '/account/list', 10, 3, 0, NULL, 1, NULL);
INSERT INTO `t_sys_menu` VALUES (243, 'cancel_black_list', 'customer', '[0],[biz],[customer],', '取消黑名单', '', '/customer/cancel_black_list', 8, 3, 0, NULL, 1, NULL);

-- ----------------------------
-- Table structure for t_sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_notice`;
CREATE TABLE `t_sys_notice`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标题',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '内容',
  `createtime` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `creater` int(11) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通知表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_notice
-- ----------------------------
INSERT INTO `t_sys_notice` VALUES (1, '世界', 10, '欢迎使用小贷业务管理系统', '2017-01-11 08:53:20', 1);

-- ----------------------------
-- Table structure for t_sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_operation_log`;
CREATE TABLE `t_sys_operation_log`  (
  `id` int(65) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `logtype` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '日志类型',
  `logname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '日志名称',
  `userid` int(65) DEFAULT NULL COMMENT '用户id',
  `classname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '类名称',
  `method` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '方法名称',
  `createtime` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `succeed` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '是否成功',
  `message` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 369 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_operation_log
-- ----------------------------
INSERT INTO `t_sys_operation_log` VALUES (368, '业务日志', '清空业务日志', 1, 'com.zsy.loan.admin.modular.controller.system.LogController', 'delLog', '2019-01-24 21:29:56', '成功', '主键id=null');

-- ----------------------------
-- Table structure for t_sys_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_relation`;
CREATE TABLE `t_sys_relation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `menuid` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单id',
  `roleid` int(11) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1743 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_relation
-- ----------------------------
INSERT INTO `t_sys_relation` VALUES (304, '206', 2);
INSERT INTO `t_sys_relation` VALUES (305, '207', 2);
INSERT INTO `t_sys_relation` VALUES (306, '208', 2);
INSERT INTO `t_sys_relation` VALUES (307, '209', 2);
INSERT INTO `t_sys_relation` VALUES (308, '210', 2);
INSERT INTO `t_sys_relation` VALUES (310, '212', 2);
INSERT INTO `t_sys_relation` VALUES (311, '213', 2);
INSERT INTO `t_sys_relation` VALUES (312, '214', 2);
INSERT INTO `t_sys_relation` VALUES (313, '215', 2);
INSERT INTO `t_sys_relation` VALUES (314, '216', 2);
INSERT INTO `t_sys_relation` VALUES (315, '217', 2);
INSERT INTO `t_sys_relation` VALUES (1646, '105', 1);
INSERT INTO `t_sys_relation` VALUES (1647, '106', 1);
INSERT INTO `t_sys_relation` VALUES (1648, '107', 1);
INSERT INTO `t_sys_relation` VALUES (1649, '108', 1);
INSERT INTO `t_sys_relation` VALUES (1650, '109', 1);
INSERT INTO `t_sys_relation` VALUES (1651, '110', 1);
INSERT INTO `t_sys_relation` VALUES (1652, '111', 1);
INSERT INTO `t_sys_relation` VALUES (1653, '112', 1);
INSERT INTO `t_sys_relation` VALUES (1654, '113', 1);
INSERT INTO `t_sys_relation` VALUES (1655, '165', 1);
INSERT INTO `t_sys_relation` VALUES (1656, '166', 1);
INSERT INTO `t_sys_relation` VALUES (1657, '167', 1);
INSERT INTO `t_sys_relation` VALUES (1658, '218', 1);
INSERT INTO `t_sys_relation` VALUES (1659, '219', 1);
INSERT INTO `t_sys_relation` VALUES (1660, '114', 1);
INSERT INTO `t_sys_relation` VALUES (1661, '115', 1);
INSERT INTO `t_sys_relation` VALUES (1662, '116', 1);
INSERT INTO `t_sys_relation` VALUES (1663, '117', 1);
INSERT INTO `t_sys_relation` VALUES (1664, '118', 1);
INSERT INTO `t_sys_relation` VALUES (1665, '162', 1);
INSERT INTO `t_sys_relation` VALUES (1666, '163', 1);
INSERT INTO `t_sys_relation` VALUES (1667, '164', 1);
INSERT INTO `t_sys_relation` VALUES (1668, '119', 1);
INSERT INTO `t_sys_relation` VALUES (1669, '120', 1);
INSERT INTO `t_sys_relation` VALUES (1670, '121', 1);
INSERT INTO `t_sys_relation` VALUES (1671, '122', 1);
INSERT INTO `t_sys_relation` VALUES (1672, '150', 1);
INSERT INTO `t_sys_relation` VALUES (1673, '151', 1);
INSERT INTO `t_sys_relation` VALUES (1674, '128', 1);
INSERT INTO `t_sys_relation` VALUES (1675, '134', 1);
INSERT INTO `t_sys_relation` VALUES (1676, '158', 1);
INSERT INTO `t_sys_relation` VALUES (1677, '159', 1);
INSERT INTO `t_sys_relation` VALUES (1678, '130', 1);
INSERT INTO `t_sys_relation` VALUES (1679, '131', 1);
INSERT INTO `t_sys_relation` VALUES (1680, '135', 1);
INSERT INTO `t_sys_relation` VALUES (1681, '136', 1);
INSERT INTO `t_sys_relation` VALUES (1682, '137', 1);
INSERT INTO `t_sys_relation` VALUES (1683, '152', 1);
INSERT INTO `t_sys_relation` VALUES (1684, '153', 1);
INSERT INTO `t_sys_relation` VALUES (1685, '154', 1);
INSERT INTO `t_sys_relation` VALUES (1686, '132', 1);
INSERT INTO `t_sys_relation` VALUES (1687, '138', 1);
INSERT INTO `t_sys_relation` VALUES (1688, '139', 1);
INSERT INTO `t_sys_relation` VALUES (1689, '140', 1);
INSERT INTO `t_sys_relation` VALUES (1690, '155', 1);
INSERT INTO `t_sys_relation` VALUES (1691, '156', 1);
INSERT INTO `t_sys_relation` VALUES (1692, '157', 1);
INSERT INTO `t_sys_relation` VALUES (1693, '133', 1);
INSERT INTO `t_sys_relation` VALUES (1694, '160', 1);
INSERT INTO `t_sys_relation` VALUES (1695, '161', 1);
INSERT INTO `t_sys_relation` VALUES (1696, '141', 1);
INSERT INTO `t_sys_relation` VALUES (1697, '142', 1);
INSERT INTO `t_sys_relation` VALUES (1698, '143', 1);
INSERT INTO `t_sys_relation` VALUES (1699, '144', 1);
INSERT INTO `t_sys_relation` VALUES (1700, '202', 1);
INSERT INTO `t_sys_relation` VALUES (1701, '203', 1);
INSERT INTO `t_sys_relation` VALUES (1702, '204', 1);
INSERT INTO `t_sys_relation` VALUES (1703, '205', 1);
INSERT INTO `t_sys_relation` VALUES (1704, '145', 1);
INSERT INTO `t_sys_relation` VALUES (1705, '148', 1);
INSERT INTO `t_sys_relation` VALUES (1706, '149', 1);
INSERT INTO `t_sys_relation` VALUES (1707, '199', 1);
INSERT INTO `t_sys_relation` VALUES (1708, '200', 1);
INSERT INTO `t_sys_relation` VALUES (1709, '201', 1);
INSERT INTO `t_sys_relation` VALUES (1710, '206', 1);
INSERT INTO `t_sys_relation` VALUES (1711, '207', 1);
INSERT INTO `t_sys_relation` VALUES (1712, '221', 1);
INSERT INTO `t_sys_relation` VALUES (1713, '223', 1);
INSERT INTO `t_sys_relation` VALUES (1714, '224', 1);
INSERT INTO `t_sys_relation` VALUES (1715, '225', 1);
INSERT INTO `t_sys_relation` VALUES (1716, '226', 1);
INSERT INTO `t_sys_relation` VALUES (1717, '227', 1);
INSERT INTO `t_sys_relation` VALUES (1718, '208', 1);
INSERT INTO `t_sys_relation` VALUES (1719, '236', 1);
INSERT INTO `t_sys_relation` VALUES (1720, '237', 1);
INSERT INTO `t_sys_relation` VALUES (1721, '238', 1);
INSERT INTO `t_sys_relation` VALUES (1722, '239', 1);
INSERT INTO `t_sys_relation` VALUES (1723, '240', 1);
INSERT INTO `t_sys_relation` VALUES (1724, '241', 1);
INSERT INTO `t_sys_relation` VALUES (1725, '242', 1);
INSERT INTO `t_sys_relation` VALUES (1726, '209', 1);
INSERT INTO `t_sys_relation` VALUES (1727, '228', 1);
INSERT INTO `t_sys_relation` VALUES (1728, '229', 1);
INSERT INTO `t_sys_relation` VALUES (1729, '230', 1);
INSERT INTO `t_sys_relation` VALUES (1730, '231', 1);
INSERT INTO `t_sys_relation` VALUES (1731, '232', 1);
INSERT INTO `t_sys_relation` VALUES (1732, '233', 1);
INSERT INTO `t_sys_relation` VALUES (1733, '235', 1);
INSERT INTO `t_sys_relation` VALUES (1734, '243', 1);
INSERT INTO `t_sys_relation` VALUES (1735, '210', 1);
INSERT INTO `t_sys_relation` VALUES (1736, '213', 1);
INSERT INTO `t_sys_relation` VALUES (1737, '214', 1);
INSERT INTO `t_sys_relation` VALUES (1738, '212', 1);
INSERT INTO `t_sys_relation` VALUES (1739, '215', 1);
INSERT INTO `t_sys_relation` VALUES (1740, '216', 1);
INSERT INTO `t_sys_relation` VALUES (1741, '217', 1);
INSERT INTO `t_sys_relation` VALUES (1742, '234', 1);

-- ----------------------------
-- Table structure for t_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role`;
CREATE TABLE `t_sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `num` int(11) DEFAULT NULL COMMENT '序号',
  `pid` int(11) DEFAULT NULL COMMENT '父角色id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色名称',
  `deptid` int(11) DEFAULT NULL COMMENT '部门名称',
  `tips` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '提示',
  `version` int(11) DEFAULT NULL COMMENT '保留字段(暂时没用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_role
-- ----------------------------
INSERT INTO `t_sys_role` VALUES (1, 1, 0, '超级管理员', NULL, 'administrator', NULL);
INSERT INTO `t_sys_role` VALUES (2, 1, 0, '操作员', 24, '操作员', NULL);

-- ----------------------------
-- Table structure for t_sys_task
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_task`;
CREATE TABLE `t_sys_task`  (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '任务名',
  `job_group` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '任务组',
  `job_class` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '执行类',
  `note` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '任务说明',
  `cron` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '定时规则',
  `data` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '执行参数',
  `exec_at` datetime(0) DEFAULT NULL COMMENT '执行时间',
  `exec_result` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '执行结果',
  `disabled` tinyint(1) DEFAULT NULL COMMENT '是否禁用',
  `createtime` datetime(0) DEFAULT NULL,
  `creator` bigint(20) DEFAULT NULL,
  `concurrent` tinyint(4) DEFAULT 0 COMMENT '是否允许并发',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_task
-- ----------------------------
INSERT INTO `t_sys_task` VALUES (1, '测试job', 'default', 'HelloJob', '测试job\n            \n            \n            \n            ', '0 7 11 * * ?', '{\n\"appname\": \"loan-lite\",\n\"version\":1\n}\n            \n            \n            \n            ', '2019-01-02 11:06:00', '执行成功', 1, '2018-12-28 09:54:00', 1, 0);

-- ----------------------------
-- Table structure for t_sys_task_log
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_task_log`;
CREATE TABLE `t_sys_task_log`  (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '任务名',
  `exec_at` datetime(0) DEFAULT NULL COMMENT '执行时间',
  `exec_success` int(11) DEFAULT NULL COMMENT '执行结果（成功:1、失败:0)',
  `job_exception` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '抛出异常',
  `id_task` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_task_log
-- ----------------------------
INSERT INTO `t_sys_task_log` VALUES (1, '测试job', '2018-12-31 09:54:00', 1, NULL, 1);
INSERT INTO `t_sys_task_log` VALUES (2, '测试job', '2018-12-31 10:04:00', 1, NULL, 1);
INSERT INTO `t_sys_task_log` VALUES (3, '测试job', '2019-01-02 11:06:00', 1, NULL, 1);

-- ----------------------------
-- Table structure for t_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像',
  `account` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '账号',
  `password` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
  `salt` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'md5密码盐',
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '名字',
  `birthday` datetime(0) DEFAULT NULL COMMENT '生日',
  `sex` int(11) DEFAULT NULL COMMENT '性别（1：男 2：女）',
  `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电话',
  `roleid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色id',
  `deptid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '部门id',
  `status` int(11) DEFAULT NULL COMMENT '状态(1：启用  2：冻结  3：删除）',
  `create_at` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `operator` int(20) DEFAULT NULL COMMENT '操作员',
  `remark` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT NULL COMMENT '保留字段',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_user
-- ----------------------------
INSERT INTO `t_sys_user` VALUES (1, NULL, 'admin', '6ab1f386d715cfb6be85de941d438b02', '8pgby', '管理员', '2017-05-05 00:00:00', 2, '', '', '1', '0', 1, '2016-01-29 08:49:53', NULL, NULL, NULL, 25);
INSERT INTO `t_sys_user` VALUES (47, NULL, 'gaoyanpeng', '153a71ac3b83dda573ff3b967be0c262', '7zjbn', '高彦鹏', NULL, 1, '', '', '2', NULL, 1, '2019-01-07 14:24:02', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user_role`;
CREATE TABLE `t_sys_user_role`  (
  `roleId` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `userId` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_user_role
-- ----------------------------
INSERT INTO `t_sys_user_role` VALUES ('c4de3cf1a62e41378d9f1205293485a3', '43e6c8d6d3134e5aa41ae2a85b87586b', 1);

-- ----------------------------
-- Table structure for t_sys_user_unit
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user_unit`;
CREATE TABLE `t_sys_user_unit`  (
  `userId` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `unitId` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_user_unit
-- ----------------------------
INSERT INTO `t_sys_user_unit` VALUES ('43e6c8d6d3134e5aa41ae2a85b87586b', 'cff0e38c05544085b56dee30e97383b4', 1);

-- ----------------------------
-- Table structure for tb_sys_status
-- ----------------------------
DROP TABLE IF EXISTS `tb_sys_status`;
CREATE TABLE `tb_sys_status`  (
  `id` bigint(20) NOT NULL,
  `acct_date` date NOT NULL COMMENT '系统时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_sys_status
-- ----------------------------
INSERT INTO `tb_sys_status` VALUES (2019, '2019-01-23');

-- ----------------------------
-- Function structure for currval
-- ----------------------------
DROP FUNCTION IF EXISTS `currval`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `currval`(seq_name VARCHAR(50)) RETURNS bigint(20)
BEGIN
     DECLARE value BIGINT;
     SELECT current_value INTO value
     FROM sequence
     WHERE upper(name) = upper(seq_name); 
     RETURN value;
END
;;
delimiter ;

-- ----------------------------
-- Function structure for nextval
-- ----------------------------
DROP FUNCTION IF EXISTS `nextval`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `nextval`(seq_name VARCHAR(50)) RETURNS bigint(20)
BEGIN
     DECLARE value BIGINT;
     UPDATE sequence
     SET current_value = current_value + increment 
     WHERE upper(name) = upper(seq_name);
     RETURN currval(seq_name); 
END
;;
delimiter ;

-- ----------------------------
-- Function structure for setval
-- ----------------------------
DROP FUNCTION IF EXISTS `setval`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `setval`(seq_name VARCHAR(50), value BIGINT) RETURNS bigint(20)
BEGIN
     UPDATE sequence
     SET current_value = value 
     WHERE upper(name) = upper(seq_name); 
     RETURN currval(seq_name); 
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
