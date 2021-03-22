SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for base_git_commit_file
-- ----------------------------
DROP TABLE IF EXISTS `base_git_commit_file`;
CREATE TABLE `base_git_commit_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `record_id` bigint(20) DEFAULT NULL,
  `job_id` bigint(20) DEFAULT NULL,
  `change_type` varchar(10) DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for base_project_user
-- ----------------------------
DROP TABLE IF EXISTS `base_project_user`;
CREATE TABLE `base_project_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project` bigint(255) DEFAULT NULL,
  `user` bigint(255) DEFAULT NULL,
  `time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=685 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for base_user_department
-- ----------------------------
DROP TABLE IF EXISTS `base_user_department`;
CREATE TABLE `base_user_department` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `project_name` varchar(255) DEFAULT NULL,
  `master` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of base_user_department
-- ----------------------------
BEGIN;
INSERT INTO `base_user_department` VALUES (1, '管理组', 'hawk-admin', 1);
COMMIT;

-- ----------------------------
-- Table structure for base_user_info
-- ----------------------------
DROP TABLE IF EXISTS `base_user_info`;
CREATE TABLE `base_user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 DEFAULT '$2a$10$HNFVDbZWY0VgwgscZ4OjuuNHbXzM/qCWxxdRde0zAC723iV/d6sDy',
  `department_id` bigint(20) DEFAULT NULL,
  `dingding` varchar(255) DEFAULT NULL,
  `auth_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_email_unque` (`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of base_user_info
-- ----------------------------
BEGIN;
INSERT INTO `base_user_info` VALUES (1, 'admin', NULL, 'admin@hawk.com', '$2a$10$HNFVDbZWY0VgwgscZ4OjuuNHbXzM/qCWxxdRde0zAC723iV/d6sDy', 1, NULL, 'admin');
COMMIT;

-- ----------------------------
-- Table structure for container_project_runtime_config
-- ----------------------------
DROP TABLE IF EXISTS `container_project_runtime_config`;
CREATE TABLE `container_project_runtime_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `build_image` varchar(255) DEFAULT NULL,
  `build_type` varchar(255) DEFAULT NULL,
  `data_image` varchar(255) DEFAULT NULL,
  `mount_path` varchar(255) DEFAULT NULL,
  `runtime_type` varchar(255) DEFAULT NULL,
  `volume` varchar(255) DEFAULT NULL,
  `data_shared_volume` varchar(255) DEFAULT NULL,
  `app_image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for container_service_configuration
