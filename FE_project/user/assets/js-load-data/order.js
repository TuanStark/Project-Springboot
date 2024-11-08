$(document).ready(function () {
    var token = localStorage.getItem('token');
    function showCount() {
        var token = localStorage.getItem('token');
        // phân tích token để lấy userId
        var decodedToken = jwt_decode(token);
        var userId = decodedToken.userId;
        const cartKey = `cart_${userId}`;

        const cartCount = JSON.parse(localStorage.getItem(cartKey)) || {};
        var totalQuantity = 0;
        $.each(cartCount, function (index, item) {
            totalQuantity++;
        });
        $('#cartCount').html(totalQuantity);
    }
    //forrmat date
    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('vi-VN', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    }
    //format tiền
    function formatCurrency(value) {
        if (isNaN(value)) {
            return '';
        }
        return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
    }
    const orderDetails = JSON.parse(localStorage.getItem('orderDetails'));

    if (orderDetails) {
        //console.log("Full response:", JSON.stringify(orderDetails, null, 2));
        $('#orderId').text(orderDetails.id);
        $('#fn').text(orderDetails.userID.firstName);
        $('#ln').text(orderDetails.userID.lastName);
        $('#phoneNB').text(orderDetails.userID.phone);
        $('#adrr').text(orderDetails.userID.address);
        $('#orderDate').text(formatDate(orderDetails.orderDate));
        $('#totals').text(formatCurrency(orderDetails.totalMoney));
        $('#tempTotal').text(formatCurrency(orderDetails.totalMoney));
        $('#subTotal').text(formatCurrency(orderDetails.totalMoney));
        paymentMethods = 1 ? $('#paymentMethod').text('Chuyển tiền khi nhận hàng') :
            $('#paymentMethod').text('Chuyển khoản');
        paymentMethods = 1 ? $('#methodPayment').text('Chuyển tiền khi nhận hàng') :
            $('#methodPayment').text('Chuyển khoản');

        // Cập nhật trạng thái đơn hàng
        const status = orderDetails.status;
        if (status === 2) {
            $('#status_order').text("Đã duyệt").css("background-color", "green");
        } else if (status === 1) {
            $('#status_order').text("Chờ xác nhận").css("background-color", "orange");
        } else {
            $('#status_order').text("Đã hủy").css("background-color", "red");
        }

        // Hiển thị chi tiết đơn hàng
        orderDetails.orderDetails.forEach(detail => {
            const productName = detail.product.name;
            const orderDetailQuantity = detail.quantity;
            const orderDetailPrice = detail.price;

            var html = `<tr class="cart_item">
                                <td class="product-name">${productName}
                                    <strong>x${orderDetailQuantity}</strong>
                                </td>
                                <td class="product-total">
                                    <span
                                    class="woocommerce-price-amount amount">${formatCurrency(orderDetailPrice * orderDetailQuantity)}</span>
                                </td>
                            </tr>`
            $('#orderDetail_item').append(html);
        });
    } else {
        alert("Không tìm thấy thông tin hóa đơn.");
    }

    $('body').off('click', '.btnUpdate').on('click', '.btnUpdate', function (e) {
        e.preventDefault();
        deleteCart();

    });
    //delete carrt
    function deleteCart() {
        $.ajax({
            url: `http://localhost:8881/cart/clearAll`,
            type: 'GET',
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                var decodedToken = jwt_decode(token);
                var Id = decodedToken.userId;
                const cartKey = `cart_${Id}`;
                localStorage.removeItem(cartKey);
                localStorage.removeItem('orderDetails');
                showCount(0);
                renderCart();
                window.location.href = '/user/index.html';
            },
            error: function (err) {
                console.error('Error editing cart:', err);
            }
        });
    }
});

