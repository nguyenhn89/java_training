// <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
// $(document).ready(function () {
//     setTimeout(function () {
//         $(".alert").fadeOut(500, function () {
//             $(this).slideUp(300, function () {
//                 $(this).remove();
//             });
//         });
//     }, 4000);
// });

window.addEventListener('load', function () {
    setTimeout(function () {
        document.querySelectorAll('.alert').forEach(function (alert) {
            alert.style.transition = 'opacity 0.5s ease, transform 0.3s ease';
            alert.style.opacity = '0';
            alert.style.transform = 'translateY(-10px)';

            // Sau khi hiệu ứng kết thúc -> remove element
            setTimeout(function () {
                alert.remove();
            }, 800); // tổng thời gian lớn hơn transition
        });
    }, 4000);
});