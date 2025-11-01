import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class TcpSender {

    public static void main(String[] args) {
        String envHost = System.getenv("RECEIVER_HOST");
        String host;
        if (envHost != null && !envHost.isEmpty()) {
            System.out.println("Connecting to Env Variable at " + envHost);
            host = envHost;
        } else if (args.length > 0) {
            host = args[0];
        } else {
            host = "java8tcpreceiver"; // Default host , use this for docker, since our docker container name is this 
        }
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 12345;

        System.out.println("Connecting to server at " + host + ":" + port);

        try (Socket socket = new Socket(host, port);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Success - Connected to server at " + host + ":" + port);
            String LogonMessage = "Logon";
            writer.write(LogonMessage);
            writer.newLine();
            writer.flush();
            System.out.println("Success - Logged in to server at " + host + ":" + port);

            // Enter username
            System.out.print("Enter username: ");
            String username = scanner.nextLine().trim();
            if (username.isEmpty()) {
                username = "Sender A";
            }
            writer.write(username);
            writer.newLine();
            writer.flush();
            System.out.println("Username sent: " + username);

            while (true) {
                System.out.print("Enter message (or 'q' to quit): ");
                String message = scanner.nextLine().trim();
                if (message.equalsIgnoreCase("q")) {
                    String LogoOutMessage = "Logoff";
                    writer.write(LogoOutMessage);
                    writer.newLine();
                    System.out.println("Exiting sender.");
                    break;
                }
                if (message.isEmpty()) {
                    System.out.println("Empty message. Try again.");
                    continue;
                }
                writer.write(message);
                writer.newLine();
                writer.flush();
                System.out.println("Message sent successfully.");
            }
        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}
