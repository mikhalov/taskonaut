(function () {
  var noteWrapper = document.getElementById('note-wrapper');
  var hiddenNoteContent = document.getElementById('hidden-note-content');
  var noteForm = document.getElementById('note-form');
  var noteTitle = document.getElementById('note-title');

  var quill = null;

  function createQuillEditor() {
    if (quill === null) {
      const defaultToolbarOptions = [
        ['bold', 'italic', 'underline', 'strike'],
        ['blockquote', 'code-block'],
        [{ 'header': 1 }, { 'header': 2 }],
        [{ 'list': 'ordered'}, { 'list': 'bullet' }],
        [{ 'script': 'sub'}, { 'script': 'super' }],
        [{ 'indent': '-1'}, { 'indent': '+1' }],
        [{ 'direction': 'rtl' }],
        [{ 'size': ['small', false, 'large', 'huge'] }],
        [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
        [{ 'color': [] }, { 'background': [] }],
        [{ 'font': [] }],
        [{ 'align': [] }],
        ['clean']
      ];

      quill = new Quill('#editor', {
        theme: 'snow',
        placeholder: 'Write your note here...',
        modules: {
          toolbar: {
            container: defaultToolbarOptions,
          },
        },
      });

      const toolbarContainer = document.getElementById('toolbar-container');
      toolbarContainer.appendChild(quill.getModule('toolbar').container);

      quill.on('text-change', function () {
        hiddenNoteContent.value = quill.root.innerHTML;
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