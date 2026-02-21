-- 初始化数据脚本
-- MySQL 8.0
-- 创建时间：2026-02-21

USE elderdb;

-- ========================================
-- 插入默认管理员用户
-- 用户名: admin
-- 密码: admin123
-- ========================================
INSERT INTO sys_user (username, password, real_name, phone, role, status) VALUES
('admin', '$2a$10$YVpEwZ3qZqZqZqZqZqZqZeZqZqZqZqZqZqZqZqZqZqZqZqZqZqZqZ', '系统管理员', '13800138000', 'ADMIN', 1);

-- ========================================
-- 插入测试护理员
-- 用户名: nurse01
-- 密码: nurse123
-- 用户名: nurse02
-- 密码: nurse123
-- ========================================
INSERT INTO sys_user (username, password, real_name, phone, role, status) VALUES
('nurse01', '$2a$10$ABC123abcABC123abcABC123abcABC123abcABC123abcABC123abcABC123abcABC', '张护士', '13900139001', 'NURSE', 1),
('nurse02', '$2a$10$ABC123abcABC123abcABC123abcABC123abcABC123abcABC123abcABC123abcABC', '李护士', '13900139002', 'NURSE', 1);

-- ========================================
-- 密码重置SQL (如果需要重置密码，请直接运行DataLoader或使用以下语句)
-- ========================================
-- 管理员密码重置为 admin123:
-- UPDATE sys_user SET password = '$2a$10$YourValidBCryptHashHere' WHERE username = 'admin';

-- ========================================
-- 插入费用类型
-- ========================================
INSERT INTO fee_type (fee_type, fee_name, unit_price, unit, status) VALUES
('BED_FEE', '床位费', 3000.00, '月', 1),
('NURSING_FEE', '护理费', 1500.00, '月', 1),
('MEAL_FEE', '餐费', 600.00, '月', 1),
('MEDICAL_FEE', '医疗费', NULL, '次', 1),
('OTHER', '其他', NULL, '次', 1);

-- ========================================
-- 插入测试床位数据
-- ========================================
INSERT INTO bed_info (bed_no, room_no, building, floor, type, price, status) VALUES
('A-101-1', '101', 'A栋', 1, '单人间', 3000.00, 0),
('A-101-2', '102', 'A栋', 1, '单人间', 3000.00, 0),
('A-201-1', '201', 'A栋', 2, '双人间', 2000.00, 0),
('A-201-2', '201', 'A栋', 2, '双人间', 2000.00, 0),
('B-101-1', '101', 'B栋', 1, '多人间', 1500.00, 0),
('B-101-2', '101', 'B栋', 1, '多人间', 1500.00, 0),
('B-101-3', '101', 'B栋', 1, '多人间', 1500.00, 0);

-- ========================================
-- 插入测试老人数据
-- ========================================
INSERT INTO elder_info (name, gender, birth_date, id_card, phone, address, nursing_level, admission_date, emergency_contact, emergency_phone, health_status, allergies, status) VALUES
('王奶奶', 0, '1945-05-15', '310101194505150001', '13800138001', '上海市黄浦区南京东路123号', '一级', '2024-01-15', '王小明', '13800138002', '{"diseases":["高血压","糖尿病"],"chronicDiseases":["高血压","糖尿病"],"mentalStatus":"清醒","mobility":"需协助"}', '{"drugAllergies":["青霉素"],"foodAllergies":["海鲜"],"other":"无"}', 1),
('李爷爷', 1, '1943-08-20', '310101194308200002', '13800138003', '上海市黄浦区人民路456号', '二级', '2024-02-01', '李小红', '13800138004', '{"diseases":["冠心病"],"chronicDiseases":["冠心病"],"mentalStatus":"清醒","mobility":"需协助"}', '{"drugAllergies":[],"foodAllergies":[],"other":"无"}', 1),
('张阿姨', 0, '1950-11-10', '310101195011100003', '13800138005', '上海市黄浦区福州路789号', '三级', '2024-02-10', '张小华', '13800138006', '{"diseases":[],"chronicDiseases":[],"mentalStatus":"清醒","mobility":"基本自理"}', '{"drugAllergies":[],"foodAllergies":["芒果"],"other":"无"}', 1);
