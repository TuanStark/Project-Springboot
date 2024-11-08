$(document).ready(function () {
    loadPage(1);

    // Xử lý sự kiện phân trang (event delegation)
    $(document).on('click', '.page-link', function (e) {
        e.preventDefault();
        var page = $(this).data('page'); // Lấy giá trị số trang từ thuộc tính data-page
        if (page) {
            loadPage(page); // Gọi hàm loadPage với số trang được chọn
        }
    });

    function loadPage(page) {
        var link = `http://localhost:8881/admin/product/getAll?page=${page}&size=6`;
        var token = localStorage.getItem("token");

        $.ajax({
            method: "GET",
            url: link,
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .done(function (msg) {
                //console.log("Full response:", JSON.stringify(msg, null, 2));
                $("#prodductGetAll").empty();
                if (msg.data.data != null) {
                    $(".dataTables_empty").hide();
                    $.each(msg.data.data, function (index, value) {
                        var stt = (msg.data.currentPage - 1) * msg.data.pageSize + index + 1;

                        var html = `<tr>
                                    <td>${stt}</td> <!-- Số thứ tự -->
                                    <td>${value.name}</td>
                                    <td>${value.category.name}</td>
                                    <td><img src="/user/assets/upload/${value.image}" style="width: 76px; height: 70px;"></td>
                                    <td>${value.createdAt}</td>
                                    <td>
                                        <a href="#" data-id="${value.id}" class="btn btn-sm btn-primary btnEdit" data-bs-toggle="modal"
                                data-bs-target="#exampleModal" id="btnEdit">Sửa</a>
                                        <a href="#" data-id="${value.id}" class="btn btn-sm btn-danger btnDelete">Xóa</a>
                                    </td>
                                </tr>`;
                        $("#prodductGetAll").append(html);
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

    // đổ dữu liệu vào combobox
    function loadComboBox(token, categoryname) {
        $.ajax({
            url: 'http://localhost:8881/admin/category/getAll?limit=10',
            method: 'GET',
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                var $combobox = $('#categoryCombobox');
                $combobox.empty();
                $.each(response.data.data, function (index, category) {
                    var isSelected = (category.name === categoryname) ? 'selected' : '';
                    $combobox.append('<option value="' + category.id + '"' + isSelected + '>' + category.name + '</option>');
                });
            },
            error: function () {
                alert('Lỗi load danh mục');
            }
        });
    }
    $(document).on('click', '.btnAddProduct', function (e) {
        var token = localStorage.getItem("token");
        $('#titleProduct').text('Thêm sản phẩm');
        loadComboBox(token, "");
    });

    //btnEditCategory
    $(document).on('click', '.btnEdit', function (e) {
        e.preventDefault();
        $('#titleProduct').text('Sửa thông tin sản phẩm');
        var id = $(this).data("id");
        var token = localStorage.getItem("token");

        $.ajax({
            method: 'GET',
            url: `http://localhost:8881/admin/product/getById/` + id,
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                //console.log("Full response:", JSON.stringify(response, null, 2));
                $('#productId').val(response.data.id);
                $('#productName').val(response.data.name);
                $('#productDecription').val(response.data.description);
                $('#productContent').val(response.data.content);
                $('#statusCheckbox').prop('checked', response.data.status === 1);
                $('#hotCheckbox').prop('checked', response.data.hot);
                $('#newCheckbox').prop('checked', response.data.newProduct);
                $('#discountCheckbox').prop('checked', response.data.promotionProduct);
                $('#productQunatity').val(response.data.quantity);
                $('#productPrice').val(response.data.price);
                $('#productDiscount').val(response.data.discount);
                $('#categoryCombobox').append(response.data.categoryName);
                $('#productSize').val(response.data.size);
                $('#productWeight').val(response.data.weight);
                $('#productIngredient').val(response.data.ingredient);
                $('#productNote').val(response.data.note);
                loadComboBox(token, response.data.categoryName);

                var imagesList = $('#imageListContainer');
                imagesList.empty();
                $.each(response.data.images, function (index, image) {
                    var html = `<tr>
                                    <td>${index + 1}</td> 
                                    <td><img src="/user/assets/upload/${image.image}" alt="Image" width="50"></td> 
                                    <td><input type="checkbox" ${image.default ? 'checked' : ''} class="default-checkbox" id="defaultCheckbox${index}"></td>
                                    <td>
                                        <a href="#" data-id="${image.id}" class="btn btn-sm btn-danger btnDelete">Xóa</a>
                                    </td>
                                </tr>`;

                    imagesList.append(html);
                });


            },
            error: function (xhr, status, error) {
                alert('Không thể lấy thông tin sản phẩm');
                console.error(xhr, status, error);
            }
        });
    })

    $(document).on('click', '.btnAddProduct', function (e) {
        $('#productId').val("");
    })

    // thêm và sửa category
    $('#btnSave').click(function (event) {
        event.preventDefault();
        var token = localStorage.getItem("token");

        var productId = $('#productId').val();
        var productName = $('#productName').val().trim();
        var productDescription = $('#productDecription').val().trim();
        var productContent = $('#productContent').val().trim();
        var productQuantity = $('#productQunatity').val().trim();
        var productPrice = $('#productPrice').val().trim();
        var productDiscount = $('#productDiscount').val().trim();
        var categoryCombobox = $('#categoryCombobox').val();
        var productSize = $('#productSize').val().trim();
        var productWeight = $('#productWeight').val().trim();
        var productIngredient = $('#productIngredient').val().trim();
        var productNote = $('#productNote').val().trim();

        var statusCheckbox = $('#statusCheckbox').is(':checked') ? 1 : 0;
        var hotCheckbox = $('#hotCheckbox').is(':checked');
        var newCheckbox = $('#newCheckbox').is(':checked');
        var discountCheckbox = $('#discountCheckbox').is(':checked');

        var images = $('#productImages')[0].files;
        var isDefaultList = [];

        if (!productName) {
            alert('Tên sản phẩm không được để trống.');
            return;
        }
        if (!productDescription) {
            alert('Mô tả sản phẩm không được để trống.');
            return;
        }
        if (!productQuantity || isNaN(productQuantity) || productQuantity <= 0) {
            alert('Số lượng sản phẩm phải là một số dương.');
            return;
        }
        if (!productPrice || isNaN(productPrice) || productPrice < 0) {
            alert('Giá sản phẩm phải là một số không âm.');
            return;
        }
        if (categoryCombobox === null) {
            alert('Vui lòng chọn một danh mục.');
            return;
        }

        var formData = new FormData();
        formData.append("name", productName);
        formData.append("description", productDescription);
        formData.append("price", productPrice);
        formData.append("discount", productDiscount);
        formData.append("ingredient", productIngredient);
        formData.append("size", productSize);
        formData.append("weight", productWeight);
        formData.append("quantity", productQuantity);
        formData.append("status", statusCheckbox);
        formData.append("note", productNote);
        formData.append("content", productContent);
        formData.append("hot", hotCheckbox);
        formData.append("newProduct", newCheckbox);
        formData.append("promotionProduct", discountCheckbox);
        formData.append("categoryID", categoryCombobox);

        for (var i = 0; i < images.length; i++) {
            formData.append("images", images[i]);
            var isDefault = $('#defaultCheckbox' + i).is(':checked');
            isDefaultList.push(isDefault);
        }
        isDefaultList.forEach(function (isDefault) {
            formData.append("isDefaultList", isDefault);
        });

        if (productId === '') {
            $.ajax({
                method: 'POST',
                url: `http://localhost:8881/admin/product/add`,
                headers: {
                    "Authorization": "Bearer " + token
                },
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    alert('Sản phẩm đã được thêm thành công!');
                    $('#productForm')[0].reset();
                    $('#imageListContainer').empty();
                },
                error: function (xhr) {
                    alert('Có lỗi xảy ra khi thêm sản phẩm: ' + xhr.responseText);
                    console.error(xhr);
                }
            });
        } else {
            $.ajax({
                method: 'PUT',
                url: `http://localhost:8881/admin/product/update/` + productId,
                headers: {
                    "Authorization": "Bearer " + token
                },
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    alert('Sản phẩm đã được cập nhật thành công!');
                    $('#productForm')[0].reset();
                    $('#imageListContainer').empty();
                    $('#productForm').hide();
                },
                error: function (xhr) {
                    alert('Có lỗi xảy ra khi cập nhật sản phẩm: ' + xhr.responseText);
                    console.error(xhr);
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
                                <td><input type="checkbox" class="default-checkbox" id="defaultCheckbox${index}"></td> <!-- Checkbox xác định hình ảnh chính -->
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


    $('#btnExist').on('click', function (e) {
        e.preventDefault();
        $('#productImages').val('');
        $('#imageListContainer').empty();
    });

});
