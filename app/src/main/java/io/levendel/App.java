package io.levendel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import io.levendel.utilities.*;

public class App {
    public static Point origin = new Point(0,0);
    public static Point bound;
    public static int tileSize = 1;
    public static ColorMap cm;
    public static Map<Tile, HashMap<Tile, Double>> costs = new HashMap<>();

    public static void main(String[] args) {
        InputStream input = App.class.getResourceAsStream("/images/perlin.png");
        BufferedImage image;

        try {
            image = ImageIO.read(input);
            bound = new Point(image.getWidth()-1, image.getHeight()-1);
            List<Tile> tiles = new ArrayList<>();

            cm = new ColorMap();
            ColorMapOperations.from_BufferedImage(cm, image, new Point(0,0));

            if ((bound.x + 1) % tileSize != 0 ||
                (bound.y + 1) % tileSize != 0) {
                return;
            }

            for (int x = origin.x; x <= bound.x; x+=tileSize) {
                for (int y = origin.y; y <= bound.y; y+=tileSize) {
                    Tile tile = new Tile(new Point(x,y));
                    calculateConnections(tile);
                }
            }

            Tile destination = new Tile(
                new Point(bound.x - tileSize + 1,
                        bound.y - tileSize + 1)
            );

            List<Tile> path = A_Star(new Tile(origin), destination);
            
            for (Tile t : path) {
                fillTile(t, new Color(0xFFFF0000));
            }

            BufferedImage result = ColorMapOperations.to_BufferedImage(cm, origin, bound);
            File file = new File("output.png");
            ImageIO.write(result, "png", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<Tile> A_Star(Tile origin, Tile destination) {
        ArrayList<Tile> path = new ArrayList<>();

        Map<Tile, Double> gScore = new HashMap<Tile,Double>();
        Map<Tile, Double> hScore = new HashMap<>();
        Map<Tile, Double> fScore = new HashMap<>();

        Map<Tile, Tile> cameFrom = new HashMap<>();

        Set<Tile> openSet = new HashSet<>();
        
        gScore.put(origin, 0.0);
        hScore.put(origin, heuristic(origin, destination));
        fScore.put(origin, hScore.get(origin) + gScore.get(origin));

        openSet.add(origin);

        while (!openSet.isEmpty()) {
            Tile current = null;

            for (Tile tile : openSet) {
                if (current == null ||
                    fScore.get(tile) < fScore.get(current)) {
                    current = tile;
                }
            }

            if (current.equals(destination)) {
                Tile step = current;

                while (!step.equals(origin)) {
                    path.add(step);
                    step = cameFrom.get(step);
                }
                path.add(origin);
                Collections.reverse(path);

                break;
            }

            openSet.remove(current);

            Map<Tile, Double> neighbors = costs.get(current);

            for (Map.Entry<Tile, Double> entry : neighbors.entrySet()) {
                Tile neighbor = entry.getKey();
                double movementCost = entry.getValue();

                double tentativeG = gScore.get(current) + movementCost;

                if (tentativeG < gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    cameFrom.put(neighbor, current);

                    gScore.put(neighbor, tentativeG);

                    hScore.put(neighbor, heuristic(neighbor, destination));

                    fScore.put(neighbor, gScore.get(neighbor) + hScore.get(neighbor));

                    openSet.add(neighbor);
                }
            }
        }



        return path;
    }
    public static void calculateConnections(Tile tile) {
        //System.out.print("Calculating connections " + tile.toString());

        Tile top = new Tile(new Point(tile.origin.x, tile.origin.y+tileSize));
        Tile bottom = new Tile(new Point(tile.origin.x, tile.origin.y-tileSize));
        Tile left = new Tile(new Point(tile.origin.x-tileSize, tile.origin.y));
        Tile right = new Tile(new Point(tile.origin.x+tileSize, tile.origin.y));

        Tile top_left = new Tile(new Point(tile.origin.x-tileSize, tile.origin.y+tileSize));
        Tile top_right = new Tile(new Point(tile.origin.x+tileSize, tile.origin.y+tileSize));
        Tile bottom_right = new Tile(new Point(tile.origin.x+tileSize, tile.origin.y-tileSize));
        Tile bottom_left = new Tile(new Point(tile.origin.x-tileSize, tile.origin.y-tileSize));

        if (!costs.containsKey(tile)) costs.put(tile, new HashMap<>());

        if (TileIsValid(top)) {
            double cost = 1.0 + Math.abs(averageHeight(tile) - averageHeight(top));
            costs.get(tile).put(top, cost);
        }
        if (TileIsValid(bottom)) {
            double cost = 1.0 + Math.abs(averageHeight(tile) - averageHeight(bottom));
            costs.get(tile).put(bottom, cost);
        }
        if (TileIsValid(left)) {
            double cost = 1.0 + Math.abs(averageHeight(tile) - averageHeight(left));
            costs.get(tile).put(left, cost);
        }
        if (TileIsValid(right)) {
            double cost = 1.0 + Math.abs(averageHeight(tile) - averageHeight(right));
            costs.get(tile).put(right, cost);
        }
        
        if (TileIsValid(top_left)) {
            double cost = Math.sqrt(2) + Math.abs(averageHeight(tile) - averageHeight(top_left));
            costs.get(tile).put(top_left, cost);
        }
        if (TileIsValid(top_right)) {
            double cost = Math.sqrt(2) + Math.abs(averageHeight(tile) - averageHeight(top_right));
            costs.get(tile).put(top_right, cost);
        }
        if (TileIsValid(bottom_left)) {
            double cost = Math.sqrt(2) + Math.abs(averageHeight(tile) - averageHeight(bottom_left));
            costs.get(tile).put(bottom_left, cost);
        }
        if (TileIsValid(bottom_right)) {
            double cost = Math.sqrt(2) + Math.abs(averageHeight(tile) - averageHeight(bottom_right));
            costs.get(tile).put(bottom_right, cost);
        }
    }
    public static boolean TileIsValid(Tile tile) {
        //System.out.print("Validating " + tile.toString());
        if (tile.origin.x > bound.x) return false;
        if (tile.origin.y > bound.y) return false;
        if (tile.origin.x < origin.x) return false;
        if (tile.origin.y < origin.y) return false;

        if (tile.origin.x+tileSize-1 > bound.x) return false;
        if (tile.origin.y+tileSize-1 > bound.y) return false;
        if (tile.origin.x+tileSize-1 < origin.x) return false;
        if (tile.origin.y+tileSize-1 < origin.y) return false;

        return true;
    }
    public static double averageHeight (Tile tile) {
        //System.out.print("Averaging height " + tile.toString());
        int full = 0;

        for (int x = tile.origin.x; x < tile.origin.x+tileSize; x++) {
            for (int y = tile.origin.y; y < tile.origin.y+tileSize; y++) {
                int red = cm.getColor(new Point(x,y)).red;
                full += red;
            }
        }

        return (double)full / (tileSize*tileSize);
    }
    public static double heuristic(Tile a, Tile b) {
        //System.out.print("Calculating heuristic " + a.toString() + " " + b.toString());
        double dx = Math.abs(a.origin.x - b.origin.x);
        double dy = Math.abs(a.origin.y - b.origin.y);

        return (dx + dy) / tileSize;
    }
    public static void fillTile(Tile tile, Color color) {
        //System.out.print("Filling tile " + tile.toString());
        for (int x = tile.origin.x; x < tile.origin.x + tileSize; x++) {
            for (int y = tile.origin.y; y < tile.origin.y + tileSize; y++) {
                cm.setColor(new Point(x,y), color);
            }
        }
    }
}
