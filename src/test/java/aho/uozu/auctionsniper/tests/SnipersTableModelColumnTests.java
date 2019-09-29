package aho.uozu.auctionsniper.tests;

import static org.junit.Assert.assertEquals;

import aho.uozu.auctionsniper.SniperSnapshot;
import aho.uozu.auctionsniper.SniperState;
import aho.uozu.auctionsniper.SnipersTableColumn;
import aho.uozu.auctionsniper.SnipersTableModel;
import org.junit.Test;

public class SnipersTableModelColumnTests {
    @Test public void
    columnZeroShouldBeItemId() {
        assertEquals(SnipersTableColumn.at(0), SnipersTableColumn.ITEM_IDENTIFIER);
    }

    @Test public void
    columnOneShouldBeLastPrice() {
        assertEquals(SnipersTableColumn.at(1), SnipersTableColumn.LAST_PRICE);
    }

    @Test public void
    columnTwoShouldBeLastBid() {
        assertEquals(SnipersTableColumn.at(2), SnipersTableColumn.LAST_BID);
    }

    @Test public void
    columnThreeShouldBeSniperStatus() {
        assertEquals(SnipersTableColumn.at(3), SnipersTableColumn.SNIPER_STATE);
    }

    @Test public void
    valueInMeh() {
        var snapshot = new SniperSnapshot("itemId", 1, 2, SniperState.JOINING);
        assertEquals(snapshot.itemId, SnipersTableColumn.ITEM_IDENTIFIER.valueIn(snapshot));
        assertEquals(snapshot.lastPrice, SnipersTableColumn.LAST_PRICE.valueIn(snapshot));
        assertEquals(snapshot.lastBid, SnipersTableColumn.LAST_BID.valueIn(snapshot));
        assertEquals(
                SnipersTableModel.textFor(snapshot.state),
                SnipersTableColumn.SNIPER_STATE.valueIn(snapshot));
    }
}
