## Description 
Server offers service for player wallet (balance).
Wallet state (balance) should be managed in memory (3rd party solution may not be used).
Balance is backed up in database (hsql or sqlite). 
When balance is not in memory, it is loaded from database and any changes are done in memory. 
Player record in database is created on demand. 

There is a periodical background process to write changes from memory to database. 
  Constraints on balance changes: 
  * Balance cannot be less than 0. 
  * If transaction exists (is duplicate), then previous response is returned. Check may take into consideration only 1000 latest transactions. 
  * If balance change is bigger than configured limit, then change is denied (explained further below). 
  * If player is in blacklist, then change is denied (explained further below). 
  
  Configuration (balance change limit and player blacklist) must be taken from external source. This can be file, database, external component etc. Client itself is a server that offers gameplay logic. Specific gameplay will not be implemented, client can just generate random balance updates. Specific communication protocol between client and server is not specified (custom protocol can be invented). Server must write proper log information, where at least IN/OUT per player must be grep’able. 
  
## Commands between servers: 
client->server: username, transaction id, balance change 
server->client: transaction id, error code, balance version, balance change, balance after change 
Database structure PLAYER(USERNAME, BALANCE_VERSION, BALANCE)

# Implementation
This application is server side of web application, which uses HTTP protocol for communication.

Used technologies:
* Java 11
* Spring Boot
* Spring Web
* Spring Data JPA
* H2 in-memory SQL DB
* Log4j
* JUnit
* Maven

To start the application run the following command from project root path:

```python
./mvnw spring-boot:run
```

When the application is up and running, you can send HTTP requests, using any HTTP client (curl, Postman, SoapUI, etc) to manage players and their balances.
  
### Player operations
to find existing or create new player
```
POST http://localhost:8080/player/get-or-create

{
"username":"l.messi"
}
```
to add player to black list
```
PUT http://localhost:8080/player/blacklist

{
"username":"l.messi"
}
```
to remove player from black list
```
DELETE http://localhost:8080/player/unblacklist

{
"username":"l.messi"
}
```
  
### Wallet operations
to change the balance
```
POST http://localhost:8080/balance/update

{
  "username": "e.clapton",
  "transactionId": 4,
  "balanceChange": 100
}
```
Note, that if player does not exist yet, this request will create player with balance equal to value of "balanceChange" variable
 
### Scheduling
The application processes transactions and players balances in memory and periodically flushes data to DB. For that purpose scheduled job is used. This job interval can be set up with **application.properties** property (ms):
```
application.config.memory-flush-interval=30000
```

### Database
For this application H2 in-memory db is used. This means that, when the application is running, you can get access to database UI dashboard and see database state. 
Link: 
```
http://localhost:8080/h2-console/login.jsp
```
```
Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:mem:mydb
User Name: sa
Password: password
``` 
These parameters are set up in **application.properties** file.

### Logging
The application writes logs to file **log4j.log**, that is located in the following directory 
 ```
 /src/main/resources/logging
```
Sample
```
2022-05-12 15:03:03 INFO  AuditService:17 - IN by player e.clapton: transactionId=4, balanceChange=100
2022-05-12 15:03:03 INFO  AuditService:28 - OUT by player e.clapton: transactionId=4, balanceVersion=2, balanceChange=100, resultingBalance=100
```

### Enviornment

Default Running on port `8080`

#### Enviornment Varibales
| Name     | Default Value  | Description   |
|--------- |----------------|---------------|
|  spring.datasource.url |jdbc:h2:mem:mydb   | H2 database hostname  |
|  spring.datasource.username|sa   | username    |
|  spring.datasource.password|password   | password    |
|  server.port| 8080  |   default port  |
|  application.config.max-balance-increase| 250000  |   maximum configurable limit  |
|  application.config.memory-flush-interval| 30000  |  scheduler interval for periodic backup  |
|  application.config.max-amount-of-latest-transactions| 1000  |  number of latest transactions to check for duplicates  |

### Dockerizing
The application can be started as docker container as Dockerfile is provided.

To build the image, run the following command from project root path:
```
docker build -t alexey.krasnopolsky/wallet-service:latest .
```
The image size is around *95.31 MB*

To run the application in docker:
```
docker run -p 8080:8080 alexey.krasnopolsky/wallet-service:latest
```
As 8080 port is exposed, you can test and send requests to a running container, using examples above, like you run the application locally 


