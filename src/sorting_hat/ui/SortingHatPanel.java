package sorting_hat.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JPanel;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import properties_manager.PropertiesManager;
import sorting_hat.data.SortingHatDataModel;
import static sorting_hat.SortingHatConstants.*;
import sorting_hat.TheSortingHat.SortingHatPropertyType;
import sorting_hat.data.SnakeCell;
import sorting_hat.data.SortingHatRecord;

/**
 * This class performs all of the rendering for The Sorting Hat game application.
 * 
 * @author Richard McKenna
 */
public class SortingHatPanel extends JPanel
{
    // THIS IS ACTUALLY OUR Sorting Hat APP, WE NEED THIS
    // BECAUSE IT HAS THE GUI STUFF THAT WE NEED TO RENDER
    private MiniGame game;
    
    // AND HERE IS ALL THE GAME DATA THAT WE NEED TO RENDER
    private SortingHatDataModel data;
    
    // WE'LL USE THIS TO FORMAT SOME TEXT FOR DISPLAY PURPOSES
    private NumberFormat numberFormatter;
 
    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING UNSELECTED TILES
    private BufferedImage blankTileImage;
    
    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING SELECTED TILES
    private BufferedImage blankTileSelectedImage;
    
    // THIS IS FOR WHEN THE USE MOUSES OVER A TILE
    private BufferedImage blankTileMouseOverImage;
    
    /**
     * This constructor stores the game and data references,
     * which we'll need for rendering.
     * 
     * @param initGame The Sorting Hat game that is using
     * this panel for rendering.
     * 
     * @param initData The Sorting Hat game data.
     */
    public SortingHatPanel(MiniGame initGame, SortingHatDataModel initData)
    {
        game = initGame;
        data = initData;
        numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMinimumFractionDigits(3);
        numberFormatter.setMaximumFractionDigits(3);
    }
    
    // MUTATOR METHODS
        // -setBlankTileImage
        // -setBlankTileSelectedImage
    
    /**
     * This mutator method sets the base image to use for rendering tiles.
     * 
     * @param initBlankTileImage The image to use as the base for rendering tiles.
     */
    public void setBlankTileImage(BufferedImage initBlankTileImage)
    {
        blankTileImage = initBlankTileImage;
    }
    
    /**
     * This mutator method sets the base image to use for rendering selected tiles.
     * 
     * @param initBlankTileSelectedImage The image to use as the base for rendering
     * selected tiles.
     */
    public void setBlankTileSelectedImage(BufferedImage initBlankTileSelectedImage)
    {
        blankTileSelectedImage = initBlankTileSelectedImage;
    }
    
    public void setBlankTileMouseOverImage(BufferedImage initBlankTileMouseOverImage)
    {
        blankTileMouseOverImage = initBlankTileMouseOverImage;
    }

    /**
     * This is where rendering starts. This method is called each frame, and the
     * entire game application is rendered here with the help of a number of
     * helper methods.
     * 
     * @param g The Graphics context for this panel.
     */
    @Override
    public void paintComponent(Graphics g)
    {
        try
        {
            // MAKE SURE WE HAVE EXCLUSIVE ACCESS TO THE GAME DATA
            game.beginUsingData();
        
            // CLEAR THE PANEL
            super.paintComponent(g);
        
            // RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
            renderBackground(g);

            // ONLY RENDER THIS STUFF IF WE'RE ACTUALLY IN-GAME
            if (!data.notStarted())
            {
                // RENDER THE SNAKE
                if (!data.won())
                    renderSnake(g);
                
                // AND THE TILES
                renderTiles(g);
                
                // AND THE DIALOGS, IF THERE ARE ANY
                renderDialogs(g);
                                
                // RENDERING THE GRID WHERE ALL THE TILES GO CAN BE HELPFUL
                // DURING DEBUGGIN TO BETTER UNDERSTAND HOW THEY RE LAID OUT
                renderGrid(g);
                
                // RENDER THE ALGORITHM NAME
                renderHeader(g);
            }

            // AND THE BUTTONS AND DECOR
            renderGUIControls(g);
            
            if (!data.notStarted())
            {
                // AND THE TIME AND TILES STATS
                renderStats(g);
            }
        
            // AND FINALLY, TEXT FOR DEBUGGING
            renderDebuggingText(g);
        }
        finally
        {
            // RELEASE THE LOCK
            game.endUsingData();    
        }
    }
    
    // RENDERING HELPER METHODS
        // - renderBackground
        // - renderGUIControls
        // - renderSnake
        // - renderTiles
        // - renderDialogs
        // - renderGrid
        // - renderDebuggingText
    
    /**`
     * Renders the background image, which is different depending on the screen. 
     * 
     * @param g the Graphics context of this panel.
     */
    public void renderBackground(Graphics g)
    {
        // THERE IS ONLY ONE CURRENTLY SET
        Sprite bg = game.getGUIDecor().get(BACKGROUND_TYPE);
        renderSprite(g, bg);
    }

