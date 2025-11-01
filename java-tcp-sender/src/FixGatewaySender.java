import quickfix.*;
import quickfix.field.*;
import quickfix.fix44.Logon;
import quickfix.fix44.Logout;
import quickfix.fix44.Heartbeat;
import quickfix.fix44.NewOrderSingle;
import java.util.Scanner;

public class FixGatewaySender implements Application {
    private volatile SessionID currentSessionId;
    private volatile Initiator initiator;

    @Override
    public void onCreate(SessionID sessionId) {
        System.out.println("Sundar FIX Sender Session created: " + sessionId);
    }

    @Override
    public void onLogon(SessionID sessionId) {
        System.out.println("Sundar FIX Sender Session Logon: " + sessionId);
        System.out.println();
        currentSessionId = sessionId;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Select option:");
            System.out.println("1. Send NewOrderSingle message");
            System.out.println("2. Logout");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            String input = scanner.nextLine();
            if (input.equals("1")) {
                sendNewOrder(sessionId);
            } else if (input.equals("2")) {
                System.out.println("Logging out...");
                try {
                    Session.lookupSession(sessionId).logout();
                } catch (Exception e) {
                    System.out.println("Logout failed: " + e.getMessage());
                }
            } else if (input.equals("3")) {
                System.out.println("Exiting.");
                if (initiator != null) {
                    try { initiator.stop(); } catch (Exception e) { /* ignore */ }
                }
                break;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void sendNewOrder(SessionID sessionId) {
        try {
            NewOrderSingle order = new NewOrderSingle(
                new ClOrdID(Long.toString(System.currentTimeMillis())),
                new Side(Side.BUY),
                new TransactTime(),
                new OrdType(OrdType.MARKET)
            );
            order.set(new OrderQty(100));
            order.set(new Symbol("EUR/USD"));
            Session.sendToTarget(order, sessionId);
            System.out.println("Sent NewOrderSingle");
        } catch (SessionNotFound e) {
            System.out.println("Session not found: " + e.getMessage());
        }
    }

    @Override
    public void onLogout(SessionID sessionId) {
        System.out.println("Logout: " + sessionId);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {}

    @Override
    public void fromAdmin(Message message, SessionID sessionId) {}

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {}

    @Override
    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        System.out.println("Received: " + message);
    }

    public static void main(String[] args) throws Exception {
        SessionSettings settings = new SessionSettings("fix.cfg");
        FixGatewaySender app = new FixGatewaySender();
        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();
        Initiator initiator = new SocketInitiator(app, storeFactory, settings, logFactory, messageFactory);
        app.initiator = initiator;
        initiator.start();
        System.out.println("Sundar POC FIX Gateway Sender started.");
        // Keep running
        Thread.currentThread().join();
    }
}
