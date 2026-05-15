# 健身房管理系统 (Gym Management System)

一个基于 Spring Boot 和 Vue 3 的前后端分离健身房管理系统，提供员工管理、会员管理、数据统计等功能。

## 技术栈

### 后端
- **Spring Boot** 
- **MyBatis**
- **MySQL** 
- **Maven** 

### 前端
- **Vue 3** 
- **Vite** 
- **Element Plus**
- **Axios**
- **Pinia/Vuex**

## 功能特性

-  管理员登录认证
-  员工管理（待实现）
-  会员管理（增删改查）
-  数据统计展示
-  **正则化搜索**

## 正则化搜索功能

本系统支持基本的正则化搜索功能，能够灵活地筛选员工数据。

### 支持的搜索模式

| 模式 | 说明 | 示例 |
|------|------|------|
| `*[包含]*` | 包含指定文本 | `*张*` - 姓名中包含"张" |
| `[前缀]*` | 以指定文本开头 | `健身*` - 职位以"健身"开头 |
| `*[后缀]` | 以指定文本结尾 | `*优秀` - 备注以"优秀"结尾 |
| `[数值]>N` | 大于指定数值 | `>30` - 年龄大于30岁 |
| `完整文本` | 精确匹配 | `经理` - 职位完全等于"经理" |

### 使用场景示例

#### 1. 模糊搜索
```
搜索字段：姓名
输入值：*张*
结果：张三、张小明、张伟等所有姓名包含"张"的员工
```

#### 2. 前缀匹配
```
搜索字段：职位
输入值：健身*
结果：健身教练、健身顾问等以"健身"开头的职位
```

#### 3. 数值比较
```
搜索字段：年龄
输入值：>25
结果：所有年龄大于25岁的员工
```

## 开发环境搭建

### 后端启动

克隆项目
```bash
git clone <repository-url>
cd gym-management-system
```

初始化数据库
```bash
mysql -u root -p < create_db.sql
```

索引优化
```bash

```

配置数据库连接
编辑 `src/main/resources/application.yaml`，修改数据库配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database
    username: your_username
    password: your_password
```

启动后端
```bash
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

### 前端启动

进入前端目录
```bash
cd gym-management-frontend
```

安装依赖
```bash
npm install
```

启动开发服务器
```bash
npm run dev
```

前端应用将在 `http://localhost:5173` 启动

## API

### 员工管理接口（待实现）

- `GET /api/employee/list` - 获取员工列表
- `POST /api/employee/add` - 新增员工
- `PUT /api/employee/update` - 更新员工
- `DELETE /api/employee/{id}` - 删除员工
- `POST /api/employee/search` - 正则化搜索员工

### 会员管理接口

- `GET /api/member/list` - 获取会员列表
- `POST /api/member/add` - 新增会员
- `PUT /api/member/update` - 更新会员
- `DELETE /api/member/{id}` - 删除会员
- `POST /api/member/search` - 正则化搜索会员

