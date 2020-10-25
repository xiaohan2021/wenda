/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 80012
Source Host           : localhost:3306
Source Database       : wenda

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2020-02-27 15:45:07
*/
----------------------------------
-- Table structure for question
----------------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`(
	`id` int(11) not null auto_increment,
	`title` varchar(255) not null,
-- 	`content` text,
	`user_id` int(11) not null,
	`created_date` datetime not null,
	`comment_count` int(11) not null,
	primary key (`id`),
	key `date_index` (`created_date`)
) engine = INNODB auto_increment=13 default charset = utf8;

----------------------------------
-- Table structure for user
----------------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`(
	`id` int(11) unsigned not null auto_increment,
	`name` varchar(64) not null default '',
	`password` varchar(128) not null default '',
	`salt` varchar(32) not null default '',
	`head_url` varchar(256) not null default '',
	primary key (`id`),
	unique key `name` (`name`)
) engine = INNODB auto_increment=1 default charset = utf8;

----------------------------------
-- Table structure for message
----------------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`(
	`id` int(11) not null auto_increment,
	`from_id` int(11) default null,
	`to_id` int(11) default null,
	`content` text,
	`create_date` datetime default null,
	`has_read` int(11) default null,
	`conversation_id` varchar(45) not null,
	primary key (`id`),
	key `conversation_index` (`conversation_id`),
	key `create_date` (`create_date`)
) engine = INNODB auto_increment=11 default charset = utf8;

----------------------------------
-- Table structure for comment
----------------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`(
	`id` int(11) not null auto_increment,
	`content` text not null,
	`user_id` int(11) not null,
	`entity_id` int(11) not null,
	`entity_type` int(11) not null,
	`create_date` datetime not null,
	`status` int(11) not null default '0',
	primary key (`id`),
	key `entity_index` (`entity_id`, `entity_type`)
) engine = INNODB auto_increment=2 default charset = utf8;

-- ----------------------------
-- Table structure for feed
-- ----------------------------
DROP TABLE IF EXISTS `feed`;
CREATE TABLE `feed` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `data` tinytext,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_index` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for login_ticket
-- ----------------------------
DROP TABLE IF EXISTS `login_ticket`;
CREATE TABLE `login_ticket` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `ticket` varchar(45) NOT NULL,
  `expired` datetime NOT NULL,
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ticket_UNIQUE` (`ticket`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;




















