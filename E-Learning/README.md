# Weather Demo App - Chương 5: Các Thư Viện Thiết Yếu

## 📝 Giới Thiệu
Đây là ứng dụng demo được xây dựng cho bài báo cáo môn Lập trình Android với Kotlin (Chương 5). Ứng dụng minh họa cách kết hợp **3 thư viện cốt lõi**: Ktor, Kotlin Serialization và Coil để xây dựng một ứng dụng thời tiết hoàn chỉnh.

## 🚀 Công Nghệ Sử Dụng
- **Ngôn ngữ:** Kotlin
- **UI Framework:** Jetpack Compose
- **Networking:** Ktor Client
- **JSON Parsing:** Kotlin Serialization
- **Image Loading:** Coil (Coroutine Image Loader)
- **API:** Open-Meteo (Miễn phí, không cần API Key) & Yandex Static Maps

## ✨ Tính Năng Nổi Bật

### 1. Giao Diện Hiện Đại (Jetpack Compose)
- Hiển thị thông tin thời tiết (Nhiệt độ, Mô tả, Icon).
- Dropdown chọn thành phố (Hà Nội, TP.HCM, Đà Nẵng, Tokyo, New York, Paris).
- Banner bản đồ trực quan của thành phố đang chọn.

### 2. Xử Lý Dữ Liệu Thời Gian Thực (Ktor + Serialization)
- Gọi API Open-Meteo để lấy thời tiết mới nhất.
- Parse JSON thành Object với `@Serializable`.
- Tự động cập nhật dữ liệu định kỳ mỗi 5 phút.

### 3. Quản Lý Bộ Nhớ Đệm Ảnh (Coil)
- Tải bản đồ từ Yandex với `AsyncImage`.
- Có nút gạt **Bật/Tắt Cache** để minh họa sự khác biệt về tốc độ:
  - **Bật:** Ảnh hiện ra tức thì nhờ Singleton ImageLoader.
  - **Tắt:** Ảnh phải tải lại từ mạng (có hiệu ứng loading).

### 4. Xử Lý Mất Mạng Thông Minh (Offline-First)
- **Dưới 5 phút mất mạng:** App hiển thị dữ liệu cũ từ Cache kèm banner cảnh báo màu vàng.
- **Trên 5 phút mất mạng:** App báo lỗi "Dữ liệu đã quá cũ" và yêu cầu kết nối mạng.
- **Tự động phục hồi:** Khi có mạng trở lại, app tự động fetch dữ liệu mới mà không cần người dùng thao tác.

## 🛠️ Hướng Dẫn Cài Đặt & Chạy

### Yêu cầu hệ thống
- Android Studio (phiên bản mới nhất)
- JDK 17+
- Android SDK 24+

### Các bước chạy
1. Clone repository hoặc mở project trong Android Studio.
2. Đợi Gradle Sync hoàn tất.
3. Chạy app trên Emulator hoặc thiết bị thật.
4. (Tùy chọn) Để xem rõ hiệu ứng tải ảnh chậm, vào Settings của Emulator -> Network -> chọn "3G".

## 🎮 Kịch Bản Demo Trên Lớp

| Bước | Thao tác | Mục đích |
|------|----------|----------|
| 1 | Chọn "Hà Nội" -> "Tokyo" -> "Hà Nội" (Nút Cache BẬT) | Chứng minh ảnh load tức thì (0ms) nhờ Cache |
| 2 | Gạt nút Cache sang TẮT, lặp lại thao tác | Chứng minh app bị chậm, hiện vòng xoay khi không có Cache |
| 3 | Bật Chế độ Máy bay | Chứng minh app không crash, hiển thị dữ liệu Cache dưới 5 phút |
| 4 | Tắt Chế độ Máy bay | Chứng minh app tự động fetch lại dữ liệu ngay lập tức |

## 📚 Kiến Trúc & Design Pattern

### Singleton Pattern
- **ImageLoader** của Coil được thiết kế theo dạng Singleton để đảm bảo toàn app chỉ dùng chung một bộ nhớ đệm.
- **HttpClient** của Ktor cũng dùng `object` trong Kotlin để tái sử dụng kết nối mạng.

### Cache Busting (Nâng cao)
- Kỹ thuật thêm `timestamp` vào URL để ép Coil tải ảnh mới, tránh dùng ảnh cũ trong Cache.

### Offline-First
- Sử dụng `SharedPreferences` để lưu trữ JSON và thời gian lấy dữ liệu, giúp người dùng vẫn xem được thông tin khi mất mạng.

## ⚠️ Lưu Ý
- API Open-Meteo cập nhật dữ liệu vài giờ một lần, nên nhiệt độ có thể không thay đổi nếu bạn fetch lại trong thời gian ngắn.
- Ứng dụng sử dụng API bản đồ Yandex (miễn phí, không cần key) để hiển thị bản đồ tĩnh.

---
*Tác giả: Phạm Nguyễn - 082206016362*