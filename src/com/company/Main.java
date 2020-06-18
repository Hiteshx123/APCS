package com.company;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Random;

public class Main {

    private final String GAME_TITLE = "Curved Breakout";
    private GameScene gameScene;

    public static void main(String[] args) {
        Main gameRunner = new Main();
        gameRunner.startGame();
    }

    public void startGame() {
        buildScene();
    }

    public void buildScene(){
        gameScene = new GameScene();
        JFrame frame = new JFrame(GAME_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(new Rectangle(1000, 600));
        frame.setResizable(false);
        frame.add(gameScene);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.repaint();

    }




}
