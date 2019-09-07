package aho.uozu.auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App implements SniperListener
{
    public static final String MAIN_WINDOW_NAME = "Auction Sniper App";
    public static final String SNIPER_STATUS_NAME = "sniper status";

    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
    public static final String JOIN_COMMAND_FORMAT = "join format";
    public static final String BID_COMMAND_FORMAT = "bid format";

    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private static final int ARG_ITEM_ID = 3;

    private MainWindow ui;

    @SuppressWarnings("unused")
    private Chat notToBeGCd;

    private App() throws Exception {
        startUserInterface();
    }

    public static void main(String... args) throws Exception {
        App main = new App();

        XMPPConnection connection = connectTo(
                args[ARG_HOSTNAME],
                args[ARG_USERNAME],
                args[ARG_PASSWORD]);

        main.joinAuction(connection, args[ARG_ITEM_ID]);
    }

    @Override
    public void sniperLost() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ui.showStatus(MainWindow.STATUS_LOST);
            }
        });
    }

    @Override
    public void sniperBidding() {
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                ui = new MainWindow();
            }
        });
    }

    private void joinAuction(XMPPConnection connection, String itemId) throws XMPPException {
        Auction nullAuction = new Auction() {
            public void bid(int amount) {}
        };
        disconnectWhenUICloses(connection);
        final Chat chat = connection.getChatManager().createChat(
                auctionId(itemId, connection),
                new AuctionMessageTranslator(new AuctionSniper(nullAuction, this)));
        this.notToBeGCd = chat;
        chat.sendMessage(JOIN_COMMAND_FORMAT);
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
