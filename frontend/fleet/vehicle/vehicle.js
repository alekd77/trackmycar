document.addEventListener("DOMContentLoaded", function () {
    // Add an event listener to the logout button
    document.getElementById('logoutButton').addEventListener('click', function () {
        // Call the logout function from auth/logout.js
        logout();
    });
    
    const addButton = document.querySelector('.add-button');
    addButton.addEventListener('click', function () {
        window.location.href = '../../fleet/vehicle/add-vehicle.html';
    });

    document.addEventListener('click', hideAllMenuOptions);

    getVehicles();
});

async function getVehicles() {
    const token = localStorage.getItem('jwt');
    const url = `${API_URL}/vehicles`;

    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const vehicles = await response.json();
            console.log('Vehicles:', vehicles);
            displayVehicles(vehicles);
        } else {
            console.error('Failed to fetch vehicles:', response.statusText);
        }
    } catch (error) {
        console.error('Error fetching vehicles:', error);
    }
}

async function displayVehicles(vehicles) {
    const tbody = document.querySelector('#vehicleTable tbody');
    tbody.innerHTML = '';

    for (const vehicle of vehicles) {
        const row = await createVehicleRow(vehicle);
        tbody.appendChild(row);
    }

    document.addEventListener('click', hideAllMenuOptions);
}

async function createVehicleRow(vehicle) {
    const row = document.createElement('tr');
    row.innerHTML = `
        <td>${vehicle.vehicleId}</td>
        <td></td>
        <td>${vehicle.name}</td>
        <td>${vehicle.description}</td>
        <td class="tracker-name-cell">-</td>
        <td></td>
    `;

    const vehicleIcon = document.createElement('img');
    vehicleIcon.src = '../../res/images/car.svg'
    const markerHexColor = await getMarkerHexColor(vehicle.vehicleId);
    vehicleIcon.style.backgroundColor = markerHexColor;
    vehicleIcon.alt = 'Car Icon';
    vehicleIcon.classList.add('vehicle-icon');
    row.children[1].appendChild(vehicleIcon);

    const menuButton = createMenuButton(vehicle.vehicleId);
    const menuOptions = await createMenuOptions(vehicle.vehicleId, row);

    menuButton.addEventListener('click', function (event) {
        event.stopPropagation();
        menuOptions.classList.toggle('visible');
    });

    const trackerNameCell = row.querySelector('.tracker-name-cell');
    const assignmentDetails = await getAssignmentInfoByVehicleId(vehicle.vehicleId);
    if (assignmentDetails?.trackerId) {
        const trackerDetails = await getTrackerDetails(assignmentDetails.trackerId);
        trackerNameCell.textContent = trackerDetails?.name;
    } else {
        trackerNameCell.textContent = '-';
    }

    row.children[5].appendChild(menuButton);
    row.children[5].appendChild(menuOptions);

    return row;
}

function createMenuButton(vehicleId) {
    const button = document.createElement('button');
    button.classList.add('button', 'menu-button');
    button.setAttribute('data-vehicle-id', vehicleId);
    button.textContent = '...';
    return button;
}

async function createMenuOptions(vehicleId, row) {
    const options = document.createElement('div');
    options.classList.add('menu-options', 'hidden');

    const editButton = document.createElement('button');
    editButton.classList.add('button', 'edit-button');
    editButton.setAttribute('data-vehicle-id', vehicleId);
    editButton.textContent = 'Edit';

    editButton.addEventListener('click', function () {
        window.location.href = `../../fleet/vehicle/edit-vehicle.html?vehicleId=${vehicleId}`;
    });

    const assignTrackerButton = document.createElement('button');
    assignTrackerButton.classList.add('button', 'assign-tracker-button');
    assignTrackerButton.setAttribute('data-vehicle-id', vehicleId);

    const assignmentInfo = await getAssignmentInfoByVehicleId(vehicleId);

    if (assignmentInfo && assignmentInfo.assignmentId) {
        // If there is an active assignment, set text to "Deactivate Tracker's Assignment"
        assignTrackerButton.textContent = 'Unassign tracker';

        assignTrackerButton.addEventListener('click', async function () {
            const assignmentId = assignmentInfo.assignmentId;
            await deactivateAssignment(assignmentId);
        });
    } else {
        // If no active assignment, set text to "Assign Tracker"
        assignTrackerButton.textContent = 'Assign Tracker';

        assignTrackerButton.addEventListener('click', function () {
            window.location.href = `../../fleet/vehicle/assign-tracker.html?vehicleId=${vehicleId}`;
        });
    }

    const deleteButton = document.createElement('button');
    deleteButton.classList.add('button', 'delete-button');
    deleteButton.setAttribute('data-vehicle-id', vehicleId);
    deleteButton.textContent = 'Delete';

    deleteButton.addEventListener('click', function () {
        const vehicleId = deleteButton.getAttribute('data-vehicle-id');
        deleteVehicle(vehicleId);
    });

    options.appendChild(editButton);
    options.appendChild(assignTrackerButton);
    options.appendChild(deleteButton);

    return options;
}

