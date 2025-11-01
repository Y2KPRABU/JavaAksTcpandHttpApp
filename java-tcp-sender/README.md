# Java TCP Sender

This project implements a simple TCP sender application in Java that connects to a TCP server and sends messages from the console.

## Project Structure

```
java-tcp-sender
├── src
│   └── Main.java          # Entry point of the application
├── build.gradle           # Gradle build configuration
└── README.md              # Project documentation
```

## Requirements

- Java Development Kit (JDK) 11 or higher
- Gradle 6.0 or higher

## Setup

1. Clone the repository or copy the folder:
   ```
   cd java-tcp-sender
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

To run the TCP sender application, execute the following command from the project root:

```
java -jar build/libs/java-tcp-sender-1.0.jar
```

You will be prompted to enter the server host and port. Then, type messages to send to the server.  
Type `Q` and press Enter to quit.

## License

This project is licensed under the MIT License.
