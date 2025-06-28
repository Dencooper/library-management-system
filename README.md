# Luận Văn Tốt Nghiệp Đại Học - Ngành Công Nghệ Thông Tin

**Học kỳ**: 3  
**Năm học**: 2024–2025

---

## Thông Tin Sinh Viên

- **Họ tên**: Lê Phương Nam
- **MSSV**: B2111810

---

## Đề Tài: Website Quản Lý Thư Viện Áp Dụng Kiến Trúc Microservices

---

## Công Nghệ Sử Dụng

- Backend: `Spring Boot`, `Spring Cloud`, `Spring Security`
- Frontend: `Vue.js`
- Cơ sở dữ liệu: `MySQL`, `MongoDB`
- Các công nghệ khác: `Eureka`, `Spring Cloud Gateway`, `FeignClient`, `JWT`, `Kafka`

---

## Kiến Trúc Microservices

Hệ thống bao gồm các service chính:

- `api-gateway`: Cổng vào hệ thống
- `discovery-server`: Service registry (Eureka)
- `config-server`: Quản lý cấu hình tập trung
- `auth-service`: Xác thực và phân quyền người dùng
- `user-service`: Quản lý thông tin người dùng
- `book-service`: Quản lý sách
- `borrow-service`: Quản lý mượn/trả sách
- `notification-service`: Gửi thông báo (email)
- `report-service`: Thống kê báo cáo
- `common`: Chứa các cấu trúc chung (ApiResponse, exception, v.v.)
- `UI`: Giao diện người dùng với Vue.js

---

## Mô Tả Dự Án

Đây là hệ thống quản lý thư viện dành cho một thư viện duy nhất, hỗ trợ các chức năng:

- Quản lý sách, người dùng, danh mục, kệ sách
- Yêu cầu mượn/trả sách với xác nhận của thủ thư
- Gửi email thông báo (nếu có triển khai notification-service)
- Theo dõi lịch sử mượn, xử lý trả trễ và cấm mượn nếu vi phạm

---

## Giao Diện Minh Họa
![image](https://github.com/user-attachments/assets/8c97cdf9-fc2f-4ed4-a8ec-34f91965f508)

---

## Hướng Dẫn Chạy Dự Án

1. Clone repository:
   ```bash
   git clone https://github.com/Dencooper/library-management-system.git
   ```
