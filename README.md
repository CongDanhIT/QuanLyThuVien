# QuanLyThuVien
sử dụng hibernate ORM, postgreSQL,JavaSwing

sử dụng Flatlaf để tối ưu giao diện trong đẹp mắt hơn

sử dụng FlatSVGIcon để thêm các icon dạng .svg cho ứng dụng


Cấu trúc package

com.app
 ├─ entity
 ├─ util
 ├─ repository
 ├─ service
 └─ view
     ├─ frame
     └─ component
Luồng

View (Swing)
   ↓
Service (Logic nghiệp vụ)
   ↓
Repository / DAO (Hibernate CRUD)
   ↓
Database



Giao diện
com.app.view: Chứa các lớp giao diện Java Swing:

  LoginFrame.java: Màn hình đăng nhập bạn đã thiết kế.

  MainFrame.java: Cửa sổ chính chứa SideBar và vùng nội dung.

  component/: Chứa các JPanel con như DashboardPanel, BookPanel, LoanPanel.
  
  
 ------ MÀU------
PHẦN ĐĂNG NHẬP:

 1. Màu Nền & Khung (Background & Surfaces)
Màu Đen Than (Dark Charcoal - #1A1A14): Được dùng làm màu nền chủ đạo cho toàn bộ ứng dụng và các trường nhập liệu (Input Fields). Màu này tạo cảm giác bảo mật, hiện đại và làm nổi bật các chi tiết màu Vàng/Đỏ.
Màu Nâu Đậm (Dark Umber - #2D2A1E): Dùng cho phần thẻ (Card) chứa nội dung đăng nhập, giúp phân biệt nhẹ nhàng với nền tổng thể nhưng vẫn giữ được tông màu trầm ấm.
2. Màu Vàng Kim (Gold Accents - Chủ đạo)
Đây là màu sắc đại diện cho sự cao quý và tri thức (Scholarly):

Vàng Hổ Phách (Amber Gold - #FFC845): Dùng cho Nút Đăng nhập chính, tạo sự thu hút thị giác mạnh nhất (Call to Action).
Vàng Champagne nhạt: Dùng cho các Icon (biểu tượng người dùng, ổ khóa, cuốn sách), Tiêu đề Tab đang được chọn ("Đăng nhập") và các liên kết quan trọng như "Quên mật khẩu?".
Vàng mờ (Muted Gold): Dùng cho các văn bản nhấn mạnh như "PREMIUM ACCESS" và dấu gạch chân của Tab đang hoạt động.
3. Màu Đỏ Crimson (Crimson Red - Điểm nhấn)
Màu đỏ được dùng tinh tế để tạo sự sang trọng và tính thẩm quyền:

Sắc Đỏ Crimson: Hiện tại đang được dùng ẩn trong các hiệu ứng hover (khi di chuột) hoặc các thông báo lỗi. Trong các màn hình Dashboard tiếp theo, màu này được dùng cho các tiêu đề bảng (Table Header) và các vệt chỉ báo (Active Indicator) ở Sidebar bên trái.
4. Màu Văn Bản & Bổ trợ (Typography & Support)
Trắng Ngà (Off-White): Dùng cho tiêu đề chính ("Kiến thức là sức mạnh vô hạn") và văn bản nội dung để đảm bảo độ đọc tốt trên nền tối mà không gây chói mắt.
Xám Trầm (Deep Grey): Dùng cho văn bản gợi ý (Placeholder) trong ô nhập liệu như "Nhập tên đăng nhập" và các thông tin phụ ở Footer (Phiên bản, Chính sách bảo mật).
Xanh Lá (Emerald Green): Dùng cho chấm nhỏ chỉ báo "MÁY CHỦ: ỔN ĐỊNH" để thông báo trạng thái hệ thống đang hoạt động tốt.
  

 