-- ----------------------------
DROP TABLE IF EXISTS `container_service_configuration`;
CREATE TABLE `container_service_configuration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(20) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `env` text CHARACTER SET utf8 COLLATE utf8_bin,
  `inner_port` int(10) DEFAULT NULL,
  `service_name` varchar(255) DEFAULT NULL,
  `tag` varchar(20) DEFAULT NULL,
  `performance_level` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT 'NOMARL',
  `branch` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `debug` tinyint(1) unsigned zerofill DEFAULT '0',
  `descn` varchar(255) DEFAULT NULL,
  `ssh` tinyint(1) unsigned zerofill DEFAULT '0',
  `ssh_password` varchar(255) DEFAULT NULL,
  `hosts` text CHARACTER SET utf8 COLLATE utf8_bin,
  `jprofiler` tinyint(1) DEFAULT NULL,
  `pre_start` text CHARACTER SET utf8 COLLATE utf8_bin,
  `property` text CHARACTER SET utf8 COLLATE utf8_bin,
  `extra_ports` varchar(100) DEFAULT NULL,
  `volume_id` bigint(20) DEFAULT NULL,
  `namespace` varchar(255) DEFAULT NULL,
  `project_type` varchar(255) DEFAULT NULL,
  `health_check_path` varchar(200) DEFAULT NULL,
  `log_path` varchar(200) DEFAULT NULL,
  `failure_threshold` int(4) DEFAULT '3',
  `initial_delay_seconds` int(4) DEFAULT '10',
  `period_seconds` int(4) DEFAULT '10',
  `nginx_location` text CHARACTER SET utf8 COLLATE utf8_bin,
  `update_time` timestamp NULL DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `mesh` tinyint(1) DEFAULT NULL,
  `profile` varchar(255) DEFAULT NULL,
  `health_check` tinyint(1) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FKdn3nsyppiae70u8nsnj5kx82b` (`volume_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for container_service_manager
-- ----------------------------
DROP TABLE IF EXISTS `container_service_manager`;
CREATE TABLE `container_service_manager` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `service_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_service_id` (`service_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for container_service_volume
-- ----------------------------
DROP TABLE IF EXISTS `container_service_volume`;
CREATE TABLE `container_service_volume` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mount_path` varchar(500) DEFAULT NULL,
  `volume_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for message_event
-- ----------------------------
DROP TABLE IF EXISTS `message_event`;
CREATE TABLE `message_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT ' ',
  `department_id` bigint(20) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `time` timestamp NULL DEFAULT NULL,
  `message` text CHARACTER SET utf8 COLLATE utf8_bin,
  `instance_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `project_index` (`project_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for monitor_measurement_group
-- ----------------------------
DROP TABLE IF EXISTS `monitor_measurement_group`;
CREATE TABLE `monitor_measurement_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
  `enable` tinyint(1) DEFAULT '1',
  `measurements` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of monitor_measurement_group
-- ----------------------------
BEGIN;
INSERT INTO `monitor_measurement_group` VALUES (1, 'system-cpu', 1, '[1]', 'cpu');
INSERT INTO `monitor_measurement_group` VALUES (2, 'system-memory', 1, '[2,3,12]', '内存');
INSERT INTO `monitor_measurement_group` VALUES (3, 'system-network', 1, '[4,5]', '网络');
INSERT INTO `monitor_measurement_group` VALUES (4, 'system-disk', 1, '[6,7]', '存储');
INSERT INTO `monitor_measurement_group` VALUES (24, 'jvm-heap', 1, '[8,9,72,73,74]', '堆内存');
INSERT INTO `monitor_measurement_group` VALUES (25, 'jvm-nonheap', 1, '[10,11,75,76]', '非堆内存');
INSERT INTO `monitor_measurement_group` VALUES (27, 'jvm-gc-time', 1, '[77,78]', 'GC时间');
INSERT INTO `monitor_measurement_group` VALUES (28, 'jvm-gc-count', 1, '[79,80]', 'GC次数');
INSERT INTO `monitor_measurement_group` VALUES (29, 'jvm-thread', 1, '[81,82,83,84]', '线程数');
INSERT INTO `monitor_measurement_group` VALUES (30, 'jvm-class', 1, '[85,86]', 'Java类');
COMMIT;

-- ----------------------------
-- Table structure for monitor_measurement_template
-- ----------------------------
DROP TABLE IF EXISTS `monitor_measurement_template`;
CREATE TABLE `monitor_measurement_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scale` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
  `template` varchar(500) CHARACTER SET utf8mb4 DEFAULT NULL,
  `enable` tinyint(1) DEFAULT '1',
  `name` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of monitor_measurement_template
-- ----------------------------
BEGIN;
INSERT INTO `monitor_measurement_template` VALUES (1, 'percent', 'sum(rate(container_cpu_usage_seconds_total{pod=\"$POD\", image != \"\"}[$INTERVAL])) by (pod_name) * 100', 1, 'cpu_user_seconds', 'cpu使用率');
INSERT INTO `monitor_measurement_template` VALUES (2, 'storage', 'container_memory_usage_bytes{pod=\"$POD\", image=\"\"}', 1, 'memory_usage', '内存已使用');
INSERT INTO `monitor_measurement_template` VALUES (3, 'storage', 'kube_pod_container_resource_limits_memory_bytes{pod=\"$POD\", image=\"\", container=\"app\"}', 1, 'memory_limit', '内存配额');
INSERT INTO `monitor_measurement_template` VALUES (4, 'rate', 'sum(rate(container_network_receive_bytes_total{pod=\"$POD\"}[$INTERVAL]))', 1, 'network_in', '网络流量(IN)');
INSERT INTO `monitor_measurement_template` VALUES (5, 'rate', 'sum(rate(container_network_transmit_bytes_total{pod=\"$POD\"}[$INTERVAL]))', 1, 'network_out', '网络流量(OUT)');
INSERT INTO `monitor_measurement_template` VALUES (6, 'rate', 'sum(rate(container_fs_reads_bytes_total{pod=\"$POD\", image != \"\"}[$INTERVAL]))', 1, 'disk_read', '磁盘读取');
INSERT INTO `monitor_measurement_template` VALUES (7, 'rate', 'sum(rate(container_fs_writes_bytes_total{pod=\"$POD\", image != \"\"}[$INTERVAL]))', 1, 'disk_write', '磁盘写入');
INSERT INTO `monitor_measurement_template` VALUES (8, 'storage', 'jvm_memory_bytes_used{pod=\"$POD\", area=\"heap\"}', 1, 'jvm_heap_usage', '堆内存(已使用)');
INSERT INTO `monitor_measurement_template` VALUES (9, 'storage', 'jvm_memory_bytes_max{pod=\"$POD\", area=\"heap\"}', 1, 'jvm_heap_max', '堆内存(最大)');
INSERT INTO `monitor_measurement_template` VALUES (10, 'storage', 'jvm_memory_bytes_max{pod=\"$POD\", area=\"nonheap\"}', 1, 'jvm_nonheap_max', '非堆内存(最大)');
INSERT INTO `monitor_measurement_template` VALUES (11, 'storage', 'jvm_memory_bytes_used{pod=\"$POD\", area=\"nonheap\"}', 1, 'jvm_nonheap_usage', '非堆内存(已使用)');
INSERT INTO `monitor_measurement_template` VALUES (12, 'storage', 'container_memory_cache{pod=\"$POD\", image=\"\"}', 1, 'memory_cache_page', '缓存');
INSERT INTO `monitor_measurement_template` VALUES (72, 'storage', 'jvm_memory_pool_bytes_used{pod=\"$POD\",image=\"\", pool=\"Par Eden Space\"}', 1, 'jvm_heap_eden_used', 'Par_Eden_Space');
INSERT INTO `monitor_measurement_template` VALUES (73, 'storage', 'jvm_memory_pool_bytes_used{pod=\"$POD\",image=\"\", pool=\"Par Survivor Space\"}', 1, 'jvm_heap_survivor_used', 'Par_Survivor_Space');
INSERT INTO `monitor_measurement_template` VALUES (74, 'storage', 'jvm_memory_pool_bytes_used{pod=\"$POD\",image=\"\", pool=\"CMS Old Gen\"}', 1, 'jvm_heap_cms_old_used', 'CMS_Old_Gen');
INSERT INTO `monitor_measurement_template` VALUES (75, 'storage', 'jvm_memory_pool_bytes_used{pod=\"$POD\",image=\"\", pool=\"Metaspace\"}', 1, 'jvm_heap_metaspace_used', 'Metaspace');
INSERT INTO `monitor_measurement_template` VALUES (76, 'storage', 'jvm_memory_pool_bytes_used{pod=\"$POD\",image=\"\", pool=\"Code Cache\"}', 1, 'jvm_heap_codecache_used', 'Code_Cache');
INSERT INTO `monitor_measurement_template` VALUES (77, 'time', 'rate(jvm_gc_collection_seconds_sum{pod=\"$POD\", gc=\"ParNew\"}[$INTERVAL])', 1, 'jvm_gc_parnew_time', 'GC_ParNew时间');
INSERT INTO `monitor_measurement_template` VALUES (78, 'time', 'rate(jvm_gc_collection_seconds_sum{pod=\"$POD\", gc=\"ConcurrentMarkSweep\"}[$INTERVAL])', 1, 'jvm_gc_cms_time', 'GC_CMS时间');
INSERT INTO `monitor_measurement_template` VALUES (79, 'count', 'increase(jvm_gc_collection_seconds_count{pod=\"$POD\", gc=\"ParNew\"}[$INTERVAL])', 1, 'jvm_gc_parnew_count', 'GC_ParNew次数');
INSERT INTO `monitor_measurement_template` VALUES (80, 'count', 'increase(jvm_gc_collection_seconds_count{pod=\"$POD\", gc=\"ConcurrentMarkSweep\"}[$INTERVAL])', 1, 'jvm_gc_cms_count', 'GC_CMS次数');
INSERT INTO `monitor_measurement_template` VALUES (81, 'count', 'jvm_threads_current{pod=\"$POD\"}', 1, 'jvm_thread_current_count', '当前线程数');
INSERT INTO `monitor_measurement_template` VALUES (82, 'count', 'jvm_threads_daemon{pod=\"$POD\"}', 1, 'jvm_thread_daemon_count', '守护线程数');
INSERT INTO `monitor_measurement_template` VALUES (83, 'count', 'jvm_threads_peak{pod=\"$POD\"}', 1, 'jvm_thread_peak_count', '峰值线程数');
INSERT INTO `monitor_measurement_template` VALUES (84, 'count', 'jvm_threads_deadlocked{pod=\"$POD\"}', 1, 'jvm_thread_deadlocked_count', '死锁线程数');
INSERT INTO `monitor_measurement_template` VALUES (85, 'count', 'jvm_classes_loaded{pod=\"$POD\"}', 1, 'jvm_class_loaded', '已加载类数');
INSERT INTO `monitor_measurement_template` VALUES (86, 'count', 'jvm_classes_unloaded_total{pod=\"$POD\"}', 1, 'jvm_classes_unloaded', '已卸载类数');
COMMIT;

-- ----------------------------
-- Table structure for project_app
-- ----------------------------
DROP TABLE IF EXISTS `project_app`;
CREATE TABLE `project_app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(20) DEFAULT NULL,
  `branch` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
  `app_name` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
  `app_path` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for project_build_job
-- ----------------------------
DROP TABLE IF EXISTS `project_build_job`;
CREATE TABLE `project_build_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(20) NOT NULL,
  `tag` varchar(100) DEFAULT NULL,
  `state` varchar(10) DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT NULL,
  `env` varchar(2000) DEFAULT NULL,
  `creator_name` varchar(255) DEFAULT NULL,
  `creator` bigint(20) DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `commit` varchar(255) DEFAULT NULL,
  `sub_apps` varchar(255) DEFAULT NULL,
  `commit_log_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_project_id` (`project_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for project_build_job_stage
-- ----------------------------
DROP TABLE IF EXISTS `project_build_job_stage`;
CREATE TABLE `project_build_job_stage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) NOT NULL,
  `success` tinyint(1) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `job_stage` varchar(20) DEFAULT NULL,
  `data` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index` (`job_id`,`title`,`job_stage`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for project_build_post
-- ----------------------------
DROP TABLE IF EXISTS `project_build_post`;
CREATE TABLE `project_build_post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `instance_id` bigint(20) DEFAULT NULL,
  `project_build_id` bigint(20) DEFAULT NULL,
  `instance_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for project_git_codebase
-- ----------------------------
DROP TABLE IF EXISTS `project_git_codebase`;
CREATE TABLE `project_git_codebase` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `protocol` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for project_git_commit_record
-- ----------------------------
DROP TABLE IF EXISTS `project_git_commit_record`;
CREATE TABLE `project_git_commit_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) DEFAULT NULL,
  `branch` varchar(100) DEFAULT NULL,
  `author` varchar(50) DEFAULT NULL,
  `commit_name` varchar(50) DEFAULT NULL,
  `commit_time` datetime DEFAULT NULL,
  `commit_email` varchar(255) DEFAULT NULL,
  `message` varchar(5000) DEFAULT NULL,
  `version_number` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for project_info
-- ----------------------------
DROP TABLE IF EXISTS `project_info`;
CREATE TABLE `project_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code_base_id` bigint(20) DEFAULT NULL,
  `command` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `department_id` bigint(20) DEFAULT NULL,
  `descn` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `project_runtime` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `group_name` varchar(255) DEFAULT NULL,
  `output` varchar(255) DEFAULT NULL,
  `mode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for project_shared
-- ----------------------------
DROP TABLE IF EXISTS `project_shared`;
CREATE TABLE `project_shared` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project` bigint(255) DEFAULT NULL,
  `user` bigint(255) DEFAULT NULL,
  `time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

SET FOREIGN_KEY_CHECKS = 1;
