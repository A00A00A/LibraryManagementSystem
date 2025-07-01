# 书籍共享与交流平台

本项目是旨在开发一个图书管理系统与书籍共享与交流平台，打破时间和空间的界限，促进导师与学生之间、以及学生之间的阅读交流与心得分享，从而提升教育氛围和学术互动。

## 一、 概述

平台的使用者主要分为三类角色：

### 1.1 导师

- **核心身份与圈子构建**
导师作为一个阅读圈子的核心，其所带学生自动组成圈子。系统支持将导师的消息及时传递给所属学生，利于学习与讨论。

- **在线推荐与点评**
与现实教学中传帮带相似，导师可在平台上推荐优秀书籍，点评学生的读书感悟，营造轻松愉快的交流氛围。

- **时空不受限**
导师无需局限于固定地点或时间，可随时在线与学生互动，真正实现信息与交流的实时传递。

- **自主管理个人主页及圈子**
导师不仅可管理个人主页，还拥有维护和管理其所属圈子的权限，同时享有与普通阅读者相同的浏览书库等基本权限。

### 1.2 学生

- **专属导师 & 多好友关系**
每位学生仅拥有一名导师（配合学校导师制），但可以拥有多个圈内同学及好友。学生可以在讨论区向导师提问，或在个人主页中分享心得体会，并接受来自他人的点评。

- **交流与互动的平台**
平台为学生提供了一个交流思想与感悟的场所，有利于彼此的相互了解和共同进步。

- **基本操作权限**
学生拥有维护个人空间、查看书库等权限。同时，当被导师邀请加入圈子后，还可以参与圈内活动和讨论。

### 1.3 管理员

- **全局管理与安全维护**
管理员拥有系统内绝对权限，负责维护平台的稳定性和安全性，确保平台信息与内容符合法律规定。

- **权限操作功能**
管理员的操作权限包括删除不符合法律规定的用户、论坛帖子、操作日志、书籍信息以及书评。管理员的存在确保平台能够高效地对违规信息进行筛查和处理。

## 二、 项目目标与设计理念

本项目通过构建读书会书籍共享平台，力图达到以下目标：

1. **基础功能稳定运行**
平台所有核心模块（登录、注册、用户与书籍管理、信息展示等）均保持正常和稳定运行。

2. **信息发布及显示迅速准确**
针对图书信息、用户动态等数据，确保平台能够迅速、准确地将信息发布到用户界面上。

3. **操作便利与交互流畅**
设计直观的用户界面，使用户在使用平台时操作简单、流程流畅，提升整体用户体验。

4. **信息交流通畅**
通过导师与学生之间的即时消息、论坛讨论等方式，保证用户之间的信息沟通无障碍。

5. **权限系统完善**
根据不同用户角色（导师、学生、管理员），提供精细化的权限控制，使各项操作都有据可依，并保障平台安全。

6. **扩展性与个性化服务**
在满足基础需求的基础上，适当拓展额外功能，提供更个性化、全面的服务。

## 三、 技术架构与实现

平台基于 Java 技术栈开发，核心组件包括：

- **后端：**
- Java SE (JDK11+)
- Spring Framework / Spring Boot（实现控制器、服务和数据访问层）
- SQLite作为数据库，结合 JDBC 框架进行数据持久化
- DAO 设计模式封装数据库操作
- JUnit 进行单元测试

- **(预期)前端：**
- HTML、CSS、JavaScript
- MVC 架构模式分离业务逻辑与界面显示

- **安全：**
- 密码采用 SHA-256 加密
- 角色权限控制实现 RBAC 模型

- **部署：**
- 使用 Maven 进行依赖管理和项目构建
- 本地开发环境建议使用 vsCode

## 四、 安装与使用

### 4.1 环境配置

- **JDK：** JDK11 或更高版本
- **构建工具：** Maven 3+
- **数据库：** SQLite

### 4.2 测试账号

- **测试管理员：** 用户admin，密码ynu#rjgc。(软件工程)

## 五、 项目结构

