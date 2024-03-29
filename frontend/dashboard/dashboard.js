document.addEventListener("DOMContentLoaded", function () {
    // Check if userData is already in localStorage
    const userData = JSON.parse(localStorage.getItem("userData"));

    if (!userData) {
        // Fetch user data using API only if userData is not available
        fetchUserData();
    } else {
        // If userData is available, display it
        displayUserData(userData);
    }

     // Add an event listener to the logout button
     document.getElementById('logoutButton').addEventListener('click', function () {
        // Call the logout function from auth/logout.js
        logout();
    });
});

async function fetchUserData() {
    try {
        const jwtToken = localStorage.getItem("jwt");

        if (!jwtToken) {
            console.error("JWT token not found");
            return;
        }

        const response = await fetch(`${API_URL}/users/current/details`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${jwtToken}`,
            },
        });

        if (response.ok) {
            const userData = await response.json();
            // Save userData in localStorage for future use
            localStorage.setItem("userData", JSON.stringify(userData));
            displayUserData(userData);
        } else {
            const errorData = await response.json();
            showError(errorData.debugMessage || "Error fetching user data");
        }
    } catch (error) {
        console.error("Error fetching user data:", error);
        showError("An unexpected error occurred");
    }
}

function displayUserData(userData) {
    const userDataDiv = document.getElementById("userData");
    userDataDiv.innerHTML = `
        <h2>User Details</h2>
        <p class="user-greeting">Hello, ${userData.name}!</p>
    `;
}

function showError(errorMessage) {
    // Display error message using SweetAlert2
    Swal.fire({
        icon: 'error',
        title: 'Error',
        text: errorMessage,
    });
}
