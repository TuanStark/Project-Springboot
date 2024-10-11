// Show Tab 1 content initially
document.getElementById('content1').style.display = 'block';

function changeTab(contentId) {
  // Hide all tab contents
  var tabContents = document.querySelectorAll('.tab-content');
  tabContents.forEach(function(content) {
    content.style.display = 'none';
  });

  // Remove the 'tab-selected' class from all tabs
  var tabs = document.querySelectorAll('.tab');
  tabs.forEach(function(tab) {
    tab.classList.remove('tab-selected');
  });

  // Show the selected tab content
  document.getElementById(contentId).style.display = 'block';

  // Add the 'tab-selected' class to the clicked tab
  document.getElementById('tab' + contentId.slice(-1)).classList.add('tab-selected');
}