let list = document.querySelector('.slider .list');
let items = document.querySelectorAll('.slider .list .item');
let dots = document.querySelectorAll('.slider .dots li');
let prev = document.getElementById('prev');
let next = document.getElementById('next');

let lengthItems = items.length;
let firstClone = items[0].cloneNode(true);
let lastClone = items[lengthItems - 1].cloneNode(true);

list.appendChild(firstClone);
list.insertBefore(lastClone, items[0]);

items = document.querySelectorAll('.slider .list .item');

let active = 1;
let lengthClonedItems = items.length - 1;


list.style.left = -items[active].offsetLeft + 'px';

next.onclick = function () {
    if (active >= lengthClonedItems - 1) {
        active += 1;
        list.style.transition = "left 0.5s ease";
        reloadSlider();

        setTimeout(() => {
            list.style.transition = "none";
            active = 1;
            list.style.left = -items[active].offsetLeft + 'px';
        }, 500);
    } else {
        active += 1;
        list.style.transition = "left 0.5s ease";
        reloadSlider();
    }
}

prev.onclick = function () {
    if (active <= 0) {
        active -= 1;
        list.style.transition = "left 0.5s ease";
        reloadSlider();

        setTimeout(() => {
            list.style.transition = "none";
            active = lengthClonedItems - 2;
            list.style.left = -items[active].offsetLeft + 'px';
        }, 500);
    } else {
        active -= 1;
        list.style.transition = "left 0.5s ease";
        reloadSlider();
    }
}

let refreshSlide = setInterval(() => { next.click() }, 4000);

function reloadSlider() {
    let checkLeft = items[active].offsetLeft;
    list.style.left = -checkLeft + 'px';

    let lastActiveDot = document.querySelector('.slider .dots li.active');
    if (lastActiveDot) {
        lastActiveDot.classList.remove('active');
    }
    if (active === 0) {
        dots[lengthItems - 1].classList.add('active');
    } else if (active === lengthClonedItems - 1) {
        dots[0].classList.add('active');
    } else {
        dots[active - 1].classList.add('active');
    }

    clearInterval(refreshSlide);
    refreshSlide = setInterval(() => { next.click() }, 4000);
}

dots.forEach((li, key) => {
    li.addEventListener('click', function () {
        active = key + 1;
        list.style.transition = "left 0.5s ease";
        reloadSlider();
    });
});
