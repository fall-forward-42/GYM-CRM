# Stage 1: Build ứng dụng với Maven và JDK 21
FROM maven:3.9.8-amazoncorretto-21 AS build

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file cấu hình Maven
COPY pom.xml .

# Cài đặt dependencies để cache
RUN mvn dependency:go-offline

# Sao chép source code vào container
COPY src ./src

# Build ứng dụng mà không chạy test
RUN mvn package -DskipTests

# Stage 2: Tạo Docker image cho ứng dụng Spring Boot
FROM amazoncorretto:21.0.4

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file JAR từ stage build
COPY --from=build /app/target/*.jar app.jar

# Định nghĩa toàn bộ biến môi trường từ `application.yml`
ENV SERVER_PORT=${SERVER_PORT:-8080} \
    SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL:-jdbc:mysql://mysql:3306/gymdb?useSSL=false} \
    SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME:-lehaitien} \
    SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD:-422003Tien@} \
    SPRING_FLYWAY_URL=${SPRING_FLYWAY_URL:-jdbc:mysql://mysql:3306/gymdb?useSSL=false} \
    SPRING_FLYWAY_USER=${SPRING_FLYWAY_USER:-lehaitien} \
    SPRING_FLYWAY_PASSWORD=${SPRING_FLYWAY_PASSWORD:-422003Tien@} \
    JWT_SIGNERKEY=${JWT_SIGNERKEY:-1TjXchw5FloESb63Kc+DFhTARvpWL4jUGCwfGWxuG5SIf/1y/LgJxHnMqaF6A/ij} \
    JWT_VALID_DURATION=${JWT_VALID_DURATION:-36000} \
    JWT_REFRESH_DURATION=${JWT_REFRESH_DURATION:-360000} \
    MINIO_URL=${MINIO_URL:-http://localhost:9000} \
    MINIO_ACCESSKEY=${MINIO_ACCESSKEY:-lehaitien} \
    MINIO_SECRETKEY=${MINIO_SECRETKEY:-422003Tien@} \
    MINIO_BUCKET=${MINIO_BUCKET:-gym-crm}

# Mở cổng cho ứng dụng
EXPOSE 8080

# Cấu hình entrypoint để chạy ứng dụng, Spring Boot sẽ tự lấy biến môi trường từ container
ENTRYPOINT ["java", "-jar", "app.jar"]
