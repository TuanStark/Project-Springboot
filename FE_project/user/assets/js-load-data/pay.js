$(document).ready(function () {
    var token = localStorage.getItem('token');

    function formatCurrency(value) {
        if (isNaN(value)) {
            return '';
        }
        return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
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

    $.ajax({
        method: 'GET',
        url: `http://localhost:8881/admin/users/myInfo`,
        headers: {
            "Authorization": "Bearer " + token
        },
    }).done(function (data) {
        //console.log("Full response:", JSON.stringify(data, null, 2));
        $("#LastName").val(data.data.lastName);
        $("#address").val(data.data.address);
        $("#phone").val(data.data.phone);
        $("#FisrtName").val(data.data.firstName);
        LoadCart();
        getTotalPrice(token)
    });

    function LoadCart() {

        var token = localStorage.getItem('token');
        var decodedToken = jwt_decode(token);
        var userId = decodedToken.userId;

        const cartKey = `cart_${userId}`;
        const cart = JSON.parse(localStorage.getItem(cartKey)) || {};

        //console.log("Full response:", JSON.stringify(cart, null, 2));

        $.each(cart, function (index, item) {
            //console.log("Full response:", JSON.stringify(item, null, 2));
            var html = `<tr class="cart_item">
                            <td class="product-names">
                                <span id="NameProduct">${item.product.name}</span> X 
                                <strong id="QuantityProduct">${item.quantity}</strong>
                            </td>
                            <td class="product-total">
                                <span class="woocommerce-price-amount amount" id="totalMoney">${formatCurrency(item.totalPrice)}</span>
                            </td>
                        </tr>`
            $('#cartItem').append(html);
        });
    }

    function getTotalPrice(token) {
        $.ajax({
            url: `http://localhost:8881/cart/total-price`,
            type: 'GET',
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (data) {
                //console.log("Full response:", JSON.stringify(data, null, 2));

                if (data != null) {
                    $('#totalPrice').val(data);
                    $('.subTotal').text(formatCurrency(data));
                    $('.Total').text(formatCurrency(data));
                } else {
                    $('.subTotal').text('0đ');
                    $('.Total').text('0đ');
                }

            },
            error: function (err) {
                console.error('Error getting total price:', err);
            }
        });
    }

    $('body').off('click', '#order_submit').on('click', '#order_submit', function (e) {
        e.preventDefault();
        var token = localStorage.getItem('token');
        var isPaymentCOD = $('#payment-method-cod').is(':checked');
        var isPaymentBAC = $('#payment-method-bacs').is(':checked');

        if (isPaymentCOD || isPaymentBAC) {
            saveOrder(token, isPaymentCOD);
        }
    });

    function saveOrder(token, isPaymentCOD) {
        var decodedToken = jwt_decode(token);
        var userId = decodedToken.userId;

        const cartKey = `cart_${userId}`;
        const cart = JSON.parse(localStorage.getItem(cartKey)) || {};

        var note = $('#note').val() || "";
        var totalMoney = $('#totalPrice').val();
        console.log(totalMoney);
        var paymentMethod = isPaymentCOD ? 1 : 0;

        let productList = [];
        $.each(cart, function (index, item) {
            const price = item.product.discount > 0 ? item.priceDiscount : item.product.price;
            productList.push({
                price: price,
                quantity: item.quantity,
                totalMoney: item.totalPrice,
                productID: item.product.id,
            });
        });
        $.ajax({
            url: 'http://localhost:8881/order/add',
            type: 'POST',
            contentType: 'application/json',
            headers: {
                "Authorization": "Bearer " + token,
            },
            data: JSON.stringify({
                status: 1,
                note: note,
                orderDate: new Date().toISOString().split('T')[0],
                totalMoney: totalMoney,
                paymentMethods: paymentMethod,
                userID: userId,
                orderDetails: productList,
            }),
            success: function (data) {
                localStorage.setItem('orderDetails', JSON.stringify(data.data));
                window.location.href = '/user/order.html';
            },
            error: function (error) {
                console.error("Error saving order:", error);
                alert("Có lỗi xảy ra khi lưu đơn hàng. Vui lòng thử lại.");
            }
        });
    }

    function updateInforUser() {
        var phone = $("#phone").val();
        var firstName = $("#FisrtName").val();
        var lastName = $("#LastName").val();
        var address = $("#address").val();
        $.ajax({
            method: "POST",
            url: "http://localhost:8881/auth/signup",
            data: {
                firstName: firstName,
                lastName: lastName,
                phone: phone,
                address: address
            },
            success: function (data) {
                console.log("Cập nhật thông tin thành công!");
            },
            error: function (err) {
                console.error('Error to update:', err);
            }
        });
    }

})