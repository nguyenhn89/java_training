🧩 Java Training Project
📘 Giới thiệu

Dự án mẫu Spring Boot được xây dựng nhằm mục đích học tập và thực hành các kỹ thuật backend Java, bao gồm:

🧠 Spring Web — REST Controller & MVC

🗄️ Spring Data JPA — kết nối với MySQL

🔐 Spring Security + JWT — xác thực & phân quyền

⚙️ Custom Repository — xử lý giao dịch thủ công (manual transaction)

⚙️ Yêu cầu môi trường
Thành phần	Phiên bản khuyến nghị
☕ Java	17 (JDK 17)
🧱 Gradle	8.x
🐬 MySQL	8.x
💻 IDE	IntelliJ IDEA / Eclipse / VSCode
🚀 Spring Boot	3.x
🧠 Các chức năng chính

✅ Khởi tạo project Spring Boot base Gradle

🧩 Xây dựng module Products CRUD

🔑 Tích hợp JWT Authentication

💾 Sử dụng Spring Data JPA / Hibernate

🧠 Có custom repository và transaction management

🚀 Hướng dẫn chạy API với Postman

Base URL:
🟢 https://java-training.onrender.com

1️⃣ Đăng ký tài khoản

POST /api/auth/register
Body (JSON):

{
"userName": "test123",
"password": "123456"
}

2️⃣ Đăng nhập

POST /api/auth/login
Body (JSON):

{
"userName": "test123",
"password": "123456"
}


Response (ví dụ):

{
"token": "Bearer eyJhbGciOiJIUzI1NiIsInR..."
}

3️⃣ Lấy danh sách sản phẩm

GET /api/products/
Headers:

Authorization: Bearer <JWT_TOKEN>

4️⃣ Lấy sản phẩm theo category

GET /api/products/product_with_category/10
Headers:

Authorization: Bearer <JWT_TOKEN>

5️⃣ Cập nhật sản phẩm

PUT /api/products/1
Headers:

Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json


Body (JSON ví dụ):

{
"name": "New Product Name",
"price": 1200,
"categoryId": 2
}

6️⃣ Xóa sản phẩm

DELETE /api/products/15
Headers:

Authorization: Bearer <JWT_TOKEN>