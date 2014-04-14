package sorting_hat.data;

/**
 * Thix class represents a grid cell where a tile can be placed in a sortable
 * list of tiles.
 *
 * @author Richard McKenna & _____________________
 */
public class SnakeCell
{
    // LOCATION OF THIS CELL IN THE GAME GRID
    public int col;
    public int row;

    /**
     * Constructor initializes this cell, which cannot be changed.
     */
    public SnakeCell(int initCol, int initRow)
    {
        col = initCol;
        row = initRow;
    }

    /**
     * Used to compare cells, two cells are equivalent if they
     * have the same column and row.
     */
    public boolean equals(Object obj)
    {
        SnakeCell otherCell = (SnakeCell) obj;
        return (col == otherCell.col) && (row == otherCell.row);
    }
}
