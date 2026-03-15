package edu.bauet.java.cse.duckrun.utils;

import java.util.prefs.Preferences;

/**
 * Saves and retrieves the best (lowest) completion time per level.
 * Uses Java's built-in Preferences API — no external files needed.
 */
public final class HighScoreManager {

    private static final Preferences PREFS =
            Preferences.userNodeForPackage(HighScoreManager.class);

    private static final String KEY_LEVEL1 = "best_time_level1";
    private static final String KEY_LEVEL2 = "best_time_level2";
    private static final String KEY_LEVEL3 = "best_time_level3";

    private static final int NO_SCORE = Integer.MAX_VALUE;

    private HighScoreManager() {}

    // ── Save ────────────────────────────────────────────────────────────────

    public static void submitLevel1(int seconds) { submit(KEY_LEVEL1, seconds); }
    public static void submitLevel2(int seconds) { submit(KEY_LEVEL2, seconds); }
    public static void submitLevel3(int seconds) { submit(KEY_LEVEL3, seconds); }

    private static void submit(String key, int seconds) {
        int current = PREFS.getInt(key, NO_SCORE);
        if (seconds < current) {
            PREFS.putInt(key, seconds);
        }
    }

    // ── Read ─────────────────────────────────────────────────────────────────

    public static String getLevel1Best() { return format(PREFS.getInt(KEY_LEVEL1, NO_SCORE)); }
    public static String getLevel2Best() { return format(PREFS.getInt(KEY_LEVEL2, NO_SCORE)); }
    public static String getLevel3Best() { return format(PREFS.getInt(KEY_LEVEL3, NO_SCORE)); }

    private static String format(int seconds) {
        if (seconds == NO_SCORE) return "--:--";
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }
}
