package aho.uozu.auctionsniper;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class MainWindow extends JFrame {
    public static final String SNIPERS_TABLE_NAME = "snipers";
    public static final String STATUS_JOINING = "joining";
    public static final String STATUS_LOST = "lost";
    public static final String STATUS_BIDDING = "bidding";
    public static final String STATUS_WINNING = "winning";
    public static final String STATUS_WON = "won";

    private final SnipersTableModel snipers = new SnipersTableModel();

    public MainWindow() {
        super("Auction Sniper");
        setName(App.MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fillContentPane(JTable snipersTable) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable() {
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }

    public void showStatusText(String statusText) {
        snipers.setStatusText(statusText);
    }

    public class SnipersTableModel extends AbstractTableModel {
        public int getColumnCount() { return 1; }
        public int getRowCount() { return 1; }
        public Object getValueAt(int rowIndex, int columnIndex) { return statusText; }

        private String statusText = STATUS_JOINING;

        public void setStatusText(String newStatusText) {
            statusText = newStatusText;
            fireTableRowsUpdated(0, 0);
        }
    }
}
