package PathX.ui;


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
import PathX.data.PathXDataModel;
import static PathX.PathXConstants.*;
import PathX.PathX.SortingHatPropertyType;
import PathX.PathX_Intersection;
import PathX.PathX_Road;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.HashMap;


/**
 * This class performs all of the rendering for The Sorting Hat game application.
 * 
 * @author Richard McKenna
 */
public class PathXPanel extends JPanel
{
    // THIS IS ACTUALLY OUR Sorting Hat APP, WE NEED THIS
    // BECAUSE IT HAS THE GUI STUFF THAT WE NEED TO RENDER
    private MiniGame game;
    
    // AND HERE IS ALL THE GAME DATA THAT WE NEED TO RENDER
    //private PathXDataModel data;
    private PathXDataModel model;
    
    //Manages portion of level to render 
    Viewport viewport;
    
    // WE'LL RECYCLE THESE DURING RENDERING
    Ellipse2D.Double recyclableCircle;
    Line2D.Double recyclableLine;
    HashMap<Integer, BasicStroke> recyclableStrokes;
    int triangleXPoints[] = {-ONE_WAY_TRIANGLE_WIDTH/2,  -ONE_WAY_TRIANGLE_WIDTH/2,  ONE_WAY_TRIANGLE_WIDTH/2};
    int triangleYPoints[] = {ONE_WAY_TRIANGLE_WIDTH/2, -ONE_WAY_TRIANGLE_WIDTH/2, 0};
    GeneralPath recyclableTriangle;
    
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
    public PathXPanel(MiniGame initGame, PathXDataModel initData)
    {
        game = initGame;
        model= initData;
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
            if (!model.notStarted())
            {
                // RENDER THE SNAKE
                if (!model.won())
                    //renderSnake(g);
                
                // AND THE TILES
                renderTiles(g);
                
                // AND THE DIALOGS, IF THERE ARE ANY
                renderDialogs(g);
                                
                // RENDERING THE GRID WHERE ALL THE TILES GO CAN BE HELPFUL
                // DURING DEBUGGIN TO BETTER UNDERSTAND HOW THEY RE LAID OUT
//                renderGrid(g);
                
                // RENDER THE ALGORITHM NAME
                renderHeader(g);
            }

            // AND THE BUTTONS AND DECOR
            renderGUIControls(g);
            
            if (!model.notStarted())
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

    // HELPER METHOD FOR RENDERING THE LEVEL BACKGROUND
    private void renderLevelBackground(Graphics2D g2)
    {
        Image backgroundImage = model.getBackgroundImage();
        g2.drawImage(backgroundImage, 0, 0, viewport.width, viewport.height, viewport.x, viewport.y, viewport.x + viewport.width, viewport.y + viewport.height, null);
    }

    // HELPER METHOD FOR RENDERING THE LEVEL ROADS
    private void renderRoads(Graphics2D g2)
    {
        // GO THROUGH THE ROADS AND RENDER ALL OF THEM
        Viewport viewport = model.getViewport();
        Iterator<PathX_Road> it = model.roadsIterator();
        g2.setStroke(recyclableStrokes.get(INT_STROKE));
        while (it.hasNext())
        {
            PathX_Road road = it.next();
            if (!model.isSelectedRoad(road))
                renderRoad(g2, road, INT_OUTLINE_COLOR);
        }
        
        // NOW DRAW THE LINE BEING ADDED, IF THERE IS ONE
        if (model.isAddingRoadEnd())
        {
            PathX_Intersection startRoadIntersection = model.getStartRoadIntersection();
            recyclableLine.x1 = startRoadIntersection.x-viewport.x;
            recyclableLine.y1 = startRoadIntersection.y-viewport.y;
            recyclableLine.x2 = model.getLastMouseX()-viewport.x;
            recyclableLine.y2 = model.getLastMouseY()-viewport.y;
            g2.draw(recyclableLine);
        }

        // AND RENDER THE SELECTED ONE, IF THERE IS ONE
        PathX_Road selectedRoad = model.getSelectedRoad();
        if (selectedRoad != null)
        {
            renderRoad(g2, selectedRoad, HIGHLIGHTED_COLOR);
        }
    }
    
    // HELPER METHOD FOR RENDERING A SINGLE ROAD
    private void renderRoad(Graphics2D g2, PathX_Road road, Color c)
    {
        g2.setColor(c);
        int strokeId = road.getSpeedLimit()/10;

        // CLAMP THE SPEED LIMIT STROKE
        if (strokeId < 1) strokeId = 1;
        if (strokeId > 10) strokeId = 10;
        g2.setStroke(recyclableStrokes.get(strokeId));

        // LOAD ALL THE DATA INTO THE RECYCLABLE LINE
        recyclableLine.x1 = road.getNode1().x-viewport.x;
        recyclableLine.y1 = road.getNode1().y-viewport.y;
        recyclableLine.x2 = road.getNode2().x-viewport.x;
        recyclableLine.y2 = road.getNode2().y-viewport.y;

        // AND DRAW IT
        g2.draw(recyclableLine);
        
        // AND IF IT'S A ONE WAY ROAD DRAW THE MARKER
        if (road.isOneWay())
        {
            this.renderOneWaySignalsOnRecyclableLine(g2);
        }
    }

    // HELPER METHOD FOR RENDERING AN INTERSECTION
    private void renderIntersections(Graphics2D g2)
    {
        Iterator<PathX_Intersection> it = model.intersectionsIterator();
        while (it.hasNext())
        {
            PathX_Intersection intersection = it.next();

            // ONLY RENDER IT THIS WAY IF IT'S NOT THE START OR DESTINATION
            // AND IT IS IN THE VIEWPORT
            if ((!model.isStartingLocation(intersection))
                    && (!model.isDestination(intersection))
                    && viewport.isCircleBoundingBoxInsideViewport(intersection.x, intersection.y, INTERSECTION_RADIUS))
            {
                // FIRST FILL
                if (intersection.isOpen())
                {
                    g2.setColor(OPEN_INT_COLOR);
                } else
                {
                    g2.setColor(CLOSED_INT_COLOR);
                }
                recyclableCircle.x = intersection.x - viewport.x - INTERSECTION_RADIUS;
                recyclableCircle.y = intersection.y - viewport.y - INTERSECTION_RADIUS;
                g2.fill(recyclableCircle);

                // AND NOW THE OUTLINE
                if (model.isSelectedIntersection(intersection))
                {
                    g2.setColor(HIGHLIGHTED_COLOR);
                } else
                {
                    g2.setColor(INT_OUTLINE_COLOR);
                }
                Stroke s = recyclableStrokes.get(INT_STROKE);
                g2.setStroke(s);
                g2.draw(recyclableCircle);
            }
        }

        // AND NOW RENDER THE START AND DESTINATION LOCATIONS
        Image startImage = model.getStartingLocationImage();
        PathX_Intersection startInt = model.getStartingLocation();
        renderIntersectionImage(g2, startImage, startInt);

        Image destImage = model.getDesinationImage();
        PathX_Intersection destInt = model.getDestination();
        renderIntersectionImage(g2, destImage, destInt);
    }

    // HELPER METHOD FOR RENDERING AN IMAGE AT AN INTERSECTION, WHICH IS
    // NEEDED BY THE STARTING LOCATION AND THE DESTINATION
    private void renderIntersectionImage(Graphics2D g2, Image img, PathX_Intersection i)
    {
        // CALCULATE WHERE TO RENDER IT
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        int x1 = i.x-(w/2);
        int y1 = i.y-(h/2);
        int x2 = x1 + img.getWidth(null);
        int y2 = y1 + img.getHeight(null);
        
        // ONLY RENDER IF INSIDE THE VIEWPORT
        if (viewport.isRectInsideViewport(x1, y1, x2, y2))
        {
            g2.drawImage(img, x1 - viewport.x, y1 - viewport.y, null);
        }        
    }
    
    private void renderOneWaySignalsOnRecyclableLine(Graphics2D g2)
    {
        // CALCULATE THE ROAD LINE SLOPE
        double diffX = recyclableLine.x2 - recyclableLine.x1;
        double diffY = recyclableLine.y2 - recyclableLine.y1;
        double slope = diffY/diffX;
        
        // AND THEN FIND THE LINE MIDPOINT
        double midX = (recyclableLine.x1 + recyclableLine.x2)/2.0;
        double midY = (recyclableLine.y1 + recyclableLine.y2)/2.0;
        
        // GET THE RENDERING TRANSFORM, WE'LL RETORE IT BACK
        // AT THE END
        AffineTransform oldAt = g2.getTransform();
        
        // CALCULATE THE ROTATION ANGLE
        double theta = Math.atan(slope);
        if (recyclableLine.x2 < recyclableLine.x1)
            theta = (theta + Math.PI);
        
        // MAKE A NEW TRANSFORM FOR THIS TRIANGLE AND SET IT
        // UP WITH WHERE WE WANT TO PLACE IT AND HOW MUCH WE
        // WANT TO ROTATE IT
        AffineTransform at = new AffineTransform();        
        at.setToIdentity();
        at.translate(midX, midY);
        at.rotate(theta);
        g2.setTransform(at);
        
        // AND RENDER AS A SOLID TRIANGLE
        g2.fill(recyclableTriangle);
        
        // RESTORE THE OLD TRANSFORM SO EVERYTHING DOESN'T END UP ROTATED 0
        g2.setTransform(oldAt);
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
            if ((s.getSpriteType().getSpriteTypeID() != BACKGROUND_TYPE) ||
                (s.getSpriteType().getSpriteTypeID() != MAP) ||
                (s.getSpriteType().getSpriteTypeID() != LEVEL_PLAY_MAP)) {   
                   //renderSprite(g, s);
            }
        }
        renderMapComponment(g, game.getGUIDecor().get(MAP));
        
        renderLevelPlayComponent(g, game.getGUIDecor().get(LEVEL_PLAY_MAP));
        
        
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
    
    // For the ViewPort of the map
    public void renderMapComponment(Graphics g , Sprite s){
        Viewport view = model.getViewport();
        if(s.getState().equals(PathXButtonState.VISIBLE_STATE.toString())){
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img,0,158,1272,697,view.getViewportX(),view.getViewportY(),view.getViewportX()+1275,view.getViewportY()+543,null);
        }
    }
    
