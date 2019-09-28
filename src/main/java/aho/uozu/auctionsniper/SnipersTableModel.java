package aho.uozu.auctionsniper;

import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel {
    public enum Column {
        ITEM_IDENTIFIER {
            @Override public Object valueIn(SniperSnapshot snapshot) {
                return snapshot.itemId;
            }
        },
        LAST_PRICE {
            @Override public Object valueIn(SniperSnapshot snapshot) {
                return snapshot.lastPrice;
            }
        },
        LAST_BID {
            @Override public Object valueIn(SniperSnapshot snapshot) {
                return snapshot.lastBid;
            }
        },
        SNIPER_STATUS {
            @Override public Object valueIn(SniperSnapshot snapshot) {
                return SnipersTableModel.textFor(snapshot.state);
            }
        };

        abstract public Object valueIn(SniperSnapshot snapshot);
        public static Column at(int offset) { return values()[offset]; }
    }

    public int getColumnCount() { return Column.values().length; }
    public int getRowCount() { return 1; }

    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);

    private static final String[] STATUS_TEXT = {
            "Joining",
            "Bidding",
            "Winning",
            "Lost",
            "Won"
    };

    private SniperSnapshot snapshot = STARTING_UP;

    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(snapshot);
    }

    public void sniperStateChanged(SniperSnapshot newSniperSnapshot) {
        snapshot = newSniperSnapshot;
        fireTableRowsUpdated(0, 0);
    }

    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }
}
