Java Training Project
+ Giới thiệu

Dự án mẫu Spring Boot được xây dựng nhằm mục đích học tập và thực hành các kỹ thuật backend Java, bao gồm:

- Khởi tạo project Spring Boot base Gradle

- Xây dựng module Products CRUD

- Tích hợp JWT Authentication and Authorization

- Sử dụng Spring Data JPA / Hibernate

- Có custom repository và transaction management

⚙️ Yêu cầu môi trường
Thành phần	Phiên bản khuyến nghị
+ Java	17 (JDK 17)
+ Gradle	8.x
+ MySQL	8.x
+ IDE	IntelliJ IDEA / Eclipse / VSCode
+ Spring Boot	3.x
+ Các chức năng chính

- Hướng dẫn chạy API với Postman

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

7️⃣ Tạo sản phẩm

Post /api/products 
Body (JSON ví dụ): 

{
"name": "product1",
"price": 1000,
"categoryId": 32,
"content" : "macbook m1",
"memo": "123"
} 
Authorization: Bearer <JWT_TOKEN> 
Content-Type: application/json 

- Hướng dẫn kết nối navicat vào server PostgreSQL:
+  host: dpg-d3mvr4m3jp1c73d4g7i0-a.oregon-postgres.render.com
+  Initial Database: java_training
+  user: root
+  password: KlePxNt8ZxxGoyFseMgMDG3rhO3MXHeB
+  port: 5432

8.
- Tìm kiếm theo tên, danh mục , giá và nhiều điều kiện cùng lúc
- Hỗ trợ phân trang (pagination) và sắp xếp (sorting).

search use JpaSpecificationExecutor
GET api/products/search_criteria_api?name=product&minPrice=10&maxPrice=500&categoryId=1&page=0&size=5&sort=price,asc

search use Criteria API
GET api/products/search_jpa_specification_executor?name=product&minPrice=10&maxPrice=500&categoryId=1&page=0&size=5&sort=price,asc
Headers:
Authorization: Bearer <JWT_TOKEN>
9.
-Thống kê số lượng sản phẩm theo danh mục
api/products/count-by-category
Authorization: Bearer <JWT_TOKEN>
-Lấy danh sách các sản phẩm có giá cao hơn giá trung bình toàn bộ sản phẩm
/api/products/expensive