// main.js - Core Application Logic, Map Integration, and API Calls

const API_BASE_URL = '/api'; // Relative path to Spring Boot Controller
let map;                     // Leaflet map instance
let disasterMarkers = L.layerGroup(); // Layer to hold all disaster markers
let shelterMarkers = L.layerGroup();  // Layer to hold all shelter markers


document.addEventListener('DOMContentLoaded', () => {
    console.log("Main application script loaded. Initializing dashboard...");

    // 1. Initialize Leaflet Map (Centered near the example seed data)
    initializeMap();

    // 2. Load Existing Data (Shelters and Active Alerts)
    loadInitialData();

    // 3. Establish Real-Time Connection
    establishSSEConnection();
});


// --- Initialization Functions ---

function initializeMap() {
    // Set view to a central location (e.g., 19.07, 72.87) with zoom level 12
    map = L.map('map').setView([19.0700, 72.8700], 12);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors'
    }).addTo(map);

    // Add layer groups to the map
    disasterMarkers.addTo(map);
    shelterMarkers.addTo(map);
}

// Function loadInitialData (Modified)

function loadInitialData() {
    console.log("Fetching initial data...");

    // Fetch all shelters (GET /api/shelters)
    fetch(`${API_BASE_URL}/shelters`)
        .then(response => response.json())
        .then(shelters => {
            shelters.forEach(s => addShelterMarker(s));

            // ✅ INTEGRATION POINT: Zoom map to fit the loaded shelters
            zoomMapToShelters();
        })
        .catch(error => console.error("Error loading shelters:", error));

    // Fetch all active alerts (GET /api/alerts/active)
    fetch(`${API_BASE_URL}/alerts/active`)
        .then(response => response.json())
        .then(alerts => {
             alerts.forEach(a => addDisasterMarker(a));
        });
}

function establishSSEConnection() {
    const eventSource = new EventSource(`${API_BASE_URL}/stream/events`);

    // Listen for the 'live-alert' event carrying the DisasterAlert DTO
    eventSource.addEventListener('live-alert', function(event) {
        try {
            const alertDTO = JSON.parse(event.data);
            handleLiveAlert(alertDTO); // Core function call
        } catch (e) {
            console.error("Failed to parse SSE data:", e);
        }
    });

    eventSource.onerror = function(err) {
        console.error("SSE Connection Error. Attempting to check backend status...");
    };

    console.log("SSE Connection established.");
}


// --- Real-Time Update Logic (Fulfills Project Goal) ---

function handleLiveAlert(alertDTO) {
    const event = alertDTO.event;
    const shelter = alertDTO.assignedShelter;
    const distance = alertDTO.distanceKm;

    console.log(`NEW LIVE ALERT: ${event.type}. Assigned Shelter: ${shelter ? shelter.name : 'None'} (${distance.toFixed(2)} km).`);

    // 1. Update the Alerts Table
    updateAlertsTable(event, shelter, distance);

    // 2. Update the Map Visualization
    addDisasterMarker(event);
    drawRadiusCircle(event.latitude, event.longitude);

    // 3. Highlight the assigned shelter
    if (shelter) {
        highlightShelterMarker(shelter.id);
        // Optional: pan map to the new event
        map.panTo([event.latitude, event.longitude]);
    }
}


// --- Map Helper Functions ---

function addShelterMarker(shelter) {
    // Red icon for full, Blue for available
    const iconColor = (shelter.occupancy >= shelter.capacity) ? 'red' : 'blue';
    const shelterIcon = new L.Icon({
        iconUrl: `https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-${iconColor}.png`,
        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41]
    });

    const marker = L.marker([shelter.latitude, shelter.longitude], {icon: shelterIcon, shelterId: shelter.id})
        .bindPopup(`<b>${shelter.name}</b><br>Capacity: ${shelter.occupancy}/${shelter.capacity}`)
        .addTo(shelterMarkers);
}

function highlightShelterMarker(shelterId) {
    shelterMarkers.eachLayer(function(marker) {
        // Find the marker by its stored ID and change its color to green
        if (marker.options.shelterId === shelterId) {
            const assignedIcon = new L.Icon({
                 iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',
                 shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
                 iconSize: [25, 41],
                 iconAnchor: [12, 41],
                 popupAnchor: [1, -34],
                 shadowSize: [41, 41]
            });
            marker.setIcon(assignedIcon);
            marker.bindPopup(`<b>ASSIGNED: ${marker.getPopup().getContent()}</b>`).openPopup();
        }
    });
}

function addDisasterMarker(event) {
     const disasterIcon = new L.Icon({
        iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41]
    });

    L.marker([event.latitude, event.longitude], {icon: disasterIcon})
        .bindPopup(`<b>ALERT: ${event.type}</b><br>Severity: ${event.severity}`)
        .addTo(disasterMarkers);
}

function drawRadiusCircle(lat, lon) {
    // ALERT_RADIUS_KM is 5.0 km (5000 meters)
    L.circle([lat, lon], {
        color: 'red',
        fillColor: '#f03',
        fillOpacity: 0.1,
        radius: 5000
    }).addTo(map);
}
/**
 * Adjusts the map view to fit all currently visible shelter markers.
 * This is crucial for initial load to show where the data is located.
 */
function zoomMapToShelters() {
    // Check if the LayerGroup has markers before attempting to get bounds
    if (shelterMarkers.getLayers().length > 0) {
        try {
            const bounds = shelterMarkers.getBounds();
            map.fitBounds(bounds, {
                padding: [50, 50] // Add some padding around the markers
            });
        } catch (e) {
            console.warn("Could not calculate bounds for map zoom. Map initialized to default view.", e);
        }
    }
}

// --- Table Helper Function ---

function updateAlertsTable(event, shelter, distance) {
    const tableBody = document.getElementById('alerts-table-body');
    if (!tableBody) {
        console.error("Alerts table body not found (ID: alerts-table-body)");
        return;
    }

    const row = tableBody.insertRow(0); // Insert at the top

    row.innerHTML = `
        <td>${event.type} (${event.severity})</td>
        <td>${event.latitude.toFixed(4)}, ${event.longitude.toFixed(4)}</td>
        <td><b>${shelter ? shelter.name : 'N/A'}</b></td>
        <td>${shelter ? distance.toFixed(2) + ' km' : 'N/A'}</td>
        <td>${new Date(event.reportedAt).toLocaleTimeString()}</td>
    `;
}