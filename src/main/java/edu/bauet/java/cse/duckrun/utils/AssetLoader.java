package edu.bauet.java.cse.duckrun.utils;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Centralized Asset Manager for DuckRun.
 * Image system active.
 * Audio system prepared but temporarily disabled.
 */
public final class AssetLoader {

    private static final Logger LOGGER = Logger.getLogger(AssetLoader.class.getName());

    private static final boolean FAIL_FAST = false;

    private static final Map<String, Image> imageCache = new HashMap<>();

    // Audio cache kept for future use
    // private static final Map<String, AudioClip> audioCache = new HashMap<>();

    private AssetLoader() {}

    // =========================================================
    // IMAGE LOADING
    // =========================================================

    public static Image getImage(String path) {

        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }

        Image image = loadImageInternal(path);
        imageCache.put(path, image);

        return image;
    }

    private static Image loadImageInternal(String path) {

        try (InputStream stream = AssetLoader.class.getResourceAsStream(path)) {

            if (stream == null) {
                return handleMissingImage(path);
            }

            return new Image(stream);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading image: " + path, e);
            return handleMissingImage(path);
        }
    }

    private static Image handleMissingImage(String path) {

        String message = "Image not found: " + path;

        if (FAIL_FAST) {
            throw new RuntimeException(message);
        }

        LOGGER.warning(message + " | Using placeholder image.");
        return createPlaceholderImage();
    }

    private static Image createPlaceholderImage() {
        return new Image(
                "data:image/png;base64," +
                        "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII="
        );
    }

    // =========================================================
    // AUDIO SYSTEM (TEMPORARILY DISABLED)
    // =========================================================

    /*
    private static final Map<String, AudioClip> audioCache = new HashMap<>();

    public static AudioClip getAudio(String path) {

        if (audioCache.containsKey(path)) {
            return audioCache.get(path);
        }

        AudioClip clip = loadAudioInternal(path);
        audioCache.put(path, clip);

        return clip;
    }

    private static AudioClip loadAudioInternal(String path) {

        try {
            URL resource = AssetLoader.class.getResource(path);

            if (resource == null) {
                return handleMissingAudio(path);
            }

            return new AudioClip(resource.toExternalForm());

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading audio: " + path, e);
            return handleMissingAudio(path);
        }
    }

    private static AudioClip handleMissingAudio(String path) {

        String message = "Audio not found: " + path;

        if (FAIL_FAST) {
            throw new RuntimeException(message);
        }

        LOGGER.warning(message + " | Audio disabled for this asset.");
        return null;
    }
    */

    // =========================================================
    // PRELOADING CONFIGURATION
    // =========================================================

    private static final String[] PRELOAD_IMAGE_PATHS = {

            "/images/duck/running.png",
            "/images/duck/running_mid_point.png",
            "/images/duck/ducking.png",
            "/images/duck/ducking_mid_point.png",
            "/images/duck/jumping.png",

            "/images/ui/menu/menu_bg.png",
            "/images/ui/menu/title2.png",
            "/images/pause_menu/pause_button.png",
            "/images/indicator/heart_full.png",
            "/images/indicator/heart_empty.png",

            "/images/backgrounds/level1.png"
    };

    // Audio preload disabled until audio files are added
    /*
    private static final String[] PRELOAD_AUDIO_PATHS = {

            "/audio/sfx/jump.wav",
            "/audio/sfx/hit.wav",
            "/audio/music/bg_music.mp3"
    };
    */

    public static void preloadAssets() {

        LOGGER.info("Preloading image assets...");

        for (String path : PRELOAD_IMAGE_PATHS) {
            getImage(path);
        }

        // Audio preloading disabled for now
        /*
        for (String path : PRELOAD_AUDIO_PATHS) {
            getAudio(path);
        }
        */

        LOGGER.info("Image preloading complete.");
    }

    public static void clearAll() {
        imageCache.clear();
        LOGGER.info("Image cache cleared.");
    }
}