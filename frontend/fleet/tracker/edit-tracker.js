document.addEventListener("DOMContentLoaded", function () {
    const returnButton = document.getElementById('returnButton');
    const editTrackerButton = document.querySelector('.submit-button');

    // Handle return button click
    returnButton.addEventListener('click', function () {
        window.location.href = "../../fleet/tracker/tracker.html";
    });

    // Handle edit vehicle button click
    editTrackerButton.addEventListener('click', handleEditTracker);

    // Initialize form fields with current vehicle data
    initializeEditForm();
});

async function initializeEditForm() {
    const token = localStorage.getItem('jwt');
    const urlParams = new URLSearchParams(window.location.search);
    const trackerId = urlParams.get('trackerId');

    try {
        const response = await fetch(`${API_URL}/trackers/${trackerId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });

        if (response.ok) {
            const trackerData = await response.json();

            // Set initial values for form fields
            document.getElementById('name').value = trackerData.name;
            document.getElementById('description').value = trackerData.description;
            document.getElementById('imei').value = trackerData.imei;
        } else {
            const responseData = await response.json();
            showErrorAlert(responseData.debugMessage || 'Failed to fetch tracker data');
        }
    } catch (error) {
        console.error('Error fetching tracker data:', error);
        showErrorAlert('An unexpected error occurred while fetching tracker data');
    }
}

async function handleEditTracker() {
    const token = localStorage.getItem('jwt');
    const urlParams = new URLSearchParams(window.location.search);
    const trackerId = urlParams.get('trackerId');
    const url = `${API_URL}/trackers/${trackerId}`;
    const editTrackerForm = document.getElementById('editTrackerForm');

    const formData = new FormData(editTrackerForm);
    const body = {
        name: formData.get('name'),
        description: formData.get('description'),
        imei: formData.get('imei'),
    };

    try {
        const response = await fetch(url, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body),
        });

        if (response.ok) {
            showSuccessAlert('Tracker updated successfully', () => {
                window.location.href = "../../fleet/tracker/tracker.html";
            });
        } else {
            const responseData = await response.json();
            showErrorAlert(responseData.debugMessage || 'Failed to update tracker');
        }
    } catch (error) {
        console.error('Error updating tracker:', error);
        showErrorAlert('An unexpected error occurred while updating tracker');
    }
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

function showErrorAlert(errorMessage) {
    Swal.fire({
        icon: 'error',
        title: 'Error',
        text: errorMessage,
    });
}
