# 养老管理系统

面向养老院的综合管理系统，提供老人档案管理、健康监测、护理服务、餐饮管理、活动组织、费用管理等全方位功能，同时支持家属端查看老人状态和互动。

## 项目结构

```
eldercare/
├── eldercare-mvp/          # 后端服务（Spring Boot）
├── eldercare-family/        # 家属端应用（uni-app）
└── 文档/                   # 项目文档
```

## 系统架构

### 技术栈

#### 后端技术栈
- **框架**: Spring Boot 3.1.4
- **数据库**: MySQL 8.0+
- **ORM**: Spring Data JPA
- **认证授权**: JWT (JSON Web Token)
- **构建工具**: Maven 3.x
- **Java版本**: JDK 17+
- **文件存储**: 本地存储
- **API文档**: 可集成Swagger

#### 前端技术栈
- **框架**: uni-app (Vue 3)
- **UI组件**: uni-ui
- **HTTP请求**: 自封装request
- **图表**: ECharts
- **状态管理**: Vuex/Pinia
- **构建工具**: HBuilderX / Vite

### 系统分层架构

```
┌─────────────────────────────────────────────────────────────┐
│                        前端展示层                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  家属端APP   │  │   护理端APP  │  │  管理后台    │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                        API网关层                           │
│              JWT认证 + 统一响应格式                         │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                      控制器层 (Controller)                  │
│       RESTful API接口 + 参数验证 + 统一异常处理              │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                       服务层 (Service)                      │
│           业务逻辑处理 + 事务管理 + 业务规则                 │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                     数据访问层 (Repository)                  │
│           Spring Data JPA + 自定义查询                     │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                      数据库层 (MySQL)                      │
│              业务数据 + 用户数据 + 事务数据                  │
└─────────────────────────────────────────────────────────────┘
```

## 功能模块

### 一、老人档案管理模块

**核心功能：**
- 老人基本信息管理（姓名、年龄、性别、联系方式）
- 入院评估记录（护理等级、健康状况、特殊需求）
- 床位分配与管理
- 紧急联系人信息
- 健康档案关联

**主要接口：**
- `GET /api/elders` - 分页查询老人列表
- `GET /api/elders/{id}` - 获取老人详情
- `POST /api/elders` - 创建老人档案
- `PUT /api/elders/{id}` - 更新老人档案
- `DELETE /api/elders/{id}` - 删除老人档案
- `POST /api/elders/{elderId}/assign-bed` - 分配床位
- `POST /api/elders/{elderId}/release-bed` - 释放床位

---

### 二、健康监测模块

**核心功能：**
- 健康数据记录（血压、心率、血糖、体温、体重）
- 健康趋势分析（折线图展示）
- 健康数据汇总统计
- 异常数据预警
- 健康报告生成

**主要接口：**
- `GET /api/health-records` - 分页查询健康记录
- `GET /api/health-records/elder/{elderId}/summary` - 获取健康汇总
- `GET /api/health-records/trend` - 获取健康趋势
- `POST /api/health-records` - 创建健康记录

---

### 三、护理服务模块

**核心功能：**
- 护理记录（日常护理、特殊护理、康复训练）
- 护理计划制定与执行
- 护理质量评价
- 护理统计分析
- 护理工作排班

**主要接口：**
- `GET /api/nursing-records` - 分页查询护理记录
- `GET /api/nursing-records/statistics` - 获取护理统计
- `POST /api/nursing-records` - 创建护理记录
- `POST /api/nursing-records/{id}/evaluation` - 提交评价

---

### 四、报警管理模块

**核心功能：**
- 实时报警记录（跌倒、紧急呼叫、健康异常）
- 报警处理流程
- 报警统计分析
- 报警历史查询
- 报警关联健康记录

**主要接口：**
- `GET /api/alarm-records` - 分页查询报警记录
- `GET /api/alarm-records/unhandled-count` - 获取未处理数量
- `POST /api/alarm-records/{id}/handle` - 处理报警
- `GET /api/alarm-records/related/health` - 关联健康记录

