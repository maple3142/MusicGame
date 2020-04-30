package net.maple3142.maniafx.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import net.maple3142.maniafx.Main;
import net.maple3142.maniafx.Views;
import net.maple3142.maniafx.gameplay.beatmap.BeatmapReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StartScreen {
    @FXML
    private VBox box;

    @FXML
    private void initialize() throws IOException, URISyntaxException {
        var builtins = Files.newDirectoryStream(Paths.get(getClass().getResource("/builtin_maps").toURI()));
        for (var p : builtins) {
            var list = new BeatmapReader(p).read();
            for (var bm : list) {
                var btn = new Button(bm.getMetadata().getTitleUnicode() + " [" + bm.getMetadata().getVersion() + "]");
                box.getChildren().add(btn);
                btn.setOnAction(event -> {
                    Main.getStage().setScene(Views.getGameScreenScene());
                    Views.getGameScreenController().playBeatmap(bm);
                });
            }
        }
    }
}
