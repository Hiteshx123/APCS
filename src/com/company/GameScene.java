package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameScene extends JPanel {

    ArrayList<Brick> bricks;

    public GameScene() {
        super();
        generateBricks();
    }


    void generateBricks(){
        bricks = new ArrayList<>();
        for (int i = 0; i < this.getBounds().getHeight() / 2; i+=10){
            for (int j = 0; j < this.getBounds().getWidth(); i += 50){
                bricks.add(new Brick(j, i, 50, 10));
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paint(g);

        for(Brick a : bricks){
            if(!a.isDestroyed) {
                g.drawImage(a.getImage(), a.x, a.y, a.imageWidth, a.imageHeight, this);
            }
        }

    }

    public class Brick extends Sprite {

        boolean isDestroyed;

        public Brick(int x, int y, int brickWidth, int brickHeight) {
            this.x = x;
            this.y = y;

            isDestroyed = false;
            this.imagePath = "brick_image.jpg";
            loadTexture();
            resizeImage(brickWidth, brickHeight);
            getImageDimensions();
        }
    }

    public class Ball extends Sprite {

        int xSpeed;
        int ySpeed;

        public Ball(int x, int y, int ballWidth, int ballHeight) {
            this.x = x;
            this.y = y;

            this.imagePath = "ball.png";
            loadTexture();
            resizeImage(ballWidth, ballHeight);
            getImageDimensions();

            initBall();
        }

        void initBall() {
            xSpeed = 1;
            ySpeed = -1;
        }

        void move(){
            x += xSpeed;
            y += ySpeed;

            if(x <= 0 || x >= GameScene.this.getBounds().getWidth()) {
                xSpeed = (int)(xSpeed * (-1 * Math.random() * 5));
            }

            if(y <= 0 || y >= GameScene.this.getBounds().getHeight()) {
                ySpeed = (int)(ySpeed * (-1 * Math.random() * 5));
            }

        }
    }

    public class Paddle extends Sprite implements KeyListener {

        boolean left;
        boolean right;

        public Paddle(int x, int y, int paddleWidth, int paddleHeight) {
            this.x = x;
            this.y = y;

            this.imagePath = "paddle.png";
            loadTexture();
            resizeImage(paddleWidth, paddleHeight);
            getImageDimensions();

            GameScene.this.addKeyListener(this);
        }

        void move(){
            if(left && x >= 0){
                x -= 5;
            } else if (right && x <= GameScene.this.getBounds().getWidth()){
                x += 5;
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT: left = true;
                    break;
                case KeyEvent.VK_RIGHT: right = true;
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT: left = false;
                    break;
                case KeyEvent.VK_RIGHT: right = false;
                    break;
            }
        }
    }

    public class Sprite {
        int x;
        int y;
        int imageWidth;
        int imageHeight;
        String imagePath;
        Image image;

         void setX(int x) {
            this.x = x;
        }

        int getX() {
            return x;
        }

        protected void setY(int y) {
            this.y = y;
        }

        int getY() {
            return y;
        }

        int getImageWidth() {
            return imageWidth;
        }

        int getImageHeight() {
            return imageHeight;
        }

        Image getImage() {
            return image;
        }

        Rectangle getRectBounds() {
            return new Rectangle(x, y,
                    image.getWidth(null), image.getHeight(null));
        }

        void getImageDimensions() {
            imageWidth = image.getWidth(null);
            imageHeight = image.getHeight(null);
        }

        void loadTexture() {
            try {
                image = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void resizeImage(int width, int height) {
            final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            final Graphics2D graphics2D = bufferedImage.createGraphics();
            graphics2D.setComposite(AlphaComposite.Src);
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.drawImage(image, 0, 0, width, height, null);
            graphics2D.dispose();
            image = bufferedImage;
        }
    }

}
