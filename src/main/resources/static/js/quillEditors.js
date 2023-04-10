function initializeQuillTitleEditor(selector) {
    const quillTitleOptions = {
        theme: 'bubble',
        placeholder: 'Title',
        modules: {
            toolbar: true,
            keyboard: {
                bindings: {
                    // Disable the Enter key completely
                    enter: {
                        key: 13, // The keycode for the Enter key
                        handler: function () {
                            return false;
                        }
                    },
                    // Disable Shift+Enter
                    shiftEnter: {
                        key: 'Enter',
                        shiftKey: true, // Add this line to detect Shift key
                        handler: function () {
                            return false;
                        }
                    }
                }
            }
        },
        bounds: selector,
        scrollingContainer: selector,
        maxLines: 1 // Limit the editor to one line
    };

    return new Quill(selector, quillTitleOptions);
}