---

### 五、饮食食谱模块

**核心功能：**
- 每日食谱管理（早餐、午餐、晚餐）
- 餐品图片展示
- 营养成分分析（热量、蛋白质、碳水、脂肪）
- 餐品评价与反馈
- 一周食谱安排
- 饮食偏好管理

**主要接口：**
- `GET /api/meal-plans` - 获取餐品计划列表
- `GET /api/meal-plans/weekly` - 获取本周食谱
- `GET /api/meal-plans/{id}` - 获取餐品详情
- `POST /api/meal-plans` - 创建餐品（支持图片上传）
- `GET /api/meal-feedbacks` - 获取评价列表
- `POST /api/meal-feedbacks` - 提交评价

---

### 六、活动运动分享模块

**核心功能：**
- 活动动态发布（早操、广场舞、棋牌、团队活动）
- 图片和视频上传
- 活动心得分享
- 点赞、评论互动
- 热门动态推荐
- 置顶动态

**主要接口：**
- `GET /api/activity-posts` - 获取活动动态列表
- `GET /api/activity-posts/hot` - 获取热门动态
- `GET /api/activity-posts/pinned` - 获取置顶动态
- `POST /api/activity-posts` - 发布动态（支持多图+视频）
- `POST /api/activity-posts/{id}/like` - 点赞/取消点赞
- `GET /api/activity-comments/post/{postId}` - 获取评论
- `POST /api/activity-comments` - 发布评论/回复

---

### 七、费用管理模块

**核心功能：**
- 费用类型管理（住宿费、护理费、餐饮费、医疗费）
- 费用记录管理
- 缴费管理
- 费用统计分析
- 欠费提醒
- 缴费历史查询

**主要接口：**
- `GET /api/fees` - 分页查询费用记录
- `GET /api/fees/summary` - 获取费用概览
- `POST /api/fees/{id}/pay` - 缴费
- `GET /api/fees/payment-history` - 获取缴费历史

---

### 八、床位管理模块

**核心功能：**
- 床位信息管理（楼栋、楼层、床位号）
- 床位状态管理（空闲、占用、维修）
- 床位类型管理
- 床位查询与统计

**主要接口：**
- `GET /api/beds` - 分页查询床位列表
- `POST /api/beds` - 创建床位
- `PUT /api/beds/{id}/status` - 更新床位状态

---

### 九、消息通知模块

**核心功能：**
- 消息推送（系统通知、健康提醒、缴费通知）
- 消息分类展示
- 未读消息提醒
- 消息标记已读
- 消息历史查询

**主要接口：**
- `GET /api/messages` - 分页查询消息
- `POST /api/messages/{id}/read` - 标记已读
- `GET /api/messages/unread-count` - 获取未读数量

---

### 十、用户管理模块

**核心功能：**
- 用户账号管理（管理员、护理员、家属）
- 角色权限管理
- 个人信息管理
- 密码修改
- 用户设置

**主要接口：**
- `GET /api/users` - 分页查询用户
- `POST /api/users` - 创建用户
- `GET /api/auth/login` - 用户登录
- `POST /api/auth/change-password` - 修改密码

---

### 十一、数据统计模块

**核心功能：**
- 仪表盘数据汇总
- 老人数量统计
- 床位占用率统计
- 健康数据统计
- 护理工作统计
- 报警数据统计

**主要接口：**
- `GET /api/dashboard/stats` - 获取系统统计数据

---

### 十二、班次排班模块

**核心功能：**
- 护理员排班管理
- 班次类型设置
- 排班日历展示
- 排班调整

**主要接口：**
- `GET /api/shift-schedules` - 获取排班列表
- `POST /api/shift-schedules` - 创建排班

---

## 数据库设计

### 核心数据表

