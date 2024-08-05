CREATE TABLE `Reference` (
	`reference_id`	bigint	NOT NULL	AUTO_INCREMENT,
	`search_link`	varchar(255)	NOT NULL,
	`news_title`	varchar(100)	NOT NULL,
	`news_image`	varchar(255)	NOT NULL,
	`news_content`	varchar(255)	NOT NULL,
	`news_link`	varchar(255)	NOT NULL,
	`create_at`	timestamp	NOT NULL,
	`update_at`	timestamp	NULL,
	`status`	varchar(20)	NOT NULL	DEFAULT 'active',
    PRIMARY KEY (`reference_id`)
);

CREATE TABLE `keyword` (
	`keyword_id`	bigint	NOT NULL	AUTO_INCREMENT	COMMENT '레디스 키값과 동일',
	`keyword`	varchar(50)	NOT NULL,
	`redis_id`	varchar(100)	NOT NULL	COMMENT 'ex)namu_realtime',
	`status`	varchar(20)	NOT NULL	DEFAULT 'active'	COMMENT '활성/비활성',
	`create_at`	timestamp	NOT NULL,
	`update_at`	timestamp	NULL,
	`reference_id`	bigint	NOT NULL,
    PRIMARY KEY (`keyword_id`),
    CONSTRAINT `FK_keyword_reference` FOREIGN KEY (`reference_id`) REFERENCES `Reference`(`reference_id`)
);
