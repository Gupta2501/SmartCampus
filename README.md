# 🎓 SmartCampus — Student Management Platform

Welcome to **SmartCampus**, a simple yet scalable platform for managing student data, academic records, and administrative tasks.  
Built with a **JavaFX frontend** and a **Spring Boot backend**, this project is ideal for learning full-stack Java development.

---

## 🚀 Key Features
- **Student Records Management**: Add, edit, view, and delete student profiles.
- **Course Management**: Manage courses, enrollments, and grades.
- **Authentication & Authorization**: Secure login and role-based access control.
- **Database Integration**: Persistent storage using relational databases (MySQL/PostgreSQL).
- **Modular Design**: JavaFX handles the UI, while Spring Boot powers the API.

---

## 🛠️ Tech Stack
- **Frontend**: JavaFX
- **Backend**: Spring Boot, Spring Data JPA
- **Database**: MySQL or PostgreSQL
- **Build Tools**: Maven
- **Version Control**: Git

---

## 🏗️ Project Structure
```
student/
├── java-fx-client/    # Frontend (JavaFX client)
│   ├── src/
│   ├── pom.xml
│   ├── .gitignore
├── java-server/       # Backend (Spring Boot server)
│   ├── src/
│   ├── createsql/     # Database creation scripts
│   ├── initdb.txt     # Sample DB data
│   ├── pom.xml
│   ├── .gitignore
├── .gitignore         # Top-level gitignore
├── README.md          # This file
```

---

## ⚙️ Getting Started

### 1. Prerequisites
- Java 17+
- Maven
- MySQL/PostgreSQL
- (Optional) SceneBuilder for JavaFX UI editing

### 2. Clone the Repository
```bash
git clone https://github.com/Gupta2501/smartcampus.git
cd student
```

### 3. Setup the Backend
- Configure your database settings inside `application.properties`.
- Run the SQL scripts inside `createsql/` and `initdb.txt` to initialize the database.
- Start the Spring Boot server:
  ```bash
  cd java-server
  mvn spring-boot:run
  ```

### 4. Setup the Frontend
- Navigate to the frontend directory:
  ```bash
  cd ../java-fx-client
  ```
- Build and run the JavaFX client from your IDE or using Maven commands.

---

## 📄 License
This project is licensed under the MIT License.

---


## 📌 Notes
- Make sure to update database configurations before running the backend.
- This project does not include Docker by default, but Docker support can be added easily.
