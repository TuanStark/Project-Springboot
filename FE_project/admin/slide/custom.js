$(document).ready(function () {
    var size = 5;
    loadPage(1, size);


    $(document).on('click', '.page-link', function (e) {
        e.preventDefault();
        var page = $(this).data('page');
        if (page) {
            loadPage(page, size);
        }
    });

    function loadPage(page, size) {
        var link = `http://localhost:8881/admin/slide/all?page=${page}&size=${size}`;
        var token = localStorage.getItem("token");

        $.ajax({
            method: "GET",
            url: link,
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .done(function (msg) {
                $("#getAll").empty();
                if (msg.data.data != null) {
                    $(".dataTables_empty").hide();
                    $.each(msg.data.data, function (index, value) {
                        var stt = (msg.data.currentPage - 1) * msg.data.pageSize + index + 1;
                        var html = `<tr>
                                    <td>${stt}</td>
                                    <td><img src="/user/assets/upload/${value.image}" style="
                                                                                height: 100px;
                                                                            "></td>
                                    <td>${value.createdAt}</td>
                                    
                                    <td>
                                        <a href="#" data-id="${value.id}" class="btn btn-sm btn-danger btnDelete">Xóa</a>
                                    </td>
                                </tr>`;
                        $("#getAll").append(html);
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
                url: `http://localhost:8881/admin/slide/delete/${id}`,
                headers: {
                    "Authorization": "Bearer " + token
                }
            })
                .done(function (msg) {
                    //alert("Đã xóa thành công!");
                    loadPage(1, size);
                })
                .fail(function (jqXHR, textStatus, errorThrown) {
                    console.error("Error:", textStatus, errorThrown);
                });
        }
    });



    $('.btnAdd').click(function () {
        $('#titleAcount').text('Thêm danh mục');

    });


    $('#btnSave').click(function (event) {
        event.preventDefault();
        var token = localStorage.getItem("token");

        var idSlide = $('#idSlide').val();
        var images = $('#productImages')[0].files[0];

        var formData = new FormData();
        formData.append('file', images);

        if (idSlide == '') {
            $.ajax({
                method: 'POST',
                url: `http://localhost:8881/admin/slide/upload`,
                headers: {
                    "Authorization": "Bearer " + token
                },
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    alert('Thêm Slide thành công!');
                    $('#exampleModal').modal('hide');
                    location.reload();
                },
                error: function (xhr, status, error) {
                    alert('Lỗi');
                    console.error(xhr, status, error);
                }
            });
        } else {
            $.ajax({
                method: 'PUT',
                url: `http://localhost:8881/admin/users/update/` + idSlide,
                headers: {
                    "Authorization": "Bearer " + token
                },
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    alert('Cập nhật thành công!');
                    $('#exampleModal').modal('hide');
                    location.reload();
                },
                error: function (xhr, status, error) {
                    alert('Lỗi');
                    console.error(xhr, status, error);
                }
            });
        }
    });

    // Khi người dùng chọn file
    $('#productImages').on('change', function () {
        $('#imageListContainer').empty(); // Xóa các hàng hình ảnh preview trước đó
        var files = this.files; // Lấy danh sách các file đã chọn
        if (files.length > 0) {
            var i = 1;
            $.each(files, function (index, file) {
                var reader = new FileReader();
                // Khi file được đọc thành công
                reader.onload = function (e) {
                    // Tạo một thẻ <img> với src là dữ liệu base64 của ảnh
                    var img = `<img src="${e.target.result}" style="margin: 10px; "alt="Image" width="50">`;
                    // Tạo HTML cho mỗi hàng chứa hình ảnh và checkbox
                    var html = `<tr>
                                <td>${i}</td> <!-- Số thứ tự -->
                                <td>${img}</td> <!-- Hiển thị hình ảnh -->
                                <td>
                                    <a href="#" data-id="#" class="btn btn-sm btn-danger btnExist">Xóa</a>
                                </td>
                                </tr>`;
                    // Thêm hàng mới vào bảng #imageListContainer
                    $('#imageListContainer').append(html);
                    i++;
                };
                // Đọc file dưới dạng DataURL (base64)
                reader.readAsDataURL(file);
            });
        }
    });

    $('#imageListContainer').on('click', '.btnExist', function (e) {
        e.preventDefault();
        $(this).closest('tr').remove();
    });
});
