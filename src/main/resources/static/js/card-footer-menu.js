document.addEventListener("DOMContentLoaded", function() {
  var openMenu = null; // Keep track of the currently open dropdown menu
  var menuButtons = document.querySelectorAll(".three-dots-container");

  menuButtons.forEach(function(menuButton) {
    menuButton.addEventListener("click", function(event) {
      event.stopPropagation(); // Prevent the event from bubbling up to the window
      var dropdownMenu = this.nextElementSibling;
      if (openMenu !== dropdownMenu) {
        closeMenu();
      }
      dropdownMenu.classList.toggle("show");
      openMenu = dropdownMenu; // Update the currently open dropdown menu
    });
  });

  window.onclick = function(event) {
    closeMenu();
  };

  function closeMenu() {
    if (openMenu !== null) {
      openMenu.classList.remove("show");
      openMenu = null; // Reset the currently open dropdown menu
    }
  }
});

function deleteNote(noteId) {
    fetch('/notes/' + noteId, {
        method: 'DELETE',
    }).then(() => {
        window.location.reload();
    }).catch((error) => {
        console.error('Error:', error);
    });
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