$(document).ready(function () {
    var size = 5;
    var limit = 4;
    loadPage(1, size, limit);

    $(document).on('click', '.page-link', function (e) {
        e.preventDefault();
        var page = $(this).data('page');
        if (page) {
            loadPage(page, size, limit);
        }
    });

    function loadPage(page, size, limit) {
        var link = `http://localhost:8881/admin/post/getAll?page=${page}&size=${size}&limit=${limit}`;
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
                        console.log("Full response:", JSON.stringify(msg, null, 2));
                        var stt = (msg.data.currentPage - 1) * msg.data.pageSize + index + 1;

                        var html = `<tr>
                                    <td>${stt}</td> <!-- Số thứ tự -->
                                    
                                    <td>${value.title}</td>
                                    <td><img src="/user/assets/upload/${value.image}" style="width: 60px; height: 60px;"></td>
                                    <td>${value.createdAt}</td>
                                    <td>${value.lastName}</td>
                                    <td>
                                        <a href="#" data-id="${value.id}" class="btn btn-sm btn-primary btnEdit" data-bs-toggle="modal"
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

    // Xóa post 
    $(document).on('click', '.btnDelete', function (e) {
        e.preventDefault();
        var token = localStorage.getItem("token");
        var id = $(this).data("id");

        if (confirm("Bạn có chắc chắn muốn xóa không?")) {


            $.ajax({
                method: "DELETE",
                url: `http://localhost:8881/admin/post/delete/${id}`, // Sửa URL cho đúng
                headers: {
                    "Authorization": "Bearer " + token
                }
            })
                .done(function (msg) {
                    alert("Đã xóa thành công!");
                    loadPage(1, size, limit);
                })
                .fail(function (jqXHR, textStatus, errorThrown) {
                    alert("Xóa không thành công. Vui lòng thử lại!"); // Thông báo lỗi cho người dùng
                    console.error("Error:", textStatus, errorThrown);
                });
        }
    });


    $('.btnAdd').click(function () {
        $('#titlePost').text('Thêm bài đăng');
    })

    //btnEditCategory
    $(document).on('click', '.btnEdit', function (e) {
        e.preventDefault();
        $('#titlePost').text('Sửa bài đăng');
        var id = $(this).data("id");
        var token = localStorage.getItem("token");

        $.ajax({
            method: 'GET',
            url: `http://localhost:8881/admin/post/getByID/` + id,
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                console.log("Full response:", JSON.stringify(response, null, 2));
                var html = `<tr>
                                <td><img src="/user/assets/upload/${response.data.image}" style="margin: 10px;" alt="Image" width="50"></td>
                                <td>
                                    <a href="#" data-id="" class="btn btn-sm btn-danger btnDelete">Xóa</a>
                                </td>
                            </tr>`;
                $('#imageListContainer').append(html);
                $('#postId').val(response.data.id);           // Đặt giá trị id vào input ẩn
                $('#title').val(response.data.title);       // Đặt giá trị tên danh mục
                $('#description').val(response.data.description);
                $('#detail').val(response.data.detail);

            },
            error: function (xhr, status, error) {
                alert('Không thể lấy thông tin danh mục');
                console.error(xhr, status, error);
            }
        });
    })

    // thêm và sửa category
    $('#btnSave').click(function (event) {
        event.preventDefault();
        var token = localStorage.getItem("token");

        var image = $('#productImages')[0].files[0]; // Lấy hình ảnh đầu tiên từ input
        var postId = $('#postId').val();
        var title = $('#title').val();
        var description = $('#description').val();
        var detail = $('#detail').val();

        var formData = new FormData();
        formData.append('postId', postId);
        formData.append('title', title);
        formData.append('description', description);
        formData.append('detail', detail);
        formData.append('file', image);

        if (postId == '') {

            if (image) {
                $.ajax({
                    url: 'http://localhost:8881/admin/post/add',
                    type: 'POST',
                    headers: {
                        "Authorization": "Bearer " + token
                    },
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (response) {
                        alert('Thêm bài đăng thành công!');
                        $('#imageListContainer').empty();
                        $('#exampleModal').modal('hide');
                        location.reload();
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert('Thêm bài đăng thất bại!');
                    }
                });
            } else {
                console.error('Chưa chọn hình ảnh nào.');
            }
        } else {
            if (image) {
                $.ajax({
                    url: 'http://localhost:8881/admin/post/update/' + postId,
                    type: 'PUT',
                    headers: {
                        "Authorization": "Bearer " + token
                    },
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (response) {
                        alert('Cập nhật bài đăng thành công!');
                        $('#imageListContainer').empty();
                        $('#exampleModal').modal('hide');
                        location.reload();
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert('Cập nhật bài đăng thất bại!');
                    }
                });
            } else {
                console.error('Chưa chọn hình ảnh nào.');
            }
        }

    });

    // Khi người dùng chọn file
    $('#productImages').on('change', function () {
        $('#imageListContainer').empty(); // Xóa các hàng hình ảnh preview trước đó
        var files = this.files; // Lấy danh sách các file đã chọn

        if (files.length > 0) {
            $.each(files, function (index, file) {
                var reader = new FileReader();

                // Khi file được đọc thành công
                reader.onload = function (e) {
                    // Tạo một thẻ <img> với src là dữ liệu base64 của ảnh
                    var img = `<img src="${e.target.result}" style="margin: 10px;" alt="Image" width="50">`;

                    // Tạo HTML cho mỗi hàng chứa hình ảnh và nút Xóa
                    var html = `<tr>
                                    <td>${img}</td> <!-- Hiển thị hình ảnh -->
                                    <td>
                                        <a href="#" data-id="${index}" class="btn btn-sm btn-danger btnDelete">Xóa</a>
                                    </td>
                                </tr>`;
                    // Thêm hàng mới vào bảng #imageListContainer
                    $('#imageListContainer').append(html);
                };

                // Đọc file dưới dạng DataURL (base64)
                reader.readAsDataURL(file);
            });
        }
    });

    $('#imageListContainer').on('click', '.btnDelete', function (e) {
        e.preventDefault();
        $(this).closest('tr').remove();
    });

});