```
书籍共享与交流平台/
├── src/
│ └── org/
│ └── example/
│ ├── Main.java
│ ├── GlobalState.java
│ ├── AbstractAction.java
│ ├── AbstractAuthenticatedAction.java
│ ├── User.java
│ ├── UserDAO.java
│ ├── UserService.java
│ ├── BookService.java
│ ├── CircleService.java
│ │
│ ├── Actions/
│ │ ├── ManagePersonalSpaceAction.java
│ │ ├── ManageUserAction.java
│ │ ├── ManageBookAction.java
│ │ └── ManageCircleAction.java
│ │
│ └── GUI/
│ ├── LoginFrame.java
│ ├── RegisterFrame.java
│ ├── MenuFrame.java
│ ├── BookFrame.java
│ ├── CircleFrame.java
│ ├── UserFrame.java
│ └── PersonalSpaceFrame.java
└── README.md
```

### 1. 主流程及全局状态
- **Main.java**
- **作用**：项目入口，初始化全局状态（GlobalState）、各 Service（UserService、BookService、CircleService）以及操作（Action）列表，并启动登录界面（LoginFrame）。
- **调用逻辑**：
- `initialize()` 方法中创建 Service 对象和全局状态对象；
- 用户登录完成后，根据登录结果以及 GlobalState 中的角色动态构造操作列表（例如管理员会另外加载 ManageUserAction）；
- 最终通过菜单界面（MenuFrame）展示各个模块的操作入口。
- **GlobalState.java**
- **作用**：保存当前登录用户的用户名、角色、认证状态及系统运行标识，供系统各模块调用以实现权限控制与状态检测。

### 2. 用户模块
包含用户实体及数据访问和业务逻辑：
- **User.java**
- 存储用户名、密码和用户角色。
- **UserDAO.java**
- 封装对数据库中用户数据的 CRUD 操作及密码加密。
- **UserService.java**
- 提供注册、更新、删除和登录验证等业务方法。
- **ManageUserAction.java**
- 仅管理员可使用，管理系统中所有用户的信息。
- **UserFrame.java**
- 为管理员提供用户管理界面，显示所有用户及提供增删改操作。

### 3. 图书模块
- **BookService.java**
- 负责管理书籍信息，包括增加、删除、修改和查询。
- **SQLiteBookDAO**（位于 `org.example.sqlite` 包下）
- 采用 SQLite 数据库存储书籍数据，BookService 通过该 DAO 层进行数据库操作。
- **ManageBookAction.java** 和 **BookFrame.java**
- 分别负责图书管理操作的业务调用和图形界面的展示。

### 4. 圈子模块
- **CircleService.java**
- 管理圈子、圈子成员和圈子消息的业务逻辑。
- **SQLiteCircleDAO**
- 采用 SQLite 数据库进行数据持久化，提供 CRUD 操作。
- **ManageCircleAction.java** 和 **CircleFrame.java**
- 对应业务调用和图形界面展示，实现圈子信息、成员管理、消息发布等功能。

### 5. 个人空间模块
- **PersonalSpaceFrame.java**
- 为用户提供修改密码的个人空间界面，不再展示或修改用户权限。
- **功能说明**：
- 显示当前登录用户的用户名（只读）；
- 提供两个密码输入框供用户输入新密码和确认密码。
- 密码须满足至少 8 个字符且包含大写字母、小写字母、数字和标点符号的要求；
- 使用 Swing Timer 每 10 秒自动刷新数据，确保信息实时更新。（此处自动刷新数据的设计由于没有用已经废弃）
- **ManagePersonalSpaceAction.java**
- 操作类，用于调用 PersonalSpaceFrame 界面。
- 在菜单中显示“个人空间”入口，由用户点击后调用 PersonalSpaceFrame 进行操作。

### 6. 操作（Actions）模块
- 包含所有操作类，均继承自 AbstractAction 或 AbstractAuthenticatedAction：
- **AbstractAction.java**：定义统一接口，由各具体操作类实现。
- **AbstractAuthenticatedAction.java**：扩展 AbstractAction，增加认证相关方法。
- **ManagePersonalSpaceAction.java**、**ManageUserAction.java**、**ManageBookAction.java**、**ManageCircleAction.java**
- 各自调用对应模块的 GUI 界面，执行业务操作。
- 在 Main.run() 中，依据 GlobalState 判断并动态加载操作列表，确保只有符合权限要求的操作显示出来。

