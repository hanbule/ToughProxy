-- auto Generated on 2019-09-28
-- DROP TABLE IF EXISTS ts_ipaddr_stat;
CREATE TABLE ts_ipaddr_stat(
	id BIGINT (15) NOT NULL AUTO_INCREMENT COMMENT 'id',
	ipaddr VARCHAR (50) NOT NULL DEFAULT '' COMMENT '本地拨号获取IP',
	pick_times INT (11) NOT NULL DEFAULT 0 COMMENT 'ip提取次数',
	begin_time TIMESTAMP UNIQUE NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
	end_time TIMESTAMP UNIQUE NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '结束时间',
	PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'ts_ipaddr_stat';