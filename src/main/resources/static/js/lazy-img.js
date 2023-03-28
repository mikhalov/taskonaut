document.addEventListener("DOMContentLoaded", function () {
  // Get an array of all visible elements on the page
  const visibleElements = Array.from(document.querySelectorAll(":not(.hidden)"));

  // Keep track of how many visible elements have loaded
  let loadedCount = 0;

  // Hide all visible elements until they are fully loaded
  visibleElements.forEach(function (visibleElement) {
    visibleElement.classList.add("hidden");
  });

  // Show all visible elements when they are fully loaded
  function showVisibleElements() {
    visibleElements.forEach(function (visibleElement) {
      visibleElement.classList.remove("hidden");
    });
  }

  // Attach a "readystatechange" event listener to the document to detect when all resources have finished loading
  document.addEventListener("readystatechange", function () {
    if (document.readyState === "complete") {
      showVisibleElements();
    }
  });
});