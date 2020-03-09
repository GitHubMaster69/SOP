package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;


public class Main {

    public static void main(String[] args) {

        // write your code here
        JFrame frame = new JFrame();

        final int WIDTH = 600;
        final int HEIGHT = 600;

        renderCanvas panel = new renderCanvas(WIDTH,HEIGHT);
        frame.setSize(WIDTH,HEIGHT);
        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
        }
