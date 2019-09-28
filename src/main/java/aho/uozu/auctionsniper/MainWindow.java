package aho.uozu.auctionsniper;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public static final String SNIPERS_TABLE_NAME = "snipers";
    public static final String APPLICATION_TITLE = "Auction sniper";

    private final SnipersTableModel snipers;

    public MainWindow(SnipersTableModel snipers) {
        super(APPLICATION_TITLE);
        this.snipers = snipers;
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
}
