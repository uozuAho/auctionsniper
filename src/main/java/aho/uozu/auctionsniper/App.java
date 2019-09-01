package aho.uozu.auctionsniper;

import javax.swing.*;

public class App
{
    public static final String MAIN_WINDOW_NAME = "Auction Sniper App";
    public static final String SNIPER_STATUS_NAME = "sniper status";

    private MainWindow ui;

    public App() throws Exception {
        startUserInterface();
    }

    public static void main(String... args) throws Exception {
        App main = new App();
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                ui = new MainWindow();
            }
        });
    }
}
