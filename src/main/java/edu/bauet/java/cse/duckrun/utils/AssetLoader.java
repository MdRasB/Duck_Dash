package edu.bauet.java.cse.duckrun.utils;

import javafx.scene.image.Image;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AssetLoader {

    private static final Map<String, Image> imageCache = new HashMap<>();

    private static final String[] ASSETS_TO_LOAD = {
        // Duck
        "/images/duck/running.png",
        "/images/duck/running_mid_point.png",
        "/images/duck/ducking.png",
        "/images/duck/ducking_mid_point.png",
        "/images/duck/jumping.png",
        // Shadow
        "/images/shadow/Shadow(normal).png",
        "/images/shadow/Shadow(small).png",
        // Enemies
        "/images/enemies/cat.png",
        "/images/enemies/Eagle_state_1.png",
        "/images/enemies/Eagle_state_2.png",
        // UI
        "/images/ui/menu/menu_bg.png",
        "/images/ui/menu/title2.png",
        "/images/pause_menu/pause_button.png",
        "/images/pause_menu/pause_menu_frame.png",
        "/images/pause_menu/restart_button.png",
        "/images/pause_menu/play_button.png",
        "/images/pause_menu/exit_button.png",
        "/images/pause_menu/settings_button.png",
        "/images/indicator/heart_full.png",
        "/images/indicator/heart_empty.png",
        // Backgrounds
        "/images/ui/hall_ui_1200x600.png",
        "/images/backgrounds/level1.png"
    };

    private AssetLoader(){}

    public static void preloadAssets() {
        System.out.println("Preloading assets...");
        for (String path : ASSETS_TO_LOAD) {
            getImage(path); // This will load and cache the image
        }
        System.out.println("Asset preloading complete.");
    }

    public static Image getImage(String path){
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }
        Image image = loadImage(path);
        if (image != null) {
            imageCache.put(path, image);
        }
        return image;
    }

    private static Image loadImage(String path){
        try{
            InputStream is = AssetLoader.class.getResourceAsStream(path);
            if (is == null) {
                System.err.println("FATAL: Could not load image: " + path);
                return createPlaceholder();
            }
            return new Image(is);
        }catch(Exception e){
            System.err.println("FATAL: Exception while loading image: " + path);
            e.printStackTrace();
            return createPlaceholder();
        }
    }
    
    private static Image createPlaceholder() {
        return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=");
    }
}
