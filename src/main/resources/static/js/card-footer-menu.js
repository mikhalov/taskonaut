document.addEventListener("DOMContentLoaded", function() {
  var menuButtons = document.querySelectorAll(".three-dots-container");

  menuButtons.forEach(function(menuButton) {
    menuButton.addEventListener("click", function(event) {
      event.stopPropagation(); // Prevent the event from bubbling up to the window
      closeMenu();
      var dropdownMenu = this.nextElementSibling;
      dropdownMenu.classList.toggle("show");
    });
  });

  // Close the dropdown menu if the user clicks outside of it
  window.onclick = function(event) {
    closeMenu();
  };
});

function deleteNote() {
  // code to delete the note
  closeMenu();
}

function editNote() {
  // code to edit the note
  closeMenu();
}

function shareNote() {
  // code to share the note
  closeMenu();
}

function closeMenu() {
  var dropdowns = document.getElementsByClassName("dropdown-menu");
  for (var i = 0; i < dropdowns.length; i++) {
    var openDropdown = dropdowns[i];
    if (openDropdown.classList.contains("show")) {
      openDropdown.classList.remove("show");
    }
  }
}