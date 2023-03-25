let mouseDownOutsideModal = false;

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
        const response = await fetch('/notes/' + noteId + '/edit');
        if (response.ok) {
            const formHtml = await response.text();
            modalContent.innerHTML = formHtml;
            modal.classList.remove("hidden");

            const form = modalContent.querySelector("form");
            const cancelButton = form.querySelector(".btn-secondary");

            form.addEventListener("submit", async (event) => {
                event.preventDefault();
                await submitForm(noteId, form);
            });

            cancelButton.addEventListener("click", closeModal);

        } else {
            console.error('Error fetching the form:', response.status);
        }
    } catch (error) {
        console.error('Error fetching the form:', error);
    }
}

async function submitForm(noteId, form) {
    const formData = new FormData(form);
    try {
        const response = await fetch('/notes/' + noteId, {
            method: 'POST',
            body: formData,
        });
        if (response.ok) {
            location.reload(); // Reload the page to update the note card
        } else {
            console.error('Error submitting the form:', response.status);
        }
    } catch (error) {
        console.error('Error submitting the form:', error);
    }
}

function closeModal() {
    const modal = document.getElementById("editModal");
    modal.classList.add("hidden");
}


function deleteNote(noteId) {
    fetch('/notes/' + noteId, {
        method: 'DELETE',
    }).then(() => {
        window.location.reload();
    }).catch((error) => {
        console.error('Error:', error);
    });
}