import java.util.Scanner;

public class Launcher {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String input = null;
        boolean useArg = args != null && args.length > 0;
        String firstInput = null;
        if (useArg) {
            firstInput = args[0];
            System.out.println("Received Args Input 0 is : " + firstInput);
        }
        while (true) {
            System.out.println("Select mode:");
            System.out.println("1. Normal TCP Sender");
            System.out.println("2. FIX Gateway Sender");
            System.out.println("3. Exit");
            //System.out.println("4. Launch Hello Web App (WAR) on Embedded Tomcat");
            System.out.print("Enter choice: ");
            if (firstInput != null) {
                input = firstInput;
                firstInput = null;
            } else if (scanner.hasNextLine()) {
                input = scanner.nextLine();
            }
            if (input.equals("1")) {
                // Pass host and port if provided
                if (args.length > 2) {
                    TcpSender.main(new String[]{args[1], args[2]});
                } else {
                    TcpSender.main(new String[]{});
                }
            } else if (input.equals("2")) {
                if (args.length > 2) {
                    FixGatewaySender.main(new String[]{args[1], args[2]});
                } else {
                    FixGatewaySender.main(new String[]{});
                }
            } else if (input.equals("3")) {
                System.out.println("Exiting.");
                break;
            } else if (input.equals("4")) {
                // Launch the WAR file using embedded Tomcat
                try {
                   // EmbeddedTomcatLauncher.launch();
                } catch (Exception e) {
                    System.out.println("Failed to launch web app: " + e.getMessage());
                }
            } else {
                System.out.println("Invalid choice. Try again after 10 seconds.");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    // Ignore
                }
            }
        }
        scanner.close();
    }
}
