let selectedRating = 0;

  function handleRating(rating) {
    selectedRating = rating;

    // Remove 'selected' class from all stars
    const stars = document.querySelectorAll('.stars');
    stars.forEach(star => star.classList.remove('selected'));

    // Add 'selected' class to the clicked star and all stars before it
    for (let i = 0; i < rating; i++) {
      stars[i].classList.add('selected');
    }
  }