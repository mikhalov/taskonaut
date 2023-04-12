(function () {
    var noteWrapper = document.getElementById('note-wrapper');
    var hiddenNoteContent = document.getElementById('hidden-note-content');
    var noteForm = document.getElementById('note-form');
    var noteTitle = document.getElementById('note-title');

    var quill = null;
    var quillTitle = null;

    function createQuillEditor() {
        if (quill === null) {
            quill = createQuillNoteEditor('#editor');
        }

        const toolbarContainer = document.getElementById('toolbar-container');
        toolbarContainer.appendChild(quill.getModule('toolbar').container);

        quill.on('text-change', function () {
            hiddenNoteContent.value = quill.root.innerHTML;
        });
    }

    function createQuillTitleEditor() {
        if (quillTitle === null) {
            quillTitle = initializeQuillTitleEditor('#note-title-editor');

            quillTitle.on('text-change', function () {
                noteTitle.value = quillTitle.root.innerHTML;
            });
        }
    }

    function hideToolbar() {
        const toolbar = document.querySelector('#toolbar-container');
        if (toolbar) {
            toolbar.classList.add('toolbar-hidden');
        }
    }

    function showToolbar() {
        const toolbar = document.querySelector('#toolbar-container');
        if (toolbar) {
            toolbar.classList.remove('toolbar-hidden');
        }
    }

    noteWrapper.addEventListener('click', function (e) {
        e.stopPropagation();
        if (!quill) {
            createQuillEditor();
        }
        if (!quillTitle) {
            createQuillTitleEditor();
        }
        if (!noteWrapper.classList.contains('active')) {
            document.getElementById('editor').classList.remove('hidden');
            noteWrapper.classList.add('active');
            showToolbar(); // Make sure this line is present
        }
    });

    document.addEventListener('click', function (e) {
        if (!noteWrapper.contains(e.target) && quill) {
            hiddenNoteContent.value = quill.root.innerHTML;
            document.getElementById('editor').classList.add('hidden');
            noteWrapper.classList.remove('active');
            hideToolbar(); // Make sure this line is present

            if (noteTitle.value.trim() !== '') {
                noteForm.submit();
            }
        }
    });
})();