$(document).ready(function () {
    // Tải phần header vào #header khi tài liệu đã sẵn sàng
    $("#header").load("/admin/partials/header.html", function (response, status, xhr) {
        console.log(status)
        if (status == "error") {
            var msg = "An error occurred: ";
            $("#header").html(msg + xhr.status + " " + xhr.statusText);
        }
    });
    // Tải phần header vào #header khi tài liệu đã sẵn sàng
    $("#accordionSidebar").load("/admin/partials/sliderbar.html", function (response, status, xhr) {
        console.log(status)
        if (status == "error") {
            var msg = "An error occurred: ";
            $("#header").html(msg + xhr.status + " " + xhr.statusText);
        }
    });
});
