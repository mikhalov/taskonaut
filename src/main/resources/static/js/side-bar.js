document.getElementById("labels-btn").addEventListener("click", function () {
    toggleSidebar();
});

document.getElementById("overlay").addEventListener("click", function () {
    closeSidebar();
});

function toggleSidebar() {
    let sidebar = document.getElementById("sidebar");
    let overlay = document.getElementById("overlay");
    let mainContent = document.querySelector(".main-content");
    let currentLeft = sidebar.style.left;

    if (currentLeft === "0px") {
        sidebar.style.left = "-250px";
        overlay.style.display = "none";
        mainContent.classList.remove("main-content-shifted");
    } else {
        sidebar.style.left = "0px";
        overlay.style.display = "block";
        mainContent.classList.add("main-content-shifted");
    }
    // Call initMasonry after the sidebar's state has changed
    setTimeout(initMasonry, 350);

}

function closeSidebar() {
    let sidebar = document.getElementById("sidebar");
    let overlay = document.getElementById("overlay");
    let mainContent = document.querySelector(".main-content");
    sidebar.style.left = "-250px";
    overlay.style.display = "none";
    mainContent.classList.remove("main-content-shifted");
    // Call initMasonry after the sidebar's state has changed
    setTimeout(initMasonry, 350);
}


window.addEventListener("scroll", function() {
    let navbar = document.querySelector(".custom-navbar");
    if (window.scrollY > 0) {
        navbar.classList.add("custom-navbar-shadow");
        navbar.classList.remove("custom-navbar-border");
    } else {
        navbar.classList.remove("custom-navbar-shadow");
        navbar.classList.add("custom-navbar-border");
    }
});

