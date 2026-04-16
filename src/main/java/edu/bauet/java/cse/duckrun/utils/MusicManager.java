package edu.bauet.java.cse.duckrun.utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

/**
 * Singleton that manages background music and sound effects.
 *
 * Sound effects (e.g. the hit sound) respect the same mute flag as music,
 * so toggling music OFF in the Settings menu silences SFX too.
 */
public class MusicManager {

    // ── Singleton ─────────────────────────────────────────────────────────────
    private static final MusicManager INSTANCE = new MusicManager();

    public static MusicManager getInstance() {
        return INSTANCE;
    }

    // ── State ─────────────────────────────────────────────────────────────────
    private boolean soundEnabled = true;

    /** Background music player — kept here for future use. */
    private MediaPlayer bgPlayer;

    // ── Sound Effects ─────────────────────────────────────────────────────────

    /**
     * Plays a one-shot sound effect from the classpath.
     * Does nothing when sound is disabled.
     *
     * @param resourcePath  e.g. "/sounds/hit.mp3"
     */
    public void playSfx(String resourcePath) {
        playSfx(resourcePath, 1.0); // default full volume
    }

    public void playSfx(String resourcePath, double volume) {
        if (!soundEnabled) return;

        try {
            URL url = MusicManager.class.getResource(resourcePath);
            if (url == null) {
                System.err.println("MusicManager: SFX not found: " + resourcePath);
                return;
            }
            AudioClip clip = new AudioClip(url.toExternalForm());
            clip.setVolume(volume);
            clip.play();
        } catch (Exception e) {
            System.err.println("MusicManager: could not play SFX: " + resourcePath);
            e.printStackTrace();
        }
    }

    // ── Music toggle (called by SettingsMenu) ─────────────────────────────────

    /**
     * Enables or disables all sound (music + SFX).
     * Also pauses / resumes the background music player if one is active.
     */
    public void setMusicEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        if (bgPlayer != null) {
            if (enabled) bgPlayer.play();
            else         bgPlayer.pause();
        }
    }

    public boolean isMusicEnabled() {
        return soundEnabled;
    }

    // ── Background music helpers (for future use) ─────────────────────────────

    public void setBgPlayer(MediaPlayer player) {
        this.bgPlayer = player;
    }

    public MediaPlayer getBgPlayer() {
        return bgPlayer;
    }
}