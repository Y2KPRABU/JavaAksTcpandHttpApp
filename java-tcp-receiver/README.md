# Java TCP Receiver

This project implements a simple TCP receiver application in Java that listens for incoming messages over a TCP socket connection.

## Project Structure

```
java-tcp-receiver
├── src
│   └── Main.java          # Entry point of the application
├── build.gradle           # Gradle build configuration
└── README.md              # Project documentation
```

## Requirements

- Java Development Kit (JDK) 11 or higher (configured for Java 24 in build.gradle)
- Gradle 6.0 or higher

## Setup

1. Clone the repository:
   ```
   git clone <repository-url>
   cd java-tcp-receiver
   ```

2. Build the project using Gradle:
   ```
   ./gradlew build
   ```
   On Windows, use:
   ```
   .\gradlew build
   ```

## Running the Application

To run the TCP receiver application, execute the following command from the project root:

```
java -jar build/libs/java-tcp-receiver-1.0.jar
```

The application will start a TCP server that listens for incoming connections on the specified port (default is 12345).

**To stop the server:**  
Press `Q` and then Enter in the console where the server is running.

## Usage

Once the server is running, you can connect to it using a TCP client and send messages. The server will print any received messages to the console.

## License

This project is licensed under the MIT License. See the LICENSE file for more details.