package aho.uozu.auctionsniper;

import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel implements SniperListener {

    @Override public String getColumnName(int column) {
        return SnipersTableColumn.at(column).name;
    }

    public int getColumnCount() { return SnipersTableColumn.values().length; }
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
        return SnipersTableColumn.at(columnIndex).valueIn(snapshot);
    }

    public void sniperStateChanged(SniperSnapshot newSniperSnapshot) {
        snapshot = newSniperSnapshot;
        fireTableRowsUpdated(0, 0);
    }

    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }
}
