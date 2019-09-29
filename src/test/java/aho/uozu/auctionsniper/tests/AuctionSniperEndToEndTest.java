package aho.uozu.auctionsniper.tests;

import aho.uozu.auctionsniper.ApplicationRunner;
import aho.uozu.auctionsniper.FakeAuctionServer;
import org.junit.After;
import org.junit.Test;

public class AuctionSniperEndToEndTest {
    private final FakeAuctionServer auction1 = new FakeAuctionServer("item-54321");
    private final FakeAuctionServer auction2 = new FakeAuctionServer("item-65432");
    private final ApplicationRunner application = new ApplicationRunner();

    @Test
    public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
        auction1.startSellingItem();
        application.startBiddingIn(auction1);
        auction1.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
        auction1.announceClosed();
        application.showsSniperHasLostAuction(auction1, 0, 0);
    }

    @Test
    public void sniperMakesAHigherBidButLoses() throws Exception {
        auction1.startSellingItem();
        application.startBiddingIn(auction1);
        auction1.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
        auction1.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding(auction1, 1000, 1098);
        auction1.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
        auction1.announceClosed();
        application.showsSniperHasLostAuction(auction1, 1000, 1098);
    }

    @Test public void
    sniperWinsAnAuctionByBiddingHigher() throws Exception {
        auction1.startSellingItem();
        application.startBiddingIn(auction1);
        auction1.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
        auction1.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding(auction1, 1000, 1098); // last price, last bid
        auction1.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
        auction1.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
        application.hasShownSniperIsWinning(auction1, 1098); // winning bid
        auction1.announceClosed();
        application.showsSniperHasWonAuction(auction1, 1098); // last price
    }

    @Test public void
    sniperBidsForMultipleItems() throws Exception {
        auction1.startSellingItem();
        auction2.startSellingItem();
        application.startBiddingIn(auction1, auction2);
        auction1.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
        auction2.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
        auction1.reportPrice(1000, 98, "other bidder");
        auction1.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
        auction2.reportPrice(500, 21, "other bidder");
        auction2.hasReceivedBid(521, ApplicationRunner.SNIPER_XMPP_ID);
        auction1.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
        auction2.reportPrice(521, 22, ApplicationRunner.SNIPER_XMPP_ID);
        application.hasShownSniperIsWinning(auction1, 1098);
        application.hasShownSniperIsWinning(auction2, 521);
        auction1.announceClosed();
        auction2.announceClosed();
        application.showsSniperHasWonAuction(auction1, 1098);
        application.showsSniperHasWonAuction(auction2, 521);
    }


    @After
    public void stopAuction() {
        auction1.stop();
    }

    @After
    public void stopApplication() {
        application.stop();
    }
}
