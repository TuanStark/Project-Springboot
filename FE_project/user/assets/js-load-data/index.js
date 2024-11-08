$(document).ready(function () {
    var token = localStorage.getItem("token");
    // load lại giỏ hàng
    //Kiểm tra đăng nhập
    if (!token) {
        alert("Bạn cần đăng nhập để truy cập trang này.");
        window.location.href = '/user/authenform.html';
    }

    function formatCurrency(value) {
        if (isNaN(value)) {
            return '';
        }
        return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
    }
    loadPromotionProducts();
    var link = "http://localhost:8881/admin/slide/all"
    var size = 13;
    var limit = 13;
    loadPage(1, size, limit);

    // Load Category
    function loadPage(page, size, limit) {
        var link1 = `http://localhost:8881/admin/category/getAll?page=${page}&size=${size}&limit=${limit}`;

        $.ajax({
            method: "GET",
            url: link1,
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .done(function (msg) {
                $("#list__category").empty();
                if (msg.data.data != null) {
                    $.each(msg.data.data, function (index, value) {
                        //console.log("Full response:", JSON.stringify(msg, null, 2));
                        var html = `<li class="categoey-item">
                                        <a href="/user/produtcs/products.html?id=${value.id}" class="category-item__link">
                                            <img class="category-item__link-icon" src="${value.image}"></img>
                                            ${value.name}
                                        </a>
                                    </li>`;
                        $("#list__category").append(html);
                    });

                }
            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                console.error("Error:", textStatus, errorThrown);
            });
    }
    // Gọi hàm này sau khi dữ liệu đã được tải từ Ajax và phần tử đã tồn tại
    function initializeSlider() {
        let list = document.querySelector('.slider .list');
        let items = document.querySelectorAll('.slider .list .item');
        let dots = document.querySelectorAll('.slider .dots li');
        let prev = document.getElementById('prev');
        let next = document.getElementById('next');

        if (!items || items.length === 0) {
            console.error("No slider items found.");
            return;
        }
        let lengthItems = items.length;
        let firstClone = items[0].cloneNode(true);
        let lastClone = items[lengthItems - 1].cloneNode(true);

        list.appendChild(firstClone);
        list.insertBefore(lastClone, items[0]);
        items = document.querySelectorAll('.slider .list .item');

        let active = 1;
        let lengthClonedItems = items.length - 1;
        setTimeout(function () {
            list.style.left = -items[active].offsetLeft + 'px';
        }, 100);

        next.onclick = function () {
            if (active >= lengthClonedItems - 1) {
                active += 1;
                list.style.transition = "left 0.5s ease";
                reloadSlider();

                setTimeout(() => {
                    list.style.transition = "none";
                    active = 1;
                    list.style.left = -items[active].offsetLeft + 'px';
                }, 500);
            } else {
                active += 1;
                list.style.transition = "left 0.5s ease";
                reloadSlider();
            }
        }

        prev.onclick = function () {
            if (active <= 0) {
                active -= 1;
                list.style.transition = "left 0.5s ease";
                reloadSlider();

                setTimeout(() => {
                    list.style.transition = "none";
                    active = lengthClonedItems - 2;
                    list.style.left = -items[active].offsetLeft + 'px';
                }, 500);
            } else {
                active -= 1;
                list.style.transition = "left 0.5s ease";
                reloadSlider();
            }
        }

        let refreshSlide = setInterval(() => { next.click() }, 4000);

        function reloadSlider() {
            if (items[active]) {
                let checkLeft = items[active].offsetLeft;
                list.style.left = -checkLeft + 'px';

                let lastActiveDot = document.querySelector('.slider .dots li.active');
                if (lastActiveDot) {
                    lastActiveDot.classList.remove('active');
                }

                if (active === 0) {
                    dots[lengthItems - 1].classList.add('active');
                } else if (active === lengthClonedItems - 1) {
                    dots[0].classList.add('active');
                } else {
                    dots[active - 1].classList.add('active');
                }

                clearInterval(refreshSlide);
                refreshSlide = setInterval(() => { next.click() }, 4000);
            }
        }

        dots.forEach((li, key) => {
            li.addEventListener('click', function () {
                active = key + 1;
                list.style.transition = "left 0.5s ease";
                reloadSlider();
            });
        });
    }
    // Load Slide Quảng cáo
    $.ajax({
        method: "GET",
        url: link,
        headers: {
            "Authorization": "Bearer " + token
        }
    }).done(function (msg) {
        $("#feature-slide").empty();
        $("#dots").empty();
        if (msg.data.data != null) {
            $.each(msg.data.data, function (index, value) {
                if (value.image != null) {
                    var html = `<div class="item">
                                <img src="/user/assets/upload/${value.image}" />
                            </div>`;
                    $("#feature-slide").append(html);

                    var dotHtml = `<li ${index === 0 ? 'class="active"' : ''}></li>`;
                    $("#dots").append(dotHtml);
                }
            });

            initializeSlider();
        }
    });


    // Load sản phẩm khuyến mãi
    function loadPromotionProducts() {
        var token = localStorage.getItem("token");
        $.ajax({
            method: "GET",
            url: `http://localhost:8881/admin/product/getAll`,
            headers: {
                "Authorization": "Bearer " + token
            }
        }).done(function (msg) {
            $("#promotionProduct").empty();
            if (msg.data.data != null) {
                $.each(msg.data.data, function (index, value) {
                    if (value.promotionProduct == true && value.status == 1) {
                        //console.log("Full response:", JSON.stringify(value, null, 2));
                        var quantity = value.quantity;
                        var statusText = '';
                        if (quantity === 0) {
                            statusText = '<div class="b" style="background-color: #ff4d4d;">Hết hàng</div>';
                        } else if (quantity > 0 && quantity <= 5) {
                            statusText = `<div class="b" style="background-color: #ffdb4d;">Sắp hết: Còn ${quantity} sản phẩm</div>`;
                        } else {
                            statusText = `<div class="b" style="background-color: #ff4d4d;">Còn ${quantity} sản phẩm</div>`;
                        }
                        var html = `<div class="grid__column-3-sp-1">
                                        <div class="home-product-item">
                                            <div class="home-product-item__wrap">
                                                <div class="home-product-item__sale">
                                                    <div class="home-product-item__imgg">
                                                        <a href="/user/product_detail/product_details.html?id=${value.id}" class="product_link">
                                                            <img src="/user/assets/upload/${value.image}"class="home-product-item__img">
                                                        </a>
                                                    </div>
                                            <div class="home-product-item__sale-watch-1">
                                                <a href="#" data-product-id=${value.id} class="sale btnAddCart">
                                                    <i class="fa-solid fa-cart-shopping"></i>
                                                </a>
                                                <a href="/user/product_detail/product_details.html?id=${value.id}" class="eye">
                                                    <i class="fa-solid fa-eye"></i>
                                                </a>
                                            </div>
                                        </div>

                                        <a href="/user/product_detail/product_details.html?id=${value.id}" class="product_link">
                                            <h4 class="home-product-item__name">${value.name}</h4>
                                        </a>
                                        <div class="home-product-item__price">
                                            <span class="home-product-item__price-old Price">${formatCurrency(value.price)}</span>
                                                    <span class="home-product-item__price-current Price"> ${formatCurrency(value.price - (value.price * value.discount / 100))}</span>
                                        </div>
                                        <div class="km">
                                                <div class="dark">
                                                    ${statusText}
                                                </div>
                                            </div>
                                        <div class="home-product-item__sale-off">
                                            <span class="home-product-item__sale-off-percent">-${value.discount}%</span>
                                        </div>
                                    </div>
                                </div>

                            </div>`;
                    }
                    $("#promotionProduct").append(html);
                });

                if ($('.filtering').hasClass('slick-initialized')) {
                    $('.filtering').slick('unslick');
                }
                $('.filtering').slick({
                    slidesToShow: 4,
                    slidesToScroll: 4,
                });
            }
        }).fail(function () {
            console.error("Failed to load products");
        });
    }
    // Load sản phẩm Hot
    $.ajax({
        method: "GET",
        url: `http://localhost:8881/admin/product/getAll?page=1&size=6&limit=6`,
        headers: {
            "Authorization": "Bearer " + token
        }
    })
        .done(function (msg) {
            $("#hotProduct").siblings('.grid__column-3-sp').remove();// xóa sau nếu dùng after
            if (msg.data.data != null) {
                $.each(msg.data.data, function (index, value) {
                    //console.log("Full response:", JSON.stringify(msg, null, 2)); //cái này lấy JSON ra xem cho rõ
                    if (value.hot == true) {
                        var html = `<div class="grid__column-3-sp">
                                        <!-- Products Item -->
                                        <div class="home-product-item">
                                            <div class="home-product-item__wrap">
                                                <div class="home-product-item__sale">
                                                        <div class="home-product-item__img2">
                                                            <a href="/user/product_detail/product_details.html?id=${value.id}" class="product_link">
                                                                <img src="/user/assets/upload/${value.image}"class="home-product-item__img">
                                                            </a>                                                        </div>
                                                    <div class="home-product-item__sale-watch-1">
                                                        <a href="#" data-product-id=${value.id} class="sale btnAddCart">
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
                        $("#hotProduct").after(html);

                    }
                });
            }
        });

    //Load bánh ngọt
    $.ajax({
        method: "GET",
        url: `http://localhost:8881/admin/product/getAll?page=1`,
        headers: {
            "Authorization": "Bearer " + token
        }
    })
        .done(function (msg) {
            //console.log("Full response:", JSON.stringify(msg, null, 2));
            $("#CakeProduct").empty();
            if (msg.data.data != null) {
                $.each(msg.data.data, function (index, value) {
                    if (value.category.name == "Bánh ngọt") {
                        // console.log("Full response:", JSON.stringify(value, null, 2)); //cái này lấy JSON ra xem cho rõ
                        var html = `<div class="grid__column-3-sp">
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
                                                        <a href="#" data-product-id=${value.id} class="sale btnAddCart">
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
                        $("#CakeProduct").append(html);

                    }
                });
            }
        });

    //Load tin tức
    $.ajax({
        method: "GET",
        url: `http://localhost:8881/admin/news/getAll?page=1&size=4&limit=4`,
        headers: {
            "Authorization": "Bearer " + token
        }
    })
        .done(function (msg) {
            //console.log("Full response:", JSON.stringify(msg, null, 2));
            $("#news").empty();
            if (msg.data.data != null) {
                $.each(msg.data.data, function (index, value) {
                    if (value != null) {
                        //console.log("Full response:", JSON.stringify(value, null, 2)); //cái này lấy JSON ra xem cho rõ
                        var html = ` <a href="" class="section__news-link">
                                <ul class="section__news-list">
                                    <li class="section__news-items">
                                        <div class="section__news-img">
                                            <img src="/user/assets/upload/${value.image}"
                                                alt="" >
                                        </div>
                                        <div class="section__news-text">
                                            <h5 class="section__news-heading">${value.title}</h5>
                                            <div class="section__news-date">${value.createdAt}</div>
                                            <p class="section__news-note">Xu hướng tìm đến các thực phẩm ăn kiêng, có
                                                lợi cho sức khỏe của chúng ta cũng như của mọi người</p>
                                        </div>
                                    </li>
                                </ul>
                            </a>`;
                        $("#news").append(html);

                    }
                });
            }
        });

    //Load bài đăng
    $.ajax({
        method: "GET",
        url: `http://localhost:8881/admin/post/getAll?page=1&size=4&limit=4`,
        headers: {
            "Authorization": "Bearer " + token
        }
    })
        .done(function (msg) {
            $("#post").empty();
            if (msg.data.data != null) {
                $.each(msg.data.data, function (index, value) {
                    if (value != null) {
                        //console.log("Full response:", JSON.stringify(value, null, 2)); //cái này lấy JSON ra xem cho rõ
                        var html = ` <a href="" class="section__news-link">
                                <ul class="section__news-list">
                                    <li class="section__news-items">
                                        <div class="section__news-img">
                                            <img src="/user/assets/upload/${value.image}"
                                                alt="" style=" width: 100%;">
                                        </div>
                                        <div class="section__news-text">
                                            <h5 class="section__news-heading">${value.title}</h5>
                                            <div class="section__news-date">${value.createdAt}</div>
                                            <p class="section__news-note">Xu hướng tìm đến các thực phẩm ăn kiêng, có
                                                lợi cho sức khỏe của chúng ta cũng như của mọi người</p>
                                        </div>
                                    </li>
                                </ul>
                            </a>`;
                        $("#post").append(html);

                    }
                });
            }
        });

    // Thêm vào giỏ hàng(thêm vào Session)
    $('body').off('click', '.btnAddCart').on('click', '.btnAddCart', function (e) {
        e.preventDefault();
        let productId = $(this).data('product-id');
        addToCart(productId);
    });
    showCount();
    function showCount() {
        var token = localStorage.getItem('token');
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
    // Hàm gọi AJAX để thêm sản phẩm vào giỏ hàng
    function addToCart(productId) {
        $.ajax({
            url: `http://localhost:8881/cart/add/${productId}?quantity=1`,
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
