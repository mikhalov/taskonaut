document.addEventListener("DOMContentLoaded", function () {
    let openMenu = null;
    const menuButtons = document.querySelectorAll(".three-dots-container");

    menuButtons.forEach(function (menuButton) {
        menuButton.addEventListener("click", function (event) {
            event.stopPropagation();
            var dropdownMenu = this.nextElementSibling;

            dropdownMenu.style.transform = '';

            if (openMenu !== dropdownMenu) {
                closeMenu();
            }
            dropdownMenu.classList.toggle("show");
            openMenu = dropdownMenu;

            checkMenuPosition(openMenu);
        });
    });

    window.onclick = function (event) {
        // Check if the click event target is inside the dropdown or a child of the dropdown
       if (!event.target.closest(".dropdown-menu") || event.target.tagName === 'BUTTON') {
               closeMenu();
           }
    };

    function closeMenu() {
        if (openMenu !== null) {
            openMenu.classList.remove("show");
            openMenu = null;
        }
    }
});

function checkMenuPosition(dropdownMenu) {
    var navbarRect = $('.custom-navbar')[0].getBoundingClientRect();

    var menuRect = dropdownMenu.getBoundingClientRect();

    var outOfViewportRight = menuRect.right > window.innerWidth;

    var notEnoughSpaceTop = menuRect.top - navbarRect.bottom < dropdownMenu.offsetHeight;


    var menuButtonRect = $('#menu-button')[0].getBoundingClientRect();
    var menuButtonHeight = menuButtonRect.height;
    var menuButtonWidth = menuButtonRect.width;

    var transformStyle = '';

    if (notEnoughSpaceTop) {
        transformStyle += 'translateY(calc(100% + ' + menuButtonHeight + 'px))';
    }

    if (outOfViewportRight) {
        transformStyle += ' translateX(calc(-100% + ' + menuButtonWidth + 'px))';
    }

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

// Function to handle setting the notification
async function setNotification(noteId) {
    const dateTime = document.getElementById("notificationDateTime").value;
    if (dateTime) {
        console.log("Notification set for:", dateTime, "Note ID:", noteId);

        const notificationData = {
            "noteId": noteId,
            "reminderDateTime": dateTime
        };

        try {
            const response = await fetch('/telegram/set-reminder', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(notificationData)
            });

            if (response.ok) {
                console.log(await response.text());
            } else {
                console.error("Failed to set reminder:", response.status, response.statusText);
            }
        } catch (error) {
            console.error("Error while setting reminder:", error);
        }
    } else {
        console.log("No date and time selected");
    }
}
