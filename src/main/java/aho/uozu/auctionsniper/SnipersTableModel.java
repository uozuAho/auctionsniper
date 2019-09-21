package aho.uozu.auctionsniper;

import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel {
    public enum Column {
        ITEM_IDENTIFIER,
        LAST_PRICE,
        LAST_BID,
        SNIPER_STATUS;

        public static Column at(int offset) { return values()[offset]; }
    }

    public int getColumnCount() { return Column.values().length; }
    public int getRowCount() { return 1; }

    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);

    private static final String[] STATUS_TEXT = {
            MainWindow.STATUS_JOINING,
            MainWindow.STATUS_BIDDING
    };

    private String state = MainWindow.STATUS_JOINING;
    private SniperSnapshot snapshot = STARTING_UP;

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (Column.at(columnIndex)) {
            case ITEM_IDENTIFIER:
                return snapshot.itemId;
            case LAST_PRICE:
                return snapshot.lastPrice;
            case LAST_BID:
                return snapshot.lastBid;
            case SNIPER_STATUS:
                return state;
            default:
                throw new IllegalArgumentException("No column at " + columnIndex);
        }
    }

    public void sniperStateChanged(SniperSnapshot newSniperSnapshot) {
        snapshot = newSniperSnapshot;
        state = STATUS_TEXT[newSniperSnapshot.state.ordinal()];
        fireTableRowsUpdated(0, 0);
    }
}
