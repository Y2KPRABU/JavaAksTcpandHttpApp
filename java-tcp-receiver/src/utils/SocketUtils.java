import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketUtils {

    public static ServerSocket createServerSocket(int port) throws IOException {
        return new ServerSocket(port);
    }

    public static Socket acceptConnection(ServerSocket serverSocket) throws IOException {
        return serverSocket.accept();
    }

    public static String readMessage(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return reader.readLine();
    }
}