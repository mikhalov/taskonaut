 (function() {
    var noteWrapper = document.getElementById('note-wrapper');
    var hiddenNoteContent = document.getElementById('hidden-note-content');
    var noteForm = document.getElementById('note-form');
    var noteTitle = document.getElementById('note-title');

    function createNoteContent() {
      var noteContent = document.createElement('textarea');
      noteContent.id = 'note-content';
      noteContent.rows = '2';
      noteContent.cols = '100';
      noteContent.placeholder = 'Write your note here...';

      noteContent.addEventListener('input', function() {
        hiddenNoteContent.value = noteContent.value;
      });

      return noteContent;
    }

    noteWrapper.addEventListener('click', function(e) {
      e.stopPropagation();
      if (!document.getElementById('note-content')) {
        var noteContent = createNoteContent();
        noteWrapper.appendChild(noteContent);
        noteWrapper.classList.add('active');
      }
    });

    document.addEventListener('click', function(e) {
      if (!noteWrapper.contains(e.target) && document.getElementById('note-content')) {
        var noteContent = document.getElementById('note-content');
        hiddenNoteContent.value = noteContent.value;
        noteWrapper.classList.remove('active');
        noteContent.parentNode.removeChild(noteContent);


        if (noteTitle.value.trim() !== '') {
          noteForm.submit();
        }
      }
    });
  })();