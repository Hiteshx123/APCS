package com.company;

import javax.swing.*;
import java.awt.*;

public class GameScene extends JFrame {

    public GameScene(String gameName) {
        super(gameName);
        GenerateBricks();
        this.repaint();
    }

    public void GenerateBricks() {
        Rectangle gameBounds = this.getBounds();
        int width = 40, height = 10, arcWidth = 10, arcHeight = 5, horizontalSpacing = 3, verticalSpacing = 3, verticalDivision = 2;
        for (int j = verticalSpacing; j < gameBounds.height/verticalDivision; j+= height + verticalSpacing){
            for (int i = width/2 + horizontalSpacing; i < gameBounds.width; i+= width + horizontalSpacing){
                this.add(new Brick(i, j, width, height, arcWidth, arcHeight));
            }
        }
    }

    public void GenerateBricks(Rectangle bounds) {

    }

    public class Brick  extends JPanel{
        int x, y, width, height, arcWidth, arcHeight;

        public Brick(int x, int y, int width, int height, int arcWidth, int arcHeight) {
            super();
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.arcWidth =arcWidth;
            this.arcHeight = arcHeight;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
            g.setColor(Color.BLACK);
        }
    }

}
