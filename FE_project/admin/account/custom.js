$(document).ready(function () {
    var size = 5;
    loadPage(1, size); // Tải trang đầu tiên

    // Xử lý sự kiện phân trang (event delegation)
    $(document).on('click', '.page-link', function (e) {
        e.preventDefault();
        var page = $(this).data('page'); // Lấy giá trị số trang từ thuộc tính data-page
        if (page) {
            loadPage(page, size); // Gọi hàm loadPage với số trang được chọn
        }
    });

    function loadPage(page, size) {
        var link = `http://localhost:8881/admin/users/getAll?page=${page}&size=${size}`;
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
                        //console.log("Full response:", JSON.stringify(msg, null, 2));
                        var stt = (msg.data.currentPage - 1) * msg.data.pageSize + index + 1;
                        var html = `<tr>
                                    <td>${stt}</td> <!-- Số thứ tự -->
                                    <td>${value.lastName}</td>
                                    <td>${value.email}</td>
                                    <td>${value.createdAt}</td>
                                    <td>${value.roles.name}</td>
                                    <td>
                                        <button type="button" class="btn ${value.status == 1 ? 'btn-primary' : 'btn-danger'}" ${value.status != 1 ? 'disabled' : 'disabled'}>
                                            ${value.status == 1 ? 'Còn hoạt động' : 'Không hoạt động'}
                                        </button>
                                    </td>
                                    <td>
                                        <a href="#" data-id="${value.id}" class="btn btn-sm btn-success btnView">Xem</a>
                                        <a href="#" data-id="${value.id}" class="btn btn-sm btn-primary btnEdit" data-bs-toggle="modal"
                                                data-bs-target="#exampleModal" id="btnEdit">Sửa</a>
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

        if (confirm("Bạn có chắc chắn muốn dừng hoạt động tài khaonr này không?")) {
            var token = localStorage.getItem("token");

            $.ajax({
                method: "DELETE",
                url: `http://localhost:8881/admin/users/delete/${id}`,
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
    // đổ dữ liệu vào combobox
    function loadComboBox(token, name) {
        $.ajax({
            url: 'http://localhost:8881/admin/roles/getAll?size=10',
            method: 'GET',
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                // console.log("Full response:", JSON.stringify(response, null, 2));
                var $combobox = $('#roleCombobox');
                $combobox.empty();
                $.each(response.data.data, function (index, value) {
                    var isSelected = (value.name === name) ? 'selected' : '';
                    $combobox.append('<option value="' + value.id + '"' + isSelected + '>' + value.name + '</option>');
                });
            },
            error: function () {
                alert('Lỗi load vai trò');
            }
        });
    }


    $('.btnAdd').click(function () {
        $('#titleAcount').text('Thêm danh mục');
        var token = localStorage.getItem("token");
        loadComboBox(token, null);
    })

    //btnEdit
    $(document).on('click', '.btnEdit', function (e) {
        e.preventDefault();
        $('#titleAcount').text('Sửa danh mục');
        var id = $(this).data("id");
        var token = localStorage.getItem("token");

        $.ajax({
            method: 'GET',
            url: `http://localhost:8881/admin/users/getById/` + id,
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                console.log("Full response:", JSON.stringify(response, null, 2));
                $('#idUser').val(response.data.id);
                $('#firstName').val(response.data.firstName);
                $('#lastNamee').val(response.data.lastName);
                $('#email').val(response.data.email);
                $('#status').prop('checked', response.data.status ? 1 : 0);

                //$('#roleCombobox').val(response.data.roles.name);
                $('#phone').val(response.data.phone);
                $('#adress').val(response.data.address);
                loadComboBox(token, response.data.roles.name);
                console.log(response.data.roles.name);

            },
            error: function (xhr, status, error) {
                alert('Không thể lấy thông tin vai trò');
                console.error(xhr, status, error);
            }
        });
    })

    // thêm và sửa category
    $('#btnSave').click(function (event) {
        event.preventDefault();
        var token = localStorage.getItem("token");

        var idUser = $('#idUser').val();
        var firstName = $('#firstName').val();
        var LastName = $('#lastNamee').val();
        var email = $('#email').val();
        var password = $('#password').val();
        var status = $('#status').is(':checked') ? 1 : 0;
        console.log(status);
        var idRoles = $('#roleCombobox').val();
        var phone = $('#phone').val();
        var address = $('#adress').val();


        if (idUser == '') {

            $.ajax({
                method: 'POST',
                url: `http://localhost:8881/admin/users/add`,
                headers: {
                    "Authorization": "Bearer " + token
                },
                data: {
                    firstName: firstName,
                    lastName: LastName,
                    password: password,
                    email: email,
                    phone: phone,
                    address: address,
                    status: status,
                    roles: idRoles,
                },
                success: function (response) {

                    alert('Thêm tài khoản thành công!');
                    $('#exampleModal').modal('hide');
                    location.reload();
                },
                error: function (xhr, status, error) {
                    alert('Lỗi khi thêm tài khoản');
                    console.error(xhr, status, error);
                }
            });
        } else {
            $.ajax({
                method: 'PUT',
                url: `http://localhost:8881/admin/users/update/` + idUser,
                headers: {
                    "Authorization": "Bearer " + token
                },
                data: JSON.stringify({
                    firstName: firstName,
                    lastName: LastName,
                    password: password,
                    email: email,
                    phone: phone,
                    address: address,
                    status: status,
                    roles: idRoles,
                }),
                contentType: 'application/json',
                success: function (response) {
                    alert('Cập nhật tài khoản thành công!');
                    $('#exampleModal').modal('hide');
                    location.reload();
                },
                error: function (xhr, status, error) {
                    alert('Lỗi khi cập nhật tài khoản');
                    console.error(xhr, status, error);
                }
            });
        }

    })
});
