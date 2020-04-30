package net.maple3142.maniafx.views;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import net.maple3142.maniafx.Main;
import net.maple3142.maniafx.Views;
import net.maple3142.maniafx.gameplay.GameResult;
import net.maple3142.maniafx.gameplay.NoteResult;

public class ResultScreen {
    @FXML
    private Text title;
    @FXML
    private Text artist;
    @FXML
    private Text count300, count200, count100, count0;
    @FXML
    private Text accuracy;
    @FXML
    private Text maxCombo;

    public void setGameResult(GameResult result) {
        title.setText(result.getTitle());
        artist.setText(result.getArtist());
        count300.setText("300: " + result.countNotes(NoteResult.SCORE_300));
        count200.setText("200: " + result.countNotes(NoteResult.SCORE_200));
        count100.setText("100: " + result.countNotes(NoteResult.SCORE_100));
        count0.setText("0: " + result.countNotes(NoteResult.SCORE_0));
        accuracy.setText("Acc: " + result.getAccuracy());
        maxCombo.setText("Max Combo: " + result.getMaxCombo());
    }

    @FXML
    private void back() {
        Main.getStage().setScene(Views.getStartScreenScene());
    }
}
