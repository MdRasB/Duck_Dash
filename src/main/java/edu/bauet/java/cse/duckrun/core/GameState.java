package edu.bauet.java.cse.duckrun.core;

/**
 * Represents the current state of the game.
 * Controls what logic should run in GameScene.
 */
public enum GameState {

    RUNNING,      // Normal gameplay
    PAUSED,       // Game temporarily stopped
    GAME_OVER,    // Player lost
    WIN           // Player reached classroom
}
