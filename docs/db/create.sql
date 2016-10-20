-- 项目表
-- added by xusy 2016-9-22
CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_number` varchar(50) NOT NULL,
  `project_name` varchar(255) NOT NULL COMMENT '项目名称',
  `project_desc` text COMMENT '项目描述',
  `project_owner_id` int(11) NOT NULL COMMENT '负责人id',
  `docs_path` varchar(255) DEFAULT NULL COMMENT '项目文档存放的根路径',
  `start_date` date DEFAULT NULL COMMENT '项目开始日期',
  `end_date` date DEFAULT NULL COMMENT '项目结束日期',
  `created_time` datetime DEFAULT NULL COMMENT '项目创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- 职员表
-- added by xusy 2016-9-22
CREATE TABLE `staff` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_number` varchar(20) DEFAULT NULL COMMENT '工号',
  `name` varchar(20) DEFAULT NULL COMMENT '姓名',
  `gender` varchar(5) DEFAULT NULL COMMENT '性别：0-女 1-男',
  `login_name` varchar(20) NOT NULL COMMENT '登录名',
  `password` varchar(255) NOT NULL,
  `department` varchar(50) DEFAULT NULL,
  `job_title` varchar(50) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- 项目职员关系表
-- added by xusy 2016-9-22
CREATE TABLE `project_staff` (
  `project_id` int(11) NOT NULL,
  `staff_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 部门表
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `department_name` varchar(20) NOT NULL COMMENT '部门名称',
  `code` varchar(20) DEFAULT NULL COMMENT '部门编号',
  `created_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- 子部门表
CREATE TABLE `sub_department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sub_department_name` varchar(20) NOT NULL COMMENT '子部门名称',
  `code` varchar(20) DEFAULT NULL COMMENT '子部门编号',
  `department_id` int(11) NOT NULL COMMENT '所属部门id',
  `created_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- 文件目录表
CREATE TABLE `directory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '目录名称',
  `pid` int(11) DEFAULT NULL COMMENT '父目录id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- 文件信息表
CREATE TABLE `file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(100) NOT NULL COMMENT '文件名',
  `file_path` varchar(255) DEFAULT NULL COMMENT '存储路径',
  `file_type` int(11) DEFAULT NULL,
  `dir_id` int(11) NOT NULL COMMENT '所属目录id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- 新增是否可作为项目负责人字段
-- 2016-10-11 10:42:17
ALTER TABLE staff ADD COLUMN is_prj_owner VARCHAR(5) AFTER job_title;

-- 修改员工表字段
ALTER TABLE staff DROP COLUMN department;

ALTER TABLE staff ADD COLUMN department_id INT;

-- 登录信息表
CREATE TABLE `login_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login_id` int(11) DEFAULT NULL COMMENT '登录用户id',
  `login_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `ip_adress` varchar(50) DEFAULT NULL COMMENT '登录的ip地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- 增加项目创建人字段
ALTER TABLE project ADD COLUMN project_creater_id INT;
UPDATE project SET project_creater_id = project_owner_id;

-- 角色表
CREATE TABLE `auth_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '权限角色名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- 权限表
CREATE TABLE `auth_function` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `func_name` varchar(100) DEFAULT NULL COMMENT '权限名称',
  `func_flag` varchar(255) DEFAULT NULL COMMENT '权限标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- 角色权限关联表
CREATE TABLE `auth_role_function` (
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `func_id` int(11) DEFAULT NULL COMMENT '权限id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 标识用户是否删除 1:删除 	 0:未删除
ALTER TABLE staff ADD COLUMN is_deleted INT DEFAULT 0;