/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : auction3

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-10-20 16:46:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_address
-- ----------------------------
DROP TABLE IF EXISTS `t_address`;
CREATE TABLE `t_address` (
  `id` bigint(20) NOT NULL,
  `acquiescence` bit(1) DEFAULT NULL,
  `address` varchar(255) NOT NULL,
  `alipay` varchar(255) DEFAULT NULL,
  `detailed_address` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `penguin` varchar(12) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_address
-- ----------------------------

-- ----------------------------
-- Table structure for t_bidders
-- ----------------------------
DROP TABLE IF EXISTS `t_bidders`;
CREATE TABLE `t_bidders` (
  `id` bigint(20) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `avatar` bigint(20) DEFAULT NULL,
  `itemId` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `sealedId` bigint(20) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_bidders
-- ----------------------------

-- ----------------------------
-- Table structure for t_carousel
-- ----------------------------
DROP TABLE IF EXISTS `t_carousel`;
CREATE TABLE `t_carousel` (
  `id` bigint(20) NOT NULL,
  `date` datetime DEFAULT NULL,
  `disable` bit(1) DEFAULT NULL,
  `pictureId` bigint(20) NOT NULL,
  `sort` int(11) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_carousel
-- ----------------------------

-- ----------------------------
-- Table structure for t_coin
-- ----------------------------
DROP TABLE IF EXISTS `t_coin`;
CREATE TABLE `t_coin` (
  `id` bigint(20) NOT NULL,
  `coinType` int(11) NOT NULL,
  `number` int(11) NOT NULL,
  `place` bigint(20) DEFAULT NULL,
  `promotion` bit(1) DEFAULT NULL,
  `reason` varchar(1024) NOT NULL,
  `source` int(11) NOT NULL,
  `time` datetime DEFAULT NULL,
  `userId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_coin
-- ----------------------------

-- ----------------------------
-- Table structure for t_collection
-- ----------------------------
DROP TABLE IF EXISTS `t_collection`;
CREATE TABLE `t_collection` (
  `id` bigint(20) NOT NULL,
  `itemId` bigint(20) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_collection
-- ----------------------------

-- ----------------------------
-- Table structure for t_config
-- ----------------------------
DROP TABLE IF EXISTS `t_config`;
CREATE TABLE `t_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `keyId` int(11) NOT NULL,
  `message` varchar(255) DEFAULT NULL,
  `value` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_kdge3nqk08nk6g5pf4nn9562i` (`keyId`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_config
-- ----------------------------

-- ----------------------------
-- Table structure for t_daily_statistics
-- ----------------------------
DROP TABLE IF EXISTS `t_daily_statistics`;
CREATE TABLE `t_daily_statistics` (
  `id` bigint(20) NOT NULL,
  `alwaysActive` int(11) DEFAULT NULL,
  `androidActive` int(11) DEFAULT NULL,
  `channelUsers` int(11) DEFAULT NULL,
  `coinConsumption` int(11) DEFAULT NULL,
  `deliveryAmount` int(11) DEFAULT NULL,
  `giftConsumption` int(11) DEFAULT NULL,
  `iosActive` int(11) DEFAULT NULL,
  `naturalFlow` int(11) DEFAULT NULL,
  `newUserRecharge` int(11) DEFAULT NULL,
  `newUsers` int(11) DEFAULT NULL,
  `oldUserRecharge` int(11) DEFAULT NULL,
  `paymentAmount` int(11) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `totalConsumption` int(11) DEFAULT NULL,
  `totalRecharge` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_daily_statistics
-- ----------------------------

-- ----------------------------
-- Table structure for t_function
-- ----------------------------
DROP TABLE IF EXISTS `t_function`;
CREATE TABLE `t_function` (
  `id` bigint(20) NOT NULL,
  `createBy` bigint(20) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `lastModifyBy` bigint(20) DEFAULT NULL,
  `lastModifyTime` datetime DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `disable` bit(1) DEFAULT b'0',
  `identification` varchar(255) NOT NULL,
  `requestMethod` varchar(255) NOT NULL,
  `urlStr` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_kxq21kg714rluijvvyglk1g5b` (`urlStr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_function
-- ----------------------------
INSERT INTO `t_function` VALUES ('832918254224670700', '1', '2017-07-27 09:42:10', '1', '2017-07-27 09:42:14', '添加单个权限', '\0', '添加单个权限', 'POST', 'post/authority/authority');
INSERT INTO `t_function` VALUES ('889866614273474600', '1', '2017-07-25 23:15:08', '1', '2017-07-25 23:15:08', '分页获取权限列表', '\0', '获取权限列表', 'GET', 'get/authority/authorities');
INSERT INTO `t_function` VALUES ('889870773840248800', '1', '2017-07-25 23:31:40', '1', '2017-07-26 00:51:17', '启用/禁用权限', '\0', '启用/禁用权限', 'PUT', 'put/authority/authority/state');
INSERT INTO `t_function` VALUES ('889871694737440800', '1', '2017-07-25 23:35:20', '1', '2017-07-25 23:35:20', '获取权限详情信息', '\0', '获取权限详情信息', 'GET', 'get/authority/authority');
INSERT INTO `t_function` VALUES ('889882734883766300', '1', '2017-07-26 00:19:12', '1', '2017-07-26 00:19:12', '分页获取角色列表', '\0', '获取角色列表', 'GET', 'get/authority/roles');
INSERT INTO `t_function` VALUES ('889889290459283500', '1', '2017-07-26 00:45:15', '1', '2017-07-26 00:52:36', '启用/禁用角色', '\0', '启用/禁用角色', 'PUT', 'put/authority/role/start');
INSERT INTO `t_function` VALUES ('889890195325845500', '1', '2017-07-26 00:48:51', '1', '2017-07-26 00:48:51', '修改权限信息', '\0', '修改权限信息', 'PUT', 'put/authority/authority');
INSERT INTO `t_function` VALUES ('890169348319084500', '1', '2017-07-26 19:18:06', '1', '2017-07-26 19:18:06', '获取角色详情', '\0', '获取角色详情', 'GET', 'get/authority/role');
INSERT INTO `t_function` VALUES ('890238532398874600', '1', '2017-07-26 23:53:01', '1', '2017-07-26 23:56:54', '分页获取管理员列表', '\0', '获取管理员列表', 'GET', 'get/authority/admins');
INSERT INTO `t_function` VALUES ('890238656210534400', '1', '2017-07-26 23:53:30', '1', '2017-07-26 23:57:02', '获取管理员详情', '\0', '获取管理员详情', 'GET', 'get/authority/admin');
INSERT INTO `t_function` VALUES ('890239008964083700', '1', '2017-07-26 23:54:54', '1', '2017-07-26 23:57:19', '启用/禁用管理员', '\0', '启用/禁用管理员', 'PUT', 'put/authority/admin/start');
INSERT INTO `t_function` VALUES ('890239210424893400', '1', '2017-07-26 23:55:42', '1', '2017-07-27 12:08:18', '重置管理员密码', '\0', '重置管理员密码', 'PUT', 'put/authority/admin/password');
INSERT INTO `t_function` VALUES ('890387310523187200', '1', '2017-07-27 09:44:12', '1', '2017-07-27 09:44:12', '添加单个角色', '\0', '添加单个角色', 'POST', 'post/authority/role');
INSERT INTO `t_function` VALUES ('890387575770972200', '1', '2017-07-27 09:45:15', '1', '2017-07-27 09:45:15', '修改角色', '\0', '修改角色', 'PUT', 'put/authority/role');
INSERT INTO `t_function` VALUES ('890420069635457000', '1', '2017-07-27 11:54:22', '1', '2017-07-27 11:58:14', '删除一个权限', '\0', '删除一个权限', 'DELETE', 'delete/authority/authority');
INSERT INTO `t_function` VALUES ('890420241593532400', '1', '2017-07-27 11:55:03', '1', '2017-07-27 11:55:03', '删除某一个角色', '\0', '删除某一个角色', 'DELETE', 'delete/authority/role');
INSERT INTO `t_function` VALUES ('890420681529884700', '1', '2017-07-27 11:56:48', '1', '2017-07-27 11:56:48', '添加一个新管理员', '\0', '添加一个新管理员', 'GET', 'post/authority/admin');
INSERT INTO `t_function` VALUES ('890423844769103900', '1', '2017-07-27 12:09:23', '1', '2017-07-27 12:09:23', '删除管理员', '\0', '删除管理员', 'DELETE', 'delete/authority/admin');
INSERT INTO `t_function` VALUES ('890424125401595900', '1', '2017-07-27 12:10:29', '1', '2017-07-27 12:10:29', '修改管理员(只能修改权限，用户名，手机号)', '\0', '修改管理员(只能修改权限，用户名，手机号)', 'PUT', 'put/authority/admin');
INSERT INTO `t_function` VALUES ('890424240837230600', '1', '2017-07-27 12:10:57', '1', '2017-07-27 12:10:57', '根据条件查询操作记录', '\0', '根据条件查询操作记录', 'GET', 'get/operationrecord/operationrecord');
INSERT INTO `t_function` VALUES ('890424323322413000', '1', '2017-07-27 12:11:17', '1', '2017-07-27 12:11:17', '批量删除操作记录', '\0', '批量删除操作记录', 'DELETE', 'delete/operationrecord/operationrecord');
INSERT INTO `t_function` VALUES ('890460437689139200', '1', '2017-07-27 12:11:18', '1', '2017-07-27 12:11:27', '获取系统用户分类', '\0', '获取系统用户分类', 'GET', 'get/authority/type');
INSERT INTO `t_function` VALUES ('894851685208293400', '1', '2017-08-08 17:24:02', '1', '2017-08-08 17:24:02', '添加轮播图', '\0', '添加轮播图', 'POST', 'post/admin/carousel');
INSERT INTO `t_function` VALUES ('894851803902902300', '1', '2017-08-08 17:24:30', '1', '2017-08-08 17:24:30', '修改轮播图', '\0', '修改轮播图', 'PUT', 'put/admin/carousel');
INSERT INTO `t_function` VALUES ('894851930105315300', '1', '2017-08-08 17:25:00', '1', '2017-08-08 17:25:00', '修改轮播图状态', '\0', '修改轮播图状态', 'PUT', 'put/admin/carousel/start');
INSERT INTO `t_function` VALUES ('894852040855912400', '1', '2017-08-08 17:25:27', '895493697515290600', '2017-08-19 16:17:50', '删除轮播图', '\0', '删除轮播图', 'DELETE', 'delete/admin/carousel');
INSERT INTO `t_function` VALUES ('894852177258872800', '1', '2017-08-08 17:25:59', '1', '2017-08-08 17:25:59', '分页查询轮播图', '\0', '分页查询轮播图', 'GET', 'get/admin/carousel');
INSERT INTO `t_function` VALUES ('894852348822683600', '1', '2017-08-08 17:26:40', '1', '2017-08-08 17:26:40', '添加商品', '\0', '添加商品', 'POST', 'post/admin/item');
INSERT INTO `t_function` VALUES ('894852506666926100', '1', '2017-08-08 17:27:18', '1', '2017-08-08 17:27:18', '条件查询所有商品', '\0', '条件查询所有商品', 'GET', 'get/admin/items');
INSERT INTO `t_function` VALUES ('894852640607830000', '1', '2017-08-08 17:27:50', '1', '2017-08-08 17:27:50', '根据商品id查询商品', '\0', '根据商品id查询商品', 'GET', 'get/admin/item');
INSERT INTO `t_function` VALUES ('894852769800781800', '1', '2017-08-08 17:28:21', '1', '2017-08-08 17:28:21', '获取商品运行分类', '\0', '获取商品运行分类', 'GET', 'get/admin/running/program/type');
INSERT INTO `t_function` VALUES ('894853048843632600', '1', '2017-08-08 17:29:27', '1', '2017-08-08 17:29:27', '删除商品', '\0', '删除商品', 'DELETE', 'delete/admin/item');
INSERT INTO `t_function` VALUES ('894853277177348100', '1', '2017-08-08 17:30:22', '1', '2017-08-08 17:30:22', '修改商品', '\0', '修改商品', 'PUT', 'put/admin/item');
INSERT INTO `t_function` VALUES ('894877313957625900', '1', '2017-08-08 19:05:52', '1', '2017-08-08 19:05:52', '前台获取分页商品', '\0', '前台获取分页商品', 'GET', 'get/client/items');
INSERT INTO `t_function` VALUES ('895551328791560200', '1', '2017-08-10 15:44:10', '1', '2017-08-10 15:44:10', '前端竞价拍卖', '\0', '前端竞价拍卖', 'POST', 'get/client/shot');
INSERT INTO `t_function` VALUES ('897638581424619500', '1', '2017-08-16 09:58:10', '1', '2017-08-16 09:58:10', '更新商品禁用状态', '\0', '更新商品禁用状态', 'PUT', 'put/admin/item/state');
INSERT INTO `t_function` VALUES ('897639254010626000', '1', '2017-08-16 10:00:50', '1', '2017-08-16 10:00:50', '查询多个分类', '\0', '查询多个分类', 'GET', 'get/admin/labels');
INSERT INTO `t_function` VALUES ('897639353335939100', '1', '2017-08-16 10:01:14', '1', '2017-08-16 10:01:14', '查询单个分类(会返回分类所属Item)', '\0', '查询单个分类(会返回分类所属Item)', 'GET', 'get/admin/label');
INSERT INTO `t_function` VALUES ('897639509657649200', '1', '2017-08-16 10:01:51', '1', '2017-08-16 10:01:51', '添加一个分类', '\0', '添加一个分类', 'POST', 'post/admin/label');
INSERT INTO `t_function` VALUES ('897639643166539800', '1', '2017-08-16 10:02:23', '1', '2017-08-16 10:02:23', '修改一个分类', '\0', '修改一个分类', 'PUT', 'put/admin/label');
INSERT INTO `t_function` VALUES ('897639814638075900', '1', '2017-08-16 10:03:04', '1', '2017-08-16 10:03:04', '删除一个分类', '\0', '删除一个分类', 'DELETE', 'delete/admin/label');
INSERT INTO `t_function` VALUES ('897639934129602600', '1', '2017-08-16 10:03:32', '1', '2017-08-16 10:07:57', '更新分类禁用状态', '\0', '更新分类禁用状态', 'PUT', 'put/admin/label/state');
INSERT INTO `t_function` VALUES ('897641902763933700', '1', '2017-08-16 10:11:22', '1', '2017-08-16 10:11:22', '查询所有常见帮助', '\0', '查询所有常见帮助', 'GET', 'get/admin/questions');
INSERT INTO `t_function` VALUES ('897641981314859000', '1', '2017-08-16 10:11:40', '1', '2017-08-16 10:11:40', '查询单个常见帮助', '\0', '查询单个常见帮助', 'GET', 'get/admin/question');
INSERT INTO `t_function` VALUES ('897642084184358900', '1', '2017-08-16 10:12:05', '1', '2017-08-16 10:12:05', '修改常见帮助', '\0', '修改常见帮助', 'PUT', 'put/admin/question');
INSERT INTO `t_function` VALUES ('897642205114531800', '1', '2017-08-16 10:12:34', '1', '2017-08-16 10:12:34', '删除一个常见帮助', '\0', '删除一个常见帮助', 'DELETE', 'delete/admin/question');
INSERT INTO `t_function` VALUES ('897642301621272600', '1', '2017-08-16 10:12:57', '1', '2017-08-16 10:12:57', '添加一个常见帮助', '\0', '添加一个常见帮助', 'POST', 'post/admin/question');
INSERT INTO `t_function` VALUES ('897642381690536000', '1', '2017-08-16 10:13:16', '1', '2017-08-16 10:13:16', '更新帮助禁用状态', '\0', '更新帮助禁用状态', 'PUT', 'put/admin/question/state');
INSERT INTO `t_function` VALUES ('897642585290440700', '1', '2017-08-16 10:14:04', '1', '2017-08-16 10:14:04', '获取所有封存记录', '\0', '获取所有封存记录', 'GET', 'get/admin/seals');
INSERT INTO `t_function` VALUES ('897642660288790500', '1', '2017-08-16 10:14:22', '1', '2017-08-16 10:14:22', '查询某一封存记录详情', '\0', '查询某一封存记录详情', 'GET', 'get/admin/seal');
INSERT INTO `t_function` VALUES ('897642756359323600', '1', '2017-08-16 10:14:45', '1', '2017-08-16 10:14:45', '获取出价记录', '\0', '获取出价记录', 'GET', 'get/admin/bidders');
INSERT INTO `t_function` VALUES ('897643034424901600', '1', '2017-08-16 10:15:51', '1', '2017-08-16 10:15:51', '根据条件查询所有商品分类', '\0', '根据条件查询所有商品分类', 'GET', 'get/admin/sorts');
INSERT INTO `t_function` VALUES ('897643094164373500', '1', '2017-08-16 10:16:06', '1', '2017-08-16 10:16:06', '查询单个分类(会返回分类所属Item)', '\0', '查询单个分类(会返回分类所属Item)', 'GET', 'get/admin/sort');
INSERT INTO `t_function` VALUES ('897643651516072000', '1', '2017-08-16 10:18:19', '1', '2017-08-16 10:18:19', '添加一个商品分类', '\0', '添加一个商品分类', 'POST', 'post/admin/sort');
INSERT INTO `t_function` VALUES ('897643747955703800', '1', '2017-08-16 10:18:42', '1', '2017-08-16 10:18:42', '修改商品分类', '\0', '修改商品分类', 'PUT', 'put/admin/sort');
INSERT INTO `t_function` VALUES ('897643853182402600', '1', '2017-08-16 10:19:07', '1', '2017-08-16 10:19:07', '删除一个商品分类', '\0', '删除一个商品分类', 'DELETE', 'delete/admin/sort');
INSERT INTO `t_function` VALUES ('897645115663712300', '1', '2017-08-16 10:24:08', '1', '2017-08-16 10:24:08', '更新商品分类禁用状态', '\0', '更新商品分类禁用状态', 'PUT', 'put/admin/sort/state');
INSERT INTO `t_function` VALUES ('897645805538639900', '1', '2017-08-16 10:26:52', '1', '2017-08-16 10:26:52', '分页查询所有用户', '\0', '分页查询所有用户', 'GET', 'get/admin/users');
INSERT INTO `t_function` VALUES ('897646005141372900', '1', '2017-08-16 10:27:40', '1', '2017-08-16 10:27:40', '管理端登录接口', '\0', '管理端登录接口', 'POST', 'post/admin/login');
INSERT INTO `t_function` VALUES ('897646121365536800', '1', '2017-08-16 10:28:07', '1', '2017-08-16 10:28:07', '修改用户密码(修改自己的密码)', '\0', '修改用户密码(修改自己的密码)', 'PUT', 'put/admin/password');
INSERT INTO `t_function` VALUES ('897646477914931200', '1', '2017-08-16 10:29:32', '1', '2017-08-16 10:29:32', '根据条件查询中奖人', '\0', '根据条件查询中奖人', 'GET', 'get/admin/orders');
INSERT INTO `t_function` VALUES ('897646544956686300', '1', '2017-08-16 10:29:48', '1', '2017-08-16 10:29:48', '查询某订单详情', '\0', '查询某订单详情', 'GET', 'get/admin/order');
INSERT INTO `t_function` VALUES ('897646649013174300', '1', '2017-08-16 10:30:13', '1', '2017-08-16 10:30:13', '查找发货地址', '\0', '查找发货地址', 'GET', 'get/admin//order/address');
INSERT INTO `t_function` VALUES ('897646790881312800', '1', '2017-08-16 10:30:47', '1', '2017-08-16 10:30:47', '添加订单发货详情', '\0', '添加订单发货详情', 'PUT', 'put/admin/order');
INSERT INTO `t_function` VALUES ('897646919772274700', '1', '2017-08-16 10:31:18', '1', '2017-08-16 10:31:27', '发送订单系统通知', '\0', '发送订单系统通知', 'POST', 'post/admin/order/notice');
INSERT INTO `t_function` VALUES ('898822725030641700', '1', '2017-08-19 16:23:32', '1', '2017-08-19 16:23:32', '图片上传接口', '\0', '图片上传接口', 'POST', 'post//file/uploadImg');
INSERT INTO `t_function` VALUES ('898822839052795900', '1', '2017-08-19 16:23:59', '1', '2017-08-19 16:23:59', '上传单个文件', '\0', '上传单个文件', 'POST', 'post/file/upload');
INSERT INTO `t_function` VALUES ('898822950579339300', '1', '2017-08-19 16:24:25', '1', '2017-08-19 16:24:25', '下载文件', '\0', '下载文件', 'GET', 'get/file/download');
INSERT INTO `t_function` VALUES ('898823022863974400', '1', '2017-08-19 16:24:43', '1', '2017-08-19 16:24:43', '查看文件', '\0', '查看文件', 'GET', 'get/file/see');
INSERT INTO `t_function` VALUES ('903084460616974300', '1', '2017-08-31 10:38:09', '1', '2017-08-31 10:38:09', '前端分页查询轮播图', '\0', '前端分页查询轮播图', 'GET', 'get/client/carouselFigures');
INSERT INTO `t_function` VALUES ('903084636094070800', '1', '2017-08-31 10:38:50', '1', '2017-08-31 10:38:50', '前台查询最新成交', '\0', '前台查询最新成交', 'GET', 'get/client/transactions');
INSERT INTO `t_function` VALUES ('903084729438306300', '1', '2017-08-31 10:39:13', '1', '2017-08-31 10:39:13', '根据最新成交记录封存id查找该期记录', '\0', '根据最新成交记录封存id查找该期记录', 'GET', 'get/client/transaction');
INSERT INTO `t_function` VALUES ('903085008204333000', '1', '2017-08-31 10:40:19', '1', '2017-08-31 10:40:19', '首页查询所有新手商品', '\0', '首页查询所有新手商品', 'GET', 'get/client//novice/items');
INSERT INTO `t_function` VALUES ('903085355383652400', '1', '2017-08-31 10:41:42', '1', '2017-08-31 10:41:42', '首页查询单个商品', '\0', '首页查询单个商品', 'GET', 'get/client/item');
INSERT INTO `t_function` VALUES ('903085441517879300', '1', '2017-08-31 10:42:02', '1', '2017-08-31 10:54:28', '用户对某一个商品进行竞拍操作', '\0', '用户对某一个商品进行竞拍操作', 'POST', 'post/client/shot');
INSERT INTO `t_function` VALUES ('903088530371117000', '1', '2017-08-31 10:54:19', '1', '2017-08-31 10:54:19', '查询多个分类', '\0', '查询多个分类', 'GET', 'get/client/labels');
INSERT INTO `t_function` VALUES ('903093963131453400', '1', '2017-08-31 11:15:54', '1', '2017-08-31 11:15:54', '分页查询用户公告通知信息', '\0', '分页查询用户公告通知信息', 'GET', 'get/client/notice/announcement/list');
INSERT INTO `t_function` VALUES ('903094079108153300', '1', '2017-08-31 11:16:22', '1', '2017-08-31 11:16:22', '分页查询用户的个人通知', '\0', '分页查询用户的个人通知', 'GET', 'get/client/notice/personal/list');
INSERT INTO `t_function` VALUES ('903094177040957400', '1', '2017-08-31 11:16:45', '1', '2017-08-31 11:16:45', '查看通知', '\0', '查看通知', 'GET', 'get/client/notice');
INSERT INTO `t_function` VALUES ('903094291973275600', '1', '2017-08-31 11:17:13', '1', '2017-08-31 11:17:13', '用户未读通知数', '\0', '用户未读通知数', 'GET', 'get/client/notice/hasUnread');
INSERT INTO `t_function` VALUES ('903094745545310200', '1', '2017-08-31 11:19:01', '1', '2017-08-31 11:19:01', '阅读通知', '\0', '阅读通知', 'POST', 'post/client/notice/read');
INSERT INTO `t_function` VALUES ('903094827262935000', '1', '2017-08-31 11:19:20', '1', '2017-08-31 11:19:20', '查询所有常见帮助', '\0', '查询所有常见帮助', 'GET', 'get/client/questions');
INSERT INTO `t_function` VALUES ('903094896758358000', '1', '2017-08-31 11:19:37', '1', '2017-08-31 11:19:37', '查询单个常见帮助', '\0', '查询单个常见帮助', 'GET', 'get/client/question');
INSERT INTO `t_function` VALUES ('903095797367701500', '1', '2017-08-31 11:23:11', '1', '2017-08-31 11:23:11', '查询常见帮助类型', '\0', '查询常见帮助类型', 'GET', 'get/client//question/state');
INSERT INTO `t_function` VALUES ('903096047067201500', '1', '2017-08-31 11:24:11', '1', '2017-08-31 11:24:11', '前台查询晒单', '\0', '前台查询晒单', 'GET', 'get/client/show');
INSERT INTO `t_function` VALUES ('903096107934941200', '1', '2017-08-31 11:24:26', '1', '2017-08-31 11:24:26', '用户添加晒单', '\0', '用户添加晒单', 'GET', 'post/client/show');
INSERT INTO `t_function` VALUES ('903096219188854800', '1', '2017-08-31 11:24:52', '1', '2017-08-31 11:24:52', '根据条件查询所有商品分类', '\0', '根据条件查询所有商品分类', 'GET', 'get/client/sorts');
INSERT INTO `t_function` VALUES ('903096297743974400', '1', '2017-08-31 11:25:11', '1', '2017-08-31 11:25:11', '查询单个分类(会返回分类所属Item)', '\0', '查询单个分类(会返回分类所属Item)', 'GET', 'get/client/sort');
INSERT INTO `t_function` VALUES ('903096389687312400', '1', '2017-08-31 11:25:33', '1', '2017-08-31 11:25:33', '获取系统睡眠时间', '\0', '获取系统睡眠时间', 'GET', 'get/client//system_time');
INSERT INTO `t_function` VALUES ('903096784748806100', '1', '2017-08-31 11:27:07', '1', '2017-08-31 11:27:07', '获取个人资料', '\0', '获取个人资料', 'GET', 'get/client/user');
INSERT INTO `t_function` VALUES ('903097131793907700', '1', '2017-08-31 11:28:30', '1', '2017-08-31 11:28:30', '前台登录', '\0', '前台登录', 'POST', 'post/client/login');
INSERT INTO `t_function` VALUES ('903097562284687400', '1', '2017-08-31 11:30:12', '1', '2017-08-31 11:30:12', '检测账号是否已经被注册', '\0', '检测账号是否已经被注册', 'GET', 'get/client/detection');
INSERT INTO `t_function` VALUES ('903097971858473000', '1', '2017-08-31 11:31:50', '1', '2017-08-31 11:31:50', '申请注册普通用户', '\0', '申请注册普通用户', 'POST', 'post/client/register');
INSERT INTO `t_function` VALUES ('903098059318100000', '1', '2017-08-31 11:32:11', '1', '2017-08-31 11:32:11', '修改用户密码(修改自己的密码)', '\0', '修改用户密码(修改自己的密码)', 'PUT', 'put/client/password');
INSERT INTO `t_function` VALUES ('903098293054079000', '1', '2017-08-31 11:33:06', '1', '2017-08-31 11:33:06', '个人竞拍记录', '\0', '个人竞拍记录', 'GET', 'get/client/auction/record');
INSERT INTO `t_function` VALUES ('903099946406772700', '1', '2017-08-31 11:39:41', '1', '2017-08-31 11:39:41', '为中奖订单添加选定收获地址', '\0', '为中奖订单添加选定收获地址', 'PUT', 'put/client/order/address');
INSERT INTO `t_function` VALUES ('903100044989694000', '1', '2017-08-31 11:40:04', '1', '2017-08-31 11:40:04', '添加一条地址信息', '\0', '添加一条地址信息', 'POST', 'post/user/address');
INSERT INTO `t_function` VALUES ('903100134894600200', '1', '2017-08-31 11:40:26', '1', '2017-08-31 11:40:26', '获取用户所有地址', '\0', '获取用户所有地址', 'GET', 'get/client/user/address');
INSERT INTO `t_function` VALUES ('903100211058966500', '1', '2017-08-31 11:40:44', '1', '2017-08-31 11:40:44', '删除一个地址', '\0', '删除一个地址', 'DELETE', 'delete/client/user/address');
INSERT INTO `t_function` VALUES ('903100718750105600', '1', '2017-08-31 11:42:45', '1', '2017-08-31 11:42:45', '修改一个地址', '\0', '修改一个地址', 'PUT', 'put/client/user/address');
INSERT INTO `t_function` VALUES ('903100803219193900', '1', '2017-08-31 11:43:05', '1', '2017-08-31 11:43:05', '查询用户收藏', '\0', '查询用户收藏', 'GET', 'get/client/user/collection');
INSERT INTO `t_function` VALUES ('903100936291876900', '1', '2017-08-31 11:43:37', '1', '2017-08-31 11:43:37', '添加一个收藏', '\0', '添加一个收藏', 'POST', 'post/client/user/collection');
INSERT INTO `t_function` VALUES ('903101067191910400', '1', '2017-08-31 11:44:08', '1', '2017-08-31 11:44:08', '删除一个收藏', '\0', '删除一个收藏', 'DELETE', 'delete/client/user/collection');
INSERT INTO `t_function` VALUES ('903101205134180400', '1', '2017-08-31 11:44:41', '1', '2017-08-31 11:44:41', '更新头像', '\0', '更新头像', 'PUT', 'put/client/user/avatar');
INSERT INTO `t_function` VALUES ('903101295433351200', '1', '2017-08-31 11:45:02', '1', '2017-08-31 11:45:02', '修改用户名', '\0', '修改用户名', 'PUT', 'put/client/user/name');
INSERT INTO `t_function` VALUES ('903101363037143000', '1', '2017-08-31 11:45:18', '1', '2017-08-31 11:45:18', '修改手机号', '\0', '修改手机号', 'PUT', 'put/client/user/phone');
INSERT INTO `t_function` VALUES ('903101528095588400', '1', '2017-08-31 11:45:58', '1', '2017-08-31 11:45:58', '推广注册普通用户', '\0', '推广注册普通用户', 'POST', 'post/client/promotion/register');
INSERT INTO `t_function` VALUES ('903101627513176000', '1', '2017-08-31 11:46:21', '1', '2017-08-31 11:46:21', '邀请有奖', '\0', '邀请有奖', 'GET', 'get/client/invite/reward');
INSERT INTO `t_function` VALUES ('903101836750225400', '1', '2017-08-31 11:47:11', '1', '2017-08-31 11:47:11', '前台获取所有转盘活动商品', '\0', '前台获取所有转盘活动商品', 'GET', 'get/activity/turntable');
INSERT INTO `t_function` VALUES ('903102001213079600', '1', '2017-08-31 11:47:51', '1', '2017-10-18 16:53:11', '用户对转盘进行抽奖', '\0', '用户对转盘进行抽奖', 'GET', 'get/activity/turntable/lottery');
INSERT INTO `t_function` VALUES ('903103109532745700', '1', '2017-08-31 11:52:15', '1', '2017-08-31 11:52:15', '获取大转盘活动时间', '\0', '获取大转盘活动时间', 'GET', 'get/activity/turntable/time');
INSERT INTO `t_function` VALUES ('903103278030520300', '1', '2017-08-31 11:52:55', '1', '2017-08-31 11:52:55', '管理员获取所有转盘活动商品', '\0', '管理员获取所有转盘活动商品', 'GET', 'get/activity/admin/turntable');
INSERT INTO `t_function` VALUES ('903103671087136800', '1', '2017-08-31 11:54:29', '1', '2017-10-18 16:48:31', '更新转盘活动商品', '\0', '更新转盘活动商品', 'PUT', 'put/activity/admin/turntable');
INSERT INTO `t_function` VALUES ('903103781518966800', '1', '2017-08-31 11:54:55', '1', '2017-10-18 16:48:45', '删除转盘活动商品', '\0', '删除转盘活动商品', 'DELETE', 'delete/activity/admin/turntable');
INSERT INTO `t_function` VALUES ('903103943314243600', '1', '2017-08-31 11:55:34', '1', '2017-10-18 16:49:01', '添加一个新的转盘抽奖商品', '\0', '添加一个新的转盘抽奖商品', 'POST', 'post/activity/admin/turntable');
INSERT INTO `t_function` VALUES ('903104247107682300', '1', '2017-08-31 11:56:46', '1', '2017-08-31 11:56:46', '设置大转盘活动时间（可进行修改操作）', '\0', '设置大转盘活动时间（可进行修改操作）', 'PUT', 'put/activity/admin/turntable/time');
INSERT INTO `t_function` VALUES ('903104473633652700', '1', '2017-08-31 11:57:40', '895493697515290600', '2017-09-04 09:36:23', '机器人后台服务是否启动中', '\0', '机器人后台服务是否启动中', 'GET', 'get/admin/robot/isBackgroundServiceRunning');
INSERT INTO `t_function` VALUES ('903104620400738300', '1', '2017-08-31 11:58:15', '1', '2017-08-31 11:58:15', '停止或启动后台服务', '\0', '停止或启动后台服务', 'POST', 'post/admin/robot/startOrStopBackgroundService');
INSERT INTO `t_function` VALUES ('903104740148117500', '1', '2017-08-31 11:58:44', '1', '2017-08-31 11:58:44', '分页查询机器人商品', '\0', '分页查询机器人商品', 'GET', 'get/admin/root/items');
INSERT INTO `t_function` VALUES ('903104848495378400', '1', '2017-08-31 11:59:09', '1', '2017-08-31 11:59:09', '修改机器人商品信息', '\0', '修改机器人商品信息', 'PUT', 'put/admin/root/item');
INSERT INTO `t_function` VALUES ('903104977684136000', '1', '2017-08-31 11:59:40', '1', '2017-08-31 11:59:40', '分页查看机器人未晒单中奖记录', '\0', '分页查看机器人未晒单中奖记录', 'GET', 'get/admin/robot/sealedBeforeShow');
INSERT INTO `t_function` VALUES ('903105103404204000', '1', '2017-08-31 12:00:10', '1', '2017-08-31 12:00:10', '添加晒单', '\0', '添加晒单', 'POST', 'post/admin/robot/showItem');
INSERT INTO `t_function` VALUES ('903105225210986500', '1', '2017-08-31 12:00:39', '1', '2017-08-31 12:00:39', '分页查询机器人用户', '\0', '分页查询机器人用户', 'GET', 'get/admin/robot/users');
INSERT INTO `t_function` VALUES ('903105398825812000', '1', '2017-08-31 12:01:21', '1', '2017-08-31 12:01:21', '查找用户信息', '\0', '查找用户信息', 'GET', 'get/admin/robot/user');
INSERT INTO `t_function` VALUES ('903105490546851800', '1', '2017-08-31 12:01:42', '1', '2017-08-31 12:01:42', '获取机器人用户头像列表', '\0', '获取机器人用户头像列表', 'GET', 'get/admin/robot/user/defaultHeadImgs');
INSERT INTO `t_function` VALUES ('903105677965131800', '1', '2017-08-31 12:02:27', '1', '2017-08-31 12:02:27', '添加单个用户', '\0', '添加单个用户', 'POST', 'post/admin/robot/user');
INSERT INTO `t_function` VALUES ('903105780499087400', '1', '2017-08-31 12:02:52', '1', '2017-08-31 12:02:52', '修改单个用户', '\0', '修改单个用户', 'PUT', 'put/admin/robot/user');
INSERT INTO `t_function` VALUES ('903105888636633100', '1', '2017-08-31 12:03:17', '1', '2017-08-31 12:03:17', '批量上传模板文件下载', '\0', '批量上传模板文件下载', 'GET', 'get/admin/robot/user/upload/tempFile');
INSERT INTO `t_function` VALUES ('903106039027597300', '1', '2017-08-31 12:03:53', '1', '2017-08-31 12:03:53', '批量上传机器人', '\0', '批量上传机器人', 'POST', 'post/admin/robot/user/upload');
INSERT INTO `t_function` VALUES ('903443760111681500', '1', '2017-09-01 10:25:52', '1', '2017-09-01 10:25:52', '设置商品前端展示', '\0', '设置商品前端展示', 'PUT', 'put/admin/item/show');
INSERT INTO `t_function` VALUES ('903443890630033400', '1', '2017-09-01 10:26:23', '1', '2017-09-06 18:20:17', '设置商品为新手商品', '\0', '设置商品为新手商品', 'PUT', 'put/admin/item/novice');
INSERT INTO `t_function` VALUES ('903447338993844200', '1', '2017-09-01 10:40:06', '1', '2017-09-01 10:40:06', '修改用户密码(修改会员的密码，进行密码重置)', '\0', '修改用户密码(修改会员的密码，进行密码重置)', 'PUT', 'put/admin/user/password');
INSERT INTO `t_function` VALUES ('903451755017142300', '1', '2017-09-01 10:57:38', '1', '2017-09-01 10:57:38', '赠送赠币', '\0', '赠送赠币', 'POST', 'post/admin/user/gift');
INSERT INTO `t_function` VALUES ('903857199342682100', '1', '2017-09-02 13:48:44', '1', '2017-09-02 13:50:04', '根据商品id查询商品活动历史记录', '\0', '根据商品id查询商品活动历史记录', 'GET', 'get/client/transaction/history');
INSERT INTO `t_function` VALUES ('904886985666068500', '1', '2017-09-05 10:00:44', '1', '2017-09-05 10:00:44', '发送短信(只能登陆等非修改操作)', '\0', '发送短信(只能登陆等非修改操作)', 'GET', 'get/sendSMS');
INSERT INTO `t_function` VALUES ('904887069237575700', '1', '2017-09-05 10:01:04', '1', '2017-09-05 10:01:04', '发送短信(修改操作)', '\0', '发送短信(修改操作)', 'POST', 'post/sendSMS');
INSERT INTO `t_function` VALUES ('905610664725184500', '1', '2017-09-07 09:56:23', '1', '2017-09-07 09:56:23', '查看用户个人收货地址', '\0', '查看用户个人收货地址', 'GET', 'get/admin/user/address');
INSERT INTO `t_function` VALUES ('905610948213997600', '1', '2017-09-07 09:57:30', '1', '2017-09-07 09:57:30', '修改用户密码(修改会员的密码，进行密码重置)', '\0', '修改用户密码(修改会员的密码，进行密码重置)', 'PUT', 'get/admin/user/password');
INSERT INTO `t_function` VALUES ('905611876895817700', '1', '2017-09-07 10:01:12', '1', '2017-09-07 10:01:12', '添加个人通知', '\0', '添加个人通知', 'POST', 'post/admin/notice');
INSERT INTO `t_function` VALUES ('905611996261515300', '1', '2017-09-07 10:01:40', '1', '2017-09-07 10:01:40', '分页查询用户的个人通知', '\0', '分页查询用户的个人通知', 'GET', 'get/admin/notice/personal/list');
INSERT INTO `t_function` VALUES ('905612141195690000', '1', '2017-09-07 10:02:15', '1', '2017-09-07 10:02:15', '添加公告通知', '\0', '添加公告通知', 'POST', 'post/admin/notice/announcement');
INSERT INTO `t_function` VALUES ('905612422247612400', '1', '2017-09-07 10:03:22', '1', '2017-09-07 10:03:22', '分页查看公告通知列表', '\0', '分页查看公告通知列表', 'GET', 'get/admin/notice/announcement/list');
INSERT INTO `t_function` VALUES ('908307139628367900', '1', '2017-09-14 20:31:12', '1', '2017-09-14 20:31:12', '每日签到', '\0', '每日签到', 'GET', 'get/activity/sign');
INSERT INTO `t_function` VALUES ('908307225410273300', '1', '2017-09-14 20:31:33', '1', '2017-09-14 20:31:33', '获取用户签到信息', '\0', '获取用户签到信息', 'GET', 'get/activity/sign/message');
INSERT INTO `t_function` VALUES ('908307387843084300', '1', '2017-09-14 20:32:11', '1', '2017-09-14 20:32:11', '领取签到固定奖品', '\0', '领取签到固定奖品', 'GET', 'get/activity/sign/prize');
INSERT INTO `t_function` VALUES ('908307525357535200', '1', '2017-09-14 20:32:44', '1', '2017-09-14 20:32:44', '积分兑换赠币', '\0', '积分兑换赠币', 'GET', 'get/activity/sign/exchange');
INSERT INTO `t_function` VALUES ('908307652675633200', '1', '2017-09-14 20:33:15', '1', '2017-09-14 20:33:15', '设置签到固定奖励', '\0', '设置签到固定奖励', 'POST', 'post/activity/sign/fixed_eward');
INSERT INTO `t_function` VALUES ('908574895330295800', '1', '2017-09-15 14:15:10', '1', '2017-09-15 14:15:10', '订单付款', '\0', '订单付款', 'POST', 'post/client/order/payment');
INSERT INTO `t_function` VALUES ('908575102528913400', '1', '2017-09-15 14:16:00', '1', '2017-09-15 14:16:00', '查看订单详情', '\0', '查看订单详情', 'GET', 'get/client/order');
INSERT INTO `t_function` VALUES ('908575242891296800', '1', '2017-09-15 14:16:33', '1', '2017-09-15 14:16:33', '设置订单状态为已签收', '\0', '设置订单状态为已签收', 'PUT', 'get/client/order/sign');
INSERT INTO `t_function` VALUES ('908575347241386000', '1', '2017-09-15 14:16:58', '1', '2017-09-15 14:16:58', '充值拍币下单', '\0', '充值拍币下单', 'POST', 'post/client/recharge');
INSERT INTO `t_function` VALUES ('909623051547574300', '1', '2017-09-18 11:40:10', '1', '2017-09-18 11:40:10', '修改用户状态', '\0', '修改用户状态', 'PUT', 'put/admin/user/start');
INSERT INTO `t_function` VALUES ('909623093838741500', '1', '2017-09-18 11:40:20', '1', '2017-09-18 11:40:20', '赠送积分', '\0', '赠送积分', 'POST', 'post/admin/user/integral');
INSERT INTO `t_function` VALUES ('909623135718867000', '1', '2017-09-18 11:40:30', '1', '2017-09-18 11:40:30', '赠送拍币', '\0', '赠送拍币', 'POST', 'post/admin/user/coin');
INSERT INTO `t_function` VALUES ('909625308619997200', '1', '2017-09-18 11:49:08', '1', '2017-09-18 11:49:08', '分页查看系统通知', '\0', '分页查看系统通知', 'GET', 'get/admin/system/notice');
INSERT INTO `t_function` VALUES ('909628459842535400', '1', '2017-09-18 12:01:40', '1', '2017-09-18 12:06:52', '删除个人通知', '\0', '删除通知', 'DELETE', 'delete/admin/notice');
INSERT INTO `t_function` VALUES ('909628721390944300', '1', '2017-09-18 12:02:42', '1', '2017-09-18 12:02:42', '发送个人通知', '\0', '发送个人通知', 'POST', 'post/adminnotice/send');
INSERT INTO `t_function` VALUES ('909628843793317900', '1', '2017-09-18 12:03:11', '1', '2017-09-18 12:06:43', '编辑个人通知', '\0', '编辑通知', 'PUT', 'put/admin/notice');
INSERT INTO `t_function` VALUES ('909629233909727200', '1', '2017-09-18 12:04:44', '1', '2017-09-18 12:04:44', '查找发货地址', '\0', '查找发货地址', 'GET', 'get/admin/order/address');
INSERT INTO `t_function` VALUES ('909713915665973200', '1', '2017-09-18 17:41:14', '1', '2017-09-18 17:41:14', '后台查询充值展示标志位', '\0', '后台查询充值展示标志位', 'GET', 'get/admin/setting/recharge');
INSERT INTO `t_function` VALUES ('909714028304007200', '1', '2017-09-18 17:41:41', '1', '2017-09-18 17:41:41', '设置充值展示标志位', '\0', '设置充值展示标志位', 'PUT', 'put/admin/setting/recharge');
INSERT INTO `t_function` VALUES ('909714315815157800', '1', '2017-09-18 17:42:49', '1', '2017-09-18 17:42:49', '前台查询充值展示标志位', '\0', '前台查询充值展示标志位', 'GET', 'get/client/setting/recharge');
INSERT INTO `t_function` VALUES ('913592284153380900', '1', '2017-09-29 10:32:29', '1', '2017-09-29 10:32:29', '查询用户是否收藏某商品', '\0', '查询用户是否收藏某商品', 'GET', 'get/client/is_collection');
INSERT INTO `t_function` VALUES ('913596659861028900', '1', '2017-09-29 10:49:52', '1', '2017-09-29 10:49:52', '查询注册赠送拍币数', '\0', '查询注册赠送拍币数', 'GET', 'get/admin/setting/gift_coin');
INSERT INTO `t_function` VALUES ('913596705524416500', '1', '2017-09-29 10:50:03', '1', '2017-09-29 10:50:08', '修改注册赠送拍币数', '\0', '修改注册赠送拍币数', 'PUT', 'put/admin/setting/gift_coin');
INSERT INTO `t_function` VALUES ('913608122164052000', '1', '2017-09-29 11:35:25', '1', '2017-09-29 11:35:25', '查看是否是超级管理员', '\0', '查看是否是超级管理员', 'GET', 'get/authority/superAdmin');
INSERT INTO `t_function` VALUES ('913609241653149700', '1', '2017-09-29 11:39:52', '1', '2017-09-29 11:39:52', '阅读系统通知', '\0', '阅读系统通知', 'POST', 'post/admin/system/notice/read');
INSERT INTO `t_function` VALUES ('913754687155470300', '1', '2017-09-29 21:17:49', '1', '2017-09-29 21:17:49', '修改用户为管理员', '\0', '修改用户为管理员', 'PUT', 'put/admin/user/type');
INSERT INTO `t_function` VALUES ('916141500654420000', '1', '2017-10-06 11:22:09', '1', '2017-10-06 11:22:09', '查看用户历史投票', '\0', '查看用户历史投票', 'GET', 'get/admin/user/sealed');
INSERT INTO `t_function` VALUES ('916141579138236400', '1', '2017-10-06 11:22:28', '1', '2017-10-06 11:22:28', '查看用户金币流动情况', '\0', '查看用户金币流动情况', 'GET', 'get/admin/user/coin_flow');
INSERT INTO `t_function` VALUES ('916141671119323100', '1', '2017-10-06 11:22:50', '1', '2017-10-06 11:22:50', '查看用户金币情况统计', '\0', '查看用户金币情况统计', 'GET', 'get/admin/user/coin_statistics');
INSERT INTO `t_function` VALUES ('916637872423960600', '1', '2017-10-07 20:14:34', '1', '2017-10-07 20:14:34', '查找充值用户', '\0', '查找充值用户', 'GET', 'get/admin/successful/recharge');
INSERT INTO `t_function` VALUES ('916637955760586800', '1', '2017-10-07 20:14:54', '1', '2017-10-07 20:14:54', '根据用户id查找流水状况', '\0', '根据用户id查找流水状况', 'GET', 'get/admin/recharge/record');
INSERT INTO `t_function` VALUES ('916640898589130800', '1', '2017-10-07 20:26:35', '1', '2017-10-07 20:26:35', '获取用户所有晒单', '\0', '获取用户所有晒单', 'GET', 'get/admin/show');
INSERT INTO `t_function` VALUES ('916641023130599400', '1', '2017-10-07 20:27:05', '1', '2017-10-07 20:27:05', '修改晒单是否首页显示', '\0', '修改晒单是否首页显示', 'PUT', 'put/admin/show');
INSERT INTO `t_function` VALUES ('916920569348227100', '1', '2017-10-08 14:57:54', '1', '2017-10-08 14:57:54', '设置执行支付方式', '\0', '设置执行支付方式', 'PUT', 'put/admin/setting/pay_type');
INSERT INTO `t_function` VALUES ('916920683492016100', '1', '2017-10-08 14:58:21', '1', '2017-10-08 14:58:21', '查询执行支付方式', '\0', '查询执行支付方式', 'GET', 'get/admin/pay/type');
INSERT INTO `t_function` VALUES ('916920760574935000', '1', '2017-10-08 14:58:40', '1', '2017-10-08 14:58:40', '查询现有所有支付方式', '\0', '查询现有所有支付方式', 'GET', 'get/admin/pay_type');
INSERT INTO `t_function` VALUES ('916920921736872000', '1', '2017-10-08 14:59:18', '1', '2017-10-08 14:59:18', '前端查询执行支付方式', '\0', '前端查询执行支付方式', 'GET', 'get/client/pay/type');
INSERT INTO `t_function` VALUES ('916998111992217600', '1', '2017-10-08 20:06:02', '1', '2017-10-08 20:06:02', '获取每个商品分配的机器人数目区间 例如: 1,5', '\0', '获取每个商品分配的机器人数目区间', 'GET', 'get/admin/robot/allocUserNumRange');
INSERT INTO `t_function` VALUES ('916998160788750300', '1', '2017-10-08 20:06:13', '1', '2017-10-08 20:06:13', '设置每个商品分配的机器人数目区间', '\0', '设置每个商品分配的机器人数目区间', 'POST', 'post/admin/robot/allocUserNumRange');
INSERT INTO `t_function` VALUES ('916998262118940700', '1', '2017-10-08 20:06:37', '1', '2017-10-08 20:06:37', '获取允许机器人竞拍的次数范围 例如: 1,5', '\0', '获取允许机器人竞拍的次数范围', 'GET', 'get/admin/robot/allowUserAuctionRange');
INSERT INTO `t_function` VALUES ('916998322974097400', '1', '2017-10-08 20:06:52', '1', '2017-10-08 20:06:52', '设置允许机器人竞拍的次数范围 例如: 1,5', '\0', '设置允许机器人竞拍的次数范围 例如: 1,5', 'POST', 'post/admin/robot/allowUserAuctionRange');
INSERT INTO `t_function` VALUES ('916998382541602800', '1', '2017-10-08 20:07:06', '1', '2017-10-08 20:07:06', '获取使用机器人运行方案5:根据用户拍币消耗数分配中奖时, 用户中奖的金额占消耗拍币的比率 (0, 1] 例如: 0.8', '\0', '获取使用机器人运行方案5:根据用户拍币消耗数分配中奖时, 用户中奖的金额占消耗拍币的比率 (0, 1] 例如: 0.8', 'GET', 'get/admin/robot/coinConsumeRate');
INSERT INTO `t_function` VALUES ('916998445397442600', '1', '2017-10-08 20:07:21', '1', '2017-10-08 20:07:21', '设置使用机器人运行方案5:根据用户拍币消耗数分配中奖时, 用户中奖的金额占消耗拍币的比率 (0, 1] 例如: 0.8', '\0', '设置使用机器人运行方案5:根据用户拍币消耗数分配中奖时, 用户中奖的金额占消耗拍币的比率 (0, 1] 例如: 0.8', 'POST', 'post/admin/robot/coinConsumeRate');
INSERT INTO `t_function` VALUES ('916998499646570500', '1', '2017-10-08 20:07:34', '1', '2017-10-08 20:07:59', '获取最小成交价', '\0', '获取最小成交价', 'GET', 'get/admin/robot/minMarketPrice');
INSERT INTO `t_function` VALUES ('916998569360097300', '1', '2017-10-08 20:07:51', '1', '2017-10-08 20:07:55', '设置最小成交价', '\0', '设置最小成交价', 'POST', 'post/admin/robot/minMarketPrice');
INSERT INTO `t_function` VALUES ('918056269640630272', '1', '2017-10-11 18:10:46', '1', '2017-10-11 18:10:46', '获取同时在线人数检测', '\0', '获取同时在线人数检测', 'GET', 'get/admin/online/number');
INSERT INTO `t_function` VALUES ('918386046880710656', '1', '2017-10-12 16:01:11', '1', '2017-10-12 16:01:11', '获取系统统计数据', '\0', '获取系统统计数据', 'GET', 'get/admin/home/daily_statistics');
INSERT INTO `t_function` VALUES ('918386128187293696', '1', '2017-10-12 16:01:30', '1', '2017-10-12 16:01:30', '用户盈亏统计', '\0', '用户盈亏统计', 'GET', 'get/admin/user/profit');
INSERT INTO `t_function` VALUES ('918386271561187328', '1', '2017-10-12 16:02:05', '1', '2017-10-12 16:02:05', '商品热度统计', '\0', '商品热度统计', 'GET', 'get/admin/item/heat');
INSERT INTO `t_function` VALUES ('919144159950929920', '1', '2017-10-14 18:13:39', '1', '2017-10-14 18:13:39', '强制删除商品', '\0', '强制删除商品', 'DELETE', 'delete/admin/forced/item');
INSERT INTO `t_function` VALUES ('920572646209355776', '1', '2017-10-18 16:49:57', '1', '2017-10-18 16:49:57', '查看转盘中奖信息', '\0', '查看转盘中奖信息', 'GET', 'get/activity/admin/turntable/winning/list');
INSERT INTO `t_function` VALUES ('920572831215910912', '1', '2017-10-18 16:50:41', '1', '2017-10-18 16:50:41', '转盘中奖商品发货', '\0', '转盘中奖商品发货', 'GET', 'get/activity/admin/turntable/winning/ship');
INSERT INTO `t_function` VALUES ('920573065539092480', '1', '2017-10-18 16:51:37', '1', '2017-10-18 16:51:37', '取消转盘中奖', '\0', '取消转盘中奖', 'GET', 'get/activity/admin/turntable/winning/cancel');
INSERT INTO `t_function` VALUES ('920573281638023168', '1', '2017-10-18 16:52:28', '1', '2017-10-18 16:52:28', '转盘中奖完成', '\0', '转盘中奖完成', 'GET', '/activity/admin/turntable/winning/finish');
INSERT INTO `t_function` VALUES ('920573732064329728', '1', '2017-10-18 16:54:16', '1', '2017-10-18 16:54:16', '查看自己大转盘中奖信息', '\0', '查看自己大转盘中奖信息', 'GET', 'get/activity/turntable/winning/list');
INSERT INTO `t_function` VALUES ('920573925455298560', '1', '2017-10-18 16:55:02', '1', '2017-10-18 16:55:02', '查看自己转盘中奖机会', '\0', '查看自己转盘中奖机会', 'GET', 'get/activity/turntable/winning/chances');
INSERT INTO `t_function` VALUES ('920574237591207936', '1', '2017-10-18 16:56:16', '1', '2017-10-18 16:56:16', '添加转盘收货地址', '\0', '添加转盘收货地址', 'POST', 'post/activity/turntable/winning/addAddress');
INSERT INTO `t_function` VALUES ('921266146735292416', '1', '2017-10-20 14:45:40', '1', '2017-10-20 14:45:40', '阅读系统通知', '\0', '阅读系统通知', 'POST', 'post/admin/notice/read');
INSERT INTO `t_function` VALUES ('921273846525329408', '1', '2017-10-20 15:16:16', '1', '2017-10-20 15:16:16', '用户未读公告通知数', '\0', '用户未读公告通知数', 'GET', 'get/client/notice/announcement/hasUnread');
INSERT INTO `t_function` VALUES ('921273927320207360', '1', '2017-10-20 15:16:35', '1', '2017-10-20 15:16:35', '用户未读个人通知数', '\0', '用户未读个人通知数', 'GET', 'get/client/notice/personal/hasUnread');

-- ----------------------------
-- Table structure for t_integral
-- ----------------------------
DROP TABLE IF EXISTS `t_integral`;
CREATE TABLE `t_integral` (
  `id` bigint(20) NOT NULL,
  `number` int(11) NOT NULL,
  `operationalType` int(11) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `userId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_integral
-- ----------------------------

-- ----------------------------
-- Table structure for t_item
-- ----------------------------
DROP TABLE IF EXISTS `t_item`;
CREATE TABLE `t_item` (
  `id` bigint(20) NOT NULL,
  `big_picture` varchar(255) DEFAULT NULL,
  `control_line` int(11) DEFAULT NULL,
  `cost` decimal(19,2) NOT NULL,
  `disable` bit(1) DEFAULT NULL,
  `fatherItemId` bigint(20) DEFAULT NULL,
  `front_show` bit(1) DEFAULT NULL,
  `in_kind` bit(1) DEFAULT NULL,
  `increase_the_price` varchar(255) DEFAULT NULL,
  `item_type` varchar(255) DEFAULT NULL,
  `label_str` varchar(255) DEFAULT NULL,
  `name` varchar(189) DEFAULT NULL,
  `novice` bit(1) DEFAULT NULL,
  `plus_code` int(11) NOT NULL,
  `price` decimal(19,2) NOT NULL,
  `purchaseAddress` varchar(255) DEFAULT NULL,
  `running_program` int(11) DEFAULT NULL,
  `small_picture` bigint(20) NOT NULL,
  `sort` int(11) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_item
-- ----------------------------

-- ----------------------------
-- Table structure for t_label
-- ----------------------------
DROP TABLE IF EXISTS `t_label`;
CREATE TABLE `t_label` (
  `id` bigint(20) NOT NULL,
  `disable` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `picture` bigint(20) DEFAULT NULL,
  `setting_url` varchar(255) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_label
-- ----------------------------

-- ----------------------------
-- Table structure for t_login_info
-- ----------------------------
DROP TABLE IF EXISTS `t_login_info`;
CREATE TABLE `t_login_info` (
  `id` bigint(20) NOT NULL,
  `createBy` bigint(20) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `lastModifyBy` bigint(20) DEFAULT NULL,
  `lastModifyTime` datetime DEFAULT NULL,
  `credential` varchar(255) DEFAULT NULL,
  `identifier` varchar(190) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  `voucherType` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK5h5laweuht04e0nq2bxl0jqx6` (`voucherType`,`identifier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_login_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_notice
-- ----------------------------
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice` (
  `id` bigint(20) NOT NULL,
  `context` text NOT NULL,
  `isSend` bit(1) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_notice
-- ----------------------------

-- ----------------------------
-- Table structure for t_notice_user
-- ----------------------------
DROP TABLE IF EXISTS `t_notice_user`;
CREATE TABLE `t_notice_user` (
  `id` bigint(20) NOT NULL,
  `noticeId` bigint(20) NOT NULL,
  `state` int(11) DEFAULT NULL,
  `userId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_notice_user
-- ----------------------------

-- ----------------------------
-- Table structure for t_operating_record
-- ----------------------------
DROP TABLE IF EXISTS `t_operating_record`;
CREATE TABLE `t_operating_record` (
  `id` bigint(20) NOT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `iphone` varchar(255) DEFAULT NULL,
  `message` text,
  `operationTime` datetime DEFAULT NULL,
  `userId` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_operating_record
-- ----------------------------

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` bigint(20) NOT NULL,
  `amounts` varchar(255) NOT NULL,
  `card` varchar(255) DEFAULT NULL,
  `density` varchar(255) DEFAULT NULL,
  `express_number` varchar(255) DEFAULT NULL,
  `item_id` bigint(20) NOT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `item_picture` varchar(255) DEFAULT NULL,
  `item_type` int(11) NOT NULL,
  `marketPrice` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `order_state` int(11) DEFAULT NULL,
  `paymentTime` datetime DEFAULT NULL,
  `price` varchar(255) NOT NULL,
  `receiptString` varchar(255) DEFAULT NULL,
  `receipt_id` bigint(20) DEFAULT NULL,
  `record_id` bigint(20) NOT NULL,
  `shipTime` datetime DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `userId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_order
-- ----------------------------

-- ----------------------------
-- Table structure for t_question
-- ----------------------------
DROP TABLE IF EXISTS `t_question`;
CREATE TABLE `t_question` (
  `id` bigint(20) NOT NULL,
  `context` text NOT NULL,
  `disable` bit(1) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_question
-- ----------------------------

-- ----------------------------
-- Table structure for t_robot_config
-- ----------------------------
DROP TABLE IF EXISTS `t_robot_config`;
CREATE TABLE `t_robot_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `message` varchar(255) DEFAULT NULL,
  `name` int(11) NOT NULL,
  `value` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sp1lc7nu550ltluj2iavr5ehh` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_robot_config
-- ----------------------------

-- ----------------------------
-- Table structure for t_robot_item
-- ----------------------------
DROP TABLE IF EXISTS `t_robot_item`;
CREATE TABLE `t_robot_item` (
  `id` bigint(20) NOT NULL,
  `controlLineMax` int(11) DEFAULT NULL,
  `controlLineMin` int(11) DEFAULT NULL,
  `crossingAuctionRate` int(11) DEFAULT NULL,
  `itemId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ah9qnmloml322o1yxeor3y30b` (`itemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_robot_item
-- ----------------------------

-- ----------------------------
-- Table structure for t_robot_user
-- ----------------------------
DROP TABLE IF EXISTS `t_robot_user`;
CREATE TABLE `t_robot_user` (
  `id` bigint(20) NOT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_robot_user
-- ----------------------------

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint(20) NOT NULL,
  `createBy` bigint(20) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `lastModifyBy` bigint(20) DEFAULT NULL,
  `lastModifyTime` datetime DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `disable` bit(1) DEFAULT NULL,
  `identification` varchar(255) NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------

-- ----------------------------
-- Table structure for t_role_function
-- ----------------------------
DROP TABLE IF EXISTS `t_role_function`;
CREATE TABLE `t_role_function` (
  `id` bigint(20) NOT NULL,
  `createBy` bigint(20) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `lastModifyBy` bigint(20) DEFAULT NULL,
  `lastModifyTime` datetime DEFAULT NULL,
  `functionId` bigint(20) DEFAULT NULL,
  `roleId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKkphwfkq4neteswrq8ikpum4pl` (`roleId`,`functionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role_function
-- ----------------------------
INSERT INTO `t_role_function` VALUES ('921272011513135104', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '920573281638023168', '1');
INSERT INTO `t_role_function` VALUES ('921272011529912320', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903103781518966800', '1');
INSERT INTO `t_role_function` VALUES ('921272011529912321', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '894852040855912400', '1');
INSERT INTO `t_role_function` VALUES ('921272011529912322', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '919144159950929920', '1');
INSERT INTO `t_role_function` VALUES ('921272011529912323', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '894853048843632600', '1');
INSERT INTO `t_role_function` VALUES ('921272011529912324', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '897639814638075900', '1');
INSERT INTO `t_role_function` VALUES ('921272011529912325', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '909628459842535400', '1');
INSERT INTO `t_role_function` VALUES ('921272011529912326', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '897642205114531800', '1');
INSERT INTO `t_role_function` VALUES ('921272011534106624', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '897643853182402600', '1');
INSERT INTO `t_role_function` VALUES ('921272011534106625', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '890423844769103900', '1');
INSERT INTO `t_role_function` VALUES ('921272011534106626', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '890420069635457000', '1');
INSERT INTO `t_role_function` VALUES ('921272011534106627', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '890420241593532400', '1');
INSERT INTO `t_role_function` VALUES ('921272011534106628', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903100211058966500', '1');
INSERT INTO `t_role_function` VALUES ('921272011534106629', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903101067191910400', '1');
INSERT INTO `t_role_function` VALUES ('921272011534106630', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '890424323322413000', '1');
INSERT INTO `t_role_function` VALUES ('921272011538300928', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903103278030520300', '1');
INSERT INTO `t_role_function` VALUES ('921272011538300929', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '920573065539092480', '1');
INSERT INTO `t_role_function` VALUES ('921272011538300930', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '920572646209355776', '1');
INSERT INTO `t_role_function` VALUES ('921272011538300931', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '920572831215910912', '1');
INSERT INTO `t_role_function` VALUES ('921272011538300932', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '908307139628367900', '1');
INSERT INTO `t_role_function` VALUES ('921272011538300933', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '908307525357535200', '1');
INSERT INTO `t_role_function` VALUES ('921272011538300934', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '908307225410273300', '1');
INSERT INTO `t_role_function` VALUES ('921272011538300935', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '908307387843084300', '1');
INSERT INTO `t_role_function` VALUES ('921272011538300936', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903101836750225400', '1');
INSERT INTO `t_role_function` VALUES ('921272011538300937', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903102001213079600', '1');
INSERT INTO `t_role_function` VALUES ('921272011538300938', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903103109532745700', '1');
INSERT INTO `t_role_function` VALUES ('921272011538300939', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '920573925455298560', '1');
INSERT INTO `t_role_function` VALUES ('921272011538300940', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '920573732064329728', '1');
INSERT INTO `t_role_function` VALUES ('921272011542495232', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '897646649013174300', '1');
INSERT INTO `t_role_function` VALUES ('921272011542495233', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '897642756359323600', '1');
INSERT INTO `t_role_function` VALUES ('921272011542495234', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '894852177258872800', '1');
INSERT INTO `t_role_function` VALUES ('921272011542495235', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '918386046880710656', '1');
INSERT INTO `t_role_function` VALUES ('921272011542495236', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '894852640607830000', '1');
INSERT INTO `t_role_function` VALUES ('921272011542495237', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '918386271561187328', '1');
INSERT INTO `t_role_function` VALUES ('921272011542495238', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '894852506666926100', '1');
INSERT INTO `t_role_function` VALUES ('921272011542495239', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '897639353335939100', '1');
INSERT INTO `t_role_function` VALUES ('921272011542495240', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '897639254010626000', '1');
INSERT INTO `t_role_function` VALUES ('921272011542495241', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '905612422247612400', '1');
INSERT INTO `t_role_function` VALUES ('921272011542495242', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '905611996261515300', '1');
INSERT INTO `t_role_function` VALUES ('921272011542495243', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '918056269640630272', '1');
INSERT INTO `t_role_function` VALUES ('921272011542495244', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '897646544956686300', '1');
INSERT INTO `t_role_function` VALUES ('921272011542495245', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '909629233909727200', '1');
INSERT INTO `t_role_function` VALUES ('921272011542495246', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '897646477914931200', '1');
INSERT INTO `t_role_function` VALUES ('921272011542495247', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '916920683492016100', '1');
INSERT INTO `t_role_function` VALUES ('921272011546689536', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '916920760574935000', '1');
INSERT INTO `t_role_function` VALUES ('921272011546689537', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '897641981314859000', '1');
INSERT INTO `t_role_function` VALUES ('921272011546689538', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '897641902763933700', '1');
INSERT INTO `t_role_function` VALUES ('921272011546689539', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '916637955760586800', '1');
INSERT INTO `t_role_function` VALUES ('921272011546689540', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '916998111992217600', '1');
INSERT INTO `t_role_function` VALUES ('921272011546689541', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '916998262118940700', '1');
INSERT INTO `t_role_function` VALUES ('921272011546689542', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '916998382541602800', '1');
INSERT INTO `t_role_function` VALUES ('921272011546689543', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903104473633652700', '1');
INSERT INTO `t_role_function` VALUES ('921272011546689544', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '916998499646570500', '1');
INSERT INTO `t_role_function` VALUES ('921272011546689545', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903104977684136000', '1');
INSERT INTO `t_role_function` VALUES ('921272011546689546', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903105398825812000', '1');
INSERT INTO `t_role_function` VALUES ('921272011546689547', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903105490546851800', '1');
INSERT INTO `t_role_function` VALUES ('921272011546689548', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903105888636633100', '1');
INSERT INTO `t_role_function` VALUES ('921272011546689549', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903105225210986500', '1');
INSERT INTO `t_role_function` VALUES ('921272011546689550', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903104740148117500', '1');
INSERT INTO `t_role_function` VALUES ('921272011546689551', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '894852769800781800', '1');
INSERT INTO `t_role_function` VALUES ('921272011546689552', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '897642660288790500', '1');
INSERT INTO `t_role_function` VALUES ('921272011550883840', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '897642585290440700', '1');
INSERT INTO `t_role_function` VALUES ('921272011550883841', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '913596659861028900', '1');
INSERT INTO `t_role_function` VALUES ('921272011550883842', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '909713915665973200', '1');
INSERT INTO `t_role_function` VALUES ('921272011550883843', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '916640898589130800', '1');
INSERT INTO `t_role_function` VALUES ('921272011550883844', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '897643094164373500', '1');
INSERT INTO `t_role_function` VALUES ('921272011550883845', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '897643034424901600', '1');
INSERT INTO `t_role_function` VALUES ('921272011550883846', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '916637872423960600', '1');
INSERT INTO `t_role_function` VALUES ('921272011550883847', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '909625308619997200', '1');
INSERT INTO `t_role_function` VALUES ('921272011550883848', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '905610664725184500', '1');
INSERT INTO `t_role_function` VALUES ('921272011550883849', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '916141579138236400', '1');
INSERT INTO `t_role_function` VALUES ('921272011555078144', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '916141671119323100', '1');
INSERT INTO `t_role_function` VALUES ('921272011555078145', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '905610948213997600', '1');
INSERT INTO `t_role_function` VALUES ('921272011555078146', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '918386128187293696', '1');
INSERT INTO `t_role_function` VALUES ('921272011555078147', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '916141500654420000', '1');
INSERT INTO `t_role_function` VALUES ('921272011555078148', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '897645805538639900', '1');
INSERT INTO `t_role_function` VALUES ('921272011555078149', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '890238656210534400', '1');
INSERT INTO `t_role_function` VALUES ('921272011555078150', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '890238532398874600', '1');
INSERT INTO `t_role_function` VALUES ('921272011555078151', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '889866614273474600', '1');
INSERT INTO `t_role_function` VALUES ('921272011555078152', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '889871694737440800', '1');
INSERT INTO `t_role_function` VALUES ('921272011555078153', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '890169348319084500', '1');
INSERT INTO `t_role_function` VALUES ('921272011555078154', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '889882734883766300', '1');
INSERT INTO `t_role_function` VALUES ('921272011559272448', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '913608122164052000', '1');
INSERT INTO `t_role_function` VALUES ('921272011559272449', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '890460437689139200', '1');
INSERT INTO `t_role_function` VALUES ('921272011559272450', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903085008204333000', '1');
INSERT INTO `t_role_function` VALUES ('921272011559272451', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903095797367701500', '1');
INSERT INTO `t_role_function` VALUES ('921272011559272452', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903096389687312400', '1');
INSERT INTO `t_role_function` VALUES ('921272011559272453', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903098293054079000', '1');
INSERT INTO `t_role_function` VALUES ('921272011559272454', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903084460616974300', '1');
INSERT INTO `t_role_function` VALUES ('921272011559272455', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903097562284687400', '1');
INSERT INTO `t_role_function` VALUES ('921272011559272456', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903101627513176000', '1');
INSERT INTO `t_role_function` VALUES ('921272011559272457', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '913592284153380900', '1');
INSERT INTO `t_role_function` VALUES ('921272011559272458', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903085355383652400', '1');
INSERT INTO `t_role_function` VALUES ('921272011563466752', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '894877313957625900', '1');
INSERT INTO `t_role_function` VALUES ('921272011563466753', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903088530371117000', '1');
INSERT INTO `t_role_function` VALUES ('921272011563466754', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903094177040957400', '1');
INSERT INTO `t_role_function` VALUES ('921272011563466755', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903093963131453400', '1');
INSERT INTO `t_role_function` VALUES ('921272011563466756', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903094291973275600', '1');
INSERT INTO `t_role_function` VALUES ('921272011563466757', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '903094079108153300', '1');
INSERT INTO `t_role_function` VALUES ('921272011563466758', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '908575102528913400', '1');
INSERT INTO `t_role_function` VALUES ('921272011563466759', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '908575242891296800', '1');
INSERT INTO `t_role_function` VALUES ('921272011563466760', '1', '2017-10-20 15:08:58', '1', '2017-10-20 15:08:58', '916920921736872000', '1');
INSERT INTO `t_role_function` VALUES ('921272011563466761', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903094896758358000', '1');
INSERT INTO `t_role_function` VALUES ('921272011563466762', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903094827262935000', '1');
INSERT INTO `t_role_function` VALUES ('921272011563466763', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '909714315815157800', '1');
INSERT INTO `t_role_function` VALUES ('921272011567661056', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '895551328791560200', '1');
INSERT INTO `t_role_function` VALUES ('921272011567661057', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903096047067201500', '1');
INSERT INTO `t_role_function` VALUES ('921272011567661058', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903096297743974400', '1');
INSERT INTO `t_role_function` VALUES ('921272011567661059', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903096219188854800', '1');
INSERT INTO `t_role_function` VALUES ('921272011567661060', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903084729438306300', '1');
INSERT INTO `t_role_function` VALUES ('921272011567661061', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903857199342682100', '1');
INSERT INTO `t_role_function` VALUES ('921272011567661062', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903084636094070800', '1');
INSERT INTO `t_role_function` VALUES ('921272011567661063', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903096784748806100', '1');
INSERT INTO `t_role_function` VALUES ('921272011567661064', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903100134894600200', '1');
INSERT INTO `t_role_function` VALUES ('921272011567661065', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903100803219193900', '1');
INSERT INTO `t_role_function` VALUES ('921272011567661066', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '898822950579339300', '1');
INSERT INTO `t_role_function` VALUES ('921272011567661067', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '898823022863974400', '1');
INSERT INTO `t_role_function` VALUES ('921272011567661068', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '890424240837230600', '1');
INSERT INTO `t_role_function` VALUES ('921272011571855360', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '904886985666068500', '1');
INSERT INTO `t_role_function` VALUES ('921272011571855361', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '898822725030641700', '1');
INSERT INTO `t_role_function` VALUES ('921272011571855362', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903103943314243600', '1');
INSERT INTO `t_role_function` VALUES ('921272011571855363', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '908307652675633200', '1');
INSERT INTO `t_role_function` VALUES ('921272011571855364', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '920574237591207936', '1');
INSERT INTO `t_role_function` VALUES ('921272011571855365', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '894851685208293400', '1');
INSERT INTO `t_role_function` VALUES ('921272011571855366', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '894852348822683600', '1');
INSERT INTO `t_role_function` VALUES ('921272011571855367', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '897639509657649200', '1');
INSERT INTO `t_role_function` VALUES ('921272011571855368', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '897646005141372900', '1');
INSERT INTO `t_role_function` VALUES ('921272011571855369', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '905611876895817700', '1');
INSERT INTO `t_role_function` VALUES ('921272011571855370', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '905612141195690000', '1');
INSERT INTO `t_role_function` VALUES ('921272011571855371', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '921266146735292416', '1');
INSERT INTO `t_role_function` VALUES ('921272011576049664', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '897646919772274700', '1');
INSERT INTO `t_role_function` VALUES ('921272011576049665', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '897642301621272600', '1');
INSERT INTO `t_role_function` VALUES ('921272011576049666', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '916998160788750300', '1');
INSERT INTO `t_role_function` VALUES ('921272011576049667', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '916998322974097400', '1');
INSERT INTO `t_role_function` VALUES ('921272011576049668', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '916998445397442600', '1');
INSERT INTO `t_role_function` VALUES ('921272011576049669', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '916998569360097300', '1');
INSERT INTO `t_role_function` VALUES ('921272011576049670', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903105103404204000', '1');
INSERT INTO `t_role_function` VALUES ('921272011576049671', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903104620400738300', '1');
INSERT INTO `t_role_function` VALUES ('921272011576049672', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903105677965131800', '1');
INSERT INTO `t_role_function` VALUES ('921272011576049673', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903106039027597300', '1');
INSERT INTO `t_role_function` VALUES ('921272011576049674', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '897643651516072000', '1');
INSERT INTO `t_role_function` VALUES ('921272011576049675', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '913609241653149700', '1');
INSERT INTO `t_role_function` VALUES ('921272011580243968', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '909623135718867000', '1');
INSERT INTO `t_role_function` VALUES ('921272011580243969', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903451755017142300', '1');
INSERT INTO `t_role_function` VALUES ('921272011580243970', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '909623093838741500', '1');
INSERT INTO `t_role_function` VALUES ('921272011580243971', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '909628721390944300', '1');
INSERT INTO `t_role_function` VALUES ('921272011580243972', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '890420681529884700', '1');
INSERT INTO `t_role_function` VALUES ('921272011580243973', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '832918254224670700', '1');
INSERT INTO `t_role_function` VALUES ('921272011580243974', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '890387310523187200', '1');
INSERT INTO `t_role_function` VALUES ('921272011580243975', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903097131793907700', '1');
INSERT INTO `t_role_function` VALUES ('921272011580243976', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903094745545310200', '1');
INSERT INTO `t_role_function` VALUES ('921272011580243977', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '908574895330295800', '1');
INSERT INTO `t_role_function` VALUES ('921272011580243978', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903101528095588400', '1');
INSERT INTO `t_role_function` VALUES ('921272011580243979', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '908575347241386000', '1');
INSERT INTO `t_role_function` VALUES ('921272011584438272', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903097971858473000', '1');
INSERT INTO `t_role_function` VALUES ('921272011584438273', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903085441517879300', '1');
INSERT INTO `t_role_function` VALUES ('921272011584438274', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903096107934941200', '1');
INSERT INTO `t_role_function` VALUES ('921272011609604096', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903100936291876900', '1');
INSERT INTO `t_role_function` VALUES ('921272011609604097', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '898822839052795900', '1');
INSERT INTO `t_role_function` VALUES ('921272011609604098', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '904887069237575700', '1');
INSERT INTO `t_role_function` VALUES ('921272011609604099', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903100044989694000', '1');
INSERT INTO `t_role_function` VALUES ('921272011609604100', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903103671087136800', '1');
INSERT INTO `t_role_function` VALUES ('921272011609604101', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903104247107682300', '1');
INSERT INTO `t_role_function` VALUES ('921272011609604102', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '894851803902902300', '1');
INSERT INTO `t_role_function` VALUES ('921272011609604103', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '894851930105315300', '1');
INSERT INTO `t_role_function` VALUES ('921272011609604104', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '894853277177348100', '1');
INSERT INTO `t_role_function` VALUES ('921272011609604105', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903443890630033400', '1');
INSERT INTO `t_role_function` VALUES ('921272011609604106', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903443760111681500', '1');
INSERT INTO `t_role_function` VALUES ('921272011609604107', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '897638581424619500', '1');
INSERT INTO `t_role_function` VALUES ('921272011609604108', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '897639643166539800', '1');
INSERT INTO `t_role_function` VALUES ('921272011613798400', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '897639934129602600', '1');
INSERT INTO `t_role_function` VALUES ('921272011613798401', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '909628843793317900', '1');
INSERT INTO `t_role_function` VALUES ('921272011613798402', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '897646790881312800', '1');
INSERT INTO `t_role_function` VALUES ('921272011613798403', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '897646121365536800', '1');
INSERT INTO `t_role_function` VALUES ('921272011613798404', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '897642084184358900', '1');
INSERT INTO `t_role_function` VALUES ('921272011613798405', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '897642381690536000', '1');
INSERT INTO `t_role_function` VALUES ('921272011613798406', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903105780499087400', '1');
INSERT INTO `t_role_function` VALUES ('921272011613798407', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903104848495378400', '1');
INSERT INTO `t_role_function` VALUES ('921272011613798408', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '913596705524416500', '1');
INSERT INTO `t_role_function` VALUES ('921272011613798409', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '916920569348227100', '1');
INSERT INTO `t_role_function` VALUES ('921272011613798410', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '909714028304007200', '1');
INSERT INTO `t_role_function` VALUES ('921272011613798411', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '916641023130599400', '1');
INSERT INTO `t_role_function` VALUES ('921272011613798412', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '897643747955703800', '1');
INSERT INTO `t_role_function` VALUES ('921272011613798413', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '897645115663712300', '1');
INSERT INTO `t_role_function` VALUES ('921272011613798414', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903447338993844200', '1');
INSERT INTO `t_role_function` VALUES ('921272011613798415', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '909623051547574300', '1');
INSERT INTO `t_role_function` VALUES ('921272011613798416', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '913754687155470300', '1');
INSERT INTO `t_role_function` VALUES ('921272011617992704', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '890424125401595900', '1');
INSERT INTO `t_role_function` VALUES ('921272011617992705', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '890239210424893400', '1');
INSERT INTO `t_role_function` VALUES ('921272011617992706', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '890239008964083700', '1');
INSERT INTO `t_role_function` VALUES ('921272011617992707', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '889890195325845500', '1');
INSERT INTO `t_role_function` VALUES ('921272011617992708', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '889870773840248800', '1');
INSERT INTO `t_role_function` VALUES ('921272011617992709', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '890387575770972200', '1');
INSERT INTO `t_role_function` VALUES ('921272011617992710', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '889889290459283500', '1');
INSERT INTO `t_role_function` VALUES ('921272011617992711', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903099946406772700', '1');
INSERT INTO `t_role_function` VALUES ('921272011617992712', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903098059318100000', '1');
INSERT INTO `t_role_function` VALUES ('921272011617992713', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903100718750105600', '1');
INSERT INTO `t_role_function` VALUES ('921272011617992714', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903101205134180400', '1');
INSERT INTO `t_role_function` VALUES ('921272011617992715', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903101295433351200', '1');
INSERT INTO `t_role_function` VALUES ('921272011617992716', '1', '2017-10-20 15:08:59', '1', '2017-10-20 15:08:59', '903101363037143000', '1');
INSERT INTO `t_role_function` VALUES ('921273460603224064', '1', '2017-10-20 15:14:44', '1', '2017-10-20 15:14:44', '908307387843084300', '2');
INSERT INTO `t_role_function` VALUES ('921273460674527232', '1', '2017-10-20 15:14:44', '1', '2017-10-20 15:14:44', '908307225410273300', '2');
INSERT INTO `t_role_function` VALUES ('921273460678721536', '1', '2017-10-20 15:14:44', '1', '2017-10-20 15:14:44', '908307139628367900', '2');
INSERT INTO `t_role_function` VALUES ('921273846550495232', '1', '2017-10-20 15:16:16', '1', '2017-10-20 15:16:16', '921273846525329408', '1');
INSERT INTO `t_role_function` VALUES ('921273927353761792', '1', '2017-10-20 15:16:35', '1', '2017-10-20 15:16:35', '921273927320207360', '1');

-- ----------------------------
-- Table structure for t_sealed
-- ----------------------------
DROP TABLE IF EXISTS `t_sealed`;
CREATE TABLE `t_sealed` (
  `id` bigint(20) NOT NULL,
  `cost` varchar(255) DEFAULT NULL,
  `income` varchar(255) DEFAULT NULL,
  `isSealed` bit(1) DEFAULT NULL,
  `item` text,
  `itemId` bigint(20) DEFAULT NULL,
  `itemName` varchar(255) DEFAULT NULL,
  `market_price` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `periods` int(11) DEFAULT NULL,
  `picture` bigint(20) DEFAULT NULL,
  `profit` varchar(255) DEFAULT NULL,
  `robot` bit(1) DEFAULT NULL,
  `sealType` int(11) DEFAULT NULL,
  `stateTime` datetime DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `total` varchar(255) DEFAULT NULL,
  `user` text,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_sealed
-- ----------------------------

-- ----------------------------
-- Table structure for t_show_item
-- ----------------------------
DROP TABLE IF EXISTS `t_show_item`;
CREATE TABLE `t_show_item` (
  `id` bigint(20) NOT NULL,
  `context` varchar(625) NOT NULL,
  `disable` bit(1) DEFAULT NULL,
  `fastUrl` varchar(255) DEFAULT NULL,
  `grade` int(11) NOT NULL,
  `head_pic` bigint(20) DEFAULT NULL,
  `itemId` bigint(20) DEFAULT NULL,
  `itemName` varchar(255) DEFAULT NULL,
  `orderId` bigint(20) NOT NULL,
  `pictures` varchar(75) DEFAULT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `title` varchar(255) NOT NULL,
  `userId` bigint(20) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_show_item
-- ----------------------------

-- ----------------------------
-- Table structure for t_sort
-- ----------------------------
DROP TABLE IF EXISTS `t_sort`;
CREATE TABLE `t_sort` (
  `id` bigint(20) NOT NULL,
  `commodities` text,
  `disable` bit(1) DEFAULT NULL,
  `name` varchar(12) NOT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_sort
-- ----------------------------

-- ----------------------------
-- Table structure for t_statistic
-- ----------------------------
DROP TABLE IF EXISTS `t_statistic`;
CREATE TABLE `t_statistic` (
  `id` bigint(20) NOT NULL,
  `coin_number` int(11) DEFAULT NULL,
  `gift_number` int(11) DEFAULT NULL,
  `integral_number` int(11) DEFAULT NULL,
  `profit_number` varchar(255) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `user_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_statistic
-- ----------------------------

-- ----------------------------
-- Table structure for t_system_notice
-- ----------------------------
DROP TABLE IF EXISTS `t_system_notice`;
CREATE TABLE `t_system_notice` (
  `id` bigint(20) NOT NULL,
  `context` text NOT NULL,
  `state` int(11) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_system_notice
-- ----------------------------

-- ----------------------------
-- Table structure for t_upload_file
-- ----------------------------
DROP TABLE IF EXISTS `t_upload_file`;
CREATE TABLE `t_upload_file` (
  `id` bigint(20) NOT NULL,
  `contentType` varchar(255) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `fileName` varchar(255) DEFAULT NULL,
  `isDelete` bit(1) NOT NULL,
  `isUsing` bit(1) NOT NULL,
  `previousId` bigint(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_tupaxrgktnfcsos4yqk0c5fo` (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_upload_file
-- ----------------------------

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL,
  `createBy` bigint(20) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `lastModifyBy` bigint(20) DEFAULT NULL,
  `lastModifyTime` datetime DEFAULT NULL,
  `disable` bit(1) DEFAULT NULL,
  `expirationTime` datetime DEFAULT NULL,
  `finalLogin` datetime DEFAULT NULL,
  `loginEquipment` int(11) DEFAULT NULL,
  `refreshToken` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKn0b7epu6ijex57u1p27ci0r48` (`refreshToken`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_message
-- ----------------------------
DROP TABLE IF EXISTS `t_user_message`;
CREATE TABLE `t_user_message` (
  `id` bigint(20) NOT NULL,
  `Level_one_proxy` bigint(20) DEFAULT NULL,
  `Level_two_proxy` bigint(20) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `admin` bit(1) DEFAULT NULL,
  `days` int(11) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `head_pic` bigint(20) DEFAULT NULL,
  `id_card` varchar(255) DEFAULT NULL,
  `integral` int(11) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `iphone` varchar(11) DEFAULT NULL,
  `join_date` datetime DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  `promotion` bigint(20) DEFAULT NULL,
  `qq_access_token` varchar(255) DEFAULT NULL,
  `qq_openid` varchar(255) DEFAULT NULL,
  `qq_userinfo` text,
  `robot` bit(1) DEFAULT NULL,
  `username` varchar(20) DEFAULT NULL,
  `wechat_access_token` varchar(255) DEFAULT NULL,
  `wechat_openid` varchar(255) DEFAULT NULL,
  `wechat_userinfo` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ud6cvkjh8ddoun007dck2ju9` (`email`),
  UNIQUE KEY `UK_hqeguhcsck4atb2690mmktdxx` (`id_card`),
  UNIQUE KEY `UK_m1yls4peqvy38ow29nwe0h6tc` (`iphone`),
  UNIQUE KEY `UK_qghxk4jo73mo5ec1jo856ep80` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_message
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `id` bigint(20) NOT NULL,
  `createBy` bigint(20) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `lastModifyBy` bigint(20) DEFAULT NULL,
  `lastModifyTime` datetime DEFAULT NULL,
  `roleId` bigint(20) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKqyv01li1yi6q3yjq4scsrrm21` (`userId`,`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
