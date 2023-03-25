function initMasonry() {
   var grid = document.querySelector('.notes-grid');
    var msnry = new Masonry( grid, {
        // options
        itemSelector: '.grid-item',
        columnWidth: '.grid-item',
        gutter: 10,
//        fitWidth: true,
        percentPosition: true
    });


}
