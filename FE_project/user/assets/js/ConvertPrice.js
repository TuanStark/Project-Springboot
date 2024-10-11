function convertToVND(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

// Lấy tất cả các phần tử có class là "product-price" và áp dụng chuyển đổi tiền tệ
document.querySelectorAll('.Price').forEach(function(element) {
    var productPrice = parseFloat(element.innerText);
    element.innerText = convertToVND(productPrice);
});