function sendNoteToTelegram(noteId) {
    const url = `/telegram/send-note/${noteId}`;

    fetch(url, {
        method: 'GET',
    })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else if (response.status === 404) {
                connectTelegramBot();
            } else {
                alert('Error sending the note to Telegram. Please try again.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while sending the note to Telegram. Please try again.');
        });
}

function connectTelegramBot() {
    const userAgrees = confirm('Do you want to connect the Telegram bot to your account?');
    if (!userAgrees) {
        return;
    }

    const url = `/telegram/get-deep-link`;

    fetch(url, {
        method: 'POST',
    })
        .then(response => response.text())
        .then(deepLink => {
            window.open(deepLink, '_blank');
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while connecting the Telegram bot. Please try again.');
        });
}