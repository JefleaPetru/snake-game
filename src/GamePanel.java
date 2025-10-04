import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import javax.swing.*;

/**
 * JPanel of the game.
 */
public class GamePanel extends JPanel implements ActionListener {

    final int cellSize = 25;
    final int fieldSize = 600;
    final int width = fieldSize / cellSize;
    int[][] field;
    ArrayList<Point> suitableCells; 
    int score = 0;
    int appleCount = 0;
    int applesNeeded = 8;
    int universe = 1;
    int highscore = 0;
    private JButton startButton;
    private JButton toggleButton;
    private boolean isPlaying = false;
    private boolean musicOn = false;
    //color for the checkrboard pattern of the background
    Color color2 = new Color(230, 230, 235);
    Color darkGreen = new Color(25, 200, 30); // Color for snake's head
    ArrayList<Point> obstacles;
    Point apple;
    Point blackHole = new Point(-1, -1);
    Timer timer;
    Music music2 = new Music();
    Random rand = new Random();
    public Snake snake;
    private SnakeGame mainGame; // Reference to the main game class
    String turnMusic = "Movement sound.wav";
    String buttonMusic = "Button press.wav";
    String loseGameMusic = "Lost Game.wav";
    String blackHoleMusic = "Portal.wav";

    /**
     * Cretaes a game panel, initializes all of its variables 
     * and adds all of the components to the panel.
     * 
     * @param music background music to be played
     * @param maingame instance of mainGame
     */
    public GamePanel(Music music, SnakeGame maingame) {
        setLayout(null);
        snake = new Snake(universe);
        apple = new Point(9, 12);
        timer = new Timer(150, this);
        obstacles = new ArrayList<>();
        this.field = new int[width][width];
        this.suitableCells = new ArrayList<>();
        getPlayingField();
        this.mainGame = maingame;

        toggleButton = new JButton("On");
        toggleButton.addActionListener(e -> {
            if (musicOn) {
                music.stopBackgroundMusic();
                toggleButton.setText("On");
            } else {
                String filename = "Audio Snake Game.wav";
                music.playBackgroundMusic(filename);
                toggleButton.setText("Off");
            }
            musicOn = !musicOn; // Toggle the state of music
            this.requestFocusInWindow();
        });

        int buttonWidth = 60;
        int buttonHeight = 30;

        toggleButton.setBounds(680, 30, buttonWidth, buttonHeight);

        startButton = new JButton("Play");
        startButton.setBounds(620, 500, 150, 30);
        startButton.addActionListener(e -> {
            music.playTrack(buttonMusic);

            if (isPlaying) {
                timer.stop();
                startButton.setText("Play");
            } else {
                timer.start();
                startButton.setText("Pause");
            }
            isPlaying = !isPlaying; // toggle the state of the game
            this.requestFocusInWindow();
        });

        add(startButton);
        add(toggleButton);

        this.setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_W && snake.getDirection() != 'D') {
                    music.playTrack(turnMusic);
                    snake.setDirection('U');
                } else if (keyCode == KeyEvent.VK_S && snake.getDirection() != 'U') {
                    music.playTrack(turnMusic);
                    snake.setDirection('D');
                } else if (keyCode == KeyEvent.VK_A && snake.getDirection() != 'R') {
                    music.playTrack(turnMusic);
                    snake.setDirection('L');
                } else if (keyCode == KeyEvent.VK_D && snake.getDirection() != 'L') {
                    music.playTrack(turnMusic);
                    snake.setDirection('R');
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        snake.move();
        Point head = snake.getHead();
        //check whether the snake loses
        isPlaying = !(snake.checkForLose() || obstacles.contains(head));
        if (!isPlaying) {
            timer.stop();
            isPlaying = false;
            universe = 1;
            music2.playTrack(loseGameMusic);
            mainGame.showEndGameOverlay(highscore); // Show end game overlay
            return;
        }
        // Check if the snake eats an apple
        if (head.equals(apple)) {
            snake.addBodyPart(music2);
            appleCount++;
            score += 5;
            spawnObstacle();
            getPlayingField();
            //check whether to spawn apple or black hole
            if (appleCount < applesNeeded) {
                spawnApple();
            } else {
                spawnBlackHole();
                // So that the apple is not drawn in the next frame
                apple = new Point(-1, -1);
            }
        } else if (head.equals(blackHole)) { // Check if the snake enters a black hole
            music2.playTrack(blackHoleMusic);
            universe++;
            loadNewUniverse();
        }
        getPlayingField(); 
        repaint();
        //update high score        
        if (score > highscore) {
            highscore = score;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                //ccordinates of the upper left corner of the cell (i,j) in pixels
                int cellX = j * cellSize; 
                int cellY = i * cellSize;
                if (apple.x == i && apple.y == j) {
                    g.setColor(Color.RED);
                } else if (field[i][j] == -4) {
                    g.setColor(Color.YELLOW);
                } else if (blackHole.x == i && blackHole.y == j) {
                    g.setColor(Color.BLACK);
                } else if (field[i][j] == -2) {
                    g.setColor(darkGreen);
                } else if (field[i][j] == -1) {
                    g.setColor(Color.GREEN);
                } else if (j % 2 == i % 2) {
                    g.setColor(color2);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(cellX, cellY, cellSize, cellSize);
            }
        }
        g.setColor(Color.BLACK);
        g.setFont(new Font("Tahoma", Font.PLAIN, 18));
        g.drawString("Score: " + score, 620, 270);
        g.drawString("Music: ", 610, 50);
        g.drawString("Universe: " + Math.min(universe, 4), 610, 100);
        g.drawString("Apples: " + appleCount + " / " + applesNeeded, 620, 230);
    }

