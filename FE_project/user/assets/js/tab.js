document.getElementById('content1').style.display = 'block';

function changeTab(contentId) {
  var tabContents = document.querySelectorAll('.tab-content');
  tabContents.forEach(function (content) {
    content.style.display = 'none';
  });

  var tabs = document.querySelectorAll('.tab');
  tabs.forEach(function (tab) {
    tab.classList.remove('tab-selected');
  });

  document.getElementById(contentId).style.display = 'block';

  document.getElementById('tab' + contentId.slice(-1)).classList.add('tab-selected');
}