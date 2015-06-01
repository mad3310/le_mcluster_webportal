/*
 * UPDATE GCE ADD LOG_ID
 */
alter table WEBPORTAL_GCE ADD COLUMN LOG_ID bigint(20) unsigned DEFAULT NULL;

/*
 * CBASE
 */
DROP TABLE WEBPORTAL_CBASE_BUCKET;
CREATE TABLE `WEBPORTAL_CBASE_BUCKET` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `CBASECLUSTER_ID` bigint(20) unsigned DEFAULT NULL,
  `HCLUSTER_ID` bigint(20) unsigned DEFAULT NULL,
  `BUCKET_NAME` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `DELETED` tinyint(4) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `CREATE_USER` bigint(20) unsigned DEFAULT NULL,
  `UPDATE_USER` bigint(20) unsigned DEFAULT NULL,
  `STATUS` tinyint(4) DEFAULT NULL,
  `DESCN` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `BUCKET_TYPE` tinyint(4) DEFAULT NULL,
  `RAMQUOTAMB` varchar(20) DEFAULT NULL,
  `AUTH_TYPE` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8;
DROP TABLE WEBPORTAL_CBASECLUSTER_INFO;
CREATE TABLE `WEBPORTAL_CBASECLUSTER_INFO` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `CBASECLUSTER_NAME` varchar(200) DEFAULT NULL,
  `STATUS` tinyint(4) DEFAULT NULL,
  `HCLUSTER_ID` bigint(20) unsigned DEFAULT NULL,
  `DELETED` tinyint(4) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '???????????',
  `CREATE_USER` bigint(20) unsigned DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `UPDATE_USER` bigint(20) unsigned DEFAULT NULL,
  `ADMIN_USER` varchar(200) DEFAULT NULL,
  `ADMIN_PASSWORD` varchar(200) DEFAULT NULL,
  `TYPE` int(10) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8;
DROP TABLE WEBPORTAL_CBASE_CONTAINER;
CREATE TABLE `WEBPORTAL_CBASE_CONTAINER` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `CONTAINER_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `MOUNT_DIR` varchar(500) DEFAULT NULL,
  `IP_ADDR` varchar(15) DEFAULT NULL,
  `GATE_ADDR` varchar(15) DEFAULT NULL,
  `IP_MASK` varchar(15) DEFAULT NULL,
  `TYPE` varchar(15) DEFAULT NULL,
  `DISK_SIZE` int(11) DEFAULT NULL,
  `CORES_NUMBER` int(11) DEFAULT NULL,
  `CPU_SPEED` int(11) DEFAULT NULL,
  `MEMORY_SIZE` int(11) DEFAULT NULL,
  `HOST_ID` bigint(20) unsigned DEFAULT NULL,
  `HOST_IP` varchar(15) DEFAULT NULL,
  `CBASECLUSTER_ID` bigint(20) unsigned DEFAULT NULL,
  `STATUS` tinyint(4) DEFAULT NULL,
  `DELETED` tinyint(4) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `CREATE_USER` bigint(20) unsigned DEFAULT NULL,
  `UPDATE_USER` bigint(20) unsigned DEFAULT NULL,
  `ZABBIXHOSTS` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8;


