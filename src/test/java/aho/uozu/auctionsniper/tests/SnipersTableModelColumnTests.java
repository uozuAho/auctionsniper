package aho.uozu.auctionsniper.tests;

import static org.junit.Assert.assertEquals;

import aho.uozu.auctionsniper.SniperSnapshot;
import aho.uozu.auctionsniper.SniperState;
import aho.uozu.auctionsniper.SnipersTableModel;
import org.junit.Test;

public class SnipersTableModelColumnTests {
    @Test public void
    columnZeroShouldBeItemId() {
        assertEquals(SnipersTableModel.Column.at(0), SnipersTableModel.Column.ITEM_IDENTIFIER);
    }

    @Test public void
    columnOneShouldBeLastPrice() {
        assertEquals(SnipersTableModel.Column.at(1), SnipersTableModel.Column.LAST_PRICE);
    }

    @Test public void
    columnTwoShouldBeLastBid() {
        assertEquals(SnipersTableModel.Column.at(2), SnipersTableModel.Column.LAST_BID);
    }

    @Test public void
    columnThreeShouldBeSniperStatus() {
        assertEquals(SnipersTableModel.Column.at(3), SnipersTableModel.Column.SNIPER_STATE);
    }

    @Test public void
    valueInMeh() {
        var snapshot = new SniperSnapshot("itemId", 1, 2, SniperState.JOINING);
        assertEquals(snapshot.itemId, SnipersTableModel.Column.ITEM_IDENTIFIER.valueIn(snapshot));
        assertEquals(snapshot.lastPrice, SnipersTableModel.Column.LAST_PRICE.valueIn(snapshot));
        assertEquals(snapshot.lastBid, SnipersTableModel.Column.LAST_BID.valueIn(snapshot));
        assertEquals(
                SnipersTableModel.textFor(snapshot.state),
                SnipersTableModel.Column.SNIPER_STATE.valueIn(snapshot));
    }
}
