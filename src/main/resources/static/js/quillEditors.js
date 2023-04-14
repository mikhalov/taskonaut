function createSimpleMDEEditor(selector) {
    return new SimpleMDE({
        element: document.querySelector(selector),
        toolbar: [
            'bold', 'italic', 'strikethrough', 'heading', '|',
            'quote', 'unordered-list', 'ordered-list', '|',
            'link', 'image', '|',
            'preview', 'side-by-side', 'fullscreen', '|',
            'guide'
        ],
        placeholder: 'Write your note here...',
        showIcons: ['code', 'table'],
        status: false,
    });
}