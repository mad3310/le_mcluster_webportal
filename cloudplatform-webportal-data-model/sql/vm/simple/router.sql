CREATE TABLE `WEBPORTAL_CLOUDVM_ROUTER` (
	`ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
	`REGION` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
	`ROUTER_ID` char(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
	`STATUS` tinyint(4) DEFAULT NULL,
	`NAME` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
	`TENANT_ID` bigint(20) unsigned DEFAULT NULL,
	`EXTERNAL_GATEWAY_INFO_NETWORK_ID` char(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
	`GATEWAY_IP` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
	`DELETED` tinyint(4) DEFAULT NULL,
	`CREATE_TIME` datetime DEFAULT NULL,
	`UPDATE_TIME` datetime DEFAULT NULL,
	`CREATE_USER` bigint(20) unsigned DEFAULT NULL,
	`UPDATE_USER` bigint(20) unsigned DEFAULT NULL,
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;
