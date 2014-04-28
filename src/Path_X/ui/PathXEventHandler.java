package Path_X.ui;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;
import mini_game.Sprite;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;
import static Path_X.PathXConstants.GAME_SCREEN_STATE;
import static Path_X.PathXConstants.LEVEL_PLAY_SCREEN_STATE;
import static Path_X.PathXConstants.MENU_SCREEN_STATE;
import static Path_X.PathXConstants.VIEWPORT_INC;
import Path_X.PathX;
import Path_X.data.SortTransaction;
import Path_X.data.PathXDataModel;
import Path_X.file.PathXFileManager;

/**
 *
 * @author Richard McKenna & _____________________
 */
public class PathXEventHandler
{
    // THE SORTING HAT GAME, IT PROVIDES ACCESS TO EVERYTHING
    private PathXMiniGame game;
    private Component frame;
    private String currentScreenState;

    /**
     * Constructor, it just keeps the game for when the events happen.
     */
    public PathXEventHandler(PathXMiniGame initGame)
    {
        game = initGame;
    }

    /**
     * Called when the user clicks the Back button.
     */
    public void respondToBackRequest()
    {
        // IF A GAME IS IN PROGRESS, THE PLAYER LOSES
//        if (game.getDataModel().inProgress())
//            game.getDataModel().endGameAsLoss();
        
        // SWITCH BACK TO THE MENU SCREEN SO THE 
        // USER MAY CHOOSE ANOTHER LEVEL
        if(game.isCurrentScreenState(LEVEL_PLAY_SCREEN_STATE)){
            game.switchToGameScreen();
        }else game.switchToSplashScreen();        
    }

    /**
     * Called when the user clicks the close window button.
     */    
    public void respondToExitRequest()
    {
        // IF THE GAME IS STILL GOING ON, END IT AS A LOSS
        if (game.getDataModel().inProgress())
        {
            game.getDataModel().endGameAsLoss();
        }
        // AND CLOSE THE ALL
        System.exit(0);        
    }

    /**
     * Called when the user clicks the New button.
     */
    public void respondToNewGameRequest()
    {
        // IF THERE IS A GAME UNDERWAY, COUNT IT AS A LOSS
        if (game.getDataModel().inProgress())
        {
            game.getDataModel().endGameAsLoss();
        }
        // RESET THE GAME AND ITS DATA
        game.reset();        
    }
    
    /**
     * Called when the user clicks the play button
    */
    public void respondToPlayButton(){
        game.switchToGameScreen();
    }

    /**
     * Called when the user clicks the reset button
     */
    public void respondToResetButton(){
        
    }
    
    /**
     * Called when the user clicks the settings button
     */
    public void respondToSettingsButton(){
        game.switchToSettingsScreen();
    }
    
    /**
     * Called when the user clicks the help button
     */
    public void respondToHelpButton(){
        game.switchToHelpScreen();
    }
    
    /**
     * Called when the user clicks the exit button
     */
    public void respondToExitButton(){
        //Prompt the user if they want to exit .
        // first need to save the game in progress if there is one in progress.
        System.exit(0);
    }
    
    /**
     * Called when the user clicks the left button
     */
    public void respondToLeftButton(){
        
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        //
        data.getViewport().scroll(VIEWPORT_INC,     0);
    }
    
    /**
     * Called when the user clicks the right button
     */
    public void respondToRightButton(){
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        data.getViewport().scroll(-VIEWPORT_INC,    0);
    }
    
    /**
     * Called when the user clicks the up button
     */
    public void respondToUpButton(){
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        
        data.getViewport().scroll(0,                VIEWPORT_INC);
    }
    
    /**
     * Called when the user clicks the down button
     */
    public void respondToDownButton(){
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        if(data.getViewport().getViewportY()==300){
            return;
        }
        data.getViewport().scroll(0,                -VIEWPORT_INC);

        
    }
    
