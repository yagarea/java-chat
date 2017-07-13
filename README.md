# java-chat
Simple encrypted chat in Java

## Introduction
Tento projekt vznikl jako moje osobní závěrečná práce v rámci edukační události lsm (lsmstepanov.cz).
Celý projekt je k dispozici každému osobním nebo edukačním účelům. Pokud vám tento projekt pomohl, prosím označte ho hvězdičkou.
Pokud do něho chcete přispět, jsem otevřený vašim pull requestům.

## Project decription
####Prefereces
Project is wrote in Java 1.8. It's using xy libraries.

####Security
* communication between server and client is encrypted by RSA
* Passwords are saved as SHA hashes

##Usage
### server
you can run it from your IDE or just using by terminal:
   ```
   java -jar server.jar port
   ```
   Example:
   ```
   java -jar target/server.jar 4077
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
Jan Černý - programmer
Jan