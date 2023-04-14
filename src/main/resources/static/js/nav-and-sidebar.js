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
        // Call initMasonry after the sidebar's state has changed
        setTimeout(initMasonry, 350);
    }

    function closeSidebar() {
        let $sidebar = $("#sidebar");
        let $overlay = $("#overlay");
        let $mainContent = $(".main-content");
        $sidebar.css("left", "-250px");
        $overlay.css("display", "none");
        $mainContent.removeClass("main-content-shifted");
        // Call initMasonry after the sidebar's state has changed
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

    // Set the input value from the URL keyword parameter if it exists
    const keyword = currentUrl.searchParams.get('keyword');
    if (keyword) {
        searchInput.val(keyword);
        navSearch.addClass('focused');
    }

    searchInput.on('focus', function () {
        navSearch.addClass('focused');
    });

    // Attach the event listener to the magnifier-btn and the form's submit event
    magnifierBtn.on('mousedown', function (event) {
        event.preventDefault();

        // Focus on the searchInput if it's not already focused
        if (!searchInput.is(':focus')) {
            searchInput.focus();
        } else {
            navSearch.trigger('submit');
        }
    });

    navSearch.on('submit', function (event) {
        const inputValue = searchInput.val();

        // Check if the input value is empty or contains only whitespace
        if (!inputValue.trim()) {
            event.preventDefault(); // Do not submit the form

        }
    });

    magnifierBtn1.on('click', function (event) {
        event.preventDefault(); // Prevent form submission

        // Check if the current page is already '/notes'
        if (window.location.pathname === '/notes') {
            // Clear the input
            searchInput.val('');
        } else {
            // Redirect to the desired URL
            window.location.href = '/notes';
        }

        navSearch.removeClass('focused');
    });
});

$(document).ready(function () {
    const currentUrl = new URL(window.location.href);
    const currentPage = currentUrl.pathname;
    const sortForm = $('#sortForm');

    if (sortForm.length && (currentPage === '/notes' || currentPage === '/notes/search' || currentPage.startsWith('/labels/'))) {
        // Keep existing query parameters
        let newActionUrl = currentUrl;

        // Remove existing 'sort' and 'asc' parameters if they exist
        newActionUrl.searchParams.delete('sort');
        newActionUrl.searchParams.delete('asc');

        // Iterate over preserved query parameters and append hidden input fields
        for (const [key, value] of newActionUrl.searchParams.entries()) {
            const preservedParam = $('<input>', {
                type: 'hidden',
                name: key,
                value: value
            });
            sortForm.append(preservedParam);
        }

        // Set the new action attribute without any query parameters
        sortForm.attr('action', currentPage);
    }
});