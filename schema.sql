USE `uptimefinder`;

CREATE TABLE `Service`(
	`id` int AUTO_INCREMENT NOT NULL,
	`name` varchar(50) NOT NULL,
	`website_url` varchar(50) NOT NULL,
	`frequency` varchar(50) NOT NULL,
	`enabled` tinyint Unsigned NULL,
	`createdAt` Datetime NULL,
    `updatedAt` Datetime NULL,
 CONSTRAINT `PK_Service` PRIMARY KEY
(
	`id` ASC
)
) ;

CREATE TABLE `UptimeMonitorTable`(
	`id` int AUTO_INCREMENT NOT NULL,
	`website_url` varchar(50) NOT NULL,
	`serviceId` int NULL,
	`status` varchar(50) NULL,
	`uptime` Datetime NULL,
	`downtime` Datetime NULL,
	`responseTime` Double NULL,
 CONSTRAINT `PK_UptimeMonitorTable` PRIMARY KEY
(
	`id` ASC
)
) ;