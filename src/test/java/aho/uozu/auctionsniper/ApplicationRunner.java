package aho.uozu.auctionsniper;

public class ApplicationRunner {

    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String XMPP_HOSTNAME = "localhost";
    public static final String SNIPER_XMPP_ID = "sniper@host.xmpp.internal/Auction";

    private AuctionSniperDriver driver;
    private String itemId;

    public void startBiddingIn(final FakeAuctionServer auction) {
        itemId = auction.getItemId();
        Thread thread = new Thread("Test Application") {
            @Override public void run() {
                try {
                    App.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriver(1000);
        driver.hasTitle(MainWindow.APPLICATION_TITLE);
        driver.hasColumnTitles();
        driver.showsSniperStatus(itemId, 0, 0, SnipersTableModel.textFor(SniperState.JOINING));
    }

    public void showsSniperHasLostAuction(int lastPrice, int lastBid) {
        driver.showsSniperStatus(itemId, lastPrice, lastBid, SnipersTableModel.textFor(SniperState.LOST));
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

    public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid,
                SnipersTableModel.textFor(SniperState.BIDDING));
    }

    public void hasShownSniperIsWinning(int winningBid) {
        driver.showsSniperStatus(itemId, winningBid, winningBid, SnipersTableModel.textFor(SniperState.WINNING));
    }

    public void showsSniperHasWonAuction(int lastPrice) {
        driver.showsSniperStatus(itemId, lastPrice, lastPrice, SnipersTableModel.textFor(SniperState.WON));
    }
}
