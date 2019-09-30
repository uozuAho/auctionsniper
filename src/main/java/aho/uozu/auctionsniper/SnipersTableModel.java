package aho.uozu.auctionsniper;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class SnipersTableModel extends AbstractTableModel implements SniperListener {

    @Override public String getColumnName(int column) {
        return SnipersTableColumn.at(column).name;
    }

    public int getColumnCount() { return SnipersTableColumn.values().length; }
    public int getRowCount() { return snapshotRows.size(); }

    private static final String[] STATUS_TEXT = {
            "Joining",
            "Bidding",
            "Winning",
            "Lost",
            "Won"
    };

    private List<SniperSnapshot> snapshotRows = new ArrayList<>();

    public Object getValueAt(int rowIndex, int columnIndex) {
        return SnipersTableColumn.at(columnIndex).valueIn(snapshotRows.get(rowIndex));
    }

    public void sniperStateChanged(SniperSnapshot newSniperSnapshot) {
        var rowNum = getRowOf(newSniperSnapshot);
        snapshotRows.set(rowNum, newSniperSnapshot);
        fireTableRowsUpdated(rowNum, rowNum);
    }

    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }

    public void addSniper(SniperSnapshot sniperSnapshot) {
        snapshotRows.add(sniperSnapshot);
        var row = snapshotRows.size() - 1;
        fireTableRowsInserted(row, row);
    }

    private int getRowOf(SniperSnapshot snapshot) {
        for (int i = 0; i < snapshotRows.size(); i++) {
            if (snapshot.itemId == snapshotRows.get(i).itemId) {
                return i;
            }
        }
        throw new IllegalStateException("no matching row with item id: " + snapshot.itemId);
    }
}
