package net.maple3142.maniafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import net.maple3142.maniafx.views.GameScreen;
import net.maple3142.maniafx.views.ResultScreen;
import net.maple3142.maniafx.views.StartScreen;

import java.io.IOException;

public class Views {
    private static Scene startScreenScene;
    private static StartScreen startScreenController;
    private static Scene gameScreenScene;
    private static GameScreen gameScreenController;
    private static Scene resultScreenScene;
    private static ResultScreen resultScreenController;

    static {
        try {
            var loader = new FXMLLoader(Views.class.getResource("views/StartScreen.fxml"));
            startScreenScene = new Scene(loader.load());
            startScreenController = loader.getController();

            loader = new FXMLLoader(Views.class.getResource("views/GameScreen.fxml"));
            gameScreenScene = new Scene(loader.load());
            gameScreenController = loader.getController();

            loader = new FXMLLoader(Views.class.getResource("views/ResultScreen.fxml"));
            resultScreenScene = new Scene(loader.load());
            resultScreenController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Scene getStartScreenScene() {
        return startScreenScene;
    }

    public static StartScreen getStartScreenController() {
        return startScreenController;
    }

    public static Scene getGameScreenScene() {
        return gameScreenScene;
    }

    public static GameScreen getGameScreenController() {
        return gameScreenController;
    }

    public static ResultScreen getResultScreenController() {
        return resultScreenController;
    }

    public static Scene getResultScreenScene() {
        return resultScreenScene;
    }
}
