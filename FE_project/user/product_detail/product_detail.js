$(document).ready(function () {
    let searchParams = new URLSearchParams(window.location.search);
    var restid = searchParams.get('id');
    var token = localStorage.getItem("token");

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

    $.ajax({
        method: 'GET',
        url: `http://localhost:8881/admin/product/getById/${restid}`,
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (response) {
            //console.log("Full response:", JSON.stringify(response, null, 2));
            //$('#productId').val(response.data.id);
            $('#productName').text(response.data.name);
            $('#productName1').text(response.data.name);
            $('#productDecription').html(response.data.description);
            $('#productDecription1').html(response.data.description);
            $('#productContent').html(response.data.content);
            $('#statusCheckbox').prop('checked', response.data.status === 1);
            $('#hotCheckbox').prop('checked', response.data.hot);
            $('#newCheckbox').prop('checked', response.data.newProduct);
            $('#discountCheckbox').prop('checked', response.data.promotionProduct);
            $('#productQunatity').val(response.data.quantity);
            $('#stockID').text(response.data.quantity);
            $('#nameCategory').text(response.data.category.name)
            $('#productDiscount').val(response.data.discount);
            //$('#categoryCombobox').append(response.data.categoryName);
            $('#productSize').html(response.data.size);
            $('#productWeight').html(response.data.weight);
            $('#productIngredient').html(response.data.ingredient);
            $('#productSize1').html(response.data.size);
            $('#productWeight1').html(response.data.weight);
            $('#productIngredient1').html(response.data.ingredient);
            $('#productNote').html(response.data.note);
            $('#productNote1').html(response.data.note);
            findProductByID(response.data.id);
            // findProductByID(response.data.category.id);

            var html = `<input type="button" value="-" class="bttn bttn-minus is-form">
                <input type="number" data-quantity-product="" value="1" class="bttn input-text" readonly>
                <input type="button" value="+" class="bttn bttn-plus is-form">`;
            $('#quantityUpdate').append(html);


            var quantityContainers = document.querySelectorAll(".checkkk .quantity");

            quantityContainers.forEach(function (container) {
                var quantityInput = container.querySelector("input[type='number']");
                var minusButton = container.querySelector(".bttn-minus");
                var plusButton = container.querySelector(".bttn-plus");
                minusButton.addEventListener("click", function () {
                    var currentValue = parseInt(quantityInput.value);
                    if (currentValue > 1) {
                        quantityInput.value = currentValue - 1;
                        editCart(response.data.id, currentValue)
                    }
                });
                plusButton.addEventListener("click", function () {
                    var currentValue = parseInt(quantityInput.value);
                    quantityInput.value = currentValue + 1;
                    editCart(response.data.id, currentValue)
                });
            });


            var html3 = ``;
            if (response.data.discount > 0) {
                var value = response.data.price;
                var html3 = `<span class="home-product-item__price-old-1">${formatCurrency(value.price)}</span>
                           <span class="home-product-item__price-current-1"> ${formatCurrency(value - (value * response.data.discount / 100))}</span>`;
            } else {
                var html3 = `<span class="home-product-item__price-current-1">90.000đ</span>`;
            }
            $('#productPrice').append(html3);

            //Load ảnh sản phẩm
            if (response.data.images.some(image => image.default == true)) {
                var defaultImage = response.data.images.find(image => image.default == true);
                var html = `<div class="main">
                                <img src="/user/assets/upload/${defaultImage.image}" class="img-feature" />
                            </div>
                            <div class="control prev">
                                <i class='bx bx-chevron-left'></i>
                            </div>
                            <div class="control next">
                                <i class='bx bx-chevron-right'></i>
                            </div>`;
                $('#mainImage').empty();
                $('#mainImage').append(html);
            }
            var imagesList = $('#listImage');
            var html2 = '';
            imagesList.empty();
            $.each(response.data.images, function (index, image) {
                html2 += `<div>
                            <img class="img-tag"
                                 src="/user/assets/upload/${image.image}"
                                 alt="">
                          </div>`;
            });
            imagesList.append(html2);
            // JS ảnh 
            var imgFeature = document.querySelector('.img-feature');
            var listImg = document.querySelectorAll('.list-image img');
            var prevBtn = document.querySelector('.prev');
            var nextBtn = document.querySelector('.next');
            var currentIndex = 0;
            function updateImageByIndex(index) {
                document.querySelectorAll('.list-image div').forEach(item => {
                    item.classList.remove('active');
                });
                currentIndex = index;
                imgFeature.src = listImg[index].getAttribute('src');
                listImg[index].parentElement.classList.add('active');
            }
            listImg.forEach((imgElement, index) => {
                imgElement.addEventListener('click', e => {
                    updateImageByIndex(index);
                });
            });
            prevBtn.addEventListener('click', e => {
                if (currentIndex === 0) {
                    currentIndex = listImg.length - 1;
                } else {
                    currentIndex--;
                }
                updateImageByIndex(currentIndex);
            });
            nextBtn.addEventListener('click', e => {
                if (currentIndex === listImg.length - 1) {
                    currentIndex = 0;
                } else {
                    currentIndex++;
                }
                updateImageByIndex(currentIndex);
            });


        },
        error: function (xhr, status, error) {
            alert('Không thể lấy thông tin sản phẩm');
            console.error(xhr, status, error);
        }
    });

    function findProductByID(productID) {
        $.ajax({
            method: 'GET',
            url: `http://localhost:5555/api?id=${productID}`,
        }).done(function (msg) {
            // console.log("API python:", JSON.stringify(msg, null, 2));
            $("#sameProducts").empty();
            if (msg['san_pham_goi_y'] && msg['san_pham_goi_y'].length > 0) {
                $.each(msg['san_pham_goi_y'], function (index, value) {
                    //console.log("API python:", JSON.stringify(value, null, 2));
                    var html = `<div class="col l-2-5">
                                         <!-- Products Item -->
                                         <div class="home-product-item">
                                             <div class="home-product-item__wrap">
                                                 <div class="home-product-item__sale">
                                                     <div class="home-product-item__img2">
                                                         <a href="/user/product_detail/product_details.html?id=${value.id}" class="product_link">
                                                                 <img src="/user/assets/upload/${value.image}"class="home-product-item__img">
                                                             </a> 
                                                     </div>
                                                    <div class="home-product-item__sale-watch-1">
                                                        <a href="#" class="sale">
                                                           <i class="fa-solid fa-cart-shopping"></i>
                                                        </a>
                                                        <a href="/user/product_detail/product_details.html?id=${value.id}" class="eye">
                                                            <i class="fa-solid fa-eye"></i>
                                                         </a>
                                                    </div>
                                                </div>

                                                <a href="/user/product_detail/product_details.html?id=${value.id}" class="product_link">
                                                    <h4 class="home-product-item__name">${value.name}</h4>
                                             </a>`;
                    if (value.discount > 0) {
                        html += `<div class="home-product-item__price">
                                                                        <span class="home-product-item__price-old Price">${formatCurrency(value.price)}</span>
                                                                        <span class="home-product-item__price-current Price"> ${formatCurrency(value.price - (value.price * value.discount / 100))}</span>
                                                                    </div>
                                                                    <div class="home-product-item__sale-off">
                                                                        <span class="home-product-item__sale-off-percent-1">-${value.discount}%</span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>`
                    } else {
                        html += `<div class="home-product-item__price">
                                                                        <span class="home-product-item__price-current Price"> ${formatCurrency(value.price)}</span>
                                                                    </div>
                                                                    <div class="home-product-item__sale-off">
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>`
                    }
                    $("#sameProducts").append(html);
                });
            } else {
                $("#sameProducts").append('<p>Không có sản phẩm nào để hiển thị.</p>');
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            console.error("Error:", textStatus, errorThrown);
            $("#sameProducts").append('<p>Đã xảy ra lỗi khi lấy sản phẩm.</p>');
        });
    }
    // Thêm vào giỏ hàng(thêm vào Session)
    $('body').off('click', '.btnAddCart').on('click', '.btnAddCart', function (e) {
        e.preventDefault();
        let productId = $(this).data('product-id');
        addToCart(productId);
    });

    // Hàm gọi AJAX để thêm sản phẩm vào giỏ hàng
    function addToCart(productId, quantity) {
        $.ajax({
            url: `http://localhost:8881/cart/add/${productId}?quantity=${quantity}`,
            method: 'POST',
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                var decodedToken = jwt_decode(token);
                var userId = decodedToken.userId;
                if (response.data.idUser == userId) {
                    const cartKey = `cart_${userId}`;

                    const cart = JSON.parse(localStorage.getItem(cartKey)) || {};
                    cart[response.data.product.id] = response.data;

                    localStorage.setItem(cartKey, JSON.stringify(cart));
                    alert("Thêm sản phẩm vào giỏ hàng thành công!");
                    showCount();
                }
            },
            error: function (error) {
                console.error('Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng:', error);
            }
        });
    }

});
