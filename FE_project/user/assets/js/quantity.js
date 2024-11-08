document.addEventListener("DOMContentLoaded", function () {
        var quantityContainers = document.querySelectorAll(".checkkk .quantity");

        quantityContainers.forEach(function (container) {
                var quantityInput = container.querySelector("input[type='number']");
                var minusButton = container.querySelector(".bttn-minus");
                var plusButton = container.querySelector(".bttn-plus");

                minusButton.addEventListener("click", function () {
                        var currentValue = parseInt(quantityInput.value);
                        if (currentValue > 1) {
                                quantityInput.value = currentValue - 1;
                        }
                });

                plusButton.addEventListener("click", function () {
                        var currentValue = parseInt(quantityInput.value);
                        quantityInput.value = currentValue + 1;
                });
        });
});
