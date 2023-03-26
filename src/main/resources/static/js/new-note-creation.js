var noteWrapper = document.querySelector('.note-wrapper');

noteWrapper.addEventListener('click', function(e) {
  var noteContent = document.getElementById('note-content');

  if (!noteContent) {
    noteContent = document.createElement('textarea');
    noteContent.id = 'note-content';
    noteContent.rows = '2';
    noteContent.cols = '100';
    noteContent.placeholder = 'Write your note here...';
    noteWrapper.appendChild(noteContent);
    noteWrapper.classList.add('active');
  }
});

document.addEventListener('click', function(e) {
  var target = e.target;

  while (target.parentNode) {
    if (target === noteWrapper) {
      return;
    }

    target = target.parentNode;
  }

  var noteContent = document.getElementById('note-content');

  if (noteContent) {
    noteWrapper.classList.remove('active');
    noteContent.parentNode.removeChild(noteContent);
  }
});
