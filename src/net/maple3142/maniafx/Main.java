package net.maple3142.maniafx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.maple3142.maniafx.gameplay.beatmap.BeatmapReader;
import net.maple3142.maniafx.gameplay.Game;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        int w = 600;
        int h = 800;

        var root = new Pane();
        var gameScene = new Scene(root, w, h);
        var game = new Game(root, w, h);
        var box = new VBox();
        box.setSpacing(10);
        box.setAlignment(Pos.CENTER);
        var builtins = Files.newDirectoryStream(Paths.get(getClass().getResource("/builtin_maps").toURI()));
        for (var p : builtins) {
            var list = new BeatmapReader(p).read();
            for (var bm : list) {
                var btn = new Button(bm.getMetadata().getTitleUnicode() + " [" + bm.getMetadata().getVersion() + "]");
                box.getChildren().add(btn);
                btn.setOnAction(event -> {
                    stage.setScene(gameScene);
                    game.setBeatmap(bm);
                    game.start();
                });
            }
        }

        var startScene = new Scene(box, w, h);
        game.setOnEnd(() -> stage.setScene(startScene));
        stage.setTitle("ManiaFX");
        stage.setScene(startScene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
