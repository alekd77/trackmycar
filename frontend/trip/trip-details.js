document.addEventListener("DOMContentLoaded", function () {
    // Add an event listener to the logout button
    document.getElementById('logoutButton').addEventListener('click', function () {
        // Call the logout function from auth/logout.js
        logout();
    });

    initMap();
});

function initMap() {
    // Fetch the tripId from the URL
    const urlParams = new URLSearchParams(window.location.search);
    const tripId = urlParams.get('tripId');

    // Fetch trip details and geolocations data from the API
    Promise.all([fetchTripDetails(tripId), fetchGeolocations(tripId)])
        .then(([tripDetails, geolocations]) => {
            // Check if tripDetails is defined and has the 'name' property
            if (tripDetails && tripDetails.name) {
                // Set trip details in HTML
                document.getElementById('trip-name').innerText = tripDetails.name;

                // Format timestamps
                const startTimestamp = new Date(tripDetails.startTimestamp).toLocaleString();
                const endTimestamp = tripDetails.endTimestamp ? new Date(tripDetails.endTimestamp).toLocaleString() : '-';

                document.getElementById('start-timestamp').innerText = startTimestamp;
                document.getElementById('end-timestamp').innerText = endTimestamp;
                document.getElementById('total-distance').innerText = tripDetails.totalDistance.toFixed(2);
                document.getElementById('max-speed').innerText = tripDetails.maxSpeed.toFixed(2);

                // Get the start location
                const startLocation = geolocations[0];

                // Create a Leaflet map and set the initial view to the start location
                const map = L.map('map').setView([startLocation.latitude, startLocation.longitude], 13);

                // Add OpenStreetMap tiles to the map
                L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    maxZoom: 19,
                    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
                }).addTo(map);

                // Add markers to the map
                addMarkers(map, geolocations);

                // Connect markers with lines
                connectMarkersWithLines(map, geolocations);

                // Mark the beginning and end of the trip
                markTripStartEnd(map, geolocations);
            } else {
                console.error('Error: Missing or invalid trip details in the response.');
            }
        })
        .catch(error => console.error('Error fetching trip details or geolocations:', error));
}


async function fetchTripDetails(tripId) {
    return fetch(`${API_URL}/trips/${tripId}`, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('jwt'),
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log('Trip details response:', data);
        return data;  // Return the entire data object
    })
    .catch(error => {
        console.error('Error fetching trip details:', error);
        throw error;
    });
}



async function fetchGeolocations(tripId) {
    return fetch(`${API_URL}/trips/${tripId}/geolocations`, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('jwt'),
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => data.geolocations)
    .catch(error => {
        throw error;
    });
}

function addMarkers(map, geolocations) {
    geolocations.forEach((geo, index) => {
        const markerColor = index === 0 ? 'green' : (index === geolocations.length - 1 ? 'red' : 'grey');

        const marker = L.marker([geo.latitude, geo.longitude], { icon: getMarkerIcon(markerColor) })
            .bindPopup(`Speed: ${geo.speed}<br>Timestamp: ${geo.timestamp}`)
            .addTo(map);
    });
}

function connectMarkersWithLines(map, geolocations) {
    const latLngs = geolocations.map(geo => L.latLng(geo.latitude, geo.longitude));
    const polyline = L.polyline(latLngs, { color: 'grey' }).addTo(map);
}

function markTripStartEnd(map, geolocations) {
    const startLocation = geolocations[0];
    const endLocation = geolocations[geolocations.length - 1];

    L.marker([startLocation.latitude, startLocation.longitude], { icon: getMarkerIcon('green') })
        .bindPopup(`Start of Trip<br>Timestamp: ${startLocation.timestamp}`)
        .addTo(map);

    L.marker([endLocation.latitude, endLocation.longitude], { icon: getMarkerIcon('red') })
        .bindPopup(`End of Trip<br>Timestamp: ${endLocation.timestamp}`)
        .addTo(map);
}

function getMarkerIcon(color) {
    return L.divIcon({
        className: 'custom-marker',
        iconSize: [20, 20],
        iconAnchor: [10, 10],
        popupAnchor: [0, -10],
        html: `<div style="background-color: ${color}; width: 20px; height: 20px; border-radius: 50%;"></div>`
    });
}
