DROP TABLE IF EXISTS `app_group`;
CREATE TABLE IF NOT EXISTS `app_group` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `app_group_name` varchar(32) NOT NULL DEFAULT '' COMMENT '应用组名称',
  `operator_name` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人姓名',
  `operator_email` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人邮箱',
  `creator_name` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人姓名',
  `creator_email` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人邮箱',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_app_group_name` (`app_group_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8 COMMENT='应用组表';

DROP TABLE IF EXISTS `app_info`;
CREATE TABLE IF NOT EXISTS `app_info` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `app_group_name` varchar(32) NOT NULL DEFAULT '' COMMENT '应用组名称',
  `app_name` varchar(32) NOT NULL DEFAULT '' COMMENT '应用名称',
  `sds_scheme_name` varchar(32) NOT NULL DEFAULT '' COMMENT '当前所使用的降级预案',
  `version` bigint(20) NOT NULL DEFAULT '0' COMMENT '当前app的数据版本，调整对应的降级预案策略或者当前app使用的降级预案都会改变数据版本',
  `operator_name` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人姓名',
  `operator_email` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人邮箱',
  `creator_name` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人姓名',
  `creator_email` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人邮箱',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_app_group_name_app_name` (`app_group_name`,`app_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8 COMMENT='应用表';


DROP TABLE IF EXISTS `sds_scheme`;
CREATE TABLE IF NOT EXISTS `sds_scheme` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `app_group_name` varchar(32) NOT NULL DEFAULT '' COMMENT '应用组名称',
  `app_name` varchar(32) NOT NULL DEFAULT '' COMMENT '应用名称',
  `sds_scheme_name` varchar(32) NOT NULL DEFAULT '' COMMENT '降级预案名称',
  `operator_name` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人姓名',
  `operator_email` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人邮箱',
  `creator_name` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人姓名',
  `creator_email` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人邮箱',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_app_group_name_app_name_sds_scheme` (`app_group_name`,`app_name`,`sds_scheme_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8 COMMENT='降级预案表';

