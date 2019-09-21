package aho.uozu.auctionsniper;

public class AuctionSniper implements AuctionEventListener {
    private final SniperListener sniperListener;
    private final Auction auction;
    private SniperSnapshot snapshot;

    private boolean isWinning = false;

    public AuctionSniper(Auction auction, SniperListener sniperListener, String itemId) {
        this.auction = auction;
        this.sniperListener = sniperListener;
        this.snapshot = SniperSnapshot.joining(itemId);
    }

    @Override
    public void auctionClosed() {
        if (isWinning) {
            sniperListener.sniperStateChanged(snapshot.won());
        } else {
            sniperListener.sniperStateChanged(snapshot.lost());
        }
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource source) {
        isWinning = source == PriceSource.FromSniper;
        int bid = price + increment;
        if (isWinning) {
            snapshot = snapshot.winning(price);
        }
        else {
            auction.bid(bid);
            snapshot = snapshot.bidding(price, bid);
        }
        sniperListener.sniperStateChanged(snapshot);
    }
}
