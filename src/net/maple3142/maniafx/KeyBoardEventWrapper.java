package net.maple3142.maniafx;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

public class KeyBoardEventWrapper {
    private EventHandler<? super KeyEvent> onKeyDown;
    private EventHandler<? super KeyEvent> onKeyUp;
    private Set<KeyCode> pressed = new HashSet<>();

    public KeyBoardEventWrapper(Node node) {
        // this class is created because keyPressed will fire multiple times when you long press a key, which is not what I want
        node.setOnKeyPressed(event -> {
            if (!pressed.contains(event.getCode())) {
                pressed.add(event.getCode());
                if (onKeyDown != null) {
                    onKeyDown.handle(event);
                }
            }
        });
        node.setOnKeyReleased(event -> {
            if (pressed.contains(event.getCode())) {
                pressed.remove(event.getCode());
                if (onKeyUp != null) {
                    onKeyUp.handle(event);
                }
            }
        });
    }

    public void setOnKeyDown(EventHandler<? super KeyEvent> onKeyPressed) {
        this.onKeyDown = onKeyPressed;
    }

    public void setOnKeyUp(EventHandler<? super KeyEvent> onKeyReleased) {
        this.onKeyUp = onKeyReleased;
    }

    public boolean isPressed(KeyCode k) {
        return this.pressed.contains(k);
    }

    public boolean isPressed(String k) {
        return isPressed(KeyCode.valueOf(k));
    }

    public boolean isPressed(char k) {
        return isPressed(String.valueOf(k));
    }
}
