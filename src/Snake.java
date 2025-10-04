import java.awt.Point;
import java.util.ArrayList;

/**
 * Creates a snake.
 */
public class Snake {
    
    String appleEatMusic = "Apple eat.wav";
    ArrayList<Point> body; // body parts of the snake
    char direction; // direction of the snake's movement
    Point head; // the head of the snake

    /**
     * Creates a snake object with snake's head in the cell (12,12)
     * of the field and with the number of body parts depending on the universe
     * the snake currently is in.
     * @param universe current universe.
     */
    public Snake(int universe) {
        body = new ArrayList<>();

        for (int i = 0; i < 3 * universe; i++) {
            body.add(new Point(i + 12, 12));
        }
        head = new Point(12, 12);
        direction = 'U'; // Initial direction set to Up
    }

    /**
     * This function moves the snake 1 cell in the current direction.
     */
    public void move() {

        for (int i = body.size() - 1; i > 0; i--) {
            body.set(i, body.get(i - 1));
        }

        if (direction == 'U') {
            head.x -= 1;
        } else if (direction == 'D') {
            head.x += 1;
        } else if (direction == 'L') {
            head.y -= 1;
        } else if (direction == 'R') {
            head.y += 1;
        }

        body.set(0, new Point(head.x, head.y));
    }

    /**
     * Adds a body part to the snake after it eats the apple.
     * 
     * @param music sound to be played when eating an apple
     */
    public void addBodyPart(Music music) {
        body.add(new Point(body.get(body.size() - 1)));
        music.playTrack(appleEatMusic);
    }

    /**
     * This function checks whether the player lost
     * (the snake has eaten itself
     * or touches the border).
     * @return true if the player loses
     */
    public boolean checkForLose() {

        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true;
            }
        }

        if (head.x < 0 || head.y < 0 || head.x > 23 || head.y > 23) {
            return true;
        }
        return false;
    }

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public ArrayList<Point> getBody() {
        return body;
    }

    public Point getHead() {
        return head;
    }
}