async function deactivateAssignment(assignmentId) {
    const token = localStorage.getItem('jwt');
    const url = `${API_URL}/assignments/deactivate/${assignmentId}`;

    try {
        const response = await fetch(url, {
            method: 'PUT',  // Assuming the endpoint supports PUT for deactivation
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            console.log('Assignment deactivated successfully');
            showSuccessAlert('Tracker has been unassigned successfully', getVehicles);
        } else {
            console.error('Failed to deactivate assignment:', response.statusText);
        }
    } catch (error) {
        console.error('Error deactivating assignment:', error);
    }
}

async function deleteVehicle(vehicleId) {
    // Display a confirmation dialog before proceeding with deletion
    const isConfirmed = await showConfirmationDialog(
        'Delete Vehicle',
        'This action will permanently remove all associated data (including trips, assignments, statistics, and any other related information). Do you want to proceed?'
    );

    if (isConfirmed) {
        const token = localStorage.getItem('jwt');
        const url = `${API_URL}/vehicles/${vehicleId}`;

        try {
            const response = await fetch(url, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                console.log('Vehicle deleted successfully');
                showSuccessAlert('Vehicle deleted successfully', getVehicles);
            } else {
                console.error('Failed to delete vehicle:', response.statusText);
            }
        } catch (error) {
            console.error('Error deleting vehicle:', error);
        }
    }
}

async function getAssignmentInfoByVehicleId(vehicleId) {
    const token = localStorage.getItem('jwt');
    const assignmentUrl = `${API_URL}/assignments/active/by-vehicle/${vehicleId}`;

    try {
        const assignmentResponse = await fetch(assignmentUrl, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (assignmentResponse?.ok) {
            const assignment = await assignmentResponse.json();
            return { assignmentId: assignment.assignmentId, vehicleId: vehicleId, trackerId: assignment.trackerId };
        } else {
            console.error(`No active assignment found for vehicle with ID: ${vehicleId}`);
            return null;
        }
    } catch (error) {
        console.error('Error fetching assignment:', error);
        return null;
    }
}

async function getTrackerDetails(trackerId) {
    const token = localStorage.getItem('jwt');
    const trackerUrl = `${API_URL}/trackers/${trackerId}`;

    try {
        const trackerResponse = await fetch(trackerUrl, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (trackerResponse?.ok) {
            return await trackerResponse.json();
        } else {
            const errorData = await trackerResponse.json();
            console.error('Failed to fetch tracker:', errorData.debugMessage);
            return null;
        }
    } catch (error) {
        console.error('Error fetching tracker:', error);
        return null;
    }
}

async function getMarkerHexColor(vehicleId) {
    const token = localStorage.getItem('jwt');
    const url = `${API_URL}/vehicles/${vehicleId}`;

    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const vehicleData = await response.json();
            return vehicleData.markerHexColor;
        } else {
            console.error('Failed to fetch markerHexColor:', response.statusText);
            // Provide a default color or handle the error as needed
            return '#000000'; // Default to black if there is an error
        }
    } catch (error) {
        console.error('Error fetching markerHexColor:', error);
        // Provide a default color or handle the error as needed
        return '#000000'; // Default to black if there is an error
    }
}

function hideAllMenuOptions() {
    const allMenuOptions = document.querySelectorAll('.menu-options');
    allMenuOptions.forEach((options) => {
        options.classList.add('hidden');
    });
}

// Function to display a confirmation dialog
async function showConfirmationDialog(title, message) {
    return new Promise((resolve) => {
        Swal.fire({
            title: title,
            text: message,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'YES',
            cancelButtonText: 'NO',
        }).then((result) => {
            resolve(result.isConfirmed);
        });
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
