import quickfix.*;
import quickfix.field.*;
import quickfix.fix44.Logon;
import quickfix.fix44.Logout;
import quickfix.fix44.Heartbeat;

public class FixGatewayReceiver implements Application {
    @Override
    public void onCreate(SessionID sessionId) {
        System.out.println("Session created: " + sessionId);
    }

    @Override
    public void onLogon(SessionID sessionId) {
        System.out.println("Logon: " + sessionId);
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
        Application application = new FixGatewayReceiver();
        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();
        Acceptor acceptor = new SocketAcceptor(application, storeFactory, settings, logFactory, messageFactory);
        acceptor.start();
        System.out.println("FIX Gateway Receiver started.");
        // Keep running
        Thread.currentThread().join();
    }
}
