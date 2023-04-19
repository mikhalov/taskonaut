$(document).ready(function () {
    $('#sortForm').on('submit', function (event) {
        event.preventDefault();

        const currentUrl = new URL(window.location.href);

        const sortValue = $('input[name="sort"]').val();
        const ascValue = $('#sortCheckbox').is(':checked') ? 'true' : 'false';

        currentUrl.searchParams.set('sort', sortValue);
        currentUrl.searchParams.set('asc', ascValue);

        window.location.href = currentUrl.toString();
    });
});

function goToPage(page) {
    const url = new URL(window.location.href);
    url.searchParams.set('page', page);
    window.location.href = url.toString();
}




function toggleSelect(event) {
    if (event) {
        event.stopPropagation();
    }
    var selectItems = document.querySelector(".select-items");
    selectItems.classList.toggle("select-hide");
}

function selectOption(element) {
    var value = element.getAttribute("data-value");
    var selected = document.querySelector(".select-selected .sorting-value"); // Updated selector
    var hiddenInput = document.querySelector(".select-selected input");
    selected.innerText = element.innerText;
    hiddenInput.value = value;
    toggleSelect(event);
}
window.addEventListener("click", function(event) {
    var selectItems = document.querySelector(".select-items");
    if (!event.target.matches(".select-selected")) {
        selectItems.classList.add("select-hide");
    }
});

function updateCheckboxLabel() {
    var checkbox = document.getElementById("sortCheckbox");
    var label = document.getElementById("checkboxLabel");

    if (checkbox.checked) {
        label.innerHTML = "&#x2191;"; // Up arrow
    } else {
        label.innerHTML = "&#x2193;"; // Down arrow
    }
}

document.addEventListener('DOMContentLoaded', function() {
    updateCheckboxLabel();
});

function exportNotes() {
    const sortValue = $('input[name="sort"]').val();
    const ascValue = $('#sortCheckbox').is(':checked') ? 'true' : 'false';
    location.href = `/notes/export/pdf?sort=${sortValue}&asc=${ascValue}`;
}

