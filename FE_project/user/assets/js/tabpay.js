document.addEventListener('DOMContentLoaded', function() {
    initialize();
});

function initialize() {
    var checkedInput = document.querySelector('input[name="payment_method"]:checked');
    if (checkedInput) {
        togglePaymentBox(checkedInput.id);
    }

    document.querySelectorAll('input[name="payment_method"]').forEach(function(input) {
        input.addEventListener('change', function() {
            togglePaymentBox(this.id);
        });
    });
}

function togglePaymentBox(method) {
    var paymentBoxes = document.querySelectorAll('.payment_box');
    paymentBoxes.forEach(function(box) {
        box.style.display = 'none';
    });

    var selectedBox = document.querySelector('.payment_box.' + method);
    if (selectedBox) {
        selectedBox.style.display = 'block';
    }
}