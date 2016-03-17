import java.util.List;

/**
 * Simulates stock trading. An entity can interact with the market by placing bids.
 * OBS! A person is allowed to place multiple bids of the same type as long as they have
 * different values (eg, "Bengt K 20" and "Bengt K 21" is OK).
 */
public class TradeSimulator {

    PriorityQueue<Bid> buyQ;
    PriorityQueue<Bid> sellQ;
    List<Bid> bids;

    /**
     * Constructer which uses a list of bids as a parameter
     *
     * @param bids list of bids
     */
    public TradeSimulator(List<Bid> bids) {
		buyQ = new PriorityQueue<Bid>((Bid o1, Bid o2) -> -o1.value.compareTo(o2.value));
        sellQ = new PriorityQueue<Bid>((Bid o1, Bid o2) -> o1.value.compareTo(o2.value));
        this.bids = bids;
    }

    /**
     * Runs a simulation using the current list of bids
     */
    public void runSimulation() {
        System.out.println(""); // empty line, just for aesthetics
        for (Bid bid : bids) {
            //handle the different bid types
            switch (bid.type) {
                case BUY:
                    buy(bid);
                    break;
                case SELL:
                    sell(bid);
                    break;
                case NEW_BUY:
                    newBuy(bid);
                    break;
                case NEW_SELL:
                    newSell(bid);
                    break;
            }
        }
        //after all bids have been processed we print the order book
        printOrderBook();
    }

    /**
     * Handles buy bids (K)
     *
     * @param buyBid The bid
     */
    private void buy(Bid buyBid) {
        // Check if we can perform the order at once
        if (sellQ.peak() != null && buyBid.value >= sellQ.peak().value) {
            Bid seller = sellQ.poll();
            System.err.println(buyBid.name + " köper från " + seller.name
                    + " för " + buyBid.value + " kr");
        } else {
            //Otherwise insert it into the array
            if (!buyQ.insert(buyBid))
                System.out.println("Duplicate orders not allowed");
        }
    }

    /**
     * Handles sell bids (S)
     *
     * @param sellBid The bid
     */
    private void sell(Bid sellBid) {
        // Check if we can perform the order at once
        if (buyQ.peak() != null && sellBid.value <= buyQ.peak().value) {
            Bid buyer = buyQ.poll();
            System.err.println(buyer.name + " köper från " + sellBid.name
                    + " för " + sellBid.value + " kr");
        } else {
            //Otherwise insert it into the array
            if (!sellQ.insert(sellBid))
                System.out.println("Duplicate orders not allowed");
        }
    }

    /**
     * Handles modifications of an existing buy bid (NK)
     *
     * @param buyBid The bid
     */
    private void newBuy(Bid buyBid) {

        // Create a duplicate of the old object (to search for in the queue).
        Bid oldObj = new Bid(buyBid.name, buyBid.type, -1, buyBid.oldValue);
        buyBid.type = Bid.BidType.BUY;

        //try to modify the key and print error message if unsuccessful
        if (!buyQ.modifyKey(oldObj, buyBid)) {
            System.err.println("Felaktig ändringsorder");
        }

        // check if we can perform a trade
        if (sellQ.peak() != null && (buyQ.peak().value > sellQ.peak().value)) {
            buyQ.poll();
            sellQ.poll();
        }
    }

    /**
     * Handles modifications of an existing sell bid (NS)
     *
     * @param sellBid The bid
     */
    private void newSell(Bid sellBid) {

        // Create a duplicate of the old object (to search for in the queue).
        Bid oldObj = new Bid(sellBid.name, sellBid.type, -1, sellBid.oldValue);
        sellBid.type = Bid.BidType.SELL;

        //try to modify the key and print error message if unsuccessful
        if (!sellQ.modifyKey(oldObj, sellBid)) {
            System.err.println("Felaktig ändringsorder");
        }

        // check if we can perform a trade
        if (buyQ.peak() != null && (buyQ.peak().value > sellQ.peak().value)) {
            buyQ.poll();
            sellQ.poll();
        }
    }

    /**
     * Prints the order book (buy queue and sell queue).
     * Both queues are printed in a sorted order.
     */
    private void printOrderBook() {

        String buyList = "";
        while (true) {
            Bid bid = buyQ.poll();
            if (bid == null) {
                break;
            } else {
                buyList += bid.toString() + ", ";
            }
        }

        String sellList = "";
        while (true) {
            Bid bid = sellQ.poll();
            if (bid == null) {
                break;
            } else {
                sellList += bid.toString() + ", ";
            }
        }

        System.out.println("");
        System.out.println("Orderbok:");
        System.out.println("Säljare: " + sellList);
        System.out.println("Köpare: " + buyList);

    }
}
