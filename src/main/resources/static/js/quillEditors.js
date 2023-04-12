function initializeQuillTitleEditor(selector) {
    const quillTitleOptions = {
        theme: 'bubble',
        placeholder: 'Title',
        modules: {
            toolbar: [
                ['bold', 'italic', 'underline', 'strike'], // Basic formatting buttons
                ['link'], // Link button
                [{ 'header': 2 }],
                ['clean'], // Remove formatting
            ],
            keyboard: {
                bindings: {
                    enter: {
                        key: 13,
                        handler: function () {
                            return false;
                        }
                    },
                    shiftEnter: {
                        key: 'Enter',
                        shiftKey: true,
                        handler: function () {
                            return false;
                        }
                    }
                }
            }
        },
        bounds: selector,
        scrollingContainer: selector,
        maxLines: 1
    };


    const quill = new Quill(selector, quillTitleOptions);

    const maxCharCount = 35;

    // Disable paste functionality
    quill.container.addEventListener('paste', function (event) {
        event.preventDefault();
    });

    // // Listen for beforeinput event using jQuery
    // $(quill.container).on('beforeinput', function (event) {
    //     // Prevent line separators
    //     if (event.originalEvent.inputType === 'insertLineBreak') {
    //         event.preventDefault();
    //     }
    // });

    // Listen for text-change event
    quill.on('text-change', function () {
        const currentText = quill.getText().trim();

        if (currentText.length > maxCharCount) {
            // If the current character count exceeds the limit, revert the change
            quill.history.undo();
            quill.history.clear();
        }
    });

    return quill;
}

function createQuillNoteEditor(selector) {
    const defaultToolbarOptions = [
        ['bold', 'italic', 'underline', 'strike'],
        ['blockquote', 'code-block'],
        [{ 'size': ['small', false, 'large'] }],
        [{ 'list': 'ordered'}, { 'list': 'bullet' }],
        [{ 'script': 'sub'}, { 'script': 'super' }],
        [{ 'color': [] }, { 'background': [] }],
        [{ 'font': [] }],
        [{ 'align': [] }],
        ['clean']
    ];

    return new Quill(selector, {
        theme: 'snow',
        placeholder: 'Write your note here...',
        modules: {
            toolbar: {
                container: defaultToolbarOptions,
            },
        },
    });
}