### 7. GUI 组件
- **LoginFrame.java**
- 登录窗口，模拟邮箱密码输入，验证用户身份后更新 GlobalState。
- **RegisterFrame.java**
- 提供注册新用户的界面，包括用户名、密码和密码确认，进行必要的格式校验。
- **MenuFrame.java**
- 主菜单界面，显示所有操作入口（如个人空间、图书管理、圈子管理、用户管理等），支持动态加载和右上角退出确认；退出时会提示确认，并根据用户操作决定是否结束整个进程。
- **其他界面**：
- **BookFrame.java**、**CircleFrame.java**、**UserFrame.java** 等分别负责具体模块的管理界面展示。

---

## 六、 系统调用逻辑

1. **应用启动**
- 主类 Main.java 初始化 GlobalState、UserService、BookService、CircleService 及部分操作类（如 ManagePersonalSpaceAction、ManageBookAction、ManageCircleAction）；由于权限尚未确定，管理员相关的 ManageUserAction 在登录前不加载。

2. **用户登录**
- LoginFrame 提供登录界面，用户输入用户名和密码后调用 UserService.login() 验证身份。验证成功后更新 GlobalState 中的用户名、角色和认证状态。

3. **菜单构建与显示**
- 登录完成后，Main.run() 根据 GlobalState 动态重新构造操作列表：
- 始终加载个人空间、图书管理、圈子管理等操作；
- 如果当前用户为管理员，则额外加载用户管理（ManageUserAction）操作。
- MenuFrame 接收操作列表并显示在界面中，用户通过双击列表项调用相应操作的 run() 方法。

4. **操作执行**
- 用户选择某个操作，如管理书籍、圈子、用户或进入个人空间，各操作类会打开相应的 GUI 界面（例如 BookFrame、CircleFrame、UserFrame、PersonalSpaceFrame）执行具体的 CRUD 操作。

5. **个人空间功能**
- 个人空间界面 PersonalSpaceFrame 允许用户仅修改密码（保留密码复杂度要求），并利用 Swing Timer 实现数据的实时刷新，确保界面同步最新数据。

6. **退出处理**
- 在 MenuFrame（及其它 GUI 界面）中，右上角退出按钮触发窗口关闭事件，系统弹出确认对话框，若用户确认退出，则调用 System.exit(0) 结束整个 JVM 进程，确保所有后台线程和资源释放。

---

## 七、 主要功能总结

- **登录与注册**：支持用户使用用户名、密码通过 LoginFrame 登录，或通过 RegisterFrame 注册新账号；登录后更新 GlobalState。
- **图书管理**：提供图书管理界面（BookFrame），管理员可修改书籍信息。
- **圈子管理**：提供圈子、圈子成员及圈子消息管理界面（CircleFrame），支持导师构建圈子，学生参与讨论。
- **用户管理**：仅管理员可使用，ManageUserAction 和 UserFrame 提供用户信息的查看与管理。
- **个人空间**：通过 PersonalSpaceFrame，用户可修改密码且实时刷新数据；不显示或修改权限信息。
- **动态操作加载**：在登录后根据 GlobalState 动态加载操作菜单，确保管理员权限和其它角色功能正确区分。
- **退出机制**：通过窗口关闭确认对话框和 System.exit(0) 确保程序安全退出。

本项目采用分层架构设计，前端界面由 Swing 构建，实现了用户登录、注册、图书、圈子、用户与个人空间模块。系统通过 GlobalState 管理当前用户状态，并动态加载与权限相关的操作入口，确保不同角色（导师、学生、管理员）获得相应的功能操作。各模块严格遵循设计原则和最佳实践，确保平台基础功能稳定运行，同时提供良好的用户体验。项目目标在于打破时间与空间的限制，促进导师与学生之间的信息交流，并为用户提供一个安全、便捷、易扩展的书籍共享平台。