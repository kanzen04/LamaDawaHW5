package sorting_hat.ui;

/**
 * This enum IS FOR KEEPING TRACK OF EACH TILE STATE. A TILE MAY
 * HAVE ONE OF 4 STATES:
 * 
 *      INVISIBLE_STATE:    USED WHEN ON THE MENU SCREEN, MEANS A TILE
 *                          IS NOT DRAWN AND CANNOT BE CLICKED
 * 
 *      VISIBLE_STATE:      USED WHEN ON THE GAME SCREEN, MEANS A TILE
 *                          IS VISIBLE AND CAN BE CLICKED (TO SELECT IT), 
 *                          BUT IS NOT CURRENTLY SELECTED
 * 
 *      SELECTED_STATE:     USED WHEN ON THE GAME SCREEN, MEANS A TILE
 *                          IS VISIBLE AND CAN BE CLICKED (TO UNSELECT IT), 
 *                          AND IS CURRENTLY SELECTED
 * 
 *      MOUSE_OVER STATE:   USED WHEN THE MOUSE IS HOVERING OVER THE TILE BUT
 *                          THE TILE HAS NOT YET BEEN SELECTED
 * 
 * @author Richard McKenna & __________________
 */
public enum SortingHatTileState
{
    INVISIBLE_STATE,
    VISIBLE_STATE,
    SELECTED_STATE,
    MOUSE_OVER_STATE
}