    private void getPlayingField() {
        ArrayList<Point> bodyparts = snake.getBody();

        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 24; j++) {
                field[i][j] = -3;
            }
        }
        // Contains snake's head
        field[(int) bodyparts.get(0).getX()][(int) bodyparts.get(0).getY()] = -2;
        for (int i = 1; i < bodyparts.size(); i++) {
            // Contains a snake
            field[(int) bodyparts.get(i).getX()][(int) bodyparts.get(i).getY()] = -1;
        }
        for (int i = 0; i < obstacles.size(); i++) {
            field[(int) obstacles.get(i).getX()][(int) obstacles.get(i).getY()] = -4;
        }
    }

    /**
     * This function uses a modification of BFS algorithm to find 
     * all the cells which are within a distance
     * of at least 2 and at most 7
     * from the head of the snake.
     * 
     * @param x x-xoordinate of the starting cell
     * @param y y-xoordinate of the starting cell
     */
    public void findCells(int x, int y) {
        Queue<int[]> nextCells = new LinkedList<>();
        int[] initCell = new int[3];
        initCell[0] = x;
        initCell[1] = y;
        initCell[2] = 0;
        
        nextCells.add(initCell);
        while (!nextCells.isEmpty()) {
            int[] currentCell = nextCells.poll();
            int currentX = currentCell[0];
            int currentY = currentCell[1];
            int distance = currentCell[2];

            if (distance > 1) {
                suitableCells.add(new Point(currentX, currentY));
            }
            if (distance == 7) {
                continue;
            }

            // Add the neighbors (if exist and are free and not visited)
            if (currentX > 0) {
                if (field[currentX - 1][currentY] == -3) {
                    field[currentX - 1][currentY] = distance + 1;
                    nextCells.add(new int[]{currentX - 1, currentY, distance + 1});
                }
            }
            if (currentX < 23) {
                if (field[currentX + 1][currentY] == -3) {
                    field[currentX + 1][currentY] = distance + 1;
                    nextCells.add(new int[]{currentX + 1, currentY, distance + 1});
                }
            }
            if (currentY > 0) {
                if (field[currentX][currentY - 1] == -3) {
                    field[currentX][currentY - 1] = distance + 1;
                    nextCells.add(new int[]{currentX, currentY - 1, distance + 1});
                }
            }
            if (currentY < 23) {
                if (field[currentX][currentY + 1] == -3) {
                    field[currentX][currentY + 1] = distance + 1;
                    nextCells.add(new int[]{currentX, currentY + 1, distance + 1});
                }
            }
        }
    }

    /**
     * Spawn an apple at a random cell among suitable cells
     * (having a distance of at most 7 from the snake's head).
     */
    private void spawnApple() {
        suitableCells.clear();
        findCells((int) snake.getHead().getX(), (int) snake.getHead().getY());
        
        // Ensure there are suitable cells available
        if (suitableCells.isEmpty()) {
            // Handle the case where no suitable cells are found
            return; // or throw an exception or log an error
        }
        
        int chooseCell = rand.nextInt(suitableCells.size());
        apple = suitableCells.get(chooseCell);
    }

    private void spawnObstacle() { 
        int x;
        int y;
        do {
            x = rand.nextInt(24);
            y = rand.nextInt(24);
        } while (snake.getBody().contains(new Point(x, y)));
        obstacles.add(new Point(x, y));
    }

    /**
     * This function spawns the black hole when the snake
     * collects enough apples to advance to the next universe.
     */
    private void spawnBlackHole() {
        int x;
        int y;
        do {
            x = rand.nextInt(24);
            y = rand.nextInt(24);
        } while (snake.getBody().contains(new Point(x, y)));
        blackHole = new Point(x, y);
    }
    
    /**
     * Afetr the player loses and presses the (re-)start button or enters ne wuniverse,
     * this function is called to set all the variables to the correct state.
     */
    public void loadNewUniverse() {
        obstacles.clear();
        snake = new Snake(universe);
        appleCount = 0;
        applesNeeded = 8 * universe;
        blackHole = new Point(-1, -1);
        apple = new Point(6, 12);
        //change the background color in the new universe
        if (universe == 1) {
            color2 = new Color(230, 230, 235);
            score = 0;
            timer.start();
        } else if (universe == 2) {
            color2 = new Color(205, 235, 235);
            timer.start();
        } else if (universe == 3) {
            color2 = new Color(250, 215, 240);
            timer.start();
        } else if (universe == 4) {
            mainGame.showWinPanel();
            universe++;
            timer.stop();
        } else {
            //make the game endless after universe 3
            score = 0;
            applesNeeded = 1024;
            
            //start the endless game with the smallest snake
            snake = new Snake(1);
            color2 = new Color(230, 230, 235);
            timer.start();
        }
    }
}
