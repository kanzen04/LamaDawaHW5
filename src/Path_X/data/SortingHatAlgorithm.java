package Path_X.data;

import java.util.ArrayList;
import Path_X.ui.PathXTile;

/**
 * This class provides the framework for the algorithms
 * we may choose to use for sorting.
 *
 * @author Richard McKenna & _____________________
 */
public abstract class SortingHatAlgorithm
{
    // THIS IS THE DATA STRUCTURE TO SORT
    protected ArrayList<PathXTile> dataToSort;
    
    // THE NAME OF THE ALGORITHM
    protected String name;

    /**
     * Constructor that sets up both instance variables.
     */
    public SortingHatAlgorithm(ArrayList<PathXTile> initDataToSort, String initName)
    {
        dataToSort = initDataToSort;
        name = initName;
    }

    /**
     * This will generate a list of the proper sorting transactions, where
     * each subclass will do so according to its own algorithm.
     */
    public abstract ArrayList<SortTransaction> generateSortTransactions();
}