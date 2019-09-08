package aho.uozu.auctionsniper;

import java.util.EventListener;

public interface AuctionEventListener extends EventListener {
    enum PriceSource {
        FromSniper, FromOtherBidder;
    };
    void auctionClosed();
    void currentPrice(int price, int increment, PriceSource source);
}
