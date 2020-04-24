package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        int w = 400;
        int h = 800;
        var root = new Pane();
        var canvas = new Canvas(w, h);
        var ctx = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        var scene = new Scene(root, w, h);
        stage.setTitle("Test Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        var game = new Game(canvas);
        var bm = new Beatmap();
        bm.notes = new HitObjectsReader().read();
        bm.numLanes = 4;
        game.setBeatmap(bm);
        var player = new MediaPlayer(new Media(getClass().getResource("yasashisani.mp3").toString()));
        game.setPlayer(player);
        var timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                game.loop(now);
            }
        };
        root.setOnKeyPressed(game::onKeyPressed);
        root.setOnKeyReleased(game::onKeyReleased);
        root.requestFocus();
        player.setOnReady(timer::start);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
