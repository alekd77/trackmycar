document.addEventListener("DOMContentLoaded", function () {
    const returnButton = document.getElementById('returnButton');
    returnButton.addEventListener('click', function () {
        window.location.href = "/fleet/vehicle/vehicle.html";
    });
    
    const assignTrackerButton = document.getElementById('submitButton');
    assignTrackerButton.addEventListener('click', handleAssignTracker);

    fetchExistingTrackers();
    toggleTrackerForm();
});

async function handleAssignTracker() {
    try {
        const urlParams = new URLSearchParams(window.location.search);
        const vehicleId = urlParams.get('vehicleId');
 
        const assignTrackerSelect = document.getElementById('assignTracker');
        const selectedOption = assignTrackerSelect.value;

        switch (selectedOption) {
            case 'new':
                await handleNewTrackerAssignment(vehicleId);
                break;
            case 'existing':
                await handleExistingTrackerAssignment(vehicleId);
                break;
            default:
                showErrorAlert('Please choose an assignment option.');
                break;
        }
    } catch (error) {
        console.error('An unexpected error occurred while handling the assignment:', error);
        showErrorAlert('An unexpected error occurred');
    }
}

async function handleNewTrackerAssignment(vehicleId) {
    try {
        const newTrackerResponse = await addNewTracker();

        if (newTrackerResponse?.ok) {
            const newTrackerResponseData = await newTrackerResponse.json();
            const newTrackerId = newTrackerResponseData.trackerId;
            const assignmentResponse = await addAssignment(vehicleId, newTrackerId);

            if (assignmentResponse?.ok) {
                console.log('Assignment successful!');
                showSuccessAlert(`Tracker has been assigned successfully`, () => {
                    window.location.href = "/fleet/vehicle/vehicle.html";
                });
            } else if (assignmentResponse.error) {
                console.error('Error assigning new tracker:', assignmentResponse.error);
                showErrorAlert(assignmentResponse.error);
            } else {
                console.error('An unexpected error occurred while adding a new tracker assignment.');
                showErrorAlert('An unexpected error occurred while adding a new tracker assignment.');
            }
            
        } else if (newTrackerResponse.error) {
            console.error('Error adding new tracker:', newTrackerResponse.error);
            showErrorAlert(newTrackerResponse.error);
            
        } else {
            console.log('An unexpected error occurred while adding a new tracker.');
            showErrorAlert('An unexpected error occurred while adding a new tracker.');
        }

    } catch (error) {
        console.error('An unexpected error occurred while handling a new tracker assignment:', error);
        showErrorAlert('An unexpected error occurred');
    }
}

async function handleExistingTrackerAssignment(vehicleId) {
    try {
        const existingTrackerSelect = document.getElementById('existingTracker');
        const selectedTrackerId = existingTrackerSelect.value;

        if (selectedTrackerId) {
            const assignmentResponse = await addAssignment(vehicleId, selectedTrackerId);

            if (assignmentResponse?.ok) {
                console.log('Assignment successful!');
                showSuccessAlert(`Tracker has been assigned successfully`, () => {
                    window.location.href = "/fleet/vehicle/vehicle.html";
                });
            } else if (assignmentResponse.error) {
                console.error(`Failed to assign existing tracker due to: ${assignmentResponse.error}`);
                showErrorAlert(assignmentResponse.error);
            }
        } else {
            showErrorAlert('Please select an existing tracker.');
        }
    } catch (error) {
        console.error('An unexpected error occurred while handling an existing tracker assignment:', error);
        showErrorAlert('An unexpected error occurred');
    }
}

async function addNewTracker() {
    const token = localStorage.getItem('jwt');
    const trackerName = document.getElementById('trackerName').value;
    const trackerDescription = document.getElementById('trackerDescription').value || '';
    const trackerImei = document.getElementById('trackerImei').value;

    const newTrackerData = {
        name: trackerName,
        description: trackerDescription,
        imei: trackerImei,
    };

    try {
        const trackerResponse = await fetch(`${API_URL}/trackers`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(newTrackerData),
        });

        if (trackerResponse?.ok) {
            console.log('Successfully added new tracker');
            return trackerResponse;
        } else {
            const trackerErrorMessage = (await trackerResponse.json()).debugMessage || 'Unknown error';
            return { error: trackerErrorMessage };
        }
    } catch (error) {
        return { error: 'An unexpected error occurred while adding a new tracker.' };
    }
}

async function addAssignment(vehicleId, trackerId) {
    const token = localStorage.getItem('jwt');
    const assignmentUrl = `${API_URL}/assignments`;
    const assignmentBody = {
        vehicleId: vehicleId,
        trackerId: trackerId,
    };

    try {
        const assignmentResponse = await fetch(assignmentUrl, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(assignmentBody),
        });

        if (assignmentResponse?.ok) {
            console.log('Successfully added assignment');
            return assignmentResponse;
        } else {
            const assignmentErrorMessage = (await assignmentResponse.json())?.debugMessage || 'Unknown error';
            return { error: assignmentErrorMessage };
        }
    } catch (error) {
        return { error: 'An unexpected error occurred while adding an assignment.' };
    }
}

async function fetchExistingTrackers() {
    const existingTrackerSelect = document.getElementById('existingTracker');

    try {
        const token = localStorage.getItem('jwt');
        const response = await fetch(`${API_URL}/trackers`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const trackers = await response.json();
            populateExistingTrackersSelect(existingTrackerSelect, trackers);
        } else {
            console.error('Error fetching existing trackers:', response.statusText);
        }
    } catch (error) {
        console.error('An unexpected error occurred while fetching trackers:', error);
    }
}

function populateExistingTrackersSelect(selectElement, trackers) {
    selectElement.innerHTML = '';
    const defaultOption = document.createElement('option');
    defaultOption.value = '';
    defaultOption.text = 'Select Existing Tracker';
    selectElement.appendChild(defaultOption);

    trackers.forEach(tracker => {
        const option = document.createElement('option');
        option.value = tracker.trackerId;
        option.text = tracker.name;
        selectElement.appendChild(option);
    });
}

function toggleTrackerForm() {
    var assignTrackerSelect = document.getElementById("assignTracker");
    var newTrackerForm = document.getElementById("newTrackerForm");
    var existingTrackerSelection = document.getElementById("existingTrackerSelection");

    if (assignTrackerSelect.value === "new") {
        newTrackerForm.style.display = "block";
        existingTrackerSelection.style.display = "none";
    } else if (assignTrackerSelect.value === "existing") {
        newTrackerForm.style.display = "none";
        existingTrackerSelection.style.display = "block";
    } else {
        newTrackerForm.style.display = "none";
        existingTrackerSelection.style.display = "none";
    }
}

async function showSuccessAlert(message) {
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

async function showErrorAlert(errorMessage) {
    Swal.fire({
        icon: 'error',
        title: 'Error',
        text: errorMessage,
    });
}
