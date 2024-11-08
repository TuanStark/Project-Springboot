$(document).ready(function () {
    var token = localStorage.getItem('token');
    renderCart();
    //  localToServer(token);
    //showCount();

    getTotalQuantity(token)

    function formatCurrency(value) {
        if (isNaN(value)) {
            return '';
        }
        return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
    }

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


    // Đang test
    function getTotalQuantity(token) {
        $.ajax({
            url: `http://localhost:8881/cart/total-quantity`,
            type: 'GET',
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (data) {
                var decodedToken = jwt_decode(token);
                var id = decodedToken.userId;
                if (id == item.idUser) {
                    $('#cartCount').html(data);
                }
            },
            error: function (err) {
                console.error('Error getting total price:', err);
            }
        });
    }

    // load cart
    function renderCart() {
        var token = localStorage.getItem('token');
        var decodedToken = jwt_decode(token);
        var userId = decodedToken.userId;
        const cartKey = `cart_${userId}`;
        const cart = JSON.parse(localStorage.getItem(cartKey)) || {};

        $('#cartItems').empty();
        if (Object.keys(cart).length === 0) {
            $('#cartItems').append('<tr><td colspan="5">Giỏ hàng trống.</td></tr>');
            return;
        }
        $.each(cart, function (index, item) {
            // console.log("Full response:", JSON.stringify(item, null, 2));
            const value = item.product;
            const quantity = item.quantity || 1;
            const priceAfterDiscount = value.price - (value.price * value.discount / 100);
            const subtotal = priceAfterDiscount * quantity;

            const cartItemsHtml = `
                <tr class="shop__table-item">
                    <td class="product-remove"><a href="#" data-id-product=${value.id} onclick="removeFromCart('${value.id}')">x</a></td>
                    <td class="product-check"><input type="checkbox"></td>
                    <td class="product-thumbnail">
                        <a href="">
                            <img width="300px" height="300px" src="/user/assets/upload/${value.image}" alt="">
                        </a>
                    </td>
                    <td class="product-name"><a href="">${value.name}</a></td>
                    <td class="product-price"><span class="price">${formatCurrency(priceAfterDiscount)}</span></td>
                    <td class="product-quantity">
                        <div class="checkkk">
                            <div class="quantity">
                                <input type="button" value="-" class="bttn bttn-minus is-form">
                                <input type="number" data-quantity-product=${quantity} value="${quantity}" class="bttn input-text" readonly>
                                <input type="button" value="+" class="bttn bttn-plus is-form">
                            </div>
                        </div>
                    </td>
                    <td class="product-subtotal">
                        <span class="price">${formatCurrency(subtotal)}</span>
                    </td>
                </tr>`;

            $('#cartItems').append(cartItemsHtml);


            $('.bttn-minus').off('click');
            $('.bttn-plus').off('click');
            // Giảm số lượng
            $('.bttn-minus').on('click', function () {
                const productId = $(this).closest('tr').find('.product-remove a').attr('data-id-product');
                const quantityInput = $(this).siblings('input[type="number"]');
                let currentValue = parseInt(quantityInput.val());

                if (currentValue > 1) {
                    quantityInput.val(currentValue - 1);
                    editCart(productId, currentValue - 1);
                }
            });
            // Tăng số lượng
            $('.bttn-plus').on('click', function () {
                const productId = $(this).closest('tr').find('.product-remove a').attr('data-id-product');
                //console.log('Product ID (plus):', productId);
                const quantityInput = $(this).siblings('input[type="number"]');
                let currentValue = parseInt(quantityInput.val());

                quantityInput.val(currentValue + 1);
                editCart(productId, currentValue + 1);
            });
            getTotalPrice(token);
        });
    }
    //btnRemove
    window.removeFromCart = function (productId) {
        $.ajax({
            url: `http://localhost:8881/cart/delete/${productId}`,
            method: 'DELETE',
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                //console.log("Full response delete:", JSON.stringify(response, null, 2));
                // Giả định rằng response.data chứa thông tin giỏ hàng đã cập nhật
                const cartData = response;
                const userId = Object.keys(cartData)[0]; // Lấy userId từ response
                const cartItem = cartData[userId]; // Lấy thông tin giỏ hàng của người dùng
                //console.log("Full response delete:", JSON.stringify(cartItem, null, 2));

                // Cập nhật localStorag
                var decodedToken = jwt_decode(token);
                var Id = decodedToken.userId;
                const cartKey = `cart_${Id}`;
                localStorage.setItem(cartKey, JSON.stringify(cartItem));

                renderCart();
                showCount();
            },
            error: function (error) {
                alert("Có lỗi xảy ra khi xóa sản phẩm khỏi giỏ hàng.");
            }
        });
    }

    // edit cart
    function editCart(productId, quantity) {
        $.ajax({
            url: `http://localhost:8881/cart/edit/${productId}?quantity=${quantity}`,
            type: 'PUT',
            contentType: 'application/json',
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                const cartData = response.data;
                const userId = Object.keys(cartData)[0];// cái này là bỏ dòng đầu tiên của cái response
                //const productId = Object.keys(cartData[userId])[0];
                const cartItem = cartData[userId];//const cartItem = cartData[userId][productId] cái này là bỏ 2 dòng dầu tiên

                //console.log("Full response update:", JSON.stringify(cartItem, null, 2))
                var decodedToken = jwt_decode(token);
                var Id = decodedToken.userId;
                const cartKey = `cart_${Id}`;

                localStorage.removeItem(cartKey);
                localStorage.setItem(cartKey, JSON.stringify(cartItem));
                renderCart();
            },
            error: function (err) {
                console.error('Error editing cart:', err);
            }
        });
    }


    // Hàm lấy tổng giá trị của giỏ hàng
    function getTotalPrice(token) {
        $.ajax({
            url: `http://localhost:8881/cart/total-price`,
            type: 'GET',
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (data) {
                //console.log("Full response:", JSON.stringify(data, null, 2));
                if (data != 0) {
                    $('#subTotal').text(formatCurrency(data));
                    $('#Total').text(formatCurrency(data));
                    $('.check-out').empty();
                    $('.check-out').append('<a href="/user/pay.html" class="btn-check">Tiến hành thanh toán</a>');
                } else {
                    $('#subTotal').text('0đ');
                    $('#Total').text('0đ');
                }

            },
            error: function (err) {
                console.error('Error getting total price:', err);
            }
        });
    }

    //Load từ local storage về lại server
    function localToServer(token) {
        var decodedToken = jwt_decode(token);
        var userId = decodedToken.userId;
        const cartKey = `cart_${userId}`;
        const cartData = localStorage.getItem(cartKey);
        //console.log("Full response:", JSON.stringify(cartData, null, 2));
        if (cartData) {
            const cartObject = JSON.parse(cartData);

            const dataToSend = {
                idUser: userId,
                cart: cartObject
            };
            console.log("Data to send:", JSON.stringify(dataToSend, null, 2));
            $.ajax({
                url: 'http://localhost:8881/cart/sync',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(dataToSend),
                headers: {
                    "Authorization": "Bearer " + token
                },
                success: function (data) {
                    console.log("Full response:", JSON.stringify(data, null, 2))
                    console.log("Đồng bộ giỏ hàng thành công:", response);
                },
                error: function (err) {
                    console.error('Error to tranform', err);
                }
            });
        }
    }

});
