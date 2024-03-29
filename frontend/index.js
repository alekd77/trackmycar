// Event listener for the window's "load" event
window.addEventListener('load', function () {
    // Check if localStorage is supported by the browser
    if (typeof Storage !== 'undefined') {
        // Clear the local storage
        localStorage.clear();
        console.log('Local storage cleared!');
    } else {
        // If localStorage is not supported, log an error message
        console.error('Local storage is not supported by this browser.');
    }
});