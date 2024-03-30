document.addEventListener("DOMContentLoaded", function () {
    // Add an event listener to the logout button
    document.getElementById('logoutButton').addEventListener('click', function () {
        // Call the logout function from auth/logout.js
        logout();
    });

    const returnButton = document.getElementById('returnButton');
    returnButton.addEventListener('click', function () {
        window.location.href = "../../fleet/vehicle/vehicle.html";
    });

    const addVehicleButton = document.querySelector('.submit-button');
    addVehicleButton.addEventListener('click', handleAddVehicle);
});

async function handleAddVehicle() {
    const vehicleResponse = await addVehicle();

    // Check if the vehicle was added successfully before showing the success alert
    if (vehicleResponse) {
        // Show the success alert and redirect after confirmation
        showSuccessAlert('Vehicle added successfully', () => {
            window.location.href = "../../fleet/vehicle/vehicle.html";
        });
    }
}

async function addVehicle() {
    const token = localStorage.getItem('jwt');
    const url = `${API_URL}/vehicles`;
    const addVehicleForm = document.getElementById('addVehicleForm');

    const formData = new FormData(addVehicleForm);
    const body = {
        name: formData.get('name'),
        description: formData.get('description'),
        markerHexColor: formData.get('markerColor'),
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
            const responseData = await response.json();
            return responseData; // Return the response data directly
        } else {
            const responseData = await response.json();
            showErrorAlert(responseData.debugMessage || 'Failed to add vehicle');
            return null;
        }
    } catch (error) {
        console.error('Error adding vehicle:', error);
        showErrorAlert('An unexpected error occurred');
        return null;
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
