document.addEventListener("DOMContentLoaded", function () {
    // Add an event listener to the logout button
    document.getElementById('logoutButton').addEventListener('click', function () {
        // Call the logout function from auth/logout.js
        logout();
    });

    document.getElementById('learnMoreButton').addEventListener('click', function () {
        // Toggle the visibility of the Learn More section
        const learnMoreSection = document.getElementById('learnMoreSection');
        learnMoreSection.style.display = (learnMoreSection.style.display === 'none') ? 'block' : 'none';
    });

    initMap();
});

// Function to initialize the map
function initMap() {
    // Coordinates for the center of Poland
    const centerOfPoland = [52.0692, 19.4807];

    // Create a Leaflet map and set the initial view to the center of Poland
    const map = L.map('map').setView(centerOfPoland, 6);

    // Add OpenStreetMap tiles to the map
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);

    // Create a table for displaying vehicle information
    const vehicleTable = document.getElementById('vehicleTable');
    vehicleTable.innerHTML = '<table id="vehicleInfoTable"></table>';

    // Fetch user vehicles and display markers on the map
    fetchUserVehicles(map);
}

// Function to fetch user vehicles and display markers on the map
async function fetchUserVehicles(map) {
    // Retrieve JWT from local storage
    const jwt = localStorage.getItem('jwt');

    // Fetch user vehicles
    fetch(`${API_URL}/vehicles`, {
        headers: {
            'Authorization': `Bearer ${jwt}`
        }
    })
    .then(response => response.json())
    .then(vehicles => {
        // Display markers for each vehicle
        vehicles.forEach(vehicle => {
            fetchLastPosition(vehicle.vehicleId)
                .then(lastPosition => {
                    displayVehicleMarker(map, vehicle, lastPosition);
                })
                .catch(error => console.error('Error displaying vehicle marker:', error));
        });
    })
    .catch(error => console.error('Error fetching user vehicles:', error));
}

// Function to fetch the last position of a vehicle using its ID and JWT
async function fetchLastPosition(vehicleId) {
    const jwt = localStorage.getItem('jwt');

    return fetch(`${API_URL}/vehicles/${vehicleId}/last-position`, {
        headers: {
            'Authorization': `Bearer ${jwt}`
        }
    })
    .then(response => response.json())
    .catch(error => {
        console.error('Error fetching last position:', error);
        throw error;
    });
}

// Function to display a marker on the map for a given vehicle and its last position
async function displayVehicleMarker(map, vehicle, lastPosition) {
    // Skip handling if there's an error or if the last position is not specified yet
    if (lastPosition.status && lastPosition.status !== 200) {
        console.error(`Error fetching last position for Vehicle ${vehicle.vehicleId}: ${lastPosition.message}`);
        return;
    }

    if (!lastPosition.timestamp || !lastPosition.latitude || !lastPosition.longitude) {
        console.error(`Last position not specified yet for Vehicle ${vehicle.vehicleId}`);
        return;
    }

    // Format the timestamp to a user-friendly format
    const formattedTimestamp = formatTimestamp(lastPosition.timestamp);

    // Create an icon for the marker with appropriate color formatting
    const markerIcon = await createMarkerIcon(vehicle.vehicleId);

    // Create a marker with the custom icon
    const marker = L.marker(
        [lastPosition.latitude, lastPosition.longitude],
        { icon: markerIcon })
        .addTo(map)
        .bindPopup(
            `Vehicle: ${vehicle.name}
            <br>Timestamp: ${formattedTimestamp}
            <br>Latitude: ${lastPosition.latitude}
            <br>Longitude: ${lastPosition.longitude}`);

    // Add click event listener to the marker
    marker.on('click', function () {
        // Zoom to the marker location with a maximum zoom level
        map.setView([lastPosition.latitude, lastPosition.longitude], 16);
    });

    // Append the vehicle information to the table
    const vehicleInfoTable = document.getElementById('vehicleInfoTable');
    const newRow = vehicleInfoTable.insertRow();

    // Add labels to the first row
     if (vehicleInfoTable.rows.length === 1) {
        const headerRow = vehicleInfoTable.createTHead().insertRow();
        headerRow.innerHTML = '<th></th><th>Name</th><th>Last seen:</th><th>Latitude:</th><th>Longitude:</th>';
    }
    
    // Create an icon for the table with appropriate color formatting
    const tableIcon = await createTableIcon(vehicle.vehicleId);
    
    // Add the icon to the first cell
    const iconCell = newRow.insertCell(0);
    iconCell.appendChild(tableIcon);

    // Add the rest of the information to the table
    newRow.innerHTML += `
        <td>${vehicle.name}</td>
        <td>${formattedTimestamp}</td>
        <td>${lastPosition.latitude}</td>
        <td>${lastPosition.longitude}</td>`;

    // Add click event listener to the table row
    newRow.addEventListener('click', function () {
        // Zoom to the marker location with a maximum zoom level
        map.setView([lastPosition.latitude, lastPosition.longitude], 16);
    });
}

// Function to create a marker icon with appropriate color formatting
async function createMarkerIcon(vehicleId) {
    const markerHexColor = await getMarkerHexColor(vehicleId);

    // Create a custom div icon with a colored circle and car icon
    const icon = L.divIcon({
        className: 'custom-marker',
        html: `
            <div class="marker-circle" style="background-color: ${markerHexColor};">
                <img src="/res/images/car.svg" class="vehicle-icon" />
            </div>`,
        iconSize: [50, 50], // Adjust the size of the icon as needed
    });

    return icon;
}


// Function to create an icon for the table with appropriate color formatting
async function createTableIcon(vehicleId) {
    const markerHexColor = await getMarkerHexColor(vehicleId);

    // Create a div with a colored circle and an image
    const div = document.createElement('div');
    div.classList.add('table-marker');
    div.style.backgroundColor = markerHexColor;
    
    const vehicleIcon = document.createElement('img');
    vehicleIcon.src = '/res/images/car.svg';
    vehicleIcon.alt = 'Car Icon';
    vehicleIcon.classList.add('vehicle-icon-table');
    
    // Set a fixed size for the icon
    div.style.width = '50px';
    div.style.height = '50px';
    div.style.borderRadius = '50%';
    div.style.padding = '5px';

    // Append the image to the div
    div.appendChild(vehicleIcon);

    return div;
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
            console.log(`Fetching markerHexColor for Vehicle ${vehicleId}...`);
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

// Function to format timestamp to a user-friendly format
function formatTimestamp(timestamp) {
    const date = new Date(timestamp);
    return date.toLocaleString(); // Adjust the format as needed
}

function toggleSection(sectionId) {
    const section = document.getElementById(sectionId);
    section.style.display = (section.style.display === 'none') ? 'block' : 'none';
}
