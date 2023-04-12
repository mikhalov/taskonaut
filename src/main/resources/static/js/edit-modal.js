let mouseDownOutsideModal = false;
let quill;

async function openModal(noteId) {
    const modal = document.getElementById("editModal");
    const modalContent = modal.querySelector(".modal-content");

    window.addEventListener("mousedown", function (event) {
        if (event.target === modal) {
            mouseDownOutsideModal = true;
        }
    });

    window.addEventListener("mouseup", function (event) {
        if (event.target === modal && mouseDownOutsideModal) {
            closeModal();
        }
        mouseDownOutsideModal = false;
    });


    try {
        const response = await fetch('/notes/' + noteId);
        if (response.ok) {
            const formHtml = await response.text();
            modalContent.innerHTML = formHtml;
            modal.classList.remove("hidden");

            const form = modalContent.querySelector("form");

            quill = createQuillNoteEditor('#note-content-modal');

            // Set Quill editor content
            const noteContentHidden = document.getElementById('note-content-hidden');
            quill.root.innerHTML = noteContentHidden.value;

            const quillTitle = initializeQuillTitleEditor('#note-title-modal');

            // Set Quill editor title
            const noteTitleHidden = document.getElementById('note-title-hidden');
            quillTitle.root.innerHTML = noteTitleHidden.value;

            // Update the hidden input fields with the Quill editor content and title when the form is submitted
            form.addEventListener('submit', function (event) {
                noteContentHidden.value = quill.root.innerHTML;
                noteTitleHidden.value = quillTitle.root.innerHTML;
            });

        } else {
            console.error('Error fetching the form:', response.status);
        }
    } catch (error) {
        console.error('Error fetching the form:', error);
    }
}


function closeModal() {
    const modal = document.getElementById("editModal");
    modal.classList.add("hidden");
}
