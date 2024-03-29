// Add an event listener to the form submission
document.getElementById('loginForm').addEventListener('submit', function (event) {
    event.preventDefault();

    // Call the submitForm function
    submitForm();
});

// Define the submitForm function for login
async function submitForm() {
    // Get user input
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const data = {
        username: username,
        password: password,
    };

    try {
        // Make a POST request to the login API endpoint
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });

        if (response.ok) {
            console.log('Login successful');

            // Assuming the server responds with a JWT
            const jwt = (await response.json()).jwt;

            // Store the JWT token in localStorage for future requests
            localStorage.setItem('jwt', jwt);

            // Redirect to the dashboard or any other authorized page
            window.location.href = '/dashboard/dashboard.html';
            
        } else {
            const errorData = await response.json();

            // Display error message using SweetAlert2
            Swal.fire({
                icon: 'error',
                title: 'Login failed',
                text: errorData.debugMessage || 'An error occurred during login. Please try again.',
            });
        }
    } catch (error) {
        console.error('Login error:', error);

        // Display generic error message using SweetAlert2
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'An error occurred during the login process. Please try again.',
        });
    }
}

