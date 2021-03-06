package aho.uozu.auctionsniper;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class App
{
    public static final String MAIN_WINDOW_NAME = "Auction Sniper App";

    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
    public static final String JOIN_COMMAND_FORMAT = "join format";
    public static final String BID_COMMAND_FORMAT = "bid format";

    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;

    private final SnipersTableModel snipers = new SnipersTableModel();
    private MainWindow ui;

    @SuppressWarnings("unused")
    private List<Chat> notToBeGCd = new ArrayList<>();

    private App() throws Exception {
        startUserInterface();
    }

    public static void main(String... args) throws Exception {
        App main = new App();

        XMPPConnection connection = connectTo(
                args[ARG_HOSTNAME],
                args[ARG_USERNAME],
                args[ARG_PASSWORD]);

        main.disconnectWhenUICloses(connection);
        main.addUserRequestListenerFor(connection);
    }

    private void addUserRequestListenerFor(final XMPPConnection connection) {
        ui.addUserRequestListener(new UserRequestListener() {
            public void joinAuction(String itemId) {
                snipers.addSniper(SniperSnapshot.joining(itemId));
                Chat chat = connection.getChatManager()
                        .createChat(auctionId(itemId, connection), null);
                notToBeGCd.add(chat);
                Auction auction = new XMPPAuction(chat);
                chat.addMessageListener(
                        new AuctionMessageTranslator(connection.getUser(),
                                new AuctionSniper(auction, new SwingThreadSniperListener(snipers), itemId)));
                auction.join();
            }
        });
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(() -> ui = new MainWindow(snipers));
    }

    private static XMPPConnection connectTo(String hostname, String username, String password) throws XMPPException
    {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);
        return connection;
    }

    private void disconnectWhenUICloses(final XMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }
}
