package edu.bauet.java.cse.duckrun.utils;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized Asset Manager for the entire game.
 * Loads and caches images and audio files.
 *
 * Professional game architecture approach.
 */
public class AssetLoader {

    // ==============================
    // CACHES
    // ==============================

    private static final Map<String, Image> imageCache = new HashMap<>();

    //for audio loader
    private static final Map<String, AudioClip> audioCache = new HashMap<>();

    // Prevent instantiation
    private AssetLoader() {}

    // ==============================
    // IMAGE LOADING
    // ==============================

    /**
     * Load image from resources and cache it.
     * Example usage:
     * AssetLoader.getImage("/images/duck/duck_run.png");
     */
    public static Image getImage(String path) {

        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }

        URL resource = AssetLoader.class.getResource(path);

        if (resource == null) {
            throw new RuntimeException("Image not found: " + path);
        }

        Image image = new Image(resource.toExternalForm());
        imageCache.put(path, image);

        return image;
    }

    // ==============================
    // AUDIO LOADING
    // ==============================

    /**
     * Load audio clip from resources and cache it.
     * Example:
     * AssetLoader.getAudio("/audio/sfx/jump.wav");
     */

    /**
    public static AudioClip getAudio(String path) {

        if (audioCache.containsKey(path)) {
            return audioCache.get(path);
        }

        URL resource = AssetLoader.class.getResource(path);

        if (resource == null) {
            throw new RuntimeException("Audio not found: " + path);
        }

        AudioClip clip = new AudioClip(resource.toExternalForm());
        audioCache.put(path, clip);

        return clip;
    }
     **/

    // ==============================
    // PRELOAD METHOD (Optional)
    // ==============================

    /**
     * Call this once in MainApp if you want
     * to preload important assets.
     */
    public static void preloadAssets() {

        // Duck sprites
        getImage("/images/duck/running.png");
        getImage("/images/duck/running_mid_point.png");
        getImage("/images/duck/ducking.png");
        getImage("/images/duck/ducking_mid_point.png");
        getImage("/images/duck/jumping.png");

        // Menu UI
        getImage("/images/ui/menu/menu_bg.png");
        getImage("/images/ui/menu/title2.png");

    }

    // ==============================
    // CLEAR CACHE (Optional)
    // ==============================

    public static void clearAll() {
        imageCache.clear();
        //audioCache.clear();
    }
}