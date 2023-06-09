$(document).ready(function () {
    $("#sidebar-btn").on("click", toggleSidebar);
    $("#overlay").on("click", closeSidebar);

    function toggleSidebar() {
        let $sidebar = $("#sidebar");
        let $overlay = $("#overlay");
        let $mainContent = $(".main-content");
        let currentLeft = $sidebar.css("left");

        if (currentLeft === "0px") {
            $sidebar.css("left", "-250px");
            $overlay.css("display", "none");
            $mainContent.removeClass("main-content-shifted");
        } else {
            $sidebar.css("left", "0px");
            $overlay.css("display", "block");
            $mainContent.addClass("main-content-shifted");
        }
        setTimeout(initMasonry, 350);
    }

    function closeSidebar() {
        let $sidebar = $("#sidebar");
        let $overlay = $("#overlay");
        let $mainContent = $(".main-content");
        $sidebar.css("left", "-250px");
        $overlay.css("display", "none");
        $mainContent.removeClass("main-content-shifted");
        setTimeout(initMasonry, 350);
    }

    $(window).on("scroll", function () {
        let $navbar = $(".custom-navbar");
        if ($(window).scrollTop() > 0) {
            $navbar.addClass("custom-navbar-shadow");
            $navbar.removeClass("custom-navbar-border");
        } else {
            $navbar.removeClass("custom-navbar-shadow");
            $navbar.addClass("custom-navbar-border");
        }
    });
});

$(document).ready(function () {
    const currentUrl = new URL(window.location.href);
    const navSearch = $('#navSearch');
    const magnifierBtn = $('#magnifier-btn');
    const magnifierBtn1 = $('#cancel-btn');
    const searchInput = navSearch.find('input[type="text"]');

    const keyword = currentUrl.searchParams.get('keyword');
    if (keyword) {
        searchInput.val(keyword);
        navSearch.addClass('focused');
    }

    searchInput.on('focus', function () {
        navSearch.addClass('focused');
    });

    magnifierBtn.on('mousedown', function (event) {
        event.preventDefault();

        if (!searchInput.is(':focus')) {
            searchInput.focus();
        } else {
            navSearch.trigger('submit');
        }
    });

    navSearch.on('submit', function (event) {
        const inputValue = searchInput.val();

        if (!inputValue.trim()) {
            event.preventDefault();

        }
    });

    magnifierBtn1.on('click', function (event) {
        event.preventDefault();

        if (window.location.pathname === '/notes') {
            searchInput.val('');
        } else {
            window.location.href = '/notes';
        }

        navSearch.removeClass('focused');
    });
});

$(document).ready(function () {
    const currentPath = window.location.pathname;

    if (currentPath === '/notes') {
        $('#exportButton').show();
    } else {
        $('#exportButton').hide();
    }
});

let currentId = null;
let currentType = null;

function openConfirmModal(itemType, itemId) {
    let mouseDownOutsideModal = false;
    const modal = document.getElementById('confirmationModal');
    modal.style.display = 'block';
    currentId = itemId;
    currentType = itemType;

    // Update the confirmation message based on the type of item being deleted
    document.getElementById('confirmModalTitle').textContent = `Confirm ${itemType.charAt(0).toUpperCase() + itemType.slice(1)} Deletion`;
    document.getElementById('confirmModalText').textContent = `Are you sure you want to delete this ${itemType}?`;

    window.addEventListener("mousedown", function (event) {
        if (event.target === modal) {
            mouseDownOutsideModal = true;
        }
    });

    window.addEventListener("mouseup", function (event) {
        if (event.target === modal && mouseDownOutsideModal) {
            closeConfirmModal();
        }
        mouseDownOutsideModal = false;
    });
}

const itemTypeEndpoints = {
    label: 'labels',
    note: 'notes',
};

function deleteItem() {
    closeConfirmModal();

    const endpoint = itemTypeEndpoints[currentType];
    const url = `/${endpoint}/${currentId}`;

    fetch(url, {
        method: 'DELETE',
    })
        .then((response) => {
            if (response.ok) {
                location.reload();
            } else {
                alert(`Error occurred while deleting the ${currentType}.`);
            }
        })
        .catch((error) => {
            console.error('Error:', error);
            alert(`Error occurred while deleting the ${currentType}.`);
        });
}

function closeConfirmModal() {
    document.getElementById('confirmationModal').style.display = 'none';
}

