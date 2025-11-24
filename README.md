📚 University Course Management System
A full-stack web application built with Spring Boot that allows teachers to manage course materials and students to access learning resources securely.

🎯 What This Project Does
This is a complete online learning platform where:
- Teachers can upload lectures, create polls, and manage course content
- Students can access materials, vote in polls, and participate in discussions
- Admin can manage users and system settings

✨ Key Features
🔐 Authentication & Security
- User registration, login, and logout
- Role-based access control (Admin, Teacher, Student)
- Spring Security implementation

👨‍🏫 Teacher Features
- Add/delete course materials and lectures
- Create and manage polls
- Moderate comments
- Edit user information
- Batch upload multiple files

👨‍🎓 Student Features
- Access lectures and materials
- Participate in polls
- Post comments on lecture and poll content
- View personal voting and comment history
- Update profile information

🌐 Additional Features
- Multi-language support
- Voting history tracking
- Comment history for users 
- Responsive web design

🔧 Technical Stack
Backend:
- Spring Boot
- Spring Security
- Spring Data JPA
- H2 Database
- Gradle Build Tool

Frontend:
- JSP (Java Server Pages) with fragments
- HTML5 & CSS3
- JavaScript
- Internationalization (i18n)
  
Architecture:
- DAO Pattern for data access
- DTO Pattern for data transfer
- Custom validators
- Exception handling


Installation & Running
1. Clone the repository
   git clone https://github.com/jyoti786J/SpringBootCourseWeb.git
   cd SpringBootCourseWeb

2. ./gradlew bootRun
3. Access the System
   - Main Application: http://localhost:8080
   - Database Console: http://localhost:8080/h2-console

Database Configuration
spring.datasource.url=jdbc:h2:file:./Data/myDB;AUTO_SERVER=TRUE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

👤 Default Accounts
Username	Password	Role	Access Level
admin	admin123	ADMIN	Full system access
mary	marypw	TEACHER	Lecture management
john	johnpw	STUDENT	Student features
jojo	jojopw	STUDENT	Student features

📁 Project Structure
src/main/java/hkmu/wadd/
├── controller/          # Web controllers & routing
├── service/            # Business logic layer  
├── dao/                # Database operations (Data Access Objects)
├── model/              # Data models (User, Course, Poll, etc.)
├── dto/                # Data Transfer Objects
├── config/             # Application configuration
├── validator/          # Input validation
├── exception/          # Custom exception handling
└── view/               # View models

src/main/resources/
├── static/css/         # CSS stylesheets
├── i18n/               # Multi-language support (English & Chinese)
├── sql/                # Database scripts
└── application.properties

src/main/webapp/WEB-INF/jsp/
├── fragments/          # JSP template fragments
└── *.jsp              # JSP view files

src/main/js/            # JavaScript files



















