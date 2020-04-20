/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50640
Source Host           : localhost:3306
Source Database       : myblog

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2020-04-21 01:33:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `articletask`
-- ----------------------------
DROP TABLE IF EXISTS `articletask`;
CREATE TABLE `articletask` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `indexUrl` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `firstUrlRegex` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `secondUrlRegex` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `titleRegex` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `contentRegex` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `splitStr` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `ignoreStr` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `type` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of articletask
-- ----------------------------
INSERT INTO `articletask` VALUES ('1', 'https://blog.csdn.net/qq_38963960', '0000333000033300003330000', 'https://blog.csdn.net/qq_38963960/article/details/\\d{8,}333000033300003330000', '<h1 class=\"title-article\">[\\ss\\SS]*<div class=\"article-info-box\">3330000333<h1 class=\"title-article\">333</h1>', '<div class=\"htmledit_views\" id=\"content_views\">[\\ss\\SS]*</div><div class=\"more-toolbox\">33300003330000333<div class=\"more-toolbox\">', '333', '0000', '0');
