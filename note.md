+ Bean trong spring boot:
Là đối tượng (object) do Spring tạo ra, quản lý vòng đời (lifecycle) và lưu trong ApplicationContext.
Khi cần dùng, Spring sẽ tự tiêm (inject) bean đó vào nơi cần thiết thay vì bạn phải new thủ công.

Bean tự động
bean trong spring boot thì có 2 cách , 1 là tự động của spring ví dụ @Service hoặc @Repository
bean do spring quản lý hoàn toàn, đăng ký ở trong ApplicationContext

Bean thủ công( tự đăng ký bean)
bắt buộc phải khi khai báo phải có @Configuration để báo cho spring biết tìm kiếm các @bean bên trong nó để tạo Bean đưa vào ApplicationContext.

Cách sử dụng: (về bản chất thì đều là inject)
- dùng @Autowired (chỉ dùng cho spring)
- dùng inject (có thể dùng framework khác)