    //For the ViewPort of the level loaded
    public void renderLevelPlayComponent(Graphics g , Sprite s){
        
        Viewport view = model.getViewport();
        if(s.getState().equals(PathXButtonState.VISIBLE_STATE.toString())){
             SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, 500,58,1260,700,view.getViewportX(),view.getViewportY(),view.getViewportX()+1275,view.getViewportY()+543,null);
        }
    }
    
//    public void renderSnake(Graphics g)
//    {
//        ArrayList<SnakeCell> snake = data.getSnake();
//        int red = 255;
//        Viewport viewport = data.getViewport();
//        for (SnakeCell sC : snake)
//        {
//            int x = data.calculateGridTileX(sC.col);
//            int y = data.calculateGridTileY(sC.row);            
//            g.setColor(new Color(0, 0, red, 200));
//            g.fillRect(x, y, TILE_WIDTH, TILE_HEIGHT);
//            red -= COLOR_INC;
//            g.setColor(Color.BLACK);
//            g.drawRect(x, y, TILE_WIDTH, TILE_HEIGHT);
//        }
//    }

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
        if (((PathXMiniGame)game).isCurrentScreenState(GAME_SCREEN_STATE) 
                && model.inProgress() || model.isPaused())
        {
            // RENDER THE TILES LEFT
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(Color.BLACK);
            String tilesRemaining = "" + model.getBadSpellsCounter();
            int x = TILE_COUNT_X + TILE_COUNT_OFFSET;
            int y = TILE_COUNT_Y + TILE_TEXT_OFFSET;
            g.drawString(tilesRemaining , x, y);
        
            // RENDER THE TIME
         //   String time = data.gameTimeToText();
            x = TIME_X + TIME_OFFSET;
            y = TIME_Y + TIME_TEXT_OFFSET;
         //   g.drawString(time, x, y);
        }        
        
        // IF THE STATS DIALOG IS VISIBLE, ADD THE TEXTUAL STATS
        if (game.getGUIDialogs().get(STATS_DIALOG_TYPE).getState().equals(PathXButtonState.VISIBLE_STATE.toString()))
        {
            g.setFont(FONT_STATS);
            g.setColor(COLOR_STATS);
          //  String currentLevel = data.getCurrentLevel();
           // int lastSlash = currentLevel.lastIndexOf("/");
           // String levelName = currentLevel.substring(lastSlash+1);
           // SortingHatRecord record = ((PathXMiniGame)game).getPlayerRecord();

            // GET ALL THE STATS
         //   String algorithm = record.getAlgorithm(currentLevel);
           // int games = record.getGamesPlayed(currentLevel);
           // int wins = record.getWins(currentLevel);
            //int perfectWins = record.getPerfectWins(currentLevel);
            //long fastestPerfectWin = record.getFastestPerfectWinTime(currentLevel);
         //   String fastestText = data.timeToText(fastestPerfectWin);

            // GET ALL THE STATS PROMPTS
            PropertiesManager props = PropertiesManager.getPropertiesManager();            
            String algorithmPrompt = props.getProperty(SortingHatPropertyType.TEXT_LABEL_STATS_ALGORITHM);
            String gamesPrompt = props.getProperty(SortingHatPropertyType.TEXT_LABEL_STATS_GAMES);
            String winsPrompt = props.getProperty(SortingHatPropertyType.TEXT_LABEL_STATS_WINS);
            String perfectWinsPrompt = props.getProperty(SortingHatPropertyType.TEXT_LABEL_STATS_PERFECT_WINS);
            String fastestPerfectWinPrompt = props.getProperty(SortingHatPropertyType.TEXT_LABEL_STATS_FASTEST_PERFECT_WIN);

            // NOW DRAW ALL THE STATS WITH THEIR LABELS
          //  int dot = levelName.indexOf(".");
           // levelName = levelName.substring(0, dot);
          //  g.drawString(levelName,                                     STATS_LEVEL_X, STATS_LEVEL_Y);
//            g.drawString(algorithmPrompt + algorithm,                   STATS_LEVEL_X, STATS_ALGORITHM_Y);
//            g.drawString(gamesPrompt + games,                           STATS_LEVEL_X, STATS_GAMES_Y);
//            g.drawString(winsPrompt + wins,                             STATS_LEVEL_X, STATS_WINS_Y);
//            g.drawString(perfectWinsPrompt + perfectWins,               STATS_LEVEL_X, STATS_PERFECT_WINS_Y);
//            if (perfectWins > 0)
//                g.drawString(fastestPerfectWinPrompt + fastestText,         STATS_LEVEL_X, STATS_FASTEST_PERFECT_WIN_Y);
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
        ArrayList<PathXTile> tilesToSort = model.getTilesToSort();
        for (int i = 0; i < tilesToSort.size(); i++)
        {
            PathXTile tile = tilesToSort.get(i);
            if (tile != null)
                renderTile(g, tile);
        }
        
        // THEN DRAW ALL THE MOVING TILES
        Iterator<PathXTile> movingTiles = model.getMovingTiles();
        while (movingTiles.hasNext())
        {
            PathXTile tile = movingTiles.next();
            renderTile(g, tile);
        }
        
        // AND THE SELECTED TILE, IF THERE IS ONE
        PathXTile selectedTile = model.getSelectedTile();
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
    public void renderTile(Graphics g, PathXTile tileToRender)
    {
        // ONLY RENDER VISIBLE TILES
        if (!tileToRender.getState().equals(PathXButtonState.INVISIBLE_STATE.toString()))
        {
            Viewport viewport = model.getViewport();
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
        if (!s.getState().equals(PathXButtonState.INVISIBLE_STATE.toString()))
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
//    public void renderGrid(Graphics g)
//    {
//        // ONLY RENDER THE GRID IF WE'RE DEBUGGING
//        if (data.isDebugTextRenderingActive())
//        {
//            for (int i = 0; i < data.getNumGameGridColumns(); i++)
//            {
//                for (int j = 0; j < data.getNumGameGridRows(); j++)
//                {
//                    int x = data.calculateGridTileX(i);
//                    int y = data.calculateGridTileY(j);
//                    g.drawRect(x, y, TILE_WIDTH, TILE_HEIGHT);
//                }
//            }
//        }
//    }
    
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
        if (model.isDebugTextRenderingActive())
        {
            // ENABLE PROPER RENDER SETTINGS
            g.setFont(FONT_DEBUG_TEXT);
            g.setColor(COLOR_DEBUG_TEXT);
            
            // GO THROUGH ALL THE DEBUG TEXT
            Iterator<String> it = model.getDebugText().iterator();
            int x = model.getDebugTextX();
            int y = model.getDebugTextY();
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