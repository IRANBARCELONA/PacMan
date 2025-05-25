package Online;

import java.io.Serializable;
import java.awt.Point;

public class GameState implements Serializable {
    public Point playerPosition;

    public GameState(Point playerPosition) {
        this.playerPosition = playerPosition;
    }
}