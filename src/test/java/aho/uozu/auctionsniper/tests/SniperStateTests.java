package aho.uozu.auctionsniper.tests;

import aho.uozu.auctionsniper.SniperState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SniperStateTests {
    @Test public void
    whenBidding_ShouldHaveLost_OnAuctionClosed() {
        assertEquals(SniperState.LOST, SniperState.BIDDING.whenAuctionClosed());
    }

    @Test public void
    whenJoining_ShouldHaveLost_OnAuctionClosed() {
        assertEquals(SniperState.LOST, SniperState.JOINING.whenAuctionClosed());
    }

    @Test public void
    whenWinning_ShouldHaveWon_OnAuctionClosed() {
        assertEquals(SniperState.WON, SniperState.WINNING.whenAuctionClosed());
    }
}
