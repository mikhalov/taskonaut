(function () {
    var noteWrapper = document.getElementById('note-wrapper');
    var hiddenNoteContent = document.getElementById('hidden-note-content');
    var noteForm = document.getElementById('note-form');
    var noteTitle = document.getElementById('note-title');

    function autoResize(textarea) {
        var minHeight = parseInt(window.getComputedStyle(textarea).getPropertyValue('min-height'));
        textarea.style.height = 'auto';
        textarea.style.height = Math.max(textarea.scrollHeight, minHeight) + 'px';
    }

    function createNoteContent() {
        var noteContent = document.createElement('textarea');
        noteContent.id = 'note-content';
        noteContent.rows = '1';
        noteContent.placeholder = 'Write your note here...';

        noteContent.addEventListener('input', function () {
            hiddenNoteContent.value = noteContent.value;
            autoResize(noteContent);
        });

        autoResize(noteContent);

        return noteContent;
    }

    noteWrapper.addEventListener('click', function (e) {
        e.stopPropagation();
        if (!document.getElementById('note-content')) {
            var noteContent = createNoteContent();
            noteWrapper.appendChild(noteContent);
            noteWrapper.classList.add('active');
        }
    });

    document.addEventListener('click', function (e) {
        if (!noteWrapper.contains(e.target) && document.getElementById('note-content')) {
            var noteContent = document.getElementById('note-content');
            hiddenNoteContent.value = noteContent.value;
            noteWrapper.classList.remove('active');
            noteContent.parentNode.removeChild(noteContent);


            if (noteTitle.value.trim() !== '') {
                noteForm.submit();
            }
        }
    });
})();


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
// Close the dropdown when clicking outside
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

// Set the initial label content based on the checkbox state
document.addEventListener('DOMContentLoaded', function() {
    updateCheckboxLabel();
});