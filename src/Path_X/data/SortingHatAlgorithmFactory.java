package Path_X.data;

import java.util.ArrayList;
import java.util.HashMap;
import Path_X.ui.PathXTile;

/**
 * This factory class builds the sorting algorithm objects to be
 * used for sorting in the game.
 *
 * @author Richard McKenna & _____________________
 */
public class SortingHatAlgorithmFactory
{
    // STORES THE SORTING ALGORITHMS WE MAY WISH TO USE
    static HashMap<SortingHatAlgorithmType, SortingHatAlgorithm> premadeSortingHatAlgorithms = null;

    /**
     * For getting a particular sorting algorithm. Note that the first
     * time it is called it initializes all the sorting algorithms and puts 
     * them in a hash map to be retrieved as needed to setup levels when loaded.
     */
    public static SortingHatAlgorithm buildSortingHatAlgorithm( SortingHatAlgorithmType algorithmType,
                                                                ArrayList<PathXTile> initDataToSort)
    {
        // INIT ALL THE ALGORITHMS WE'LL USE IF IT HASN'T DONE SO ALREADY
        if (premadeSortingHatAlgorithms == null)
        {
            premadeSortingHatAlgorithms = new HashMap();
            premadeSortingHatAlgorithms.put(SortingHatAlgorithmType.BUBBLE_SORT,    new BubbleSortAlgorithm(initDataToSort,        algorithmType.toString()));
            premadeSortingHatAlgorithms.put(SortingHatAlgorithmType.SELECTION_SORT, new SelectionSortAlgorithm(initDataToSort,  algorithmType.toString()));
        }
        // RETURN THE REQUESTED ONE
        return premadeSortingHatAlgorithms.get(algorithmType);
    }
}

/**
 * This class builds all the transactions necessary for performing
 * bubble sort on the data structure. This can then be used to
 * compare to student moves during the game.
 */
class BubbleSortAlgorithm extends SortingHatAlgorithm
{
    /**
     * Constructor only needs to init the inherited stuff.
     */
    public BubbleSortAlgorithm(ArrayList<PathXTile> initDataToSort, String initName)
    {
        // INVOKE THE PARENT CONSTRUCTOR
        super(initDataToSort, initName);
    }
    
    /**
     * Build and return all the transactions necessary to sort using bubble sort.
     */
    public ArrayList<SortTransaction> generateSortTransactions()
    {
        // HERE'S THE LIST OF TRANSACTIONS
        ArrayList<SortTransaction> transactions = new ArrayList();
        
        // FIRST LET'S COPY THE DATA TO A TEMPORARY ArrayList
        ArrayList<PathXTile> copy = new ArrayList();
        for (int i = 0; i < dataToSort.size(); i++)
            copy.add(dataToSort.get(i));

        // NOW SORT THE TEMPORARY DATA STRUCTURE
        for (int i = copy.size()-1; i > 0; i--)
        {
            for (int j = 0; j < i; j++)
            {
                // TEST j VERSUS j+1
                if (copy.get(j).getID() > copy.get(j+1).getID())
                {
                    // BUILD AND KEEP THE TRANSACTION
                    SortTransaction sT = new SortTransaction(j, j+1);
                    transactions.add(sT);
                    
                    // SWAP
                    PathXTile temp = copy.get(j);
                    copy.set(j, copy.get(j+1));
                    copy.set(j+1, temp);
                }
            }
        }
        return transactions;
    }
}

/**
 * This class builds all the transactions necessary for performing
 * selection sort on the data structure. This can then be used to
 * compare to student moves during the game.
 */
class SelectionSortAlgorithm extends SortingHatAlgorithm
{
    /**
     * Constructor only needs to init the inherited stuff.
     */
    public SelectionSortAlgorithm(ArrayList<PathXTile> initDataToSort, String initName)
    {
        // INVOKE THE PARENT CONSTRUCTOR
        super(initDataToSort, initName);
    }        
    
    /**
     * Build and return all the transactions necessary to sort using selection sort.
     */
    public ArrayList<SortTransaction> generateSortTransactions()
    {
        // HERE'S THE LIST OF TRANSACTIONS        
        ArrayList<SortTransaction> transactions = new ArrayList();
        
        // FIRST LET'S COPY THE DATA TO A TEMPORARY ArrayList
        ArrayList<PathXTile> copy = new ArrayList();
        for (int i = 0; i < dataToSort.size(); i++)
            copy.add(dataToSort.get(i));

        // NOW LET'S SORT THE COPY
        for (int i = 0; i < copy.size()-1; i++)
        {
            int minIndex = i;
            for (int j = i+1; j < copy.size(); j++)
            {
                // TEST j VERSUS j+1
                if (copy.get(minIndex).getID() > copy.get(j).getID())
                {
                    minIndex = j;
                }
            }
            
            // BUILD AND RECORD THE TRANSACTION
            SortTransaction sT = new SortTransaction(i, minIndex);
            transactions.add(sT);
            
            // AND SWAP
            swap(copy, i, minIndex);
        }
        return transactions;
    }
    
    public void swap(ArrayList<PathXTile> tiles, int index1, int index2)
    {
        PathXTile temp = tiles.get(index1);
        tiles.set(index1, tiles.get(index2));
        tiles.set(index2, temp);
    }
}