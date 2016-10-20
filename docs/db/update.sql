-- 角色相关数据
INSERT INTO `auth_role` VALUES (1, 'superAdmin');
INSERT INTO `auth_role` VALUES (2, 'admin');
INSERT INTO `auth_role` VALUES (3, 'user');

-- 权限相关数据
INSERT INTO `auth_function` VALUES (1, '创建项目', 'PROJECT_INSERT');
INSERT INTO `auth_function` VALUES (2, '修改项目', 'PROJECT_UPDATE');
INSERT INTO `auth_function` VALUES (3, '查看项目', 'PROJECT_READ');
INSERT INTO `auth_function` VALUES (4, '删除项目', 'PROJECT_DELETE');
INSERT INTO `auth_function` VALUES (5, '查看所有项目', 'PROJECT_READ_ALL');
INSERT INTO `auth_function` VALUES (6, '查看员工', 'STAFF_READ');
INSERT INTO `auth_function` VALUES (7, '修改员工', 'STAFF_UPDATE');
INSERT INTO `auth_function` VALUES (8, '添加员工', 'STAFF_INSERT');
INSERT INTO `auth_function` VALUES (9, '删除员工', 'STAFF_DELETE');
INSERT INTO `auth_function` VALUES (10, '创建目录', 'DIRECTORY_INSERT');
INSERT INTO `auth_function` VALUES (11, '修改目录', 'DIRECTORY_UPDATE');
INSERT INTO `auth_function` VALUES (12, '删除目录', 'DIRECTORY_DELETE');
INSERT INTO `auth_function` VALUES (13, '查看目录结构', 'DIRECTORY_READ');
INSERT INTO `auth_function` VALUES (14, '上传文件', 'FILE_INSERT');
INSERT INTO `auth_function` VALUES (15, '下载文件', 'FILE_READ');
INSERT INTO `auth_function` VALUES (16, '删除文件', 'FILE_DELETE');
INSERT INTO `auth_function` VALUES (17, '修改文件', 'FILE_UPDATE');

-- 角色权限关联数据
INSERT INTO `auth_role_function` VALUES (2, 3);
INSERT INTO `auth_role_function` VALUES (2, 1);
INSERT INTO `auth_role_function` VALUES (2, 2);
INSERT INTO `auth_role_function` VALUES (2, 4);
INSERT INTO `auth_role_function` VALUES (2, 6);
INSERT INTO `auth_role_function` VALUES (2, 10);
INSERT INTO `auth_role_function` VALUES (2, 11);
INSERT INTO `auth_role_function` VALUES (2, 12);
INSERT INTO `auth_role_function` VALUES (2, 14);
INSERT INTO `auth_role_function` VALUES (2, 15);
INSERT INTO `auth_role_function` VALUES (2, 17);
INSERT INTO `auth_role_function` VALUES (2, 16);
INSERT INTO `auth_role_function` VALUES (3, 3);
INSERT INTO `auth_role_function` VALUES (3, 15);
INSERT INTO `auth_role_function` VALUES (1, 3);
INSERT INTO `auth_role_function` VALUES (1, 1);
INSERT INTO `auth_role_function` VALUES (1, 2);
INSERT INTO `auth_role_function` VALUES (1, 4);
INSERT INTO `auth_role_function` VALUES (1, 6);
INSERT INTO `auth_role_function` VALUES (1, 7);
INSERT INTO `auth_role_function` VALUES (1, 8);
INSERT INTO `auth_role_function` VALUES (1, 9);
INSERT INTO `auth_role_function` VALUES (1, 10);
INSERT INTO `auth_role_function` VALUES (1, 11);
INSERT INTO `auth_role_function` VALUES (1, 12);
INSERT INTO `auth_role_function` VALUES (1, 14);
INSERT INTO `auth_role_function` VALUES (1, 15);
INSERT INTO `auth_role_function` VALUES (1, 16);
INSERT INTO `auth_role_function` VALUES (1, 17);