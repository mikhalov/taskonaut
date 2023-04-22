function sendNoteToTelegram(noteId) {
    // Replace with the actual note ID and URL
    const url = `/telegram/send-note/${noteId}`;

    fetch(url, {
        method: 'GET',
    })
        .then(response => {
            if (response.ok) {
                // Refresh the page on a successful response
                location.reload();
            } else {
                // Display an alert on an error response
                alert('Error sending the note to Telegram. Please try again.');
            }
        })
        .catch(error => {
            // Display an alert on a network error or other issues
            console.error('Error:', error);
            alert('An error occurred while sending the note to Telegram. Please try again.');
        });
}