module edu.bauet.java.cse.duckrun {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires java.logging;
    requires java.prefs;

    opens edu.bauet.java.cse.duckrun to javafx.graphics;
    opens edu.bauet.java.cse.duckrun.scenes to javafx.fxml;
    opens edu.bauet.java.cse.duckrun.ui to javafx.fxml;
    opens edu.bauet.java.cse.duckrun.entities to javafx.graphics;
}