    /**
     * Renders all the GUI decor and buttons.
     * 
     * @param g this panel's rendering context.
     */
    public void renderGUIControls(Graphics g)
    {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> decorSprites = game.getGUIDecor().values();
        for (Sprite s : decorSprites)
        {
            if (s.getSpriteType().getSpriteTypeID() != BACKGROUND_TYPE)
                renderSprite(g, s);
        }
        
        // AND NOW RENDER THE BUTTONS
        Collection<Sprite> buttonSprites = game.getGUIButtons().values();
        for (Sprite s : buttonSprites)
        {
            renderSprite(g, s);
        }
    }
    
    public void renderHeader(Graphics g)
    {
        g.setColor(COLOR_ALGORITHM_HEADER);
        
    }
    
    public void renderSnake(Graphics g)
    {
        ArrayList<SnakeCell> snake = data.getSnake();
        int red = 255;
        Viewport viewport = data.getViewport();
        for (SnakeCell sC : snake)
        {
            int x = data.calculateGridTileX(sC.col);
            int y = data.calculateGridTileY(sC.row);            
            g.setColor(new Color(0, 0, red, 200));
            g.fillRect(x, y, TILE_WIDTH, TILE_HEIGHT);
            red -= COLOR_INC;
            g.setColor(Color.BLACK);
            g.drawRect(x, y, TILE_WIDTH, TILE_HEIGHT);
        }
    }

    /**
     * This method renders the on-screen stats that change as
     * the game progresses. This means things like the game time
     * and the number of tiles remaining.
     * 
     * @param g the Graphics context for this panel
     */
    public void renderStats(Graphics g)
    {
        // RENDER THE GAME TIME AND THE TILES LEFT FOR IN-GAME
        if (((SortingHatMiniGame)game).isCurrentScreenState(GAME_SCREEN_STATE) 
                && data.inProgress() || data.isPaused())
        {
            // RENDER THE TILES LEFT
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(Color.BLACK);
            String tilesRemaining = "" + data.getBadSpellsCounter();
            int x = TILE_COUNT_X + TILE_COUNT_OFFSET;
            int y = TILE_COUNT_Y + TILE_TEXT_OFFSET;
            g.drawString(tilesRemaining , x, y);
        
            // RENDER THE TIME
            String time = data.gameTimeToText();
            x = TIME_X + TIME_OFFSET;
            y = TIME_Y + TIME_TEXT_OFFSET;
            g.drawString(time, x, y);
        }        
        
        // IF THE STATS DIALOG IS VISIBLE, ADD THE TEXTUAL STATS
        if (game.getGUIDialogs().get(STATS_DIALOG_TYPE).getState().equals(SortingHatTileState.VISIBLE_STATE.toString()))
        {
            g.setFont(FONT_STATS);
            g.setColor(COLOR_STATS);
            String currentLevel = data.getCurrentLevel();
            int lastSlash = currentLevel.lastIndexOf("/");
            String levelName = currentLevel.substring(lastSlash+1);
            SortingHatRecord record = ((SortingHatMiniGame)game).getPlayerRecord();

            // GET ALL THE STATS
            String algorithm = record.getAlgorithm(currentLevel);
            int games = record.getGamesPlayed(currentLevel);
            int wins = record.getWins(currentLevel);
            int perfectWins = record.getPerfectWins(currentLevel);
            long fastestPerfectWin = record.getFastestPerfectWinTime(currentLevel);
            String fastestText = data.timeToText(fastestPerfectWin);

            // GET ALL THE STATS PROMPTS
            PropertiesManager props = PropertiesManager.getPropertiesManager();            
            String algorithmPrompt = props.getProperty(SortingHatPropertyType.TEXT_LABEL_STATS_ALGORITHM);
            String gamesPrompt = props.getProperty(SortingHatPropertyType.TEXT_LABEL_STATS_GAMES);
            String winsPrompt = props.getProperty(SortingHatPropertyType.TEXT_LABEL_STATS_WINS);
            String perfectWinsPrompt = props.getProperty(SortingHatPropertyType.TEXT_LABEL_STATS_PERFECT_WINS);
            String fastestPerfectWinPrompt = props.getProperty(SortingHatPropertyType.TEXT_LABEL_STATS_FASTEST_PERFECT_WIN);

            // NOW DRAW ALL THE STATS WITH THEIR LABELS
            int dot = levelName.indexOf(".");
            levelName = levelName.substring(0, dot);
            g.drawString(levelName,                                     STATS_LEVEL_X, STATS_LEVEL_Y);
            g.drawString(algorithmPrompt + algorithm,                   STATS_LEVEL_X, STATS_ALGORITHM_Y);
            g.drawString(gamesPrompt + games,                           STATS_LEVEL_X, STATS_GAMES_Y);
            g.drawString(winsPrompt + wins,                             STATS_LEVEL_X, STATS_WINS_Y);
            g.drawString(perfectWinsPrompt + perfectWins,               STATS_LEVEL_X, STATS_PERFECT_WINS_Y);
            if (perfectWins > 0)
                g.drawString(fastestPerfectWinPrompt + fastestText,         STATS_LEVEL_X, STATS_FASTEST_PERFECT_WIN_Y);
        }
    }
        
