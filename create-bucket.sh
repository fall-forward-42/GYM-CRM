#!/bin/sh

# Đợi MinIO khởi động
sleep 5

# Cấu hình MinIO Client
mc alias set myminio http://minio:9000 lehaitien 422003Tien@

# Tạo bucket nếu chưa có
mc ls myminio/gym-crm || mc mb myminio/gym-crm

echo "Bucket gym-crm đã được tạo!"
