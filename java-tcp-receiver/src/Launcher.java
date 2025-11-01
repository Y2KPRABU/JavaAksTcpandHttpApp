import java.util.Scanner;

public class Launcher {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        if (args.length > 0) {
            String input = args[0];
            if (input.equals("1")) {
                Tcpreceiver.main(new String[]{});
            } else if (input.equals("2")) {
                FixGatewayReceiver.main(new String[]{});
            } else if (input.equals("3")) {
                System.out.println("Exiting.");
            } else {
                System.out.println("Invalid argument. Use 1 for TCP Receiver, 2 for FIX Gateway Receiver, 3 to Exit.");
            }
        } else {
            while (true) {
                System.out.println("Select mode:");
                System.out.println("1. Normal TCP Receiver");
                System.out.println("2. FIX Gateway Receiver");
                System.out.println("3. Exit");
                System.out.print("Enter choice: ");
                String input = scanner.nextLine();
                if (input.equals("1")) {
                    Tcpreceiver.main(new String[]{});
                } else if (input.equals("2")) {
                    FixGatewayReceiver.main(new String[]{});
                } else if (input.equals("3")) {
                    System.out.println("Exiting.");
                    break;
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            }
        }
        scanner.close();
    }
}
