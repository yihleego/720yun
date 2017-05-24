CREATE DATABASE `panorama` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
use `panorama`;


CREATE TABLE `720yun_pano`(
`id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '自增长主键',
`pano_id` VARCHAR(50) UNIQUE NOT NULL DEFAULT '0' COMMENT '720yun的panoid',
`pano_url` VARCHAR(500) NOT NULL DEFAULT '0' COMMENT '720yun的pano的链接',
`pano_xml_url` VARCHAR(500) NOT NULL DEFAULT '0' COMMENT '720云的pano的xml的链接',
`create_date` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
`crawl_date` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '爬取时间',
`status` TINYINT UNSIGNED  NOT NULL DEFAULT '0' COMMENT '状态 0：未爬 1：已爬 2：爬失败'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '720yun 全景信息表';


CREATE TABLE `regular`(
`id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '自增长主键',
`link_rid` INT NOT NULL COMMENT '规则复制/指向id',
`uid` INT NOT NULL COMMENT '用户ID',
`name` VARCHAR(50) NOT NULL DEFAULT 'no name' COMMENT '规则名',
`main_page` TINYINT UNSIGNED NOT NULL DEFAULT '0' COMMENT '列表页类型',
`detail_page` TINYINT UNSIGNED NOT NULL DEFAULT '0' COMMENT '详细页类型',
`url` TEXT NOT NULL COMMENT '目标网址',
`list` VARCHAR(500) NOT NULL DEFAULT 'list' COMMENT '列表区',
`row` VARCHAR(500) NOT NULL DEFAULT 'row' COMMENT '列表行',
`href` VARCHAR(500) NOT NULL DEFAULT 'a[href]' COMMENT '详细页链接',
`noise` VARCHAR(50) COMMENT '去噪标识',
`item_name` TEXT NOT NULL COMMENT '爬取项名字的JSON',
`item_regular` TEXT NOT NULL COMMENT '爬取项规则的JSON',
`item_regular_type` TEXT NOT NULL COMMENT '爬取项规则类型的JSON 0:text 1:ownText 2:html 3:outerHtml 4:src 5:href 6:abs:href',
`add_time` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '添加时间',
`update_time` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
`frequency` INT NOT NULL DEFAULT '0' COMMENT '爬取频率',
`time_interval` INT NOT NULL DEFAULT '0' COMMENT '增量检测频率',
`keep_uptodate` TINYINT UNSIGNED NOT NULL DEFAULT '0' COMMENT '状态 0:否 1:是',
`last_crawl_time` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '上次爬取时间',
`status` TINYINT UNSIGNED NOT NULL DEFAULT '0' COMMENT '状态 0:未上线 1:已上线 2:已下线 3:失效/禁封',
`comment` VARCHAR(500) NOT NULL DEFAULT '无备注' COMMENT '备注',
CONSTRAINT `fk_regular_user` FOREIGN KEY (`uid`) REFERENCES `user` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '规则表'; 
CREATE INDEX `idx_regular_uid` ON `regular`(`uid`);
CREATE INDEX `idx_regular_status` ON `regular`(`status`);

CREATE TABLE `task`(
`id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '自增长主键',
`rid`INT NOT NULL COMMENT '规则ID',
`url` TEXT NOT NULL COMMENT '任务URL',
`add_time` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '任务添加时间',
`crawl_time` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '爬取结束时间',
`status` TINYINT UNSIGNED NOT NULL DEFAULT '0' COMMENT '状态 0:未爬取 1:已爬取 2:失败',
CONSTRAINT `fk_task_regular` FOREIGN KEY (`rid`) REFERENCES `regular` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '任务表';
CREATE INDEX `idx_task_rid` ON `task`(`rid`);
CREATE INDEX `idx_task_url` ON `task`(`url`);
CREATE INDEX `idx_task_status` ON `task`(`status`);

CREATE TABLE `result`(
`id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '自增长主键',
`rid` INT NOT NULL COMMENT '规则ID',
`url` TEXT NOT NULL COMMENT '任务URL',
`result` TEXT NOT NULL COMMENT '爬取项结果的JSON',
`start_time` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '开始爬取时间',
`crawl_time` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '爬取结束时间',
`status` TINYINT UNSIGNED NOT NULL DEFAULT '0' COMMENT '状态 0:成功 1:失败'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '结果表';
CREATE INDEX `idx_result_rid` ON `result`(`rid`);
CREATE INDEX `idx_result_url` ON `result`(`url`);

CREATE TABLE `dajs`(
`id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '自增长主键',
`rid` INT NOT NULL COMMENT '规则ID',
`js` TEXT NOT NULL COMMENT 'js代码',
`add_time` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '添加时间',
`update_time` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '更新时间',
CONSTRAINT `fk_dajs_regular` FOREIGN KEY (`rid`) REFERENCES `regular` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '数据分析JS表';