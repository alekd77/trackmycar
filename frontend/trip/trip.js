document.addEventListener('DOMContentLoaded', function() {
    // Add an event listener to the logout button
    document.getElementById('logoutButton').addEventListener('click', function () {
        // Call the logout function from auth/logout.js
        logout();
    });

    document.addEventListener('click', hideAllMenuOptions);

    getTrips();
});

async function getTrips() {
    const token = localStorage.getItem('jwt');
    const url = `${API_URL}/trips`;

    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const trips = await response.json();
            console.log('Trips:', trips);
            displayTrips(trips);
        } else {
            console.error('Failed to fetch trips:', response.statusText);
        }
    } catch (error) {
        console.error('Error fetching trips:', error);
    }
}

async function displayTrips(trips) {
    const tbody = document.querySelector('#tripsTable tbody');
    tbody.innerHTML = '';

    for (const trip of trips) {
        const row = await createTripTableRow(trip);
        tbody.appendChild(row);
    }

    document.addEventListener('click', hideAllMenuOptions);
}

async function createTripTableRow(trip) {
    const row = document.createElement('tr');
    const formattedStartTimestamp = new Date(trip.startTimestamp).toLocaleString();
    const formattedTotalDistance = trip.totalDistance ? trip.totalDistance.toFixed(2) + ' km' : '-';
    const formattedMaxSpeed = trip.maxSpeed ? trip.maxSpeed.toFixed(2) + ' km/h' : '-';

    row.innerHTML = `
        <td>${trip.tripId}</td>
        <td>${trip.vehicle.name}</td>
        <td>${trip.name}</td>
        <td>${formattedStartTimestamp}</td>
        <td>${formattedTotalDistance}</td>
        <td>${formattedMaxSpeed}</td>
        <td></td>
    `;

    const menuButton = createMenuButton(trip.tripId);
    const menuOptions = await createMenuOptions(trip.tripId);

    menuButton.addEventListener('click', function (event) {
        event.stopPropagation();
        menuOptions.classList.toggle('visible');
    });

    row.children[6].appendChild(menuButton);
    row.children[6].appendChild(menuOptions);

    return row;
}

function createMenuButton(tripId) {
    const button = document.createElement('button');
    button.classList.add('button', 'menu-button');
    button.setAttribute('data-trip-id', tripId);
    button.textContent = '...';
    return button;
}

async function createMenuOptions(tripId) {
    const options = document.createElement('div');
    options.classList.add('menu-options', 'hidden');

    const viewDetailsButton = document.createElement('button');
    viewDetailsButton.classList.add('button', 'view-details-button');
    viewDetailsButton.setAttribute('data-trip-id', tripId);
    viewDetailsButton.textContent = 'View Details';

    viewDetailsButton.addEventListener('click', function () {
        window.location.href = `../trip/trip-details.html?tripId=${tripId}`;
    });

    const deleteButton = document.createElement('button');
    deleteButton.classList.add('button', 'delete-button');
    deleteButton.setAttribute('data-trip-id', tripId);
    deleteButton.textContent = 'Delete';

    deleteButton.addEventListener('click', function () {
        deleteVehicle(tripId);
    });

    options.appendChild(viewDetailsButton);
    options.appendChild(deleteButton);

    return options;
}

async function deleteTrip(tripId) {
    // Display a confirmation dialog before proceeding with deletion
    const isConfirmed = await showConfirmationDialog(
        'Delete Trip',
        'This action will permanently remove all associated data. Do you want to proceed?'
    );

    if (isConfirmed) {
        const token = localStorage.getItem('jwt');
        const url = `${API_URL}/trips/${tripId}`;

        try {
            const response = await fetch(url, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                console.log('Trip deleted successfully');
                showSuccessAlert('Trip deleted successfully', getTrips);
            } else {
                console.error('Failed to delete trip:', response.statusText);
            }
        } catch (error) {
            console.error('Error deleting trip:', error);
        }
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
