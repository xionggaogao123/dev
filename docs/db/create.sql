-- 项目表
-- added by xusy 2016-9-22
CREATE TABLE `project` (
  `id` int(11) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 职员表
-- added by xusy 2016-9-22
CREATE TABLE `staff` (
  `id` int(11) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 项目职员关系表
-- added by xusy 2016-9-22
CREATE TABLE `project_staff` (
  `project_id` int(11) NOT NULL,
  `staff_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;