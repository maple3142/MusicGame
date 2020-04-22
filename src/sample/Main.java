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
        int h = 400;
        var root = new Pane();
        var canvas = new Canvas(w, h);
        var ctx = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        var scene = new Scene(root, w, h);
        stage.setTitle("Test Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        var game = new Game(w, h);
        game.setLanes(new HitObjectsReader().read());
        var player = new MediaPlayer(new Media(getClass().getResource("sweet witch girl.mp3").toString()));
        game.setPlayer(player);
        var timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                game.loop(now, ctx);
            }
        };
        root.setOnKeyPressed(game::onKeyPressed);
        root.requestFocus();
        player.setOnReady(() -> timer.start());

    }

    public static void main(String[] args) {
        launch(args);
    }
}
