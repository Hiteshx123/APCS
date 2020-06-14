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
        gameScene = new GameScene(GAME_TITLE);
        gameScene.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameScene.setBounds(getGameBounds());
        gameScene.setVisible(true);
    }

    public Rectangle getGameBounds() {
        Rectangle virtualBounds = new Rectangle();
        GraphicsEnvironment ge = GraphicsEnvironment.
                getLocalGraphicsEnvironment();
        GraphicsDevice[] gs =
                ge.getScreenDevices();
        for (int j = 0; j < gs.length; j++) {
            GraphicsDevice gd = gs[j];
            GraphicsConfiguration[] gc =
                    gd.getConfigurations();
            for (int i=0; i < gc.length; i++) {
                virtualBounds =
                        virtualBounds.union(gc[i].getBounds());
            }
        }
        return virtualBounds;
    }



}
