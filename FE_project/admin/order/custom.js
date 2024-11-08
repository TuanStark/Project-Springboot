$(document).ready(function () {
    var size = 5;
    var limit = 4;
    loadPage();
    // formart tiền
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
    $(document).on('click', '.page-link', function (e) {
        e.preventDefault();
        var page = $(this).data('page');
        if (page) {
            loadPage();
        }
    });

    function loadPage() {
        var token = localStorage.getItem("token");
        $.ajax({
            method: "GET",
            url: `http://localhost:8881/admin/order/getAll?limit=100`,
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .done(function (msg) {
                //console.log("Full response:", JSON.stringify(msg, null, 2));
                $("#GetAll").empty();
                if (msg.data.data != null) {
                    $(".dataTables_empty").hide();
                    $.each(msg.data.data, function (index, value) {
                        //console.log("Full response:", JSON.stringify(value, null, 2));
                        var stt = (msg.data.currentPage - 1) * msg.data.pageSize + index + 1;
                        var html = `<tr>
                                    <td>${stt}</td> 
                                    <td>${value.orderId}</td>
                                    <td>${value.userID.lastName}</td>
                                    <td>${formatCurrency(value.totalMoney)}</td>
                                    <td>
                                        <select id="Status" data-id-order="${value.id}" onchange="getStatusValue(this)">
                                            <option value="1" ${value.status == 1 ? 'selected' : ''}>Chưa thanh toán</option>
                                            <option value="0" ${value.status == 0 ? 'selected' : ''}>Đã thanh toán</option>
                                        </select>
                                    </td>
                                    <td>${formatDate(value.orderDate)}</td>
                                   
                                    <td><span>${value.paymentMethods == 1 ? 'Tiền mặt' : 'Chuyển khoản'}</span></td>  
                                     <td>
                                        <a href="#" data-bs-toggle="modal"
                                data-bs-target="#exampleModal" data-id="${value.id}" class="btn btn-primary btnView">Xem</a>
                                    </td>
                                </tr>`;
                        $("#GetAll").append(html);
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

    // Xóa post 
    $(document).on('click', '.btnView', function (e) {
        e.preventDefault();
        var token = localStorage.getItem("token");
        var id = $(this).data("id");
        $.ajax({
            method: "GET",
            url: `http://localhost:8881/admin/order/getById/${id}`,
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .done(function (response) {
                //console.log("Full response:", JSON.stringify(response, null, 2));
                const value = response.data;
                $('#OrderId').val(value.orderId)
                $('#lastName').val(value.userID.lastName)
                $('#totalMoney').val(formatCurrency(value.totalMoney))
                $('#phone').val(value.userID.phone)
                $('#inputAddress').val(value.userID.address)
                $('#orderDate').val(formatDate(value.orderDate))
                $('#status').val(value.status == 1 ? "Chưa thanh toán" : "Đã thanh toán")
                // Hiển thị chi tiết đơn hàng
                $('#getAllorderDetail').empty();
                var i = 1;
                value.orderDetails.forEach(detail => {

                    const productName = detail.product.name;
                    const orderDetailQuantity = detail.quantity;
                    const orderDetailPrice = detail.price;

                    var html = `<tr class="cart_item">
                                <td>${i}</td>
                                <td>${productName}</td>
                                <td>${formatCurrency(orderDetailPrice)}</td>
                                <td>${orderDetailQuantity}</td>
                                <td>${formatCurrency(orderDetailQuantity * orderDetailPrice)}</td>
                            </tr>`
                    $('#getAllorderDetail').append(html);
                    i++;
                });

            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                alert("Xóa không thành công. Vui lòng thử lại!");
                console.error("Error:", textStatus, errorThrown);
            });
    });

    window.getStatusValue = function (selectElement) {
        var token = localStorage.getItem("token");
        const selectedValue = selectElement.value;
        const orderId = selectElement.getAttribute('data-id-order');
        //console.log("Selected Value:", selectedValue);
        //console.log("Order ID:", orderId);
        $.ajax({
            method: "PUT",
            url: `http://localhost:8881/admin/order/update/${orderId}?status=${selectedValue}`,
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .done(function (response) {
                //console.log("Full response:", JSON.stringify(response, null, 2));
                //alert("update thành công")
            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                alert("Xóa không thành công. Vui lòng thử lại!");
                console.error("Error:", textStatus, errorThrown);
            });

    }

});
