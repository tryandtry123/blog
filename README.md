# 博客系统

一个使用 Vue 3 + Spring Boot 构建的现代化博客系统。

## 技术栈

### 前端
- Vue 3 - 渐进式 JavaScript 框架
- Vite - 现代前端构建工具
- Element Plus - UI 组件库
- Pinia - 状态管理
- Vue Router - 路由管理
- Axios - HTTP 客户端

### 后端
- Spring Boot 3.2.1 - Java Web 框架
- Spring Security - 安全框架
- JWT - 用户认证
- JPA/Hibernate - ORM 框架
- MySQL - 数据库

## 功能特性

- 用户认证
  - 注册
  - 登录
  - JWT token 认证
  
- 文章管理
  - 创建文章
  - 编辑文章
  - 删除文章
  - 查看文章列表
  - 分页显示

- 用户权限
  - 基于角色的访问控制
  - 文章作者权限验证

## 项目结构

### 前端 (blog-frontend)
```
blog-frontend/
├── src/
│   ├── assets/         # 静态资源
│   ├── components/     # 通用组件
│   ├── router/         # 路由配置
│   ├── stores/         # Pinia 状态管理
│   ├── utils/          # 工具函数
│   ├── views/          # 页面组件
│   ├── App.vue         # 根组件
│   └��─ main.js         # 入口文件
├── public/             # 公共资源
├── index.html          # HTML 模板
├── package.json        # 项目配置
└── vite.config.js      # Vite 配置
```

### 后端 (blog-backend)
```
blog-backend/
├── src/main/
│   ├── java/com/blog/
│   │   ├── config/     # 配置类
│   │   ├── controller/ # 控制器
│   │   ├── model/      # 数据模型
│   │   ├── repository/ # 数据访问层
│   │   ├── security/   # 安全相关
│   │   ├── service/    # 业务逻辑层
│   │   └── BlogApplication.java
│   └── resources/
│       └── application.yml # 应用配置
└── pom.xml             # Maven 配置
```

## 快速开始

### 前端启动
```bash
cd blog-frontend
npm install
npm run dev
```

### 后端启动
1. 确保 MySQL 已安装并运行
2. 修改 `application.yml` 中的数据库配置
3. 使用 IDE 运行 `BlogApplication.java`

## API 接口

### 认证接口
- POST `/api/auth/register` - 用户注册
- POST `/api/auth/login` - 用户登录

### 文章接口
- GET `/api/articles` - 获取文章列表
- GET `/api/articles/{id}` - 获取单篇文章
- POST `/api/articles` - 创建文章
- PUT `/api/articles/{id}` - 更新文章
- DELETE `/api/articles/{id}` - 删除文章

## 数据库设计

### users 表
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    role VARCHAR(20) NOT NULL,
    created_at DATETIME
);
```

### articles 表
```sql
CREATE TABLE articles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    author_id BIGINT NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (author_id) REFERENCES users(id)
);
```

## 安全性

- 使用 Spring Security 进行安全控制
- 密码使用 BCrypt 加密存储
- JWT token 用于用户认证
- CORS 配置确保安全的跨域请求
- 基于角色的访问控制

## 开发环境要求

- Node.js 16+
- JDK 17+
- MySQL 8.0+
- Maven 3.6+

## 部署说明

### 前端部署
1. 构建生产版本：
```bash
npm run build
```
2. 将 `dist` 目录部署到 Web 服务器

### 后端部署
1. 打包：
```bash
mvn clean package
```
2. 运行 JAR 文件：
```bash
java -jar target/blog-backend-1.0-SNAPSHOT.jar
```

## 贡献指南

1. Fork 项目
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 许可证

MIT License 
