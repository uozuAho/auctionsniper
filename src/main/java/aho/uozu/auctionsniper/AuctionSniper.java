package aho.uozu.auctionsniper;

public class AuctionSniper implements AuctionEventListener {
    private final SniperListener sniperListener;
    private final Auction auction;

    private boolean isWinning = false;

    public AuctionSniper(Auction auction, SniperListener sniperListener) {
        this.auction = auction;
        this.sniperListener = sniperListener;
    }

    @Override
    public void auctionClosed() {
        if (isWinning) {
            sniperListener.sniperWon();
        } else {
            sniperListener.sniperLost();
        }
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource source) {
        isWinning = source == PriceSource.FromSniper;
        if (isWinning) {
            sniperListener.sniperWinning();
        }
        else {
            int bid = price + increment;
            auction.bid(bid);
            sniperListener.sniperBidding(new SniperState("todo: ItemId here", price, bid));
        }
    }
}
