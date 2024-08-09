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

CREATE TABLE `reference` (
                             `reference_id` bigint NOT NULL AUTO_INCREMENT,
                             `search_link` varchar(255) NOT NULL,
                             `news_title` varchar(100) NOT NULL,
                             `news_image` varchar(255) NOT NULL,
                             `news_content` varchar(255) NOT NULL,
                             `news_link` varchar(255) NOT NULL,
                             `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             `status` varchar(20) NOT NULL DEFAULT 'active',
                             PRIMARY KEY (`reference_id`)
);

CREATE TABLE `keyword` (
                           `keyword_id` bigint NOT NULL AUTO_INCREMENT COMMENT '레디스 키값과 동일',
                           `keyword` varchar(50) NOT NULL,
                           `status` varchar(20) NOT NULL DEFAULT 'active' COMMENT '활성/비활성',
                           `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           `reference_id` bigint NOT NULL,
                           PRIMARY KEY (`keyword_id`),
                           UNIQUE (`keyword`),
                           CONSTRAINT `FK_keyword_reference` FOREIGN KEY (`reference_id`) REFERENCES `Reference`(`reference_id`)
);

CREATE TABLE `community_period` (
                                    `community_period_id` bigint NOT NULL AUTO_INCREMENT,
                                    `community` varchar(100) NOT NULL COMMENT '커뮤니티 명',
                                    `period` varchar(100) NOT NULL COMMENT '기간',
                                    PRIMARY KEY (`community_period_id`),
                                    UNIQUE (`community`, `period`) -- 이 부분이 복합 유니크 제약 조건
);

CREATE TABLE `keyword_community_period` (
                                            `keyword_id` bigint NOT NULL,
                                            `community_period_id` bigint NOT NULL,
                                            CONSTRAINT `FK_keyword_community_period_keyword` FOREIGN KEY (`keyword_id`) REFERENCES `keyword`(`keyword_id`),
                                            CONSTRAINT `FK_keyword_community_period_community_period` FOREIGN KEY (`community_period_id`) REFERENCES `community_period`(`community_period_id`),
                                            PRIMARY KEY (`keyword_id`, `community_period_id`)
);
