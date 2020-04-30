package net.maple3142.maniafx;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage stage) {
        Main.stage = stage;

        stage.setTitle("ManiaFX");
        stage.setScene(Views.getStartScreenScene());
        stage.setWidth(600);
        stage.setHeight(800);
        stage.setResizable(false);
        stage.show();

//        stage.setScene(Views.getResultScreenScene());
//        var data = new GameData();
//        data.putResult(NoteResult.SCORE_300);
//        data.putResult(NoteResult.SCORE_300);
//        data.putResult(NoteResult.SCORE_100);
//        data.putResult(NoteResult.SCORE_0);
//        data.putResult(NoteResult.SCORE_200);
//        var bm = new Beatmap();
//        var m = new BeatmapMetadata();
//        m.setTitleUnicode("Test Chart");
//        m.setArtistUnicode("メイプル");
//        bm.setMetadata(m);
//        Views.getResultScreenController().setGameResult(new GameResult(bm, data));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
