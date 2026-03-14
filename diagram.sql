CREATE DATABASE task_management_db;
use task_management_db;

CREATE TABLE `tbl_user` (
  `id` bigint PRIMARY KEY,
  `name` varchar(50),
  `username` varchar(50) UNIQUE,
  `email` varchar(100) UNIQUE,
  `password` varchar(255),
  `status` ENUM('ACTIVE','INACTIVE','BANNED') DEFAULT 'INACTIVE',
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `role_id` bigint
);

CREATE TABLE `tbl_role` (
  `id` bigint PRIMARY KEY,
  `name` varchar(50) UNIQUE,
  `description` varchar(255),
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `tbl_task` (
  `id` bigint PRIMARY KEY,
  `title` varchar(50) UNIQUE,
  `description` varchar(255),
  `status` ENUM('TODO','IN_PROGRESS','DONE') DEFAULT 'IN_PROGRESS',
  `deadline` timestamp,
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` bigint
);

CREATE TABLE `tbl_audit_log` (
  `id` bigint PRIMARY KEY,
  `action` ENUM('CREATE','UPDATE','DELETE','LOGIN') DEFAULT 'LOGIN',
  `entity_type` varchar(50),
  `entity_id` bigint,
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  `user_id` bigint
);

ALTER TABLE `tbl_user` ADD FOREIGN KEY (`role_id`) REFERENCES `tbl_role` (`id`);
ALTER TABLE `tbl_task` ADD FOREIGN KEY (`user_id`) REFERENCES `tbl_user` (`id`);
ALTER TABLE `tbl_audit_log` ADD FOREIGN KEY (`user_id`) REFERENCES `tbl_user` (`id`);
show index from tbl_user;

