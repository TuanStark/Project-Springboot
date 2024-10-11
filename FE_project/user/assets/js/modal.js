const buyBtns = document.querySelectorAll('.js-find-button');
        const modal = document.querySelector('.modal');
        const modalClose = document.querySelector('.js-header-icon-link');
        const modalBody = document.querySelector('.js-modal__body');
        
        function show() {
            modal.classList.add('open');
        }
        // nhấn vào thì ẩn đi
        function hide() {
            modal.classList.remove('open');
        }
        function stoptt(event) {
            event.stopPropagation();
        }

        for (const buyBtn of buyBtns) {
            buyBtn.addEventListener('click', show);
        }

        modalClose.addEventListener('click', hide);

        modal.addEventListener('click', hide);

        modalBody.addEventListener('click', stoptt);