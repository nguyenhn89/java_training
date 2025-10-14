# --- Giai đoạn 1: Build ứng dụng ---
FROM gradle:8.10-jdk17 AS build

# Đặt thư mục làm việc trong container
WORKDIR /app

# Copy toàn bộ project vào container
COPY . .

# Build file jar (tạo ra trong build/libs/)
RUN gradle clean build -x test

# --- Giai đoạn 2: Runtime ---
FROM eclipse-temurin:17-jdk

# Tạo thư mục làm việc
WORKDIR /app

# Copy file jar từ giai đoạn build sang
COPY --from=build /app/build/libs/*.jar app.jar

# Expose cổng 8080
EXPOSE 8080

# Lệnh khởi động ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
