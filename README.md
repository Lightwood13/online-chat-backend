## Online chat

A simple online chat with friends and groups.
Frontend and backend are done separately in 
https://github.com/Lightwood13/online-chat-frontend,
https://github.com/Lightwood13/online-chat-backend.
Frontend is currently deployed to Netlify and backend to AWS.

You can check it out at https://onlinechat.party.

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

### Deployment

Frontend is deployed as a static page to Netlify.

Backend is deployed using AWS. Database is deployed to the RDS and
application is packaged as Docker image and deployed to ECS container running on EC2.

Application uses HTTPS with a free certificate issued by Let's Encrypt.