    /**
     * Renders all the game tiles, doing so carefully such
     * that they are rendered in the proper order.
     * 
     * @param g the Graphics context of this panel.
     */
    public void renderTiles(Graphics g)
    {
        // DRAW THE GRID
        ArrayList<SortingHatTile> tilesToSort = data.getTilesToSort();
        for (int i = 0; i < tilesToSort.size(); i++)
        {
            SortingHatTile tile = tilesToSort.get(i);
            if (tile != null)
                renderTile(g, tile);
        }
        
        // THEN DRAW ALL THE MOVING TILES
        Iterator<SortingHatTile> movingTiles = data.getMovingTiles();
        while (movingTiles.hasNext())
        {
            SortingHatTile tile = movingTiles.next();
            renderTile(g, tile);
        }
        
        // AND THE SELECTED TILE, IF THERE IS ONE
        SortingHatTile selectedTile = data.getSelectedTile();
        if (selectedTile != null)
            renderTile(g, selectedTile);
    }

    /**
     * Helper method for rendering the tiles that are currently moving.
     * 
     * @param g Rendering context for this panel.
     * 
     * @param tileToRender Tile to render to this panel.
     */
    public void renderTile(Graphics g, SortingHatTile tileToRender)
    {
        // ONLY RENDER VISIBLE TILES
        if (!tileToRender.getState().equals(SortingHatTileState.INVISIBLE_STATE.toString()))
        {
            Viewport viewport = data.getViewport();
            int correctedTileX = (int)(tileToRender.getX());
            int correctedTileY = (int)(tileToRender.getY());

            // THEN THE TILE IMAGE
            SpriteType bgST = tileToRender.getSpriteType();
            Image img = bgST.getStateImage(tileToRender.getState());
            g.drawImage(img,    correctedTileX, 
                                correctedTileY, 
                                bgST.getWidth(), bgST.getHeight(), null); 
        }
    }
    
    /**
     * Renders the game dialog boxes.
     * 
     * @param g This panel's graphics context.
     */
    public void renderDialogs(Graphics g)
    {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> dialogSprites = game.getGUIDialogs().values();
        for (Sprite s : dialogSprites)
        {
            // RENDER THE DIALOG, NOTE IT WILL ONLY DO IT IF IT'S VISIBLE
            renderSprite(g, s);
        }
    }
    
    /**
     * Renders the s Sprite into the Graphics context g. Note
     * that each Sprite knows its own x,y coordinate location.
     * 
     * @param g the Graphics context of this panel
     * 
     * @param s the Sprite to be rendered
     */
    public void renderSprite(Graphics g, Sprite s)
    {
        // ONLY RENDER THE VISIBLE ONES
        if (!s.getState().equals(SortingHatTileState.INVISIBLE_STATE.toString()))
        {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int)s.getX(), (int)s.getY(), bgST.getWidth(), bgST.getHeight(), null); 
        }
    }

    /**
     * This method renders grid lines in the game tile grid to help
     * during debugging.
     * 
     * @param g Graphics context for this panel.
     */
    public void renderGrid(Graphics g)
    {
        // ONLY RENDER THE GRID IF WE'RE DEBUGGING
        if (data.isDebugTextRenderingActive())
        {
            for (int i = 0; i < data.getNumGameGridColumns(); i++)
            {
                for (int j = 0; j < data.getNumGameGridRows(); j++)
                {
                    int x = data.calculateGridTileX(i);
                    int y = data.calculateGridTileY(j);
                    g.drawRect(x, y, TILE_WIDTH, TILE_HEIGHT);
                }
            }
        }
    }
    
    /**
     * Renders the debugging text to the panel. Note
     * that the rendering will only actually be done
     * if data has activated debug text rendering.
     * 
     * @param g the Graphics context for this panel
     */
    public void renderDebuggingText(Graphics g)
    {
        // IF IT'S ACTIVATED
        if (data.isDebugTextRenderingActive())
        {
            // ENABLE PROPER RENDER SETTINGS
            g.setFont(FONT_DEBUG_TEXT);
            g.setColor(COLOR_DEBUG_TEXT);
            
            // GO THROUGH ALL THE DEBUG TEXT
            Iterator<String> it = data.getDebugText().iterator();
            int x = data.getDebugTextX();
            int y = data.getDebugTextY();
            while (it.hasNext())
            {
                // RENDER THE TEXT
                String text = it.next();
                g.drawString(text, x, y);
                y += 20;
            }   
        } 
    }
}