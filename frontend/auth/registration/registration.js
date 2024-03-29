// Add an event listener to the form submission
document.getElementById('registrationForm').addEventListener('submit', function (event) {
    event.preventDefault();

    // Call the submitForm function
    submitForm();
});

// Define the submitForm function
async function submitForm() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var email = document.getElementById("email").value;
    var name = document.getElementById("name").value;

    var data = {
        username: username,
        password: password,
        email: email,
        name: name
    };

    try {
        var response = await fetch(`${API_URL}/auth/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            console.log("Registration successful");

            // Display success message using SweetAlert2
            Swal.fire({
                icon: 'success',
                title: 'Registration successful',
                text: 'You can now log in with your credentials.',
            }).then(() => {
                // Redirect to the login page
                window.location.href = "../login/login.html";
            });
        } else {
            var errorData = await response.json();

            // Display error message using SweetAlert2
            Swal.fire({
                icon: 'error',
                title: 'Registration failed',
                text: errorData.debugMessage || 'An error occurred during registration. Please try again.',
            });
        }
    } catch (error) {
        console.error("Error during API call:", error);

        // Display generic error message using SweetAlert2
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'An error occurred during the registration process. Please try again.',
        });
    }
}