| 表名 | 说明 | 主要字段 |
|------|------|---------|
| sys_user | 用户表 | id, username, password, real_name, role, elder_id |
| elder | 老人档案表 | id, name, gender, age, phone, address, nursing_level |
| bed | 床位表 | id, building, floor, room_number, bed_number, status |
| health_record | 健康记录表 | id, elder_id, blood_pressure, heart_rate, blood_sugar, body_temperature |
| nursing_record | 护理记录表 | id, elder_id, nursing_type, content, caregiver |
| alarm_record | 报警记录表 | id, elder_id, alarm_type, status, handle_by, handle_notes |
| meal_plan | 餐品计划表 | id, meal_name, meal_type, meal_date, image_url, calories, protein, carbs, fat |
| meal_feedback | 餐品评价表 | id, meal_plan_id, elder_id, rating, comment, feedback_type |
| activity_post | 活动动态表 | id, elder_id, activity_type, title, content, images, videos, like_count, comment_count |
| activity_comment | 活动评论表 | id, post_id, parent_id, user_id, content, reply_to_user_id |
| activity_like | 活动点赞表 | id, post_id, user_id |
| fee_record | 费用记录表 | id, elder_id, fee_type, amount, status, payment_date |
| message | 消息表 | id, type, title, content, is_read, user_id |
| shift_schedule | 排班表 | id, nurse_id, shift_type, shift_date, status |

## 接口规范

### 统一响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    // 业务数据
  }
}
```

### 状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权/Token失效 |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

### 分页参数

| 参数 | 类型 | 说明 | 默认值 |
|------|------|------|--------|
| page | Integer | 页码 | 0 |
| size | Integer | 每页数量 | 10 |

### 分页响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "list": [],
    "total": 100,
    "size": 10,
    "page": 0
  }
}
```

## 部署说明

### 后端部署

```bash
# 进入后端目录
cd eldercare-mvp/backend

# 修改数据库配置
# 编辑 src/main/resources/application.yml

# 编译打包
mvn clean package -DskipTests

# 运行
java -jar target/eldercare-backend-0.0.1-SNAPSHOT.jar
```

**环境要求：**
- JDK 17+
- MySQL 8.0+
- Maven 3.x

### 前端部署

```bash
# 使用HBuilderX打开 eldercare-family 项目
# 运行到浏览器/小程序/App
```

**开发工具：**
- HBuilderX 3.x

### 数据库初始化

```sql
-- 创建数据库
CREATE DATABASE eldercare CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 导入SQL脚本（如有）
```

## 配置说明

### 后端配置 (application.yml)

```yaml
server:
  port: 7777

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/eldercare?useUnicode=true&characterEncoding=utf8
    username: root
    password: your_password

jwt:
  secret: your-secret-key
  expiration: 86400000  # 24小时
```

### 前端配置

```javascript
// api/request.js
const BASE_URL = 'http://localhost:7777/api'
```

## 开发指南

### 后端开发

1. 创建实体类 (Entity)
2. 创建数据访问接口 (Repository)
3. 创建DTO类
4. 创建服务层 (Service)
5. 创建控制器 (Controller)
6. 配置路由和权限

### 前端开发

1. 创建API接口文件 (api/*.js)
2. 创建页面组件 (pages/*/*.vue)
3. 配置路由 (pages.json)
4. 引入组件和样式

## 项目文档

- [需求规格说明书](./需求规格说明书.md)
- [养老系统设计方案](./养老系统设计方案.md)
- [数据库设计文档](./数据库设计文档.md)
- [MVP开发任务清单](./MVP开发任务清单.md)
- [业界方案技术对比](./业界方案技术对比.md)
- [后端启动指南](./后端启动指南.md)
- [前端启动指南](./前端启动指南.md)
- [家属端APP设计文档](./家属端APP设计文档.md)
- [项目交付总结](./项目交付总结.md)

## 联系方式

如有问题，请联系项目维护团队。

## 许可证

[MIT License](LICENSE)
