$(document).ready(function () {
    var size = 5;
    var limit = 5;
    loadPage(1, size, limit); // Tải trang đầu tiên

    // Xử lý sự kiện phân trang (event delegation)
    $(document).on('click', '.page-link', function (e) {
        e.preventDefault();
        var page = $(this).data('page'); // Lấy giá trị số trang từ thuộc tính data-page
        if (page) {
            loadPage(page, size, limit); // Gọi hàm loadPage với số trang được chọn
        }
    });

    function loadPage(page, size, limit) {
        var link = `http://localhost:8881/admin/category/getAll?page=${page}&size=${size}&limit=${limit}`;
        var token = localStorage.getItem("token");

        $.ajax({
            method: "GET",
            url: link,
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .done(function (msg) {
                $("#categoryGetAll").empty();

                if (msg.data.data != null) {
                    $(".dataTables_empty").hide();

                    $.each(msg.data.data, function (index, value) {
                        var stt = (msg.data.currentPage - 1) * msg.data.pageSize + index + 1;

                        var html = `<tr>
                                    <td>${stt}</td> <!-- Số thứ tự -->
                                    
                                    <td>${value.name}</td>
                                    <td><img src="${value.image}" style="width: 24px; height: 24px;"></td>
                                    <td>${value.createdAt}</td>
                                    <td>
                                        <a href="#" data-id="${value.id}" class="btn btn-sm btn-primary btnEditCategory" data-bs-toggle="modal"
                                data-bs-target="#exampleModal" id="btnEditCategory">Sửa</a>
                                        <a href="#" data-id="${value.id}" class="btn btn-sm btn-danger btnDelete">Xóa</a>
                                    </td>
                                </tr>`;
                        $("#categoryGetAll").append(html);
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

    // Xóa một danh mục
    $(document).on('click', '.btnDelete', function (e) {
        e.preventDefault();
        var id = $(this).data("id");

        if (confirm("Bạn có chắc chắn muốn xóa không?")) {
            var token = localStorage.getItem("token");

            $.ajax({
                method: "DELETE",
                url: `http://localhost:8881/admin/category/delete/${id}`,
                headers: {
                    "Authorization": "Bearer " + token
                }
            })
                .done(function (msg) {
                    alert("Đã xóa thành công!");
                    loadPage(1);
                })
                .fail(function (jqXHR, textStatus, errorThrown) {
                    console.error("Error:", textStatus, errorThrown);
                });
        }
    });

    $('.btnAddCategory').click(function () {
        $('#titleCategory').text('Thêm danh mục');
    })

    //btnEditCategory
    $(document).on('click', '.btnEditCategory', function (e) {
        e.preventDefault();
        $('#titleCategory').text('Sửa danh mục');
        var id = $(this).data("id");
        var token = localStorage.getItem("token");

        $.ajax({
            method: 'GET',
            url: `http://localhost:8881/admin/category/getById/` + id,
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                console.log("Full response:", JSON.stringify(response, null, 2));
                $('#categoryId').val(response.data.id);           // Đặt giá trị id vào input ẩn
                $('#categoryName').val(response.data.name);       // Đặt giá trị tên danh mục
                $('#categoryImage').val(response.data.image);     // Đặt giá trị hình ảnh

            },
            error: function (xhr, status, error) {
                alert('Không thể lấy thông tin danh mục');
                console.error(xhr, status, error);
            }
        });
    })

    // thêm và sửa category
    $('#btnSaveCategory').click(function (event) {
        event.preventDefault();  // Ngăn submit form mặc định
        var token = localStorage.getItem("token");

        //var categoryId = $(this).data("id");
        var categoryId = $('#categoryId').val();
        var categoryName = $('#categoryName').val();
        var categoryImage = $('#categoryImage').val();


        if (categoryId == '') {

            $.ajax({
                method: 'POST',
                url: `http://localhost:8881/admin/category/create`,
                headers: {
                    "Authorization": "Bearer " + token
                },
                data: {
                    name: categoryName,
                    image: categoryImage
                },
                success: function (response) {

                    alert('Thêm danh mục thành công!');// thông báo thành công
                    $('#exampleModal').modal('hide');// ẩn đi popup
                    location.reload();//load lại trang
                },
                error: function (xhr, status, error) {
                    alert('Lỗi khi thêm danh mục');
                    console.error(xhr, status, error);
                }
            });
        } else {
            $.ajax({
                method: 'PUT',
                url: `http://localhost:8881/admin/category/update/` + categoryId,
                headers: {
                    "Authorization": "Bearer " + token
                },
                data: {
                    name: categoryName,
                    image: categoryImage
                },
                success: function (response) {
                    alert('Cập nhật danh mục thành công!');// thông báo thành công
                    $('#exampleModal').modal('hide');// ẩn đi popup
                    location.reload();//load lại trang
                },
                error: function (xhr, status, error) {
                    alert('Lỗi khi cập nhật danh mục');
                    console.error(xhr, status, error);
                }
            });
        }

    })
});
