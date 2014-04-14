package sorting_hat.data;

/**
 * This class represents a swap that takes place during 
 * the sorting of a data structure.
 *
 * @author Richard McKenna & _____________________
 */
public class SortTransaction
{
    // THE TWO INDICES OF THE DATA BEING SWAPPED
    private int fromIndex;
    private int toIndex;

    /**
     * Constructor initializes the swap indices.
     */
    public SortTransaction(int initFromIndex, int initToIndex)
    {
        fromIndex = initFromIndex;
        toIndex = initToIndex;
    }

    // ACCESSOR METHODS
    public int getFromIndex()   { return fromIndex; }
    public int getToIndex()     { return toIndex; }

    /**
     * For testing equivalence between transaction objects.
     */
    public boolean equals(Object obj)
    {
        SortTransaction otherTransaction = (SortTransaction)obj;
        return ((fromIndex == otherTransaction.fromIndex) 
                    && (toIndex == otherTransaction.toIndex))
                ||
                    ((fromIndex == otherTransaction.toIndex) 
                    && (toIndex == otherTransaction.fromIndex));
    }
}