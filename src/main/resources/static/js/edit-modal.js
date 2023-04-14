let mouseDownOutsideModal = false;
let quill;

window.openModal = async function openModal(noteId) {
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

            // Initialize SimpleMDE editor for note content
            const simplemde = createSimpleMDEEditor('#note-content-modal');

            // Set SimpleMDE editor content
            const noteContentTextarea = document.getElementById('note-content-modal');
            simplemde.value(noteContentTextarea.value);

            // Set input field title value
            const noteTitleInput = document.getElementById('note-title-input');
            noteTitleInput.value = noteTitleInput.value;

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