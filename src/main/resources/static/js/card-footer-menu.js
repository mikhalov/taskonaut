document.addEventListener("DOMContentLoaded", function () {
    let openMenu = null; // Keep track of the currently open dropdown menu
    const menuButtons = document.querySelectorAll(".three-dots-container");

    menuButtons.forEach(function (menuButton) {
        menuButton.addEventListener("click", function (event) {
            event.stopPropagation(); // Prevent the event from bubbling up to the window
            var dropdownMenu = this.nextElementSibling;

            // Reset the transform styles before checking the position
            dropdownMenu.style.transform = '';

            if (openMenu !== dropdownMenu) {
                closeMenu();
            }
            dropdownMenu.classList.toggle("show");
            openMenu = dropdownMenu; // Update the currently open dropdown menu

            // Check the position after updating the open menu
            checkMenuPosition(openMenu);
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

function checkMenuPosition(dropdownMenu) {
    // Get the custom navbar's bounding rectangle
    var navbarRect = $('.custom-navbar')[0].getBoundingClientRect();

    // Get the dropdown menu's bounding rectangle
    var menuRect = dropdownMenu.getBoundingClientRect();

    // Check if the menu is out of the viewport to the right
    var outOfViewportRight = menuRect.right > window.innerWidth;

    // Check if there's not enough space at the top (considering the custom navbar)
    var notEnoughSpaceTop = menuRect.top - navbarRect.bottom < dropdownMenu.offsetHeight;


    // Get the dimensions of the menu-button element
    var menuButtonRect = $('#menu-button')[0].getBoundingClientRect();
    var menuButtonHeight = menuButtonRect.height;
    var menuButtonWidth = menuButtonRect.width;

    // Initialize transform style
    var transformStyle = '';

    if (notEnoughSpaceTop) {
        // Apply the vertical adjustment to the menu's position (considering the custom navbar and menu-button height)
        transformStyle += 'translateY(calc(100% + ' + menuButtonHeight + 'px))';
    }

    if (outOfViewportRight) {
        // Apply the horizontal adjustment to the menu's position (subtracting the menu-button width)
        transformStyle += ' translateX(calc(-100% + ' + menuButtonWidth + 'px))';
    }

    // Set the transform style
    dropdownMenu.style.transform = transformStyle;
}


function checkMenuPositionForLabelsDropdown(dropdownMenu) {
    const navbar = document.querySelector('.custom-navbar');
    const navbarRect = navbar.getBoundingClientRect();
    const menuRect = dropdownMenu.getBoundingClientRect();

    const notEnoughSpaceTop = menuRect.top - navbarRect.bottom < dropdownMenu.offsetHeight;

    const menuButton = document.getElementById('menu-button');
    const menuButtonRect = menuButton.getBoundingClientRect();
    const menuButtonHeight = menuButtonRect.height;

    let transformStyle = '';

    if (notEnoughSpaceTop) {
        transformStyle += `translateY(calc(100% + ${menuButtonHeight}px))`;
    }

    dropdownMenu.style.transform = transformStyle;
}

function toggleDropdown(event) {
    const dropdownContent = event.target.nextElementSibling;
    dropdownContent.classList.toggle("show");

    if (dropdownContent.classList.contains("show")) {
        checkMenuPositionForLabelsDropdown(dropdownContent);
    }

    const dropdowns = document.querySelectorAll(".custom-dropdown-content");
    dropdowns.forEach(openDropdown => {
        if (openDropdown !== event.target.nextElementSibling && openDropdown.classList.contains('show')) {
            openDropdown.classList.remove('show');
        }
    });
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
        const submitButton = customDropdown.find('.input-submit');
        submitButton.hide();

        input.on('input', function (e) {
            const val = e.target.value.toUpperCase();
            let hasMatches = false;

            labels.each(function () {
                const label = $(this);
                const labelName = label.find("input[type='submit']").val().toUpperCase();

                if (labelName.includes(val)) {
                    label.show();
                    hasMatches = true;
                } else {
                    label.hide();
                }
            });

            if (hasMatches) {
                submitButton.hide();
            } else {
                submitButton.show();
            }
        });
    });
});
