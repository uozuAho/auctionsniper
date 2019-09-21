package aho.uozu.auctionsniper;

import java.util.EventListener;

public interface SniperListener extends EventListener {
    void sniperJoining(SniperState sniperState);
    void sniperBidding(SniperState sniperState);
    void sniperWinning();
    void sniperWon();
    void sniperLost();
}
