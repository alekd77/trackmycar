// Function to handle logout
function logout() {
    // Clear user data from localStorage (and perform any additional cleanup)
    localStorage.removeItem('jwt');
    localStorage.removeItem('userData');

    // Redirect to the login page
    window.location.href = '../../index.html';
}
