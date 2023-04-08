$(document).ready(function() {
    $("#labels-btn").on("click", toggleSidebar);
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

    $(window).on("scroll", function() {
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

function centerNavSearch() {
    const $customNavbar = $("#custom-navbar");
    const $navContent = $("#nav-content");
    const $navSearch = $("#nav-search");

    const navbarWidth = $customNavbar.width();
    const navContentWidth = $navContent.width();
    const navSearchWidth = $navSearch.width();

    const remainingSpace = navbarWidth - navContentWidth;
    const navSearchMargin = (remainingSpace - navSearchWidth) / 2;

    $navSearch.css("marginLeft", navContentWidth + navSearchMargin);
}

// Call the function to center the nav-search when the page loads
$(document).ready(centerNavSearch);

// Call the function to center the nav-search when the window is resized
$(window).on("resize", centerNavSearch);