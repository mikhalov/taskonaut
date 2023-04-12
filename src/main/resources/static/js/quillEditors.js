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
        ['image'],
        ['clean']
    ];

    return new Quill(selector, {
        theme: 'snow',
        placeholder: 'Write your note here...',
        modules: {
            toolbar: {
                container: defaultToolbarOptions,
                handlers: {
                    'image': imageHandler,
                },
            },
        },
    });
}

function imageHandler() {
    const input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.setAttribute('accept', 'image/*');
    input.click();

    input.addEventListener('change', () => {
        const file = input.files[0];
        if (file) {
            // Upload the image and get the URL for the uploaded image
            uploadImage(file).then((imageUrl) => {
                // Insert the image URL in the editor using the custom image blot
                const range = this.quill.getSelection(true);
                this.quill.insertEmbed(range.index, 'customImage', { url: imageUrl });
                this.quill.setSelection(range.index + 1);
            });
        }
    });
}
async function uploadImage(file) {
    const formData = new FormData();
    formData.append('image', file);

    const response = await fetch(`https://api.imgbb.com/1/upload?key=d63a64ad9c8727a5e12fded220139bff`, {
        method: 'POST',
        body: formData,
    });

    if (response.ok) {
        const data = await response.json();
        return data.data.url; // Get the image URL from the response
    } else {
        throw new Error('Failed to upload image');
    }
}

const BlockEmbed = Quill.import('blots/block/embed');

class CustomImageBlot extends BlockEmbed {
    static create(value) {
        let node = super.create(value);
        node.setAttribute('src', value.url);
        node.setAttribute('width', '200');
        // node.setAttribute('height', '200');
        node.addEventListener('click', () => {
            window.open(value.url, '_blank');
        });
        return node;
    }

    static value(node) {
        return { url: node.getAttribute('src') };
    }
}

CustomImageBlot.blotName = 'customImage';
CustomImageBlot.tagName = 'img';

Quill.register(CustomImageBlot);