## Online chat

A simple online chat with friends and groups.
Frontend and backend are done separately in 
https://github.com/Lightwood13/online-chat-frontend,
https://github.com/Lightwood13/online-chat-backend.

### Features

- Realtime messaging
![screenshot-1](/screenshots/1.png)
- Friend system
![screenshot-1](/screenshots/2.png)
- Group creation
![screenshot-1](/screenshots/3.png)
- Group moderation
![screenshot-1](/screenshots/4.png)

### Backend

Backend is a RESTful API written in Java using Spring Boot, MVC, Data and Security.
Data is stored in a Postgres database.
STOMP over Websockets is used to send realtime notifications to clients.

### Frontend

Frontend is written using React.js.
