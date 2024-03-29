document.addEventListener("DOMContentLoaded", function () {
    const returnButton = document.getElementById('returnButton');
    returnButton.addEventListener('click', function () {
        window.location.href = "../../fleet/tracker/tracker.html";
    });

    const addTrackerButton = document.querySelector('.submit-button');
    addTrackerButton.addEventListener('click', function () {
        addTracker();
    });
});

// Add a new tracker
async function addTracker() {
    const token = localStorage.getItem('jwt');
    const url = `${API_URL}/trackers`;
    const addTrackerForm = document.getElementById('addTrackerForm');

    const formData = new FormData(addTrackerForm);
    const body = {
        name: formData.get('name'),
        description: formData.get('description'),
        imei: formData.get('imei'),
    };

    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body),
        });

        if (response.ok) {
            showSuccessAlert('Tracker added successfully', () => {
                window.location.href = "../../fleet/tracker/tracker.html";
            });
        } else {
            // Display SweetAlert for error handling
            const responseData = await response.json();
            const errorMessage = responseData.debugMessage || 'Failed to add tracker';
            showErrorAlert(errorMessage);
        }
    } catch (error) {
        console.error('Error adding tracker:', error);
        showErrorAlert('An unexpected error occurred');
    }
}

// Function to show SweetAlert for success
function showSuccessAlert(message) {
    Swal.fire({
        icon: 'success',
        title: 'Success',
        text: message,
    });
}

function showSuccessAlert(message, callback) {
    Swal.fire({
        icon: 'success',
        title: 'Success',
        text: message,
    }).then((result) => {
        if (result.isConfirmed) {
            // Execute the callback function after confirmation
            callback();
        }
    });
}

// Function to show SweetAlert for error handling
function showErrorAlert(errorMessage) {
    Swal.fire({
        icon: 'error',
        title: 'Error',
        text: errorMessage,
    });
}
