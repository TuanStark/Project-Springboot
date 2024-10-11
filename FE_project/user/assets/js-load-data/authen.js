$(document).ready(function () {
    checkAndRefreshToken();

    $("#btn-sign-up").click(function (event) {
        event.preventDefault();
        var firstName = $("#firstName").val();
        var lastName = $("#lastName").val();
        var phone = $("#phone").val();
        var address = $("#address").val();
        var email = $("#email1").val();
        var password = $("#password1").val();

        $.ajax({
            method: "POST",
            url: "http://localhost:8881/auth/signup",
            data: {
                email: email,
                password: password,
                firstName: firstName,
                lastName: lastName,
                phone: phone,
                address: address
            },
            success: function (msg) {
                var notification = $("#notification");

                if (msg.data.authenticated) {
                    notification.text("Đăng kí thành công!");
                    notification.css("background-color", "green");
                    showNotification();
                    window.location.href = './authenform.html';
                } else {
                    notification.text("Đăng kí thất bại!");
                    notification.css("background-color", "red");
                    showNotification();
                }
            }
        });
    });

    $("#btn-signin").click(function (event) {
        event.preventDefault();
        var email = $("#email").val();
        var password = $("#password").val();

        $.ajax({
            method: "POST",
            url: "http://localhost:8881/auth/login",
            data: {
                email: email,
                password: password
            },
            success: function (msg) {
                console.log(msg)
                var notification = $("#notification");
                if (msg.data.authenticated) {
                    notification.text("Đăng nhập thành công!");
                    notification.css("background-color", "blue");
                    showNotification();
                    var token = msg.data.token;
                    localStorage.setItem("token", token)

                    // Giải mã token
                    var decodedToken = jwt_decode(token);
                    //console.log("Decoded Token:", decodedToken);
                    // Truy cập claim 'scope' chứa danh sách roles
                    var roles = decodedToken.scope;
                    //console.log("User roles:", roles);

                    if (roles.includes('ROLE_ADMIN')) {
                        window.location.href = '/admin/index.html';
                    } else if (roles.includes('ROLE_USER')) {
                        window.location.href = './index.html';
                    } else {
                        console.error("Unknown role:", roles);
                    }
                    // Kiểm tra và làm mới token nếu sắp hết hạn
                    //checkAndRefreshToken();

                    // Tự động gọi lại checkAndRefreshToken sau 50 phút (3000 giây)
                    //setTimeout(checkAndRefreshToken, 50 * 60 * 1000);
                    //setTimeout(checkAndRefreshToken, 20 * 1000); // 20 giây
                } else {
                    notification.text("Đăng nhập thất bại!");
                    notification.css("background-color", "red");
                    showNotification();
                }
            }
        });
    });

    // Kiểm tra access token sắp hết hạn
    function isAccessTokenExpiring() {
        var token = localStorage.getItem("token");
        if (!token) return false;
        var payload = JSON.parse(atob(token.split('.')[1])); // Giải mã JWT
        console.log(payload)
        var exp = payload.exp * 1000; // Thời gian hết hạn tính bằng milliseconds
        console.log(exp)
        var now = new Date().getTime();
        //return exp - now < 5 * 60 * 1000; // Kiểm tra nếu còn dưới 5 phút
        return exp - now < 30 * 1000; // Kiểm tra nếu còn dưới 30 giây
    }

    // nếu hết hạn thì gửi yêu cầu làm token mới
    function checkAndRefreshToken() {
        if (isAccessTokenExpiring()) {
            var refreshToken = localStorage.getItem("token");
            console.log(refreshToken)
            $.ajax({
                url: "http://localhost:8881/auth/refresh",
                type: "POST",
                data: {
                    token: refreshToken
                },
                success: function (response) {
                    console.log("Full response:", JSON.stringify(response, null, 2));
                    console.log("New token:", response.data.token);
                    localStorage.setItem("token", response.data.token);
                },
                error: function () {
                    console.log("Failed to refresh token", textStatus, errorThrown);
                }
            });
        }
    }

    function showNotification() {
        var notification = $("#notification");
        notification.addClass("show");

        setTimeout(function () {
            notification.removeClass("show");
        }, 2000);// sau 2 giây ẩn thông báo
    }



})
const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');

signUpButton.addEventListener('click', () => {
    container.classList.add('right-panel-active');
});

signInButton.addEventListener('click', () => {
    container.classList.remove('right-panel-active');
});
