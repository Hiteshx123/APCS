package com.company;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;



public class Canvas extends JPanel implements ActionListener, KeyListener {

    public static final int HEIGHT = 600;
    public static final int WIDTH = 720;


    private int horizontalCount;
    private BufferedImage image;
    private Graphics2D bufferedGraphics;
    private Timer time;
    private static final Font endFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    private static final Font scoreFont = new Font(Font.SANS_SERIF, Font.BOLD, 15);

    private Paddle player;
    private Ball ball;
    ArrayList<ArrayList<Brick> > bricks;


    public Canvas(){
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        bufferedGraphics = image.createGraphics();
        time = new Timer(15, this);
        player = new Paddle((WIDTH/2)-(Paddle.PADDLE_WIDTH/2));
        ball = new Ball(((player.getX() + (Paddle.PADDLE_WIDTH / 2)) - (Ball.DIAMETER / 2)),
                (Paddle.Y_POS - (Ball.DIAMETER + 10)), -5, -5);

        bricks = new ArrayList<>();
        horizontalCount = WIDTH / Brick.BRICK_WIDTH;
        for(int i = 0; i < 8; ++i){
            ArrayList<Brick> temp = new ArrayList<Brick>();
            for(int j = 0; j < horizontalCount; ++j){
                Brick tempBrick = new Brick((j * Brick.BRICK_WIDTH), ((i+2) * Brick.BRICK_HEIGHT), Brick.Type.LOW);
                temp.add(tempBrick);
            }
            bricks.add(temp);
            addKeyListener(this);
            this.setFocusable(true);
            requestFocus();
        }
    }

    @Override public void actionPerformed(ActionEvent e){
        checkCollisions();
        ball.move();
        playMove();
        for(int i = 0; i < bricks.size(); ++i){
            ArrayList<Brick> al = bricks.get(i);
            for(int j = 0; j < al.size(); ++j){
                Brick b = al.get(j);
                if(b.dead()){
                    al.remove(b);
                }
            }
        }
        repaint();
    }

    void playMove(){
        if(left)
        if(player.getX() - 1 > 0)
            player.setX(player.getX() - 1 - (Paddle.PADDLE_WIDTH / 2));
        if(right)
        if(player.getX() + 1 < this.getWidth())
            player.setX(player.getX() + 1 + (Paddle.PADDLE_WIDTH / 2));
    }


    private void checkCollisions() {
        if(player.hitPaddle(ball)){
            ball.setDY(ball.getDY() * -1);
            return;
        }

        if(ball.getX() >= (WIDTH - Ball.DIAMETER) || ball.getX() <= 0){
            ball.setDX(ball.getDX() * -1);
        }
        if(ball.getY() > (Paddle.Y_POS + Paddle.PADDLE_HEIGHT + 10)){
            resetBall();
        }
        if(ball.getY() <= 0){
            ball.setDY(ball.getDY() * -1);
        }


        int brickRowsActive = 0;
        for(ArrayList<Brick> alb : bricks){
            if(alb.size() == horizontalCount){
                ++brickRowsActive;
            }
        }
        int ii = 0;
        if(brickRowsActive == 0){
            ii = 0;
        } else {
            ii = brickRowsActive - 1;
        }
        for(int i = (brickRowsActive==0) ? 0 : (brickRowsActive - 1); i < bricks.size(); ++i){
            for(Brick b : bricks.get(i)){
                if(b.hitBy(ball)){
                    player.setScore(player.getScore() + b.getBrickType().getPoints());
                    b.decrementType();
                }
            }
        }
    }


    private void resetBall() {
        if(gameOver()){
            time.stop();
            return;
        }
        ball.setX(WIDTH/2);
        ball.setY((HEIGHT/2) + 80);
        player.setLives(player.getLives() - 1);
        player.setScore(player.getScore() - 1000);
    }

    private boolean gameOver() {
        if(player.getLives() <= 1)
            return true;
        return false;
    }


    @Override public void paintComponent(Graphics g){
        super.paintComponent(g);
        bufferedGraphics.clearRect(0, 0, WIDTH, HEIGHT);
        bufferedGraphics.setColor(Color.WHITE);
        bufferedGraphics.fillRect(0, 0, WIDTH, HEIGHT);
        player.drawPaddle(bufferedGraphics);
        ball.drawBall(bufferedGraphics);
        for(ArrayList<Brick> row : bricks){
            for(Brick b : row){
                b.drawBrick(bufferedGraphics);
            }
        }
        bufferedGraphics.setFont(scoreFont);
        bufferedGraphics.drawString("Score: " + player.getScore(), 10, 25);
        if(gameOver() &&
                ball.getY() >= HEIGHT){
            bufferedGraphics.setColor(Color.black);
            bufferedGraphics.setFont(endFont);
            bufferedGraphics.drawString("Game Over!  Score: " + player.getScore(), (WIDTH/2) - 85, (HEIGHT/2));
        }
        if(empty()){
            bufferedGraphics.setColor(Color.black);
            bufferedGraphics.setFont(endFont);
            bufferedGraphics.drawString("You won!  Score: " + player.getScore(), (WIDTH/2) - 85, (HEIGHT/2));
            time.stop();
        }
        g.drawImage(image, 0, 0, this);
        Toolkit.getDefaultToolkit().sync();
    }



    private boolean empty() {
        for(ArrayList<Brick> al : bricks){
            if(al.size() != 0){
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args){
        JFrame frame = new JFrame();
        Canvas c = new Canvas();
        frame.add(c);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    boolean left = false;
    boolean right = false;
    boolean space = false;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                left = true;
                break;
            case KeyEvent.VK_RIGHT:
                right = true;
                break;
            case KeyEvent.VK_SPACE:
                if(!time.isRunning()){
                    time.start();
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                left = false;
                break;
            case KeyEvent.VK_RIGHT:
                right = false;
                break;
            case KeyEvent.VK_SPACE:
                space = false;
                break;
        }
    }
}
