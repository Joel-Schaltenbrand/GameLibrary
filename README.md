# Game Library

[![GitHub](https://img.shields.io/github/license/Joel-Schaltenbrand/GameLibrary?style=for-the-badge)](https://github.com/Joel-Schaltenbrand/GameLibrary/blob/master/LICENSE)
[![GitHub tag](https://img.shields.io/github/v/tag/Joel-Schaltenbrand/GameLibrary?style=for-the-badge)](https://github.com/Joel-Schaltenbrand/GameLibrary/tags)
[![Docker Image](https://img.shields.io/badge/Docker%20image-ghcr.io%2Fjoel--schaltenbrand%2Fgamelibrary-green/gamelibrary?style=for-the-badge&link=https://ghcr.io/joel-schaltenbrand/gamelibrary)](https://ghcr.io/joel-schaltenbrand/gamelibrary)

A simple game library featuring different games to play in Springboot Web.

### Releases

Check out the latest releases at [GitHub Releases](https://github.com/joel-schaltenbrand/GameLibrary/releases).

### Features

* Play the classic game Hangman.
* Play the classic game Simon.
* Play the classic game Tic-Tac-Toe.
* More games to be added soon!

---

## Built With

### Languages

- [HTML](https://developer.mozilla.org/en-US/docs/Web/HTML)
- [CSS](https://developer.mozilla.org/en-US/docs/Web/CSS)
- [JavaScript](https://www.javascript.com/)
- [Java](https://www.java.com/en/)

### Frameworks, Libraries, and Tools

#### Frameworks

- [Bootstrap](https://getbootstrap.com/)
- [Spring Boot](https://spring.io/projects/spring-boot)

#### JavaScript-Libraries

- [jQuery](https://jquery.com/)
- [Stomp.js](https://github.com/stomp-js/stompjs)
- [SockJS](https://github.com/sockjs/sockjs-client)

#### Tools

- [Font Awesome](https://fontawesome.com/) - Icon toolkit
- [Maven](https://maven.apache.org/) - Dependency management

---

## Getting Started

### Use as Docker Container

1. (Optional) Pull the image: `docker pull ghcr.io/joel-schaltenbrand/gamelibrary:latest`
2. Run the container: `docker run -d -p 8080:8080 ghcr.io/joel-schaltenbrand/gamelibrary:latest` (Or your custom port on
   the left)
3. Open a web browser and go to `http://localhost:8080` (Or your custom port) to access the Game Library.
4. Select the game you want to play by clicking on its corresponding button.
5. Stop the container: `docker stop <container-id>`
6. Remove the container: `docker rm <container-id>`
7. Remove the image: `docker rmi ghcr.io/joel-schaltenbrand/gamelibrary:latest`

### Use as Maven Project

To run the application, you will need to have Java (20) and Maven installed on your machine.

1. Clone or download the project files to your local machine.
2. If needed, update the `server.port` property in the `application.properties` file to your desired port.
3. Navigate to the project directory using a command prompt or terminal.
4. Start a local web server by running the following command: `mvn spring-boot:run`
5. Open a web browser and go to `http://localhost:8080` (or your custom port) to access the Game Library.
6. Select the game you want to play by clicking on its corresponding button.

---

## Author

### [Joel Schaltenbrand](https://github.com/Joel-Schaltenbrand)

* [LinkedIn](https://www.linkedin.com/in/joel-schaltenbrand/)
* [Website](https://joelschaltenbrand.ch/)

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
