-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS `gym_management_system`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- 2. 使用数据库
USE `gym_management_system`;

-- 3. 创建 admin 表
CREATE TABLE IF NOT EXISTS admin (
    admin_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '管理员ID',
    admin_account VARCHAR(50) NOT NULL UNIQUE COMMENT '管理员账号',
    admin_password VARCHAR(100) NOT NULL COMMENT '管理员密码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 4. 插入默认管理员数据 ID: 1, 账号: admin, 密码: 123456
INSERT INTO admin (admin_id, admin_account, admin_password) VALUES
(1, 'admin', '123456');

-- 5. 创建 classes 表
CREATE TABLE IF NOT EXISTS classes (
    class_id INT PRIMARY KEY COMMENT '课程ID',
    class_name VARCHAR(50) NOT NULL COMMENT '课程名称',
    class_begin DATETIME COMMENT '开始时间',
    class_time VARCHAR(20) COMMENT '时长',
    coach TEXT COMMENT '教练'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健身课程表';

-- 5.5. 插入数据
INSERT INTO classes (class_id, class_name, class_begin, class_time, coach) VALUES
(1, '增肌', '2026-01-01 15:00', '60分钟', '琨哥教练'),
(2, '减脂', '2026-01-02 10:20', '90分钟', '琨哥教练'),
(3, '瑜伽', '2026-03-02 18:00', '45分钟', '琨哥教练'),
(4, '运动康复', '2026-02-02 10:00', '90分钟', '运动康教练'),
(5, '综合格斗', '2026-02-03 15:00', '60分钟', '综合格斗教练'),
(6, '塑形', '2026-03-03 15:00', '60分钟', '塑形教练'),
(7, '普拉提', '2026-03-03 17:30', '90分钟', '普拉提教练'),
(8, '爵士舞', '2026-02-22 09:00', '60分钟', '爵士舞教练'),
(9, '杠铃操', '2026-02-04 15:00', '60分钟', '杠铃操教练'),
(10, '动感单车', '2026-03-03 15:00', '45分钟', '动感单车教练'),
(11, '健美操', '2026-02-22 18:00', '60分钟', '健美操教练');

-- 6. 创建 member 表
CREATE TABLE IF NOT EXISTS member (
    member_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '会员ID',
    member_password VARCHAR(100) NOT NULL DEFAULT '123456' COMMENT '会员密码',
    member_name VARCHAR(50) COMMENT '会员姓名',
    member_gender VARCHAR(10) COMMENT '会员性别',
    member_age INT COMMENT '会员年龄',
    member_height INT COMMENT '会员身高(cm)',
    memberweight INT COMMENT '会员体重(kg)',
    member_phone BIGINT COMMENT '会员电话',
    card_time VARCHAR(50) COMMENT '开卡时间',
    card_class INT DEFAULT 0 COMMENT '剩余课程数',
    card_next_class INT DEFAULT 0 COMMENT '下次课程编号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员表';

-- 7. 插入测试会员数据
INSERT INTO member (member_name, member_gender, member_age, member_height, memberweight, member_phone, card_time, card_class, card_next_class) VALUES
('张三', '男', 25, 175, 70, 13800138001, '2026-01-01', 10, 1),
('李四', '女', 28, 165, 55, 13800138002, '2026-01-05', 8, 2),
('王五', '男', 30, 180, 75, 13800138003, '2026-02-01', 12, 3);

-- 8. 创建 employee 表（员工表）
CREATE TABLE IF NOT EXISTS employee (
    employee_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '员工ID',
    employee_name VARCHAR(50) NOT NULL COMMENT '员工姓名',
    employee_gender VARCHAR(10) COMMENT '员工性别',
    employee_age INT COMMENT '员工年龄',
    entry_time VARCHAR(50) COMMENT '入职时间',
    staff VARCHAR(50) COMMENT '职位',
    employee_message TEXT COMMENT '员工备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- 9. 插入测试员工数据
INSERT INTO employee (employee_name, employee_gender, employee_age, entry_time, staff, employee_message) VALUES
('琨哥教练', '男', 35, '2025-01-01', '健身教练', '资深健身教练，拖拖拖锟斤拷'),
('运动康教练', '男', 32, '2025-03-15', '康复教练', '运动康复专家'),
('瑜老师', '女', 28, '2025-06-01', '瑜伽教练', '专业瑜伽教练'),
('张伟', '男', 30, '2025-02-01', '健身教练', '擅长增肌训练'),
('李娜', '女', 26, '2025-03-10', '瑜伽教练', '资深瑜伽导师'),
('王强', '男', 28, '2025-04-05', '助教', '工作认真负责'),
('刘芳', '女', 24, '2025-05-15', '前台', '服务态度好'),
('陈明', '男', 35, '2024-11-01', '经理', '健身房负责人'),
('赵雪', '女', 29, '2025-06-20', '瑜伽教练', '擅长冥想课程'),
('孙浩', '男', 32, '2025-01-15', '康复教练', '运动康复专家'),
('周婷', '女', 25, '2025-07-01', '前台', '形象气质佳'),
('吴磊', '男', 27, '2025-08-10', '助教', '辅助教练经验丰富'),
('郑洁', '女', 31, '2025-02-28', '保洁', '负责场地清洁'),
('黄鹏', '男', 33, '2024-12-15', '健身教练', '国家一级运动员'),
('林梅', '女', 23, '2025-09-01', '前台', '微笑服务标兵'),
('徐洋', '男', 29, '2025-04-22', '助教', '耐心指导新会员'),
('高峰', '男', 36, '2024-10-01', '经理', '运营管理经验丰富');