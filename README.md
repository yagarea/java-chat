# java-chat
Encrypted terminal chat written in java.

## Introduction
The entire project is available for all personal or educational purposes. If this 
project helped you, mark this repository with a star. If you want to contribute, 
I'm open to your pull requests. Feel free to contact me on j.cerny.zdar@gmail.com.

---

## Project description
Project is written in [Java 1.8](https://www.java.com/) using 
[Maven](https://maven.apache.org/) as dependency management.

**Used libraries:**
- [JUnit](https://junit.org/j) - for testing
- [Google Guava](https://guava.dev/) - as hashing utility

### Security
* Communication between server and client is encrypted by RSA.
* Passwords are saved as SHA hashes.
* Password are hashed with randomized salt

---

## Usage

### Build project using maven
If you do not have maven already installed on your system you can install by:

On arch based systems:
```bash
sudo pacman -S maven
```

On debian based systems:
```bash
sudo apt install maven
```

If you are using other operating system than mentioned above visit 
[official maven install documentation](http://maven.apache.org/install.html).

When you have maven installed just run `maven package` project directory. All compiled
files will appear in `target` directory.

### Running server
You can run it from your IDE or just using the terminal:
```bash
java -jar server.jar authenticationFile port
```

Example:
```bash
java -jar target/server.jar Authentication.txt 4444
```

The `authenticationFile` can be any text file which server can use to store registered
accounts. Make sure server has rights for reading and writing to this file.

#### Server console contains these commands:
- `clients`   - print list of connected clients
- `kick`  _(nickname of the user to be kicked)_ - kick specific connected client
- `broadcast` _(message)_ - send message to all connected clients
- `help` - print list of server console commands

### Running client
You can run it from your IDE to, but in [IntelliJ IDEA](https://www.jetbrains.com/idea/),
password input is not hidden and appears as normal test input. You can of course execute 
it from the terminal:

```bash
java -jar client.jar localhost port
```

Example:

```bash
java -jar target/client.jar localhost 4077
```

##### Client contains these features:
- `:clients` - prints list of connected clients
- `@nickname` - send private message only to tagged user

