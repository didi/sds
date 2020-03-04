DROP TABLE IF EXISTS `app_group`;
CREATE TABLE IF NOT EXISTS `app_group` (
    `id` bigint PRIMARY KEY AUTO_INCREMENT,
    `app_group_name` varchar(32),
    `operator_name` varchar(50) NOT NULL DEFAULT '' ,
    `operator_email` varchar(50) NOT NULL DEFAULT '' ,
    `creator_name` varchar(50) NOT NULL DEFAULT '' ,
    `creator_email` varchar(50) NOT NULL DEFAULT '' ,
    `modify_time` timestamp,
    `create_time`  timestamp,
    UNIQUE KEY `uniq_app_group_name` (`app_group_name`)
);

DROP TABLE IF EXISTS `app_info`;
CREATE TABLE IF NOT EXISTS `app_info` (
    `id` bigint PRIMARY KEY AUTO_INCREMENT,
    `app_group_name` varchar(32),
    `app_name` varchar(32),
    `sds_scheme_name` varchar(32),
    `version` bigint,
    `operator_name` varchar(50) NOT NULL DEFAULT '' ,
    `operator_email` varchar(50) NOT NULL DEFAULT '' ,
    `creator_name` varchar(50) NOT NULL DEFAULT '' ,
    `creator_email` varchar(50) NOT NULL DEFAULT '' ,
    `modify_time` timestamp,
    `create_time` timestamp,
    UNIQUE KEY `uniq_app_group_name_app_name` (`app_group_name`,`app_name`)
);

DROP TABLE IF EXISTS `sds_scheme`;
CREATE TABLE IF NOT EXISTS `sds_scheme` (
    `id` bigint PRIMARY KEY AUTO_INCREMENT,
    `app_group_name` varchar(32) NOT NULL DEFAULT '',
    `app_name` varchar(32) NOT NULL DEFAULT '',
    `sds_scheme_name` varchar(32) NOT NULL DEFAULT '',
    `operator_name` varchar(50) NOT NULL DEFAULT '' ,
    `operator_email` varchar(50) NOT NULL DEFAULT '' ,
    `creator_name` varchar(50) NOT NULL DEFAULT '' ,
    `creator_email` varchar(50) NOT NULL DEFAULT '' ,
    `modify_time` timestamp,
    `create_time` timestamp,
    UNIQUE KEY `uniq_app_group_name_app_name_sds_scheme` (`app_group_name`,`app_name`,`sds_scheme_name`)
);

DROP TABLE IF EXISTS `point_strategy`;
CREATE TABLE IF NOT EXISTS `point_strategy` (
    `id` bigint PRIMARY KEY AUTO_INCREMENT, 
    `app_group_name` varchar(32) NOT NULL DEFAULT '' , 
    `app_name` varchar(32) NOT NULL DEFAULT '' ,
    `point` varchar(200) NOT NULL DEFAULT '' , 
    `sds_scheme_name` varchar(32) NOT NULL DEFAULT '' ,
    `visit_threshold` bigint(20) NOT NULL DEFAULT '-1' , 
    `visit_growth_rate` int(11) NOT NULL DEFAULT '-1' , 
    `visit_growth_threshold` bigint(20) NOT NULL DEFAULT '-1' , 
    `concurrent_threshold` int(11) NOT NULL DEFAULT '-1' , 
    `exception_threshold` bigint(20) NOT NULL DEFAULT '-1' ,
    `exception_rate_threshold` int(11) NOT NULL DEFAULT '-1' ,
    `exception_rate_start` int(11) NOT NULL DEFAULT '-1' ,
    `timeout_threshold` bigint(20) NOT NULL DEFAULT '-1' ,
    `timeout_count_threshold` bigint(20) NOT NULL DEFAULT '-1' ,
    `token_bucket_generated_tokens_in_second` bigint(20) NOT NULL DEFAULT '-1' ,
    `token_bucket_size` bigint(20) NOT NULL DEFAULT '-1' ,
    `delay_time` bigint(20) NOT NULL DEFAULT '-1' ,
    `retry_interval` bigint(20) NOT NULL DEFAULT '-1' ,
    `downgrade_rate` tinyint(4) NOT NULL DEFAULT '0' ,
    `pressure_test_downgrade` tinyint(4) NOT NULL DEFAULT '0' ,
    `status` tinyint(4) NOT NULL DEFAULT '0' ,
    `operator_name` varchar(50) NOT NULL DEFAULT '' ,
    `operator_email` varchar(50) NOT NULL DEFAULT '' ,
    `creator_name` varchar(50) NOT NULL DEFAULT '' ,
    `creator_email` varchar(50) NOT NULL DEFAULT '' ,
    `modify_time` timestamp ,
    `create_time` timestamp , 
    UNIQUE KEY `uniq_point_strategy` (`app_group_name`,`app_name`,`point`,`sds_scheme_name`)
);

DROP TABLE IF EXISTS `heartbeat`;
CREATE TABLE IF NOT EXISTS `heartbeat` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT, 
  `app_group_name` varchar(32) NOT NULL DEFAULT '' , 
  `app_name` varchar(32) NOT NULL DEFAULT '' ,
  `point` varchar(164) NOT NULL DEFAULT '' , 
  `app_ip` varchar(16) NOT NULL DEFAULT '' ,
  `downgrade_num` bigint(20) NOT NULL DEFAULT '-1' ,
  `visit_num` bigint(20) NOT NULL DEFAULT '-1' ,
  `timeout_num` bigint(20) NOT NULL DEFAULT '-1' ,
  `exception_num` bigint(20) NOT NULL DEFAULT '-1' ,
  `max_concurrent_num` bigint(20) NOT NULL DEFAULT '-1' ,
  `statistics_cycle_time` timestamp ,
  `modify_time` timestamp ,
  `create_time` timestamp 
);

DROP TABLE IF EXISTS `point_dict`;
CREATE TABLE IF NOT EXISTS `point_dict` (
    `id` bigint PRIMARY KEY AUTO_INCREMENT, 
    `app_group_name` varchar(32) NOT NULL DEFAULT '' , 
    `app_name` varchar(32) NOT NULL DEFAULT '' ,
    `point` varchar(200) NOT NULL DEFAULT '' , 
    `status` tinyint(4) NOT NULL DEFAULT '1' ,
    `modify_time` timestamp ,
    `create_time` timestamp , 
    UNIQUE KEY `uniq_point_strategy_dict` (`app_group_name`,`app_name`,`point`)
);

DROP TABLE IF EXISTS `point_return_value`;
CREATE TABLE IF NOT EXISTS `point_return_value` (
    `id` bigint PRIMARY KEY AUTO_INCREMENT,
    `app_group_name` varchar(32) NOT NULL DEFAULT '' ,
    `app_name` varchar(32) NOT NULL DEFAULT '' ,
    `point` varchar(200) NOT NULL DEFAULT '' ,
    `return_value_str` text NOT NULL DEFAULT '' ,
    `status` tinyint(4) NOT NULL DEFAULT '0' ,
    `operator_name` varchar(50) NOT NULL DEFAULT '' ,
    `operator_email` varchar(50) NOT NULL DEFAULT '' ,
    `creator_name` varchar(50) NOT NULL DEFAULT '' ,
    `creator_email` varchar(50) NOT NULL DEFAULT '' ,
    `modify_time` timestamp ,
    `create_time` timestamp ,
    UNIQUE KEY `uniq_point_return_value` (`app_group_name`,`app_name`,`point`)
);