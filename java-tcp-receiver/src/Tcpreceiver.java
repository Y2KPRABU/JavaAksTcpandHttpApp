import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class Tcpreceiver {
    private static PrintWriter logWriter;

    public static void main(String[] args) {
        log("ENTER main()");
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 12345;
        try {
            logWriter = new PrintWriter(new BufferedWriter(new FileWriter("receiverlog.log", true)), true);
        } catch (IOException e) {
            System.err.println("Could not open log file: " + e.getMessage());
            logWriter = null;
        }
        log("Server is listening on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName("0.0.0.0"))) {
            while (true) {
                long startWait = System.currentTimeMillis();
                while (true) {
                    if (serverSocket.isClosed()) break;
                    if (serverSocket.getSoTimeout() > 0) {
                        // Already has a timeout
                        break;
                    }
                    if (serverSocket.getLocalSocketAddress() != null) {
                        break;
                    }
                    if (System.currentTimeMillis() - startWait > 30000) {
                        log("Receiver has been waiting for a client connection for more than 30 seconds.");
                        startWait = System.currentTimeMillis(); // reset timer after logging
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // Ignore
                    }
                }
                Socket socket = serverSocket.accept();
                log("New client connected: " + socket.getRemoteSocketAddress());
                // Handle each client in a new thread
                new Thread(() -> handleClient(socket)).start();
            }
        } catch (IOException e) {
            log("Server error: " + e.getMessage());
            e.printStackTrace();
        }
        log("EXIT main()");
    }

    private static void handleClient(Socket socket) {
        log("ENTER handleClient() for " + socket.getRemoteSocketAddress());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())))
         {  String LogonMessage = reader.readLine();
            String username = reader.readLine();
            if (username == null || username.trim().isEmpty()) {
                username = "Unknown";
            }
            log(LogonMessage + " User '" + username + "' connected from " + socket.getRemoteSocketAddress());
            String message;
            while ((message = reader.readLine()) != null) {
                log("[" + username + "] " + message);
            }
        } catch (IOException e) {
            log("Error handling client: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                log("Error closing socket: " + e.getMessage());
            }
        }
        log("EXIT handleClient() for " + socket.getRemoteSocketAddress());
    }

    private static void log(String msg) {
        String logMsg = LocalDateTime.now() + " - " + msg;
        System.out.println(logMsg);
        if (logWriter != null) {
            logWriter.println(logMsg);
        }
    }
}
// This code implements a simple TCP server that listens for incoming connections on a specified port.
// It accepts client connections and reads messages sent by the clients, printing them to the console.
// The server runs indefinitely, handling each client connection in a separate method.
// Make sure to handle exceptions properly to avoid server crashes.
// To test this server, you can use a TCP client that connects to the same port and sends messages.
// For example, you can use telnet or write a simple TCP client in Java or another language.
// Remember to close the socket and streams to free up resources after use.
// This server can be extended to handle multiple clients concurrently using threads or executors if needed.