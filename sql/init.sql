-- 养老系统 MVP 数据库初始化脚本
-- MySQL 8.0
-- 创建时间：2026-02-21

-- 创建数据库
CREATE DATABASE IF NOT EXISTS elderdb DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE elderdb;

-- ========================================
-- 1. 用户表 (sys_user)
-- ========================================
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    role VARCHAR(20) NOT NULL COMMENT '角色：ADMIN-管理员, NURSE-护理员, FAMILY-家属',
    dept_id BIGINT COMMENT '部门ID（预留）',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-启用 0-禁用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    UNIQUE KEY uk_username (username),
    INDEX idx_role (role),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ========================================
-- 2. 老人档案表 (elder_info)
-- ========================================
DROP TABLE IF EXISTS elder_info;
CREATE TABLE elder_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender TINYINT NOT NULL COMMENT '性别：0-女 1-男',
    birth_date DATE COMMENT '出生日期',
    id_card VARCHAR(18) UNIQUE COMMENT '身份证号',
    phone VARCHAR(20) COMMENT '联系电话',
    address VARCHAR(200) COMMENT '家庭住址',
    nursing_level VARCHAR(20) COMMENT '护理等级：一级/二级/三级',
    admission_date DATE COMMENT '入院日期',
    bed_id BIGINT COMMENT '床位ID',
    emergency_contact VARCHAR(50) COMMENT '紧急联系人',
    emergency_phone VARCHAR(20) COMMENT '紧急联系电话',
    health_status TEXT COMMENT '健康状况（JSON格式）',
    allergies TEXT COMMENT '过敏史（JSON格式）',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-在院 2-出院 3-转院',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    INDEX idx_bed_id (bed_id),
    INDEX idx_nursing_level (nursing_level),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='老人档案表';

-- ========================================
-- 3. 床位表 (bed_info)
-- ========================================
DROP TABLE IF EXISTS bed_info;
CREATE TABLE bed_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    bed_no VARCHAR(20) NOT NULL UNIQUE COMMENT '床位编号',
    room_no VARCHAR(20) NOT NULL COMMENT '房间号',
    building VARCHAR(20) COMMENT '楼栋',
    floor TINYINT COMMENT '楼层',
    type VARCHAR(20) NOT NULL COMMENT '房型：单人间/双人间/多人间',
    price DECIMAL(10,2) COMMENT '床位费',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-空闲 1-占用 2-维护',
    elder_id BIGINT COMMENT '占用老人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    INDEX idx_status (status),
    INDEX idx_floor (floor),
    INDEX idx_elder_id (elder_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='床位表';

-- ========================================
-- 4. 健康记录表 (health_record)
-- ========================================
DROP TABLE IF EXISTS health_record;
CREATE TABLE health_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    elder_id BIGINT NOT NULL COMMENT '老人ID',
    record_type VARCHAR(20) NOT NULL COMMENT '记录类型：VITAL_SIGN-生命体征, CHECKUP-体检',
    record_data JSON NOT NULL COMMENT '记录数据（JSON格式）',
    device_id VARCHAR(50) COMMENT '设备ID（预留）',
    record_time DATETIME NOT NULL COMMENT '记录时间',
    creator_id BIGINT NOT NULL COMMENT '记录人ID',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_elder_time (elder_id, record_time),
    INDEX idx_record_type (record_type),
    INDEX idx_record_time (record_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康记录表';

-- ========================================
-- 5. 护理记录表 (nursing_record)
-- ========================================
DROP TABLE IF EXISTS nursing_record;
CREATE TABLE nursing_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    elder_id BIGINT NOT NULL COMMENT '老人ID',
    nursing_type VARCHAR(20) NOT NULL COMMENT '护理类型：FEEDING-喂饭, BATHING-洗澡, TURNING-翻身',
    content TEXT NOT NULL COMMENT '护理内容描述',
    nursing_time DATETIME NOT NULL COMMENT '护理时间',
    nurse_id BIGINT NOT NULL COMMENT '护理员ID',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-完成 2-未完成（预留）',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_elder_time (elder_id, nursing_time),
    INDEX idx_nurse_id (nurse_id),
    INDEX idx_nursing_type (nursing_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理记录表';

-- ========================================
-- 6. 告警记录表 (alarm_record)
-- ========================================
DROP TABLE IF EXISTS alarm_record;
CREATE TABLE alarm_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    elder_id BIGINT COMMENT '关联老人ID（可为空）',
    alarm_type VARCHAR(20) NOT NULL COMMENT '告警类型：SOS-紧急呼叫, FALL-跌倒, VITAL_ABNORMAL-生命体征异常',
    level TINYINT NOT NULL COMMENT '告警级别：1-一般 2-重要 3-紧急',
    content VARCHAR(500) NOT NULL COMMENT '告警内容描述',
    location VARCHAR(100) COMMENT '告警位置',
    device_id VARCHAR(50) COMMENT '设备ID（预留）',
    alarm_time DATETIME NOT NULL COMMENT '告警时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '处理状态：0-待处理 1-处理中 2-已处理',
    handler_id BIGINT COMMENT '处理人ID',
    handle_time DATETIME COMMENT '处理时间',
    handle_remark VARCHAR(500) COMMENT '处理备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_alarm_time (alarm_time),
    INDEX idx_status (status),
    INDEX idx_level (level),
    INDEX idx_elder_id (elder_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='告警记录表';

-- ========================================
-- 7. 费用类型表 (fee_type)
-- ========================================
DROP TABLE IF EXISTS fee_type;
CREATE TABLE fee_type (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    fee_type VARCHAR(20) NOT NULL UNIQUE COMMENT '费用类型代码：BED_FEE, NURSING_FEE等',
    fee_name VARCHAR(50) NOT NULL COMMENT '费用名称',
    unit_price DECIMAL(10,2) COMMENT '单价',
    unit VARCHAR(10) COMMENT '计价单位：天/月/次',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-启用 0-禁用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='费用类型表';

-- ========================================
-- 8. 费用记录表 (fee_record)
-- ========================================
DROP TABLE IF EXISTS fee_record;
CREATE TABLE fee_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    elder_id BIGINT NOT NULL COMMENT '老人ID',
    fee_type_id BIGINT NOT NULL COMMENT '费用类型ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    fee_date DATE NOT NULL COMMENT '费用日期',
    period VARCHAR(10) COMMENT '费用周期（YYYY-MM）',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '支付状态：1-未支付 2-已支付',
    payment_time DATETIME COMMENT '支付时间',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_elder_id (elder_id),
    INDEX idx_fee_type_id (fee_type_id),
    INDEX idx_period (period),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='费用记录表';
