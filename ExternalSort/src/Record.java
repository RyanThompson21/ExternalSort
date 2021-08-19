import java.nio.ByteBuffer;

/**
 * @author ryanjt5
 * @version 4/14/20
 */
public class Record implements Comparable<Record> {
    // non negative integer value
    private long value;
    private double key;
    // int from [0,7]
    private int flag;


    /**
     * creates a new record with a given key value pair
     * 
     * @param v
     *            long which will be the value
     * @param k
     *            double which is the key
     */
    public Record(long v, double k) {
        value = v;
        key = k;
    }


    /**
     * creates a new record with an additional flag for keeping track of runs
     * 
     * @param v
     *            value
     * @param k
     *            key
     * @param fl
     *            run number
     */
    public Record(long v, double k, int fl) {
        value = v;
        key = k;
        flag = fl;
    }


    /**
     * getter for this record's value
     * 
     * @return this record's value
     */
    public long getValue() {
        return value;
    }


    /**
     * getter for key
     * 
     * @return this record's key
     */
    public double getKey() {
        return key;
    }


    /**
     * getter for the run# flag
     * 
     * @return the index of the run this record is from
     */
    public int getFlag() {
        return flag;
    }


    /**
     * gets the key value pair in a byte array
     * 
     * @return a byte array with the record's value first followed by its key
     */
    public byte[] getTotal() {
        ByteBuffer total = ByteBuffer.allocate(Long.BYTES + Double.BYTES);
        total.putLong(value);
        total.putDouble(key);
        return total.array();
    }


    /**
     * compares this record to the given one
     * 
     * @param a
     *            record being compared to
     * @return if this > a -- 1
     *         if this < a -- -1
     *         if this = a -- 0
     */
    @Override
    public int compareTo(Record a) {
        if (this.getKey() > a.getKey()) {
            return 1;
        }
        else if (this.getKey() < a.getKey()) {
            return -1;
        }
        else {
            return 0;
        }
        // return (((Double)this.getKey()).compareTo((Double)a.getKey()));
    }


    /**
     * equals method
     * 
     * @param ob
     *            object being compared
     * 
     * @return true if the records are the same
     */
    @Override
    public boolean equals(Object ob) {
        if (this.getClass() != ob.getClass()) {
            return false;
        }
        else {
            Record comp = (Record)ob;
            return ((this.getKey() == comp.getKey()) && (this.getValue() == comp
                .getValue()));
        }
    }


    /**
     * a string rep of this record
     * 
     * @return a string rep of this record
     */
    public String toString() {
        return "" + value + " " + key;
    }
}
