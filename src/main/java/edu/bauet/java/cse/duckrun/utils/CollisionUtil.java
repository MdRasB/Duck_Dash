package edu.bauet.java.cse.duckrun.utils;

import javafx.geometry.Bounds;

public final class CollisionUtil {

    private CollisionUtil() {}

    public static boolean isColliding(Bounds a, Bounds b) {
        if (a == null || b == null) return false;
        return a.intersects(b);
    }
}