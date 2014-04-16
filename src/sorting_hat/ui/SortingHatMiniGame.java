package sorting_hat.ui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import sorting_hat.data.SortingHatDataModel;
import mini_game.MiniGame;
import mini_game.MiniGameState;
import static sorting_hat.SortingHatConstants.*;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import properties_manager.PropertiesManager;
import sorting_hat.SortingHatConstants;
import sorting_hat.TheSortingHat.SortingHatPropertyType;
import sorting_hat.file.SortingHatFileManager;
import sorting_hat.data.SortingHatRecord;

/**
 * This is the actual mini game, as extended from the mini game framework. It
 * manages all the UI elements.
 *
 * @author Richard McKenna
 */
public class SortingHatMiniGame extends MiniGame {

    // THE PLAYER RECORD FOR EACH LEVEL, WHICH LIVES BEYOND ONE SESSION
    private SortingHatRecord record;

    // HANDLES GAME UI EVENTS
    private SortingHatEventHandler eventHandler;

    // HANDLES ERROR CONDITIONS
    private SortingHatErrorHandler errorHandler;

    // MANAGES LOADING OF LEVELS AND THE PLAYER RECORDS FILES
    private SortingHatFileManager fileManager;

    // THE SCREEN CURRENTLY BEING PLAYED
    private String currentScreenState;
    private Sprite s;
    private SpriteType sT;
   
    // ACCESSOR METHODS
    // - getPlayerRecord
    // - getErrorHandler
    // - getFileManager
    // - isCurrentScreenState
    /**
     * Accessor method for getting the player record object, which summarizes
     * the player's record on all levels.
     *
     * @return The player's complete record.
     */
    public SortingHatRecord getPlayerRecord() {
        return record;
    }

    /**
     * Accessor method for getting the application's error handler.
     *
     * @return The error handler.
     */
    public SortingHatErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /**
     * Accessor method for getting the app's file manager.
     *
     * @return The file manager.
     */
    public SortingHatFileManager getFileManager() {
        return fileManager;
    }

    /**
     * Used for testing to see if the current screen state matches the
     * testScreenState argument. If it mates, true is returned, else false.
     *
     * @param testScreenState Screen state to test against the current state.
     *
     * @return true if the current state is testScreenState, false otherwise.
     */
    public boolean isCurrentScreenState(String testScreenState) {
        return testScreenState.equals(currentScreenState);
    }

    // VIEWPORT UPDATE METHODS
    // - initViewport
    // - scroll
    // - moveViewport
    // SERVICE METHODS
    // - displayStats
    // - savePlayerRecord
    // - switchToGameScreen
    // - switchToSplashScreen
    // - updateBoundaries
    /**
     * This method displays makes the stats dialog display visible, which
     * includes the text inside.
     */
    public void displayStats() {
        // MAKE SURE ONLY THE PROPER DIALOG IS VISIBLE
        //guiDialogs.get(WIN_DIALOG_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        // guiDialogs.get(STATS_DIALOG_TYPE).setState(SortingHatTileState.VISIBLE_STATE.toString());
    }

    /**
     * This method forces the file manager to save the current player record.
     */
    public void savePlayerRecord() {
        fileManager.saveRecord(record);
    }

    /**
     * This method switches the application to the game screen, making all the
     * appropriate UI controls visible & invisible.
     */
    public void switchToGameScreen() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(GAME_SCREEN_STATE);

        // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(BACK_BUTTON_TYPE).setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(true);
        //  guiDecor.get(MISCASTS_COUNT_TYPE).setState(SortingHatTileState.VISIBLE_STATE.toString());
        //  guiDecor.get(TIME_TYPE).setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get(STATS_BUTTON_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(STATS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(UNDO_BUTTON_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(UNDO_BUTTON_TYPE).setEnabled(false);
        // guiDecor.get(ALGORITHM_TYPE).setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get("PLAY_BUTTON_TYPE").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("PLAY_BUTTON_TYPE").setEnabled(false);
        guiButtons.get("RESET_BUTTON_TYPE").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("RESET_BUTTON_TYPE").setEnabled(false);
        guiButtons.get("SETTINGS_BUTTON_TYPE").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("SETTINGS_BUTTON_TYPE").setEnabled(false);
        guiButtons.get("HELP_BUTTON_TYPE").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("HELP_BUTTON_TYPE").setEnabled(false);
        guiButtons.get("LEFT_BUTTON").setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get("LEFT_BUTTON").setEnabled(true);
        guiButtons.get("RIGHT_BUTTON").setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get("RIGHT_BUTTON").setEnabled(true);
        guiButtons.get("UP_BUTTON").setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get("UP_BUTTON").setEnabled(true);
        guiButtons.get("DOWN_BUTTON").setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get("DOWN_BUTTON").setEnabled(true);
        guiButtons.get("SOUND_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("SOUND_BUTTON").setEnabled(false);
        guiButtons.get("MUSIC_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("MUSIC_BUTTON").setEnabled(false);

        guiButtons.get("LEVEL_BUTTON").setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get("LEVEL_BUTTON").setEnabled(true);
        // DEACTIVATE THE LEVEL SELECT BUTTONS
        ArrayList<String> levels = props.getPropertyOptionsList(SortingHatPropertyType.LEVEL_OPTIONS);
        for (String level : levels) {
//            guiButtons.get(level).setState(SortingHatTileState.INVISIBLE_STATE.toString());
            //         guiButtons.get(level).setEnabled(false);
        }

        // AND CHANGE THE SCREEN STATE
        currentScreenState = GAME_SCREEN_STATE;

        // PLAY THE GAMEPLAY SCREEN SONG
        //   audio.stop(SortingHatPropertyType.SONG_CUE_MENU_SCREEN.toString()); 
        // audio.play(SortingHatPropertyType.SONG_CUE_GAME_SCREEN.toString(), true);        
    }

    /**
     * This method switches the application to the menu screen, making all the
     * appropriate UI controls visible & invisible.
     */
    public void switchToSplashScreen() {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(MENU_SCREEN_STATE);

        // DEACTIVATE THE TOOLBAR CONTROLS
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(BACK_BUTTON_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(false);
        //  guiDecor.get(MISCASTS_COUNT_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        //  guiDecor.get(TIME_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(STATS_BUTTON_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(STATS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(UNDO_BUTTON_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(UNDO_BUTTON_TYPE).setEnabled(false);
        //    guiDecor.get(ALGORITHM_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("PLAY_BUTTON_TYPE").setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get("PLAY_BUTTON_TYPE").setEnabled(true);
        guiButtons.get("RESET_BUTTON_TYPE").setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get("RESET_BUTTON_TYPE").setEnabled(true);
        guiButtons.get("SETTINGS_BUTTON_TYPE").setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get("SETTINGS_BUTTON_TYPE").setEnabled(true);
        guiButtons.get("HELP_BUTTON_TYPE").setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get("HELP_BUTTON_TYPE").setEnabled(true);
        guiButtons.get("EXIT_BUTTON").setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get("EXIT_BUTTON").setEnabled(true);
        guiButtons.get("LEFT_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("LEFT_BUTTON").setEnabled(false);
        guiButtons.get("RIGHT_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("RIGHT_BUTTON").setEnabled(false);
        guiButtons.get("UP_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("UP_BUTTON").setEnabled(false);
        guiButtons.get("DOWN_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("DOWN_BUTTON").setEnabled(false);
        guiButtons.get("SOUND_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("SOUND_BUTTON").setEnabled(false);
        guiButtons.get("MUSIC_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("MUSIC_BUTTON").setEnabled(false);
        guiButtons.get("LEVEL_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("LEVEL_BUTTON").setEnabled(false);

        // ACTIVATE THE LEVEL SELECT BUTTONS
        // DEACTIVATE THE LEVEL SELECT BUTTONS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(SortingHatPropertyType.LEVEL_OPTIONS);
        for (String level : levels) {
            //       guiButtons.get(level).setState(SortingHatTileState.VISIBLE_STATE.toString());
            //       guiButtons.get(level).setEnabled(true);
        }

        // DEACTIVATE ALL DIALOGS
        //    guiDialogs.get(WIN_DIALOG_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        //   guiDialogs.get(STATS_DIALOG_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        // HIDE THE TILES
        ((SortingHatDataModel) data).enableTiles(false);

        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = MENU_SCREEN_STATE;

        // AND UPDATE THE DATA GAME STATE
        data.setGameState(MiniGameState.NOT_STARTED);

        // PLAY THE WELCOME SCREEN SONG
        //  audio.play(SortingHatPropertyType.SONG_CUE_MENU_SCREEN.toString(), true); 
        //  audio.stop(SortingHatPropertyType.SONG_CUE_GAME_SCREEN.toString());
    }

    public void switchToHelpScreen() {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(HELP_SCREEN_STATE);

        // DEACTIVATE THE TOOLBAR CONTROLS
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(BACK_BUTTON_TYPE).setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(true);
        //  guiDecor.get(MISCASTS_COUNT_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        //  guiDecor.get(TIME_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(STATS_BUTTON_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(STATS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(UNDO_BUTTON_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(UNDO_BUTTON_TYPE).setEnabled(false);
        //    guiDecor.get(ALGORITHM_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("PLAY_BUTTON_TYPE").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("PLAY_BUTTON_TYPE").setEnabled(false);
        guiButtons.get("RESET_BUTTON_TYPE").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("RESET_BUTTON_TYPE").setEnabled(false);
        guiButtons.get("SETTINGS_BUTTON_TYPE").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("SETTINGS_BUTTON_TYPE").setEnabled(false);
        guiButtons.get("HELP_BUTTON_TYPE").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("HELP_BUTTON_TYPE").setEnabled(false);
        guiButtons.get("LEFT_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("LEFT_BUTTON").setEnabled(false);
        guiButtons.get("RIGHT_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("RIGHT_BUTTON").setEnabled(false);
        guiButtons.get("UP_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("UP_BUTTON").setEnabled(false);
        guiButtons.get("DOWN_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("DOWN_BUTTON").setEnabled(false);
        guiButtons.get("SOUND_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("SOUND_BUTTON").setEnabled(false);
        guiButtons.get("MUSIC_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("MUSIC_BUTTON").setEnabled(false);
        guiButtons.get("LEVEL_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("LEVEL_BUTTON").setEnabled(false);

        // ACTIVATE THE LEVEL SELECT BUTTONS
        // DEACTIVATE THE LEVEL SELECT BUTTONS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(SortingHatPropertyType.LEVEL_OPTIONS);
        for (String level : levels) {
            //       guiButtons.get(level).setState(SortingHatTileState.VISIBLE_STATE.toString());
            //       guiButtons.get(level).setEnabled(true);
        }

        // DEACTIVATE ALL DIALOGS
        //    guiDialogs.get(WIN_DIALOG_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        //   guiDialogs.get(STATS_DIALOG_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        // HIDE THE TILES
        ((SortingHatDataModel) data).enableTiles(false);

        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = HELP_SCREEN_STATE;

    }

    public void switchToSettingsScreen() {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(SETTINGS_SCREEN_STATE);

        // DEACTIVATE THE TOOLBAR CONTROLS
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(BACK_BUTTON_TYPE).setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(true);
        //  guiDecor.get(MISCASTS_COUNT_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        //  guiDecor.get(TIME_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(STATS_BUTTON_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(STATS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(UNDO_BUTTON_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(UNDO_BUTTON_TYPE).setEnabled(false);
        //    guiDecor.get(ALGORITHM_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("PLAY_BUTTON_TYPE").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("PLAY_BUTTON_TYPE").setEnabled(false);
        guiButtons.get("RESET_BUTTON_TYPE").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("RESET_BUTTON_TYPE").setEnabled(false);
        guiButtons.get("SETTINGS_BUTTON_TYPE").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("SETTINGS_BUTTON_TYPE").setEnabled(false);
        guiButtons.get("HELP_BUTTON_TYPE").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("HELP_BUTTON_TYPE").setEnabled(false);
        guiButtons.get("LEFT_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("LEFT_BUTTON").setEnabled(false);
        guiButtons.get("RIGHT_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("RIGHT_BUTTON").setEnabled(false);
        guiButtons.get("UP_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("UP_BUTTON").setEnabled(false);
        guiButtons.get("DOWN_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("DOWN_BUTTON").setEnabled(false);
        guiButtons.get("SOUND_BUTTON").setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get("SOUND_BUTTON").setEnabled(true);
        guiButtons.get("MUSIC_BUTTON").setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get("MUSIC_BUTTON").setEnabled(true);
         guiButtons.get("LEVEL_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("LEVEL_BUTTON").setEnabled(false);

        // ACTIVATE THE LEVEL SELECT BUTTONS
        // DEACTIVATE THE LEVEL SELECT BUTTONS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(SortingHatPropertyType.LEVEL_OPTIONS);
        for (String level : levels) {
            //       guiButtons.get(level).setState(SortingHatTileState.VISIBLE_STATE.toString());
            //       guiButtons.get(level).setEnabled(true);
        }

        // DEACTIVATE ALL DIALOGS
        //    guiDialogs.get(WIN_DIALOG_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        //   guiDialogs.get(STATS_DIALOG_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        // HIDE THE TILES
        ((SortingHatDataModel) data).enableTiles(false);

        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = SETTINGS_SCREEN_STATE;

    }
    
    public void switchToLevelPlayScreen() {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(LEVEL_PLAY_SCREEN_STATE);

        // DEACTIVATE THE TOOLBAR CONTROLS
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(BACK_BUTTON_TYPE).setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(true);
        //  guiDecor.get(MISCASTS_COUNT_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        //  guiDecor.get(TIME_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(STATS_BUTTON_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(STATS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(UNDO_BUTTON_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get(UNDO_BUTTON_TYPE).setEnabled(false);
        //    guiDecor.get(ALGORITHM_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("PLAY_BUTTON_TYPE").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("PLAY_BUTTON_TYPE").setEnabled(false);
        guiButtons.get("RESET_BUTTON_TYPE").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("RESET_BUTTON_TYPE").setEnabled(false);
        guiButtons.get("SETTINGS_BUTTON_TYPE").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("SETTINGS_BUTTON_TYPE").setEnabled(false);
        guiButtons.get("HELP_BUTTON_TYPE").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("HELP_BUTTON_TYPE").setEnabled(false);
        guiButtons.get("LEFT_BUTTON").setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get("LEFT_BUTTON").setEnabled(true);
        guiButtons.get("RIGHT_BUTTON").setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get("RIGHT_BUTTON").setEnabled(true);
        guiButtons.get("UP_BUTTON").setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get("UP_BUTTON").setEnabled(true);
        guiButtons.get("DOWN_BUTTON").setState(SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.get("DOWN_BUTTON").setEnabled(true);
        guiButtons.get("SOUND_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("SOUND_BUTTON").setEnabled(false);
        guiButtons.get("MUSIC_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("MUSIC_BUTTON").setEnabled(false);
        guiButtons.get("LEVEL_BUTTON").setState(SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.get("LEVEL_BUTTON").setEnabled(false);
        
        
        
        // ACTIVATE THE LEVEL SELECT BUTTONS
        // DEACTIVATE THE LEVEL SELECT BUTTONS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(SortingHatPropertyType.LEVEL_OPTIONS);
        for (String level : levels) {
            //       guiButtons.get(level).setState(SortingHatTileState.VISIBLE_STATE.toString());
            //       guiButtons.get(level).setEnabled(true);
        }

        // DEACTIVATE ALL DIALOGS
        //    guiDialogs.get(WIN_DIALOG_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        //   guiDialogs.get(STATS_DIALOG_TYPE).setState(SortingHatTileState.INVISIBLE_STATE.toString());
        // HIDE THE TILES
        ((SortingHatDataModel) data).enableTiles(false);

        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = LEVEL_PLAY_SCREEN_STATE;

    }

    // METHODS OVERRIDDEN FROM MiniGame
    // - initAudioContent
    // - initData
    // - initGUIControls
    // - initGUIHandlers
    // - reset
    // - updateGUI
    @Override
    /**
     * Initializes the sound and music to be used by the application.
     */
    public void initAudioContent() {
        try {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String audioPath = props.getProperty(SortingHatPropertyType.PATH_AUDIO);

            // LOAD ALL THE AUDIO
            loadAudioCue(SortingHatPropertyType.AUDIO_CUE_SELECT_TILE);
            loadAudioCue(SortingHatPropertyType.AUDIO_CUE_DESELECT_TILE);
            loadAudioCue(SortingHatPropertyType.AUDIO_CUE_GOOD_MOVE);
            loadAudioCue(SortingHatPropertyType.AUDIO_CUE_BAD_MOVE);
            loadAudioCue(SortingHatPropertyType.AUDIO_CUE_CHEAT);
            loadAudioCue(SortingHatPropertyType.AUDIO_CUE_UNDO);
            loadAudioCue(SortingHatPropertyType.AUDIO_CUE_WIN);
            loadAudioCue(SortingHatPropertyType.SONG_CUE_MENU_SCREEN);
            loadAudioCue(SortingHatPropertyType.SONG_CUE_GAME_SCREEN);

            // PLAY THE WELCOME SCREEN SONG
            audio.play(SortingHatPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InvalidMidiDataException | MidiUnavailableException e) {
            errorHandler.processError(SortingHatPropertyType.TEXT_ERROR_LOADING_AUDIO);
        }
    }

    /**
     * This helper method loads the audio file associated with audioCueType,
     * which should have been specified via an XML properties file.
     */
    private void loadAudioCue(SortingHatPropertyType audioCueType)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException,
            InvalidMidiDataException, MidiUnavailableException {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String audioPath = props.getProperty(SortingHatPropertyType.PATH_AUDIO);
        String cue = props.getProperty(audioCueType.toString());
        audio.loadAudio(audioCueType.toString(), audioPath + cue);
    }

    /**
     * Initializes the game data used by the application. Note that it is this
     * method's obligation to construct and set this Game's custom GameDataModel
     * object as well as any other needed game objects.
     */
    @Override
    public void initData() {
        // INIT OUR ERROR HANDLER
        errorHandler = new SortingHatErrorHandler(window);

        // INIT OUR FILE MANAGER
        fileManager = new SortingHatFileManager(this);

        // LOAD THE PLAYER'S RECORD FROM A FILE
        record = fileManager.loadRecord();

        // INIT OUR DATA MANAGER
        data = new SortingHatDataModel(this);
    }

    /**
     * Initializes the game controls, like buttons, used by the game
     * application. Note that this includes the tiles, which serve as buttons of
     * sorts.
     */
    @Override
    public void initGUIControls() {
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        float x, y;
        

        // FIRST PUT THE ICON IN THE WINDOW
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(SortingHatPropertyType.PATH_IMG);
        String windowIconFile = props.getProperty(SortingHatPropertyType.IMAGE_WINDOW_ICON);
        img = loadImage(imgPath + windowIconFile);
        window.setIconImage(img);

        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        canvas = new SortingHatPanel(this, (SortingHatDataModel) data);

        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        currentScreenState = MENU_SCREEN_STATE;
        img = loadImage(imgPath + props.getProperty(SortingHatPropertyType.IMAGE_BACKGROUND_MENU));
        sT = new SpriteType(BACKGROUND_TYPE);

        sT.addState(MENU_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(SortingHatPropertyType.IMAGE_BACKGROUND_GAME));
        sT.addState(GAME_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(SortingHatPropertyType.IMAGE_BACKGROUND_HELP));
        sT.addState(HELP_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(SortingHatPropertyType.IMAGE_BACKGROUND_SETTINGS));
        sT.addState(SETTINGS_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(SortingHatPropertyType.IMAGE_BACKGROUND_LEVEL_PLAY));
        sT.addState(LEVEL_PLAY_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, MENU_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);

        // LOAD THE WAND CURSOR
//        String cursorName = props.getProperty(SortingHatPropertyType.IMAGE_CURSOR_WAND);
//        img = loadImageWithColorKey(imgPath + cursorName, COLOR_KEY);
//        Point cursorHotSpot = new Point(0,0);
//        Cursor wandCursor = Toolkit.getDefaultToolkit().createCustomCursor(img, cursorHotSpot, cursorName);
//        window.setCursor(wandCursor);
        // ADD A BUTTON FOR EACH LEVEL AVAILABLE
        ArrayList<String> levels = props.getPropertyOptionsList(SortingHatPropertyType.LEVEL_OPTIONS);
        ArrayList<String> levelImageNames = props.getPropertyOptionsList(SortingHatPropertyType.LEVEL_IMAGE_OPTIONS);
//        ArrayList<String> levelMouseOverImageNames = props.getPropertyOptionsList(SortingHatPropertyType.LEVEL_MOUSE_OVER_IMAGE_OPTIONS);
//        float totalWidth = levels.size() * (LEVEL_BUTTON_WIDTH + LEVEL_BUTTON_MARGIN) - LEVEL_BUTTON_MARGIN;
//        Viewport viewport = data.getViewport();
//        x = (viewport.getScreenWidth() - totalWidth)/2.0f;
//        for (int i = 0; i < levels.size(); i++)
//        {
//            sT = new SpriteType(LEVEL_SELECT_BUTTON_TYPE);
//            img = loadImageWithColorKey(imgPath + levelImageNames.get(i), COLOR_KEY);
//            sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
//            img = loadImageWithColorKey(imgPath + levelMouseOverImageNames.get(i), COLOR_KEY);
//            sT.addState(SortingHatTileState.MOUSE_OVER_STATE.toString(), img);
//            s = new Sprite(sT, x, LEVEL_BUTTON_Y, 0, 0, SortingHatTileState.VISIBLE_STATE.toString());
//            guiButtons.put(levels.get(i), s);
//            x += LEVEL_BUTTON_WIDTH + LEVEL_BUTTON_MARGIN;
//        }

        // ADD THE CONTROLS ALONG THE NORTH OF THE GAME SCREEN
        // THEN THE NEW BUTTON
        String newButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_NEW);
        sT = new SpriteType(NEW_GAME_BUTTON_TYPE);
        img = loadImage(imgPath + newButton);
        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        String newMouseOverButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_NEW_MOUSE_OVER);
        img = loadImage(imgPath + newMouseOverButton);
        sT.addState(SortingHatTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, NEW_BUTTON_X, NEW_BUTTON_Y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.put(NEW_GAME_BUTTON_TYPE, s);

        // AND THE BACK BUTTON
        String backButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_BACK);
        sT = new SpriteType(BACK_BUTTON_TYPE);
        img = loadImage(imgPath + backButton);
        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        String backMouseOverButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_BACK_MOUSE_OVER);
        img = loadImage(imgPath + backMouseOverButton);
        sT.addState(SortingHatTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, BACK_BUTTON_X, BACK_BUTTON_Y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.put(BACK_BUTTON_TYPE, s);

        // PLAY BUTTON
        String playButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_PLAY);
        sT = new SpriteType(PLAY_BUTTON_TYPE);
        img = loadImage(imgPath + playButton);
        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        String playMouseOverButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_PLAY_MOUSE_OVER);
        img = loadImage(imgPath + playMouseOverButton);
        sT.addState(SortingHatTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 300, 550, 0, 0, SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.put(PLAY_BUTTON_TYPE, s);

        // RESET BUTTON
        String resetButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_RESET);
        sT = new SpriteType(RESET_BUTTON_TYPE);
        img = loadImage(imgPath + resetButton);
        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        String resetMouseOverButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_RESET_MOUSE_OVER);
        img = loadImage(imgPath + resetMouseOverButton);
        sT.addState(SortingHatTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 500, 550, 0, 0, SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.put(RESET_BUTTON_TYPE, s);

        // SETTINGS BUTTON
        String settingsButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_SETTINGS);
        sT = new SpriteType(SETTINGS_BUTTON_TYPE);
        img = loadImage(imgPath + settingsButton);
        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        String settingsMouseOverButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_SETTINGS_MOUSE_OVER);
        img = loadImage(imgPath + settingsMouseOverButton);
        sT.addState(SortingHatTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 700, 550, 0, 0, SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.put(SETTINGS_BUTTON_TYPE, s);

        
        // ALTERNATE METHOD FOR HELP BUTTON
        String helpButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_HELP);
        sT = new SpriteType(HELP_BUTTON_TYPE);
        img = loadImage(imgPath + helpButton);
        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        String helpMouseOverButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_HELP_MOUSE_OVER);
        img = loadImage(imgPath + helpMouseOverButton);
        sT.addState(SortingHatTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 900, 550, 0, 0, SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.put(HELP_BUTTON_TYPE, s);

        //EXIT BUTTON
        sT = new SpriteType("EXIT_BUTTON");
        img = loadImage("img/sorting_hat/ExitButton.png");
        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        img = loadImage("img/sorting_hat/ExitButtonMouseOver.png");
        sT.addState("MOUSE_OVER_STATE", img);
        s = new Sprite(sT, 1200, 0, 0, 0, SortingHatTileState.VISIBLE_STATE.toString());
        guiButtons.put("EXIT_BUTTON", s);

        //IMPLEMENTING THE NAVIGATION BUTTONS
        //LEFT BUTTON
        sT = new SpriteType("LEFT_BUTTON");
        img = loadImage("img/sorting_hat/LeftButton.png");
        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        sT.addState("MOUSE_OVER_STATE", img);
        s = new Sprite(sT, 50, 610, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.put("LEFT_BUTTON", s);

        //RIGHT BUTTON
        sT = new SpriteType("RIGHT_BUTTON");
        img = loadImage("img/sorting_hat/RightButton.png");
        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        sT.addState("MOUSE_OVER_STATE", img);
        s = new Sprite(sT, 150, 610, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.put("RIGHT_BUTTON", s);

        //UP BUTTON
        sT = new SpriteType("UP_BUTTON");
        img = loadImage("img/sorting_hat/UpButton.png");
        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        sT.addState("MOUSE_OVER_STATE", img);
        s = new Sprite(sT, 85, 600, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.put("UP_BUTTON", s);

        //DOWN BUTTON
        sT = new SpriteType("DOWN_BUTTON");
        img = loadImage("img/sorting_hat/DownButton.png");
        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        sT.addState("MOUSE_OVER_STATE", img);
        s = new Sprite(sT, 85, 650, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.put("DOWN_BUTTON", s);

        //SETTINGS CHECK BOX
        //SOUND CHECK BOX
        sT = new SpriteType("SOUND_BUTTON");
        img = loadImage("img/sorting_hat/SoundButtonYes.png");
        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        sT.addState("MOUSE_OVER_STATE", img);
        img = loadImage("img/sorting_hat/SoundButtonNo.png");
        sT.addState("SELECTED_STATE", img);
        s = new Sprite(sT, 400, 300, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.put("SOUND_BUTTON", s);

        //MUSIC CHECK BOX
        sT = new SpriteType("MUSIC_BUTTON");
        img = loadImage("img/sorting_hat/MusicButtonYes.png");
        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        sT.addState("MOUSE_OVER_STATE", img);
        s = new Sprite(sT, 400, 400, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.put("MUSIC_BUTTON", s);

        
        // LEVEL BUTTON
        sT = new SpriteType("LEVEL_BUTTON");
        img = loadImage("img/sorting_hat/LevelButton.png");
        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        sT.addState("MOUSE_OVER_STATE", img);
        s = new Sprite(sT, 100, 300, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.put("LEVEL_BUTTON", s);
        
        // AND THE MISCASTS COUNT
//        String miscastCountContainer = props.getProperty(SortingHatPropertyType.IMAGE_DECOR_MISCASTS);
//        sT = new SpriteType(MISCASTS_COUNT_TYPE);
//        img = loadImage(imgPath + miscastCountContainer);
//        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
//        s = new Sprite(sT, TILE_COUNT_X, TILE_COUNT_Y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
//        guiDecor.put(MISCASTS_COUNT_TYPE, s);
        // AND THE TIME DISPLAY
        String timeContainer = props.getProperty(SortingHatPropertyType.IMAGE_DECOR_TIME);
        sT = new SpriteType(TIME_TYPE);
        img = loadImage(imgPath + timeContainer);
        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        s = new Sprite(sT, TIME_X, TIME_Y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
        guiDecor.put(TIME_TYPE, s);

        // AND THE STATS BUTTON
        String statsButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_STATS);
        sT = new SpriteType(STATS_BUTTON_TYPE);
        img = loadImage(imgPath + statsButton);
        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        String statsMouseOverButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_STATS_MOUSE_OVER);
        img = loadImage(imgPath + statsMouseOverButton);
        sT.addState(SortingHatTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, STATS_X, STATS_Y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.put(STATS_BUTTON_TYPE, s);

        // AND THEN THE UNDO BUTTON
        String undoButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_UNDO);
        sT = new SpriteType(UNDO_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + undoButton, COLOR_KEY);
        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        String undoMouseOverButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_UNDO_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + undoMouseOverButton, COLOR_KEY);
        sT.addState(SortingHatTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, UNDO_X, UNDO_Y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
        guiButtons.put(UNDO_BUTTON_TYPE, s);

        // AND THE TILE STACK
        String tileStack = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_TEMP_TILE);
        sT = new SpriteType(ALGORITHM_TYPE);
        img = loadImageWithColorKey(imgPath + tileStack, COLOR_KEY);
        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        s = new Sprite(sT, TEMP_TILE_X, TEMP_TILE_Y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
        guiDecor.put(ALGORITHM_TYPE, s);

        // NOW ADD THE DIALOGS
        // AND THE STATS DISPLAY
//        String statsDialog = props.getProperty(SortingHatPropertyType.IMAGE_DIALOG_STATS);
//        sT = new SpriteType(STATS_DIALOG_TYPE);
        //    img = loadImageWithColorKey(imgPath + statsDialog, COLOR_KEY);
        //    sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
        //   x = (viewport.getScreenWidth()/2) - (img.getWidth(null)/2);
        //  y = (viewport.getScreenHeight()/2) - (img.getHeight(null)/2);
        //  s = new Sprite(sT, x, y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
        // guiDialogs.put(STATS_DIALOG_TYPE, s);
        
    // AND THE WIN CONDITION DISPLAY
//        String winDisplay = props.getProperty(SortingHatPropertyType.IMAGE_DIALOG_WIN);
//        sT = new SpriteType(WIN_DIALOG_TYPE);
//        img = loadImageWithColorKey(imgPath + winDisplay, COLOR_KEY);
//        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
//        x = (viewport.getScreenWidth()/2) - (img.getWidth(null)/2);
//        y = (viewport.getScreenHeight()/2) - (img.getHeight(null)/2);
//        s = new Sprite(sT, x, y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
//        guiDialogs.put(WIN_DIALOG_TYPE, s);
        // THEN THE TILES STACKED TO THE TOP LEFT
        ((SortingHatDataModel) data).initTiles();
    }

    /**
     * Initializes the game event handlers for things like game gui buttons.
     */
    @Override
    public void initGUIHandlers() {
        // WE'LL RELAY UI EVENTS TO THIS OBJECT FOR HANDLING
        eventHandler = new SortingHatEventHandler(this);

        // WE'LL HAVE A CUSTOM RESPONSE FOR WHEN THE USER CLOSES THE WINDOW
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                eventHandler.respondToExitRequest();
            }
        });

        // SEND ALL LEVEL SELECTION HANDLING OFF TO THE EVENT HANDLER
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(SortingHatPropertyType.LEVEL_OPTIONS);
//        for (String levelFile : levels)
//        {
//            Sprite levelButton = guiButtons.get(levelFile);
//            levelButton.setActionCommand(PATH_DATA + levelFile);
//            levelButton.setActionListener(new ActionListener(){
//                Sprite s;
//                public ActionListener init(Sprite initS) 
//                {   s = initS; 
//                    return this;    }
//                public void actionPerformed(ActionEvent ae)
//                {   eventHandler.respondToSelectLevelRequest(s.getActionCommand());    }
//            }.init(levelButton));
//        }   

        // NEW GAME EVENT HANDLER
        guiButtons.get(NEW_GAME_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToNewGameRequest();
            }
        });

        // STATS BUTTON EVENT HANDLER
        guiButtons.get(STATS_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToDisplayStatsRequest();
            }
        });

        // PLAY BUTTON EVENT HANDLER
        guiButtons.get("PLAY_BUTTON_TYPE").setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToPlayButton();
            }
        });

        //RESET BUTTON EVENT HANDLER
        guiButtons.get("RESET_BUTTON_TYPE").setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToResetButton();
            }
        });
        
        // SETTINGS BUTTON EVENT HANDLER
         guiButtons.get("SETTINGS_BUTTON_TYPE").setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToSettingsButton();
            }
        });
         
        // HELP BUTTON EVENT HANDLER         
        guiButtons.get("HELP_BUTTON_TYPE").setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToHelpButton();
            }
        });
        
        // EXIT BUTTON EVENT HANDLER
        guiButtons.get("EXIT_BUTTON").setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToExitButton();
            }
        });

        // LEFT BUTTON EVENT HANDLER
        guiButtons.get("LEFT_BUTTON").setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToLeftButton();
            }
        });
        // RIGHT BUTTON EVENT HANDLER
        guiButtons.get("RIGHT_BUTTON").setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToRightButton();
            }
        });
        // UP BUTTON EVENT HANDLER
        guiButtons.get("UP_BUTTON").setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToUpButton();
            }
        });
        // DOWN BUTTON EVENT HANDLER
        guiButtons.get("DOWN_BUTTON").setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToDownButton();
            }
        });
        
        // SOUND BUTTON EVENT HANDLER
        guiButtons.get("SOUND_BUTTON").setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToSoundButtonPress();
            }
        });

        // MUSIC BUTTON EVENT HANDLER
        guiButtons.get("MUSIC_BUTTON").setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                eventHandler.respondToMusicButtonPress();
            }
        });
        
        //LEVEL BUTTON EVENT HANDLER
        guiButtons.get("LEVEL_BUTTON").setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                eventHandler.respongToLevelButtonPress();
            }
        });
        
        // BACK BUTTON EVENT HANDLER
        guiButtons.get(BACK_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToBackRequest();
            }
        });
        
        // UNDO BUTTON EVENT HANDLER
        guiButtons.get(UNDO_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToUndoRequest();
            }
        });
        
        // KEY LISTENER - LET'S US PROVIDE CUSTOM RESPONSES
        this.setKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                eventHandler.respondToKeyPress(ke.getKeyCode());
            }
        });
    }

    /**
     * Invoked when a new game is started, it resets all relevant game data and
     * gui control states.
     */
    @Override
    public void reset() {
        data.reset(this);
    }

    /**
     * Updates the state of all gui controls according to the current game
     * conditions.
     */
    @Override
    public void updateGUI() {
        // GO THROUGH THE VISIBLE BUTTONS TO TRIGGER MOUSE OVERS
        Iterator<Sprite> buttonsIt = guiButtons.values().iterator();
        while (buttonsIt.hasNext()) {
            Sprite button = buttonsIt.next();

            // ARE WE ENTERING A BUTTON?
            if (button.getState().equals(SortingHatTileState.VISIBLE_STATE.toString())) {
                if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                    button.setState(SortingHatTileState.MOUSE_OVER_STATE.toString());
                }
            } // ARE WE EXITING A BUTTON?
            else if (button.getState().equals(SortingHatTileState.MOUSE_OVER_STATE.toString())) {
                if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                    button.setState(SortingHatTileState.VISIBLE_STATE.toString());
                }
            }
        }
    }
}
