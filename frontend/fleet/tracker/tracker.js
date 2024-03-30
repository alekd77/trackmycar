document.addEventListener("DOMContentLoaded", function () {
    const addButton = document.querySelector('.add-button');
    addButton.addEventListener('click', function () {
        window.location.href = '../../fleet/tracker/add-tracker.html';
    });

    getTrackers();
});

// Get list of trackers
async function getTrackers() {
    const token = localStorage.getItem('jwt');
    const url = `${API_URL}/trackers`;

    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const trackers = await response.json();
            console.log('Trackers:', trackers);

            displayTrackers(trackers);
        } else {
            console.error('Failed to fetch trackers:', response.statusText);
        }
    } catch (error) {
        console.error('Error fetching trackers:', error);
    }
}

// Display trackers in a table
async function displayTrackers(trackers) {
    const tbody = document.querySelector('#trackerTable tbody');
    tbody.innerHTML = '';

    for (const tracker of trackers) {
        const row = await createTrackerRow(tracker);
        tbody.appendChild(row);
    }

    document.addEventListener('click', hideAllMenuOptions);
}

async function createTrackerRow(tracker) {
    const row = document.createElement('tr');
    row.innerHTML = `
    <td>${tracker.trackerId}</td>
    <td></td>
    <td>${tracker.name}</td>
    <td>${tracker.description}</td>
    <td>${tracker.imei}</td>
    <td></td>
    `;

    const trackerIcon = document.createElement('img');
    trackerIcon.src = '../../res/images/tracker.svg'
    trackerIcon.alt = 'Tracker Icon';
    trackerIcon.classList.add('tracker-icon');
    row.children[1].appendChild(trackerIcon);

    const menuButton = createMenuButton(tracker.trackerId);
    const menuOptions = await createMenuOptions(tracker.trackerId, row);

    menuButton.addEventListener('click', function (event) {
        event.stopPropagation();
        menuOptions.classList.toggle('visible');
    });

    row.children[5].appendChild(menuButton);
    row.children[5].appendChild(menuOptions);

    return row;
}

function createMenuButton(trackerId) {
    const button = document.createElement('button');
    button.classList.add('button', 'menu-button');
    button.setAttribute('data-tracker-id', trackerId);
    button.textContent = '...';
    return button;
}

async function createMenuOptions(trackerId, row) {
    const options = document.createElement('div');
    options.classList.add('menu-options', 'hidden');

    const editButton = document.createElement('button');
    editButton.classList.add('button', 'edit-button');
    editButton.setAttribute('data-tracker-id', trackerId);
    editButton.textContent = 'Edit';

    editButton.addEventListener('click', async function () {
        window.location.href = `../../fleet/tracker/edit-tracker.html?trackerId=${trackerId}`;
    });

    const deleteButton = document.createElement('button');
    deleteButton.classList.add('button', 'delete-button');
    deleteButton.setAttribute('data-tracker-id', trackerId);
    deleteButton.textContent = 'Delete';

    deleteButton.addEventListener('click', async function () {
        const trackerId = deleteButton.getAttribute('data-tracker-id');
        deleteTracker(trackerId);
    });

    options.appendChild(editButton);
    options.appendChild(deleteButton);

    return options;
}

async function deleteTracker(trackerId) {
    // Display a confirmation dialog before proceeding with deletion
    const isConfirmed = await showConfirmationDialog(
        'Delete Tracker',
        'This action will permanently remove all associated data (including trips, assignments, statistics, and any other related information). Do you want to proceed?'
    );

    if (isConfirmed) {
        const token = localStorage.getItem('jwt');
        const url = `${API_URL}/trackers/${trackerId}`;

        try {
            const response = await fetch(url, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                console.log('Tracker deleted successfully');
                showSuccessAlert('Tracker deleted successfully', getTrackers);
            } else {
                console.error('Failed to delete tracker:', response.statusText);
            }
        } catch (error) {
            console.error('Error deleting tracker:', error);
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
