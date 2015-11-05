CREATE TABLE `WEBPORTAL_CLOUDVM_NETWORK` (
	`ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
	`REGION` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
	`NETWORK_ID` char(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
	`STATUS` tinyint(4) DEFAULT NULL,
	`NAME` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
	`SHARED` tinyint(4) DEFAULT NULL,
	`TENANT_ID` bigint(20) unsigned DEFAULT NULL,
	`NETWORK_TYPE` tinyint(4) DEFAULT NULL,
	`EXTERNAL` tinyint(4) DEFAULT NULL,
	`DELETED` tinyint(4) DEFAULT NULL,
	`CREATE_TIME` datetime DEFAULT NULL,
	`UPDATE_TIME` datetime DEFAULT NULL,
	`CREATE_USER` bigint(20) unsigned DEFAULT NULL,
	`UPDATE_USER` bigint(20) unsigned DEFAULT NULL,
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;