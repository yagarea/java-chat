# java-chat
Encrypted terminal chat written in java.

## Introduction
This project was created as my personal final work within the educational event LSM (lsmstepanov.cz). The entire project is available for all personal or educational purposes. If this project helped you, mark this repository with a star. If you want to contribute, I'm open to your pull requests. Feel free to contact me on j.cerny.zdar@gmail.com.

## Project description
#### Prefereces
Project is written in Java 1.8. It's using xy libraries.
Project is using Maven as dependency management.

#### Security
* Communication between server and client is encrypted by RSA.
* Passwords are saved as SHA hashes.
* Password are hashed with randomised salt

## Usage
### Server
You can run it from your IDE or just using the terminal:
   ```
   java -jar server.jar authenticationFile port
   ```
   Example:
   ```
   java -jar target/server.jar Authentication.txt 4077
   ```
##### Server console contains these commands:
* clients   - print list of connected clients
* kick  (nickname of the user to be kicked) - kick specific connected client
* broadcast (message) - send message to all connected clients
* help - print list of server console commands
### Client
You can run it from your IDE to, but in IntelliJ IDEA, password input is the normal text input. You can of course execute it from the terminal:
```
java -jar client.jar localhost port
```
Example:
```
java -jar target/client.jar localhost 4077
```

##### Client contains these features:
* :clients - prints list of connected clients
* @nickname - send private message only to tagged user

## Credits
##### Jan Černý - programmer