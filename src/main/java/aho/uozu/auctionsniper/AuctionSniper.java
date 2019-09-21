package aho.uozu.auctionsniper;

public class AuctionSniper implements AuctionEventListener {
    private final SniperListener sniperListener;
    private final Auction auction;
    private final String itemId;

    private boolean isWinning = false;

    public AuctionSniper(Auction auction, SniperListener sniperListener, String itemId) {
        this.auction = auction;
        this.sniperListener = sniperListener;
        this.itemId = itemId;
    }

    @Override
    public void auctionClosed() {
        if (isWinning) {
            sniperListener.sniperStateChanged(new SniperSnapshot(itemId, 0, 0, SniperState.WON));
        } else {
            sniperListener.sniperStateChanged(new SniperSnapshot(itemId, 0, 0, SniperState.LOST));
        }
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource source) {
        isWinning = source == PriceSource.FromSniper;
        int bid = price + increment;
        if (isWinning) {
            sniperListener.sniperStateChanged(new SniperSnapshot(itemId, price, bid, SniperState.WINNING));
        }
        else {
            auction.bid(bid);
            sniperListener.sniperStateChanged(new SniperSnapshot(itemId, price, bid, SniperState.BIDDING));
        }
    }
}
