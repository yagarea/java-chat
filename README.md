# java-chat
Encrypted terminal chat wrote in java

## Introduction
This project was created as my personal final work within the educational event LSM (lsmstepanov.cz). The entire project is available for all personal or educational purposes. If this project helped you, mark this repository with star. If you want to contribute, I'm open to your pull requests. Feel free to contact me on j.cerny.zdar@gmail.com

## Project description
#### Prefereces
Project is wrote in Java 1.8. It's using xy libraries.

#### Security
* Communication between server and client is encrypted by RSA
* Passwords are saved as SHA hashes

## Usage
### Server
you can run it from your IDE or just using by terminal:
   ```
   java -jar server.jar authenticationFile port
   ```
   Example:
   ```
   java -jar target/server.jar Authentication.txt 4077
   ```
 
### Client
You can run it from your IDE to, but in IntelliJ IDEA is password input normal text input. Of course  you can execute it from terminal:
```
java -jar client.jar localhost port
```
Example:
```
java -jar target/client.jar localhost 4077
```
## Credits
##### Jan Černý - programmer
##### Jan Žarnikov - My teacher