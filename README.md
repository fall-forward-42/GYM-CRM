# GYM‑CRM – Hướng dẫn cài đặt & chạy dự án Spring Boot

> **Ngôn ngữ**: 🇻🇳 Tiếng Việt  
> **Mục tiêu**: Giúp bạn clone mã nguồn, dựng môi trường phụ trợ (DB + monitoring) và chạy ứng dụng trong vài phút.

---

## 1 . Giới thiệu
GYM‑CRM là hệ thống quản lý phòng gym (CRM) được xây dựng bằng **Spring Boot 3, Maven, PostgreSQL** và đóng gói sử dụng **Docker Compose**.  
Repo còn bao gồm cấu hình **Prometheus + Grafana** để giám sát cùng **MinIO** cho lưu trữ tệp.

## 2 . Yêu cầu hệ thống
| Thành phần | Phiên bản gợi ý | Ghi chú |
|------------|----------------|---------|
| **Git** | >= 2.30 | Clone mã nguồn |
| **JDK** | **17** hoặc 21 | Kiểm tra `java -version` |
| **Maven** | >= 3.9 | Không bắt buộc nếu bạn dùng `mvnw` |
| **Docker Engine** | >= 24.x | Cần cho Docker Compose |
| **Docker Compose** | v2 (đã tích hợp) |  

> Bạn có thể phát triển **không cần Docker** (chạy DB cục bộ), nhưng Docker Compose là cách nhanh nhất để dựng toàn bộ stack.

---

## 3 . Clone dự án
```bash
# 1. Lấy mã nguồn
$ git clone https://github.com/fall-forward-42/GYM-CRM.git
$ cd GYM-CRM

# 2. (Tuỳ chọn) Kiểm tra nhánh
$ git checkout main
```

---

## 4 . Chạy nhanh bằng Docker Compose (khuyên dùng)
```bash
# Khởi tạo toàn bộ dịch vụ: Postgres, MinIO, Prometheus, Grafana & ứng dụng Spring Boot
$ docker compose up -d

# Theo dõi log ứng dụng
$ docker compose logs -f app
```
Sau khi container lên thành công:
- **API**: <http://localhost:8080>
- **Swagger UI**: <http://localhost:8080/swagger-ui/index.html>
- **Grafana**: <http://localhost:3000>  (tài khoản mặc định: `admin` / `admin`)
- **Prometheus**: <http://localhost:9090>
- **MinIO Console**: <http://localhost:9001> (user/pass được cấu hình trong `.env`)

> 📝 **.env** – Nếu repo có file mẫu `.env.sample`, hãy copy thành `.env` rồi tùy chỉnh biến (mật khẩu DB, access key MinIO…). Docker Compose sẽ tự đọc file này.

Để dừng toàn bộ dịch vụ:
```bash
$ docker compose down -v     # -v xoá volume DB nếu muốn làm sạch
```

---

## 5 . Chạy cục bộ (không Docker)
1. **Chuẩn bị cơ sở dữ liệu** (PostgreSQL 15):
   ```bash
   docker run -d \
     --name postgres-gym \
     -e POSTGRES_USER=gymcrm \
     -e POSTGRES_PASSWORD=gymcrm \
     -e POSTGRES_DB=gym_crm \
     -p 5432:5432 \
     postgres:15
   ```
2. **Cấu hình biến môi trường** (hoặc chỉnh `src/main/resources/application.yml`):
   ```bash
   export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/gym_crm
   export SPRING_DATASOURCE_USERNAME=gymcrm
   export SPRING_DATASOURCE_PASSWORD=gymcrm
   ```
3. **Biên dịch & chạy ứng dụng**:
   ```bash
   ./mvnw spring-boot:run   # Hoặc sử dụng IDE như IntelliJ IDEA / VS Code
   ```

Sau khi khởi động, truy cập Swagger để khám phá API.

---

## 6 . Xây dựng image Docker riêng (tuỳ chọn)
```bash
$ docker build -t gym-crm:latest .
```
Bạn có thể push image này lên Docker Hub hoặc registry nội bộ để triển khai.

---

## 7 . Kiểm thử & Quality Gate
```bash
# Chạy Unit Test
$ ./mvnw test

# Kiểm tra format + phân tích tĩnh (nếu bật plugin)
$ ./mvnw verify
```
Pipeline CI đã được định nghĩa trong **`Jenkinsfile`** (có thể cài Jenkins hoặc GitHub Actions). Các bước chính:
1. Build & Unit Test
2. Build Docker image
3. Push image
4. Triển khai tới môi trường dev/staging

---

## 8 . Tài liệu API
- **Swagger/OpenAPI** – `/v3/api-docs`  (JSON)  
- **Swagger UI** – ` /swagger-ui/index.html`

---

## 9 . Contributing
1. Fork repository, tạo nhánh feature: `git checkout -b feature/my-feature`  
2. Commit rõ ràng: `feat: thêm chức năng X`  
3. Mở Pull Request, mô tả chi tiết.

---

## 10 . FAQ
| Câu hỏi | Trả lời |
|---------|---------|
| **Port đã được sử dụng?** | Đổi cổng trong `.env` hoặc `application.yml` (key `server.port`). |
| **Container database không connect?** | Kiểm tra biến `SPRING_DATASOURCE_URL`, chờ Postgres khởi động xong hoặc bật `depends_on` trong compose |
| **Login Grafana** | Tài khoản mặc định `admin/admin`; đổi mật khẩu khi đăng nhập lần đầu. |

---

> **Liên hệ**: Mở Issue hoặc liên hệ nhóm phát triển qua email ghi trong `pom.xml`.

Chúc bạn cài đặt thành công 🎉