    /**
     * Called when the user clicks the sound button
     */
    public void respondToSoundButtonPress(){
//        sT = new SpriteType("UP_BUTTON");
//        img = loadImage("img/sorting_hat/UpButton.png");
    }
    
    /**
     * Called when the user clicks a button to select a level.
     */    
    public void respondToMusicButtonPress(){
        
    }
    
    /**
     * Called when the user clicks the level button
     * 
     */
    public void respondToLevelButtonPress(){
        game.switchToLevelPlayScreen();
    }
    
    /**
     * Called when the user presses the pause button
     */
    public void respondToPauseButtonPress(){
        // check to see if the game is play or pause. if in play then pause it and vice versa
    }
    
    /**
     * Called when the start button is pressed in level play screen
     */
    public void respondToStartButtonPress(){
        // start the level playing
    }
    
    /**
     * C
     * 
     */
    public void respondToSelectLevelRequest(String levelFile)
    {
        // WE ONLY LET THIS HAPPEN IF THE MENU SCREEN IS VISIBLE
        if (game.isCurrentScreenState(MENU_SCREEN_STATE))
        {
            // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
            PathXDataModel data = (PathXDataModel)game.getDataModel();
        
            // UPDATE THE DATA
            PathXFileManager fileManager = game.getFileManager();
            fileManager.loadLevel(levelFile);
            data.reset(game);

            // GO TO THE GAME
            game.switchToGameScreen();
        }        
    }

    /**
     * Called when the user clicks the Stats button.
     */
    public void respondToDisplayStatsRequest()
    {
        // DISPLAY THE STATS
        game.displayStats();
    }
    
    
    /**
     * Called when the user clicks the Undo button.
     */    
    public void respondToUndoRequest()
    {
        // THIS IS ONLY RELEVANT TO THE IN-GAME STATE
        if (game.isCurrentScreenState(GAME_SCREEN_STATE))
        {
            PathXDataModel data = (PathXDataModel)game.getDataModel();
            data.undoLastMove();
        }        
    }

    /**
     * Called when the user presses a key on the keyboard.
     */    
    public void respondToKeyPress(int keyCode)
    {
        PathXDataModel data = (PathXDataModel)game.getDataModel();

        // CHEAT BY ONE MOVE. NOTE THAT IF WE HOLD THE C
        // KEY DOWN IT WILL CONTINUALLY CHEAT
        if (keyCode == KeyEvent.VK_C)
        {            
            // ONLY DO THIS IF THE GAME IS NO OVER
            if (data.inProgress())
            {
                // FIND A MOVE IF THERE IS ONE
                SortTransaction move = data.getNextSwapTransaction();
                if (move != null)
                {
                    data.swapTiles(move.getFromIndex(), move.getToIndex());
                    game.getAudio().play(PathX.SortingHatPropertyType.AUDIO_CUE_CHEAT.toString(), false);
                }
            }
        }
        // UNDO THE LAST MOVE, AGAIN, U CAN BE HELD DOWN
        else if (keyCode == KeyEvent.VK_U)
        {
            // UNDO IS ONLY RELEVANT TO THE GAME SCREEN
            if (game.isCurrentScreenState(GAME_SCREEN_STATE))
            {
                data.undoLastMove();
                game.getAudio().play(PathX.SortingHatPropertyType.AUDIO_CUE_UNDO.toString(), false);                 
            }            
        }
        // WASD MOVES THE VIEWPORT
        else if (keyCode == KeyEvent.VK_DOWN)  data.getViewport().scroll(0,                -VIEWPORT_INC);
        else if (keyCode == KeyEvent.VK_RIGHT)  data.getViewport().scroll(-VIEWPORT_INC,    0);
        else if (keyCode == KeyEvent.VK_UP)  data.getViewport().scroll(0,                VIEWPORT_INC);
        else if (keyCode == KeyEvent.VK_LEFT)  data.getViewport().scroll(VIEWPORT_INC,     0);
    }
}