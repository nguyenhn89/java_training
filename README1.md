Hướng dẫn deploy code lên host free : render.com thông qua github

Phần 1.
Database có thể dùng của aws mysql hoặc postgreSQL của render miễn phí.
Tại phần new service -> chon postgreSQL sau đó cấu hình và lấy thông tin cấu hình đưa vào Environment Variables bên trên.

Phần 2.
Thay đổi thông tin application.yml trong source sao cho khớp với thông tin cấu hình cụ thể.(tham khảo application-pro.yml)
Phần 3:
+ Tạo Dockerfile trong source code.
+ Tạo tài khoản của render.com qua github
+ Tại phần new service -> chon New web service -> chọn dự án github 
+ Language chọn docker
+ Environment Variables thêm các biến môi trường thành phần cho cấu hình DB hoặc biến 
  - JWT_SECRET:a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
  - PORT : 5432
  - SPRING_DATASOURCE_PASSWORD: KlePxNt8ZxxGoyFseMgMDG3rhO3MXHeB
  - SPRING_DATASOURCE_URL:jdbc:postgresql://dpg-d3mvr4m3jp1c73d4g7i0-a.oregon-postgres.render.com:5432/java_training
  - SPRING_DATASOURCE_USERNAME:root
+ Lưu thay đổi và deploy save để render tự deploy kiểm tra lại ở log và events menu trái