package com.md5_2;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class ForestFire {

    private final Color TREE = Color.GREEN;
    private final Color FIRE = Color.DARKORANGE;
    private final Color COAL = Color.BLACK;
    private final Color WATER = Color.LIGHTBLUE;
    private static WritableImage board;
    private static WritableImage nextGen;
    private String pathChangeable = "src/main/resources/TestBig3.png";

    private HashMap<String, Integer> checkNeighbours(Vector<Color> colorVector) {
        HashMap<String, Integer> colorMap = new HashMap<>();
        int tree = 0;
        int fire = 0;
        for (Color color : colorVector) {
            if (color.equals(TREE)) tree++;
            else if (color.equals(FIRE)) fire++;
        }
        colorMap.put("tree", tree);
        colorMap.put("fire", fire);
        return colorMap;
    }

    private HashMap<String, Integer> scanPixel(int x, int y) {
        Vector<Color> colorVector = new Vector<>();
        for (int dy = -1; dy <= 1; dy++) {
            int newY = y + dy;
            for (int dx = -1; dx <= 1; dx++) {
                int newX = x + dx;
                try {
                    colorVector.add(board.getPixelReader().getColor(newX, newY));
                } catch (IndexOutOfBoundsException e) {
                    colorVector.add(COAL);
                }
            }
        }
        return checkNeighbours(colorVector);
    }

    private void burn(int x, int y, HashMap<String, Integer> colorMap, double baseFireChance) {
        if (colorMap.get("fire") > 0) {
            double chance = ThreadLocalRandom.current().nextDouble(0, 1);
            double fireChance = baseFireChance + 0.05 * colorMap.get("fire");
            if (chance <= fireChance) nextGen.getPixelWriter().setColor(x, y, FIRE);
        }
    }

    private void regrow(int x, int y, HashMap<String, Integer> colorMap, double regrowChance) {
        if (colorMap.get("fire") == 0) {
            double chance = ThreadLocalRandom.current().nextDouble(0, 1);
            if (chance <= regrowChance) nextGen.getPixelWriter().setColor(x, y, TREE);
        }
    }

    protected void castRitual(){
        for(int y = 0; y < board.getHeight(); y++){
            for(int x = 0; x < board.getWidth(); x++){
                Color pixel = board.getPixelReader().getColor(x,y);
                if(pixel.equals(FIRE)) nextGen.getPixelWriter().setColor(x,y,TREE);
            }
        }
        FileHandler.saveImage(nextGen, "RitualedImage");
    }

    protected void dropWaterBomb(int x, int y){

    }



    private void iteration(double baseFireChance, boolean isRegrow, int ticks, String path) {
        board = FileHandler.openImage(path);
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                HashMap<String, Integer> colorMap = scanPixel(x, y);
                Color pixel = board.getPixelReader().getColor(x, y);
                if (pixel.equals(TREE)) burn(x, y, colorMap, baseFireChance);
                else if (pixel.equals(FIRE)){
                    double burnoutChance = ThreadLocalRandom.current().nextInt(0, 20);
                    if(ticks % 20 == burnoutChance) nextGen.getPixelWriter().setColor(x, y, COAL);
                }
                else if (pixel.equals(COAL) && isRegrow) regrow(x, y, colorMap, 0.5);
            }
        }
        FileHandler.saveImage(nextGen, String.valueOf((ticks + 1)));
    }

    public void logic(double baseFireChance, boolean isRegrow, int flag, int iter) {
        if (iter == 0) {
            iteration(baseFireChance, isRegrow, iter, pathChangeable);
        }
        else {
            if (flag == 1) {
                String path = "src/main/java/images/" + iter + ".png";
                iteration(baseFireChance, isRegrow, iter, path);
            }
        }
    }

    public ForestFire(String path) {
        nextGen = FileHandler.openImage(path);
    }

}
