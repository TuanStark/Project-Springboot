$(document).ready(function () {
    let searchParams = new URLSearchParams(window.location.search);
    var restid = searchParams.get('id');
    var token = localStorage.getItem("token");
    console.log(restid);
    var size = 12;
    loadPage(1, size);

    function formatCurrency(value) {
        if (isNaN(value)) {
            return '';
        }
        return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
    }
    $(document).on('click', '.page-link', function (e) {
        e.preventDefault();
        var page = $(this).data('page');
        if (page) {
            loadPage(page, size);
        }
    });
    function loadPage(page, size) {
        $.ajax({
            method: "GET",
            url: `http://localhost:8881/admin/product/getByCategory/${restid}?page=${page}&size=${size}`,
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .done(function (msg) {
                $("#produtcs").empty();
                if (msg.data.data != null) {
                    $.each(msg.data.data, function (index, value) {
                        //console.log("Full response:", JSON.stringify(msg, null, 2));
                        var html = `<div class="grid__column-3-sp" style="margin-bottom: 10px;">
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
                        $("#produtcs").append(html);
                    });

                    var currentPage = msg.data.currentPage;
                    var totalPages = msg.data.totalPage;
                    var pageSize = msg.data.pageSize;
                    var totalElements = msg.data.totalElement;

                    // Cấu trúc giao diện phân trang
                    var pagination = `<div class="row">
                                    <div class="col-sm-5">
                                        
                                    </div>
                                    <div class="col-sm-7">
                                        <nav aria-label="Page navigation example">
                                            <ul class="pagination">`;

                    // Nút 'Previous'
                    pagination += `<li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a class="page-link" href="#" aria-label="Previous" onclick="loadPage(${currentPage - 1})">
                                        <span aria-hidden="true">&laquo;</span>
                                    </a>
                                </li>`;
                    // Hiển thị số trang
                    for (var i = 1; i <= totalPages; i++) {
                        pagination += `<li class="page-item ${i == currentPage ? 'active' : ''}">
                                       <a class="page-link" href="#" onclick="loadPage(${i})">${i}</a>
                                   </li>`;
                    }
                    // Nút 'Next'
                    pagination += `<li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="page-link" href="#" aria-label="Next" onclick="loadPage(${currentPage + 1})">
                                        <span aria-hidden="true">&raquo;</span>
                                    </a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>`;

                    $("#paginationContainer").html(pagination);
                    // Hiển thị phân trang
                    var pagination = generatePagination(msg.data.currentPage, msg.data.totalPage);
                    $("#paginationContainer").html(pagination);
                } else {
                    $(".dataTables_empty").show();
                    $("#paginationContainer").hide();
                }
            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                console.error("Error:", textStatus, errorThrown);
            });
    }
    // Hàm tạo giao diện phân trang
    function generatePagination(currentPage, totalPages) {
        var pagination = `<nav aria-label="Page navigation example">
                            <ul class="pagination">`;
        // Nút 'Previous'
        pagination += `<li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link" href="#" data-page="${currentPage - 1}" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                  </li>`;
        // Các số trang
        for (var i = 1; i <= totalPages; i++) {
            pagination += `<li class="page-item ${i == currentPage ? 'active' : ''}">
                           <a class="page-link" href="#" data-page="${i}">${i}</a>
                       </li>`;
        }
        // Nút 'Next'
        pagination += `<li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="#" data-page="${currentPage + 1}" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>`;
        pagination += `</ul></nav>`;
        return pagination;
    }

    // Thêm vào giỏ hàng
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
            url: `http://localhost:8881/cart/add/${productId}`,
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

})