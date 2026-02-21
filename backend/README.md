# 养老系统后端服务

基于 Spring Boot 开发的养老管理系统后端服务，提供完整的老人护理、健康管理、餐饮服务、活动管理等功能。

## 技术栈

- **框架**: Spring Boot 3.1.4
- **数据库**: MySQL + Spring Data JPA
- **认证**: JWT
- **构建工具**: Maven
- **Java版本**: Java 17+

## 服务端口

- 默认端口: 7777
- 配置文件: `src/main/resources/application.yml`

## 功能模块

### 1. 认证授权模块 (`/api/auth`)

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/auth/login` | POST | 用户登录，返回JWT token |
| `/api/auth/change-password` | POST | 修改密码 |
| `/api/auth/current-user` | GET | 获取当前登录用户信息 |

### 2. 仪表盘模块 (`/api/dashboard`)

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/dashboard/stats` | GET | 获取系统统计数据 |

**统计数据包括**:
- 老人总数、床位总数、已占用床位数
- 健康记录数、护理记录数
- 待处理/已处理报警数
- 费用记录数、餐饮记录数
- 班次排班数、活动数、用户数

### 3. 老人档案模块 (`/api/elders`)

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/elders` | GET | 分页查询老人列表 |
| `/api/elders/{id}` | GET | 获取老人详情 |
| `/api/elders` | POST | 创建老人档案 |
| `/api/elders/{id}` | PUT | 更新老人档案 |
| `/api/elders/{id}` | DELETE | 删除老人档案 |
| `/api/elders/{elderId}/assign-bed` | POST | 分配床位 |
| `/api/elders/{elderId}/release-bed` | POST | 释放床位 |
| `/api/elders/stats` | GET | 获取老人统计数据 |

### 4. 床位管理模块 (`/api/beds`)

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/beds` | GET | 分页查询床位列表 |
| `/api/beds/{id}` | GET | 获取床位详情 |
| `/api/beds` | POST | 创建床位 |
| `/api/beds/{id}` | PUT | 更新床位信息 |
| `/api/beds/{id}` | DELETE | 删除床位 |
| `/api/beds/{id}/status` | PUT | 更新床位状态 |

### 5. 健康档案模块 (`/api/health-records`)

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/health-records` | GET | 分页查询健康记录 |
| `/api/health-records/{id}` | GET | 获取健康记录详情 |
| `/api/health-records` | POST | 创建健康记录 |
| `/api/health-records/{id}` | PUT | 更新健康记录 |
| `/api/health-records/{id}` | DELETE | 删除健康记录 |
| `/api/health-records/elder/{elderId}/summary` | GET | 获取老人健康汇总（平均血压、心率、血糖、体温） |
| `/api/health-records/trend` | GET | 获取健康趋势数据 |
| `/api/health-records/related/nursing` | GET | 获取关联护理记录 |

### 6. 护理记录模块 (`/api/nursing-records`)

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/nursing-records` | GET | 分页查询护理记录 |
| `/api/nursing-records/{id}` | GET | 获取护理记录详情 |
| `/api/nursing-records` | POST | 创建护理记录 |
| `/api/nursing-records/{id}` | PUT | 更新护理记录 |
| `/api/nursing-records/{id}` | DELETE | 删除护理记录 |
| `/api/nursing-records/statistics` | GET | 获取护理统计数据 |
| `/api/nursing-records/{id}/evaluation` | POST | 提交护理评价 |

### 7. 报警记录模块 (`/api/alarm-records`)

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/alarm-records` | GET | 分页查询报警记录 |
| `/api/alarm-records/{id}` | GET | 获取报警记录详情 |
| `/api/alarm-records` | POST | 创建报警记录 |
| `/api/alarm-records/{id}` | PUT | 更新报警记录 |
| `/api/alarm-records/{id}` | DELETE | 删除报警记录 |
| `/api/alarm-records/{id}/handle` | POST | 处理报警 |
| `/api/alarm-records/unhandled-count` | GET | 获取未处理报警数量 |
| `/api/alarm-records/related/health` | GET | 获取关联健康记录 |
| `/api/alarm-records/history` | GET | 获取报警历史记录 |

### 8. 餐饮记录模块 (`/api/dining-records`)

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/dining-records` | GET | 分页查询餐饮记录 |
| `/api/dining-records/{id}` | GET | 获取餐饮记录详情 |
| `/api/dining-records` | POST | 创建餐饮记录 |
| `/api/dining-records/{id}` | PUT | 更新餐饮记录 |
| `/api/dining-records/{id}` | DELETE | 删除餐饮记录 |
| `/api/dining-records/meal-plan` | GET | 获取餐饮计划（早中晚餐安排） |
| `/api/dining-records/{id}/feedback` | POST | 提交餐饮反馈 |
| `/api/dining-records/nutrition-report` | GET | 获取营养报告 |

