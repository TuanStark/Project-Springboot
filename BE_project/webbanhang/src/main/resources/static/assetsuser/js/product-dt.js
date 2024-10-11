var imgFeature = document.querySelector('.img-feature');
var listImg = document.querySelectorAll('.list-image img');
var prevBtn = document.querySelector('.prev');
var nextBtn = document.querySelector('.next');

var currentIndex = 0;

function updateImageByIndex(index) {
    // Remove active class from all list-image divs
    document.querySelectorAll('.list-image div').forEach(item => {
        item.classList.remove('active');
    });

    currentIndex = index;
    imgFeature.src = listImg[index].getAttribute('src');
   listImg[index].parentElement.classList.add('active');
}

listImg.forEach((imgElement, index) => {
    imgElement.addEventListener('click', e => {
        updateImageByIndex(index);
    });
});

prevBtn.addEventListener('click', e => {
    if (currentIndex === 0) {
        currentIndex = listImg.length - 1;
    } else {
        currentIndex--;
    }
    updateImageByIndex(currentIndex);
});

nextBtn.addEventListener('click', e => {
    if (currentIndex === listImg.length - 1) {
        currentIndex = 0;
    } else {
        currentIndex++;
    }
    updateImageByIndex(currentIndex);
});
