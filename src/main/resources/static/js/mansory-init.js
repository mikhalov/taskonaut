function initMasonry() {
   var grid = document.querySelector('.notes-grid');
    var msnry = new Masonry( grid, {
        itemSelector: '.grid-item',
        columnWidth: '.grid-item',
        gutter: 20,
        percentPosition: true
    });
}
