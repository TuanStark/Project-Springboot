const listImage = document.querySelector('.list-image');
const imgs = document.getElementsByName('slide-show-img');
const btnLeft = document.querySelector('.btn-left');
const btnRight = document.querySelector('.btn-right');
const lenght = imgs.length;
let current = 0;

const handleChangeSlide = () => {// cái này sang phải nên nó trùng với cái btnRight
    if(current == lenght - 1){
        current = 0;
        let width = imgs[0].offsetWidth;
        listImage.style.transform = `translateX(0px)`
        document.querySelector('.active').classList.remove('active')
        document.querySelector('.index-item-'+current).classList.add('active')
    }else {
        current ++;
        let width = imgs[0].offsetWidth;
        listImage.style.transform = `translateX(${width * -1 * current}px)`
        document.querySelector('.active').classList.remove('active')
        document.querySelector('.index-item-'+current).classList.add('active')
    }
}

let handleEvnetChangeSlide =  setInterval(handleChangeSlide, 4000)

btnRight.addEventListener('click', () => {
    clearInterval(handleEvnetChangeSlide)
    handleChangeSlide()
    handleEvnetChangeSlide =  setInterval(handleChangeSlide, 4000)
})
btnLeft.addEventListener('click', () => {// không trùng với cái sang phải nên phải viết lại
    clearInterval(handleEvnetChangeSlide);
    if(current == 0){
        current = lenght - 1;
        let width = imgs[0].offsetWidth;
        listImage.style.transform = `translateX(${width * -1 * current}px)`
        document.querySelector('.active').classList.remove('active')
        document.querySelector('.index-item-'+current).classList.add('active')
    }else {
        current --;
        let width = imgs[0].offsetWidth;
        listImage.style.transform = `translateX(${width * -1 * current}px)`
        document.querySelector('.active').classList.remove('active')
        document.querySelector('.index-item-'+current).classList.add('active')
    }
    handleEvnetChangeSlide =  setInterval(handleChangeSlide, 4000);
})



