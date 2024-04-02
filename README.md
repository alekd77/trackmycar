# **TrackMyCar!**

**TrackMyCar!** is a vehicle tracking system that enables users to access real-time location data for their car, along with the ability to explore historical route information.

The system consists of 3 components:

* **Web Server:**
  * Developed using Java and the Spring Boot framework for efficient server-side operations.
  * Utilizes MySQL DB for data storage, Spring Data JPA/Hibernate for database interactions and Spring Security for user authentication and authorization.

* **Web Application (UI):**
  * Built with HTML, CSS, and JavaScript to deliver a user-friendly and intuitive interface.
  * Leverages Leaflet (https://leafletjs.com/) for interactive map visualizations and OpenStreetMap (https://www.openstreetmap.org/) for detailed map data.

* **Mobile Application (Future Development):**
  * Planned for future implementation, this mobile app will utilize GPS technology to collect vehicle location data and seamlessly transmit it to the server for centralized management and analysis.

## Deployment:
* The server and MySQL database are currently deployed on a private Railway network (https://railway.app/).
* The web application frontend is hosted on GitHub Pages and can be accessed at: https://alekd77.github.io/trackmycar/

## License:
This project is distributed under the terms of the MIT License.

#### Note: This project is for educational purposes only.
