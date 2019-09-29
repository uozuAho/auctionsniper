package aho.uozu.auctionsniper;

public enum SnipersTableColumn {
    ITEM_IDENTIFIER("Item") {
        @Override public Object valueIn(SniperSnapshot snapshot) {
            return snapshot.itemId;
        }
    },
    LAST_PRICE("Last Price") {
        @Override public Object valueIn(SniperSnapshot snapshot) {
            return snapshot.lastPrice;
        }
    },
    LAST_BID("Last Bid") {
        @Override public Object valueIn(SniperSnapshot snapshot) {
            return snapshot.lastBid;
        }
    },
    SNIPER_STATE("State") {
        @Override public Object valueIn(SniperSnapshot snapshot) {
            return SnipersTableModel.textFor(snapshot.state);
        }
    };

    public final String name;
    abstract public Object valueIn(SniperSnapshot snapshot);
    public static SnipersTableColumn at(int offset) { return values()[offset]; }

    SnipersTableColumn(String name) {
        this.name = name;
    }
}
