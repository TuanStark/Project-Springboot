$(document).ready(function () {
    // Tải phần header vào #header khi tài liệu đã sẵn sàng
    $("#header").load("/user/partials/header.html", function (response, status, xhr) {
        console.log(status)
        if (status == "error") {
            var msg = "An error occurred: ";
            $("#header").html(msg + xhr.status + " " + xhr.statusText);
        }
    });
    $("#footer").load("/user/partials/footer.html", function (response, status, xhr) {
        console.log(status)
        if (status == "error") {
            var msg = "An error occurred: ";
            $("#header").html(msg + xhr.status + " " + xhr.statusText);
        }
    });
    // Tải phần header vào #header khi tài liệu đã sẵn sàng
    $("#sliderBar").load("/user/partials/sliderbar.html", function (response, status, xhr) {
        console.log(status)
        if (status == "error") {
            var msg = "An error occurred: ";
            $("#header").html(msg + xhr.status + " " + xhr.statusText);
        }
    });
});