### 9. 活动管理模块 (`/api/activities`)

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/activities` | GET | 分页查询活动列表 |
| `/api/activities/{id}` | GET | 获取活动详情 |
| `/api/activities` | POST | 创建活动 |
| `/api/activities/{id}` | PUT | 更新活动信息 |
| `/api/activities/{id}` | DELETE | 删除活动 |
| `/api/activities/{id}/join` | POST | 报名参加活动 |
| `/api/activities/{id}/cancel` | POST | 取消报名 |
| `/api/activities/joined` | GET | 获取已报名活动列表 |

### 10. 活动报名模块 (`/api/activity-enrollments`)

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/activity-enrollments` | GET | 分页查询报名记录 |
| `/api/activity-enrollments/{id}` | GET | 获取报名详情 |
| `/api/activity-enrollments` | POST | 活动报名 |
| `/api/activity-enrollments/{id}/cancel` | PUT | 取消报名 |
| `/api/activity-enrollments/{id}` | DELETE | 删除报名记录 |

### 11. 费用管理模块 (`/api/fees`)

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/fees` | GET | 分页查询费用记录 |
| `/api/fees/{id}` | GET | 获取费用记录详情 |
| `/api/fees` | POST | 创建费用记录 |
| `/api/fees/{id}` | PUT | 更新费用记录 |
| `/api/fees/{id}` | DELETE | 删除费用记录 |
| `/api/fees/{id}/pay` | POST | 缴费 |
| `/api/fees/elder/{elderId}/total` | GET | 获取老人费用总额 |
| `/api/fees/fee-types` | GET | 获取所有费用类型 |
| `/api/fees/summary` | GET | 获取费用概览 |
| `/api/fees/payment-history` | GET | 获取缴费历史记录 |

### 12. 消息中心模块 (`/api/messages`)

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/messages` | GET | 分页查询消息列表 |
| `/api/messages/{id}` | GET | 获取消息详情 |
| `/api/messages/{id}/read` | POST | 标记消息已读 |
| `/api/messages/read-all` | POST | 全部标记已读 |
| `/api/messages/unread-count` | GET | 获取未读消息数量 |
| `/api/messages/{id}` | DELETE | 删除消息 |

### 13. 用户管理模块 (`/api/users`)

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/users` | GET | 分页查询用户列表 |
| `/api/users/{id}` | GET | 获取用户详情 |
| `/api/users` | POST | 创建用户 |
| `/api/users/{id}` | PUT | 更新用户信息 |
| `/api/users/{id}` | DELETE | 删除用户 |
| `/api/users/info` | GET | 获取当前用户信息 |
| `/api/users/update` | PUT | 更新当前用户信息 |
| `/api/users/elders` | GET | 获取关联老人列表 |
| `/api/users/settings` | POST | 更新用户设置 |
| `/api/users/clear-cache` | POST | 清除缓存 |
| `/api/users/feedback` | POST | 提交反馈 |
| `/api/users/unread-count` | GET | 获取未读消息数量 |

### 14. 班次排班模块 (`/api/shift-schedules`)

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/shift-schedules` | GET | 分页查询排班列表 |
| `/api/shift-schedules/{id}` | GET | 获取排班详情 |
| `/api/shift-schedules` | POST | 创建排班 |
| `/api/shift-schedules/{id}` | PUT | 更新排班信息 |
| `/api/shift-schedules/{id}` | DELETE | 删除排班 |

## 统一响应格式

所有接口返回统一的 JSON 格式：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {...}
}
```

## 启动方式

### 开发环境启动
```bash
mvn spring-boot:run
```

### 打包后启动
```bash
mvn clean package
java -jar target/eldercare-backend-0.0.1-SNAPSHOT.jar
```

## 数据库配置

在 `application.yml` 中配置数据库连接：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/eldercare?useUnicode=true&characterEncoding=utf8&useSSL=false
    username: root
    password: your_password
```

## JWT 认证

登录成功后会返回 JWT Token，后续请求需要在 Header 中携带：

```
Authorization: Bearer {token}
```

以下接口无需认证：
- `/api/auth/login`

其他接口均需要携带有效的 JWT Token。
