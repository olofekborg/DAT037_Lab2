public class Bid{

    public String name;
    public BidType type;
    public Integer oldValue;
    public Integer value;

    public Bid(String name, BidType type, Integer oldValue, Integer newValue) {
        this.name = name;
        this.type = type;
        this.oldValue = oldValue;
        this.value = newValue;
    }

    public Bid(String name, String type, String oldValue, String newValue) {
        this(name, BidType.strToBidType(type), Integer.parseInt(oldValue), Integer.parseInt(newValue));
    }

    public Bid(String name, String type, String value) {
        this(name, type, "-1", value);
    }

    /**
     * @return String with the name and value
     */
    public String toString() {
        return name + " " + value;
    }

    /**
     * Checks if this object is equal to another object. Must be same object or
     * have the same name and value
     * @param o Object to compare to
     * @return true if equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bid bid = (Bid) o;

        if (name != null ? !name.equals(bid.name) : bid.name != null) return false;
        return !(value != null ? !value.equals(bid.value) : bid.value != null);
    }

    /**
     * Generates a hash code based on the name and the value
     * @return The hash code
     */
    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    /**
     * Represents the possible bid types
     */
    public enum BidType {
        BUY, SELL, NEW_BUY, NEW_SELL;

        public static BidType strToBidType(String str) {
            if (str.equals("K"))
                return BUY;
            if (str.equals("S"))
                return SELL;
            if (str.equals("NK"))
                return NEW_BUY;
            if (str.equals("NS"))
                return NEW_SELL;
            else
                return null;
        }
    }
}