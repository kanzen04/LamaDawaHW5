package PathX;

/**
 * This class represents an intersection in a level. Note that an intersection
 * connects roads and can be thought of as a node on a graph.
 * 
 * @author Richard McKenna
 */
public class PathX_Intersection
{
    // INTERSECTION LOCATION
    public int x;
    public int y;
    
    // IS IT OPEN OR NOT
    public boolean open;

    /**
     * Constructor allows for a custom location, note that all
     * intersections start as open.
     */
    public PathX_Intersection(int initX, int initY)
    {
        x = initX;
        y = initY;
        open = true;
    }

    // ACCESSOR METHODS
    public int getX()       {   return x;       }
    public int getY()       {   return y;       }
    public boolean isOpen() {   return open;    }
    
    // MUTATOR METHODS
    public void setX(int x)
    {   this.x = x;         }
    public void setY(int y)
    {   this.y = y;         }
    public void setOpen(boolean open)
    {   this.open = open;   }
    
    /**
     * This toggles the intersection open/closed.
     */
    public void toggleOpen()
    {
        open = !open;
    }
    
    /**
     * Returns a textual representation of this intersection.
     */
    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
}