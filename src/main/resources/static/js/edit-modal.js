

async function openModal(noteId) {
    let mouseDownOutsideModal = false;
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
            modalContent.innerHTML = await response.text();
            modal.classList.remove("hidden");

            const form = modalContent.querySelector("form");
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
