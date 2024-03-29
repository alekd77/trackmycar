document.addEventListener("DOMContentLoaded", function () {
    const returnButton = document.getElementById('returnButton');
    const editVehicleButton = document.querySelector('.submit-button');

    // Handle return button click
    returnButton.addEventListener('click', function () {
        window.location.href = "/fleet/vehicle/vehicle.html";
    });

    // Handle edit vehicle button click
    editVehicleButton.addEventListener('click', handleEditVehicle);

    // Initialize form fields with current vehicle data
    initializeEditForm();
});

async function initializeEditForm() {
    const token = localStorage.getItem('jwt');
    const urlParams = new URLSearchParams(window.location.search);
    const vehicleId = urlParams.get('vehicleId');

    try {
        const response = await fetch(`${API_URL}/vehicles/${vehicleId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });

        if (response.ok) {
            const vehicleData = await response.json();

            // Set initial values for form fields
            document.getElementById('name').value = vehicleData.name;
            document.getElementById('description').value = vehicleData.description;
            document.getElementById('markerColor').value = vehicleData.markerHexColor;
        } else {
            const responseData = await response.json();
            showErrorAlert(responseData.debugMessage || 'Failed to fetch vehicle data');
        }
    } catch (error) {
        console.error('Error fetching vehicle data:', error);
        showErrorAlert('An unexpected error occurred while fetching vehicle data');
    }
}

async function handleEditVehicle() {
    const token = localStorage.getItem('jwt');
    const urlParams = new URLSearchParams(window.location.search);
    const vehicleId = urlParams.get('vehicleId');
    const url = `${API_URL}/vehicles/${vehicleId}`;
    const editVehicleForm = document.getElementById('editVehicleForm');

    const formData = new FormData(editVehicleForm);
    const body = {
        name: formData.get('name'),
        description: formData.get('description'),
        markerHexColor: formData.get('markerColor'),
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
            showSuccessAlert('Vehicle updated successfully', () => {
                window.location.href = "/fleet/vehicle/vehicle.html";
            });
        } else {
            const responseData = await response.json();
            showErrorAlert(responseData.debugMessage || 'Failed to update vehicle');
        }
    } catch (error) {
        console.error('Error updating vehicle:', error);
        showErrorAlert('An unexpected error occurred while updating vehicle');
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