DROP TABLE IF EXISTS `point_strategy`;
CREATE TABLE IF NOT EXISTS `point_strategy` (
    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `app_group_name` varchar(32) NOT NULL DEFAULT '' COMMENT '应用组名称',
    `app_name` varchar(32) NOT NULL DEFAULT '' COMMENT '应用名称',
    `point` varchar(200) NOT NULL DEFAULT '' COMMENT '降级点名称',
    `sds_scheme_name` varchar(32) NOT NULL DEFAULT '' COMMENT '降级预案名称',
    `visit_threshold` bigint(20) NOT NULL DEFAULT '-1' COMMENT '访问量阈值',
    `visit_growth_rate` int(11) NOT NULL DEFAULT '-1' COMMENT '秒级别的访问量增长比率，15表示15%，280表示280%, -1表示不使用',
    `visit_growth_threshold` bigint(20) NOT NULL DEFAULT '-1' COMMENT '秒级别的访问量增长起始阈值',
    `concurrent_threshold` int(11) NOT NULL DEFAULT '-1' COMMENT '并发量阈值',
    `exception_threshold` bigint(20) NOT NULL DEFAULT '-1' COMMENT '异常量阈值',
    `exception_rate_threshold` int(11) NOT NULL DEFAULT '-1' COMMENT '异常率阈值，取值为[0-100]，15表示异常率阈值为15%，-1表示不对异常率进行降级',
    `exception_rate_start` int(11) NOT NULL DEFAULT '-1' COMMENT '异常率降级判断的起点（标准），当访问量超过该值才开始计算异常率，避免采样数太小而失准',
    `timeout_threshold` bigint(20) NOT NULL DEFAULT '-1' COMMENT '超时阈值，单位毫秒',
    `timeout_count_threshold` bigint(20) NOT NULL DEFAULT '-1' COMMENT '超时次数阈值，如果超过timeout_threshold的访问量达到该值，就应该降级',
    `token_bucket_generated_tokens_in_second` bigint(20) NOT NULL DEFAULT '-1' COMMENT '令牌桶每个1秒能生成多少个令牌，-1表示不生效',
    `token_bucket_size` bigint(20) NOT NULL DEFAULT '-1' COMMENT '令牌桶中桶最多能存储多少令牌，-1表示不生效',
    `delay_time` bigint(20) NOT NULL DEFAULT '-1' COMMENT '降级延迟时间，单位毫秒',
    `retry_interval` bigint(20) NOT NULL DEFAULT '-1' COMMENT '异常量降级特有的降级延迟期间重试时间周期，单位毫秒',
    `downgrade_rate` tinyint(4) NOT NULL DEFAULT '0' COMMENT '降级比例',
    `pressure_test_downgrade` tinyint(4) NOT NULL DEFAULT '0' COMMENT '压测流量是否降级，0-对待压测流量和正常流量一致，1-对待压测流量必降级（哪怕降级比例是0）',
    `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '本策略是否开启可用，1 可用 0不可用（默认）',
    `operator_name` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人姓名',
    `operator_email` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人邮箱',
    `creator_name` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人姓名',
    `creator_email` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人邮箱',
    `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期', PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_point_strategy` (`app_group_name`,`app_name`,`point`,`sds_scheme_name`) USING BTREE,
    KEY `idx_point` (`point`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=511 DEFAULT CHARSET=utf8 COMMENT='降级点策略信息表'


DROP TABLE IF EXISTS `heartbeat`;
CREATE TABLE IF NOT EXISTS `heartbeat` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `app_group_name` varchar(32) NOT NULL DEFAULT '' COMMENT '应用组名称',
  `app_name` varchar(32) NOT NULL DEFAULT '' COMMENT '应用名称',
  `point` varchar(164) NOT NULL DEFAULT '' COMMENT '降级点名称',
  `app_ip` varchar(16) NOT NULL DEFAULT '' COMMENT '应用IP地址',
  `downgrade_num` bigint(20) NOT NULL DEFAULT '-1' COMMENT '降级量',
  `visit_num` bigint(20) NOT NULL DEFAULT '-1' COMMENT '访问量',
  `timeout_num` bigint(20) NOT NULL DEFAULT '-1' COMMENT '超时量',
  `exception_num` bigint(20) NOT NULL DEFAULT '-1' COMMENT '异常量',
  `max_concurrent_num` bigint(20) NOT NULL DEFAULT '-1' COMMENT '最大并发量',
  `statistics_cycle_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '统计周期时间',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=834679 DEFAULT CHARSET=utf8 COMMENT='心跳表';

DROP TABLE IF EXISTS `point_dict`;
CREATE TABLE IF NOT EXISTS `point_dict` (
    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `app_group_name` varchar(32) NOT NULL DEFAULT '' COMMENT '应用组名称',
    `app_name` varchar(32) NOT NULL DEFAULT '' COMMENT '应用名称',
    `point` varchar(200) NOT NULL DEFAULT '' COMMENT '降级点名称',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否有效,1:有效 0:无效',
    `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期', PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_point_strategy_dict` (`app_group_name`,`app_name`,`point`) USING BTREE, KEY `idx_point` (`point`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2603 DEFAULT CHARSET=utf8 COMMENT='降级点字典表'

DROP TABLE IF EXISTS `point_return_value`;
CREATE TABLE IF NOT EXISTS `point_return_value` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `app_group_name` varchar(32) NOT NULL DEFAULT '' COMMENT '应用组名称',
  `app_name` varchar(32) NOT NULL DEFAULT '' COMMENT '应用名称',
  `point` varchar(200) NOT NULL DEFAULT '' COMMENT '降级点名称',
  `return_value_str` text COMMENT '降级点返回值（一般是json格式）',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '本降级点返回值是否可用，1 可用 0不可用（默认）',
  `operator_name` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人姓名',
  `operator_email` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人邮箱',
  `creator_name` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人姓名',
  `creator_email` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人邮箱',
  `operator_id` bigint(20) NOT NULL DEFAULT '-1' COMMENT '操作人id',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_point_return_value` (`app_group_name`,`app_name`,`point`) USING BTREE,
  KEY `idx_point` (`point`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=289 DEFAULT CHARSET=utf8 COMMENT='降级点返回值信息表';