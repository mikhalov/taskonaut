document.addEventListener("DOMContentLoaded", function () {
  var openMenu = null; // Keep track of the currently open dropdown menu
  var menuButtons = document.querySelectorAll(".three-dots-container");

  menuButtons.forEach(function (menuButton) {
    menuButton.addEventListener("click", function (event) {
      event.stopPropagation(); // Prevent the event from bubbling up to the window
      var dropdownMenu = this.nextElementSibling;
      if (openMenu !== dropdownMenu) {
        closeMenu();
      }
      dropdownMenu.classList.toggle("show");
      openMenu = dropdownMenu; // Update the currently open dropdown menu
    });
  });

  window.onclick = function (event) {
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


function closeMenu() {
  var dropdowns = document.getElementsByClassName("dropdown-menu");
  for (var i = 0; i < dropdowns.length; i++) {
    var openDropdown = dropdowns[i];
    if (openDropdown.classList.contains("show")) {
      openDropdown.classList.remove("show");
    }
  }
}


  function toggleDropdown(event) {
            var dropdownContent = event.target.nextElementSibling;
            dropdownContent.classList.toggle("show");

            var dropdowns = document.getElementsByClassName("custom-dropdown-content");
            for (var i = 0; i < dropdowns.length; i++) {
                var openDropdown = dropdowns[i];
                if (openDropdown !== event.target.nextElementSibling && openDropdown.classList.contains('show')) {
                    openDropdown.classList.remove('show');
                }
            }
        }

        window.addEventListener('click', function (event) {
            if (!event.target.matches('.custom-dropdown-button') && event.target.type !== 'text' && event.target.id !== 'each-label') {
                var dropdowns = document.getElementsByClassName("custom-dropdown-content");
                for (var i = 0; i < dropdowns.length; i++) {
                    var openDropdown = dropdowns[i];
                    if (openDropdown.classList.contains('show')) {
                        openDropdown.classList.remove('show');
                    }
                }
            }
        });

$(document).ready(function () {
  $('.custom-dropdown').each(function () {
    const customDropdown = $(this);
    const input = customDropdown.find('#label-name');
    const labels = customDropdown.find('.each-label');

    input.on('input', function (e) {
      const val = e.target.value.toUpperCase();

      labels.each(function () {
        const label = $(this);
        const labelName = label.find("input[type='submit']").val().toUpperCase();

        if (labelName.includes(val)) {
          label.show();
        } else {
          label.hide();
        }
      });
    });
  });
});