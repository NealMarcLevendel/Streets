package io.levendel.utilities;
import java.awt.image.BufferedImage;

//import org.apache.commons.math3.complex.Complex;

import io.levendel.utilities.*;
public class ColorMapOperations {
        public static BufferedImage to_BufferedImage(ColorMap map, Point origin, Point bound) {
            if (origin.x > bound.x || origin.y > bound.y) {
                throw new IllegalArgumentException("bound must be larger than origin");
            }
            BufferedImage img = new BufferedImage(bound.x-origin.x, bound.y-origin.y, BufferedImage.TYPE_INT_ARGB);
            for (int x = origin.x; x < bound.x; x++) {
                for (int y = origin.y; y < bound.y; y++) {
                    img.setRGB(x, y, (map.getColor(new Point(x, y))).toInteger());
                    //img.setRGB(x, y, (Color.BLACK.toInteger()));
                    //System.out.println(Integer.toHexString((map.getColor(new Point(x, y))).toInteger()));
                }
            }
            return img;
        }
        public static void from_BufferedImage(ColorMap map, BufferedImage img, Point origin) {
            for (int x = 0; x < img.getWidth(); x++) {
                for (int y = 0; y < img.getHeight(); y++) {
                    Color c = new Color((0xFF << 24) | img.getRGB(x, y));
                    map.setColor(new Point(x+origin.x, y+origin.y), c ); //new Color((0xFF << 24) | img.getRGB(x, y))
                    //System.out.println((c.alpha & 0xFF) + " " + (c.red & 0xFF) + " " + (c.green & 0xFF) + " " + (c.blue & 0xFF));
                }
            }
        }
        /*public static int [] to_intArray(ColorMap map, Point origin, Point bound) {
            if (origin.x > bound.x || origin.y > bound.y) {
                throw new IllegalArgumentException("bound must be larger than origin");
            }
            int width = bound.x-origin.x;
            int height = bound.y-origin.y;

            int [] img = new int[width*height];
            int index = 0;
            Raster raster = new Raster(width, height);

            for (int i = 0; i < img.length; i++) {
                img[i] = map.getColor(raster.incr()).toInteger();
            }
            return img;
            
        }*/
        /*public static Complex [] to_frequencyBins(ColorMap map, Point origin, Point bound) {
            if (origin.x > bound.x || origin.y > bound.y) {
                throw new IllegalArgumentException("bound must be larger than origin");
            }

            int width = bound.x - origin.x;
            int height = bound.y - origin.y;
            int resolution = width * height;

            Complex [] complex = new Complex [resolution];

            for (int i = 0; i < resolution; i++) {
                Point p = Raster.to_2D(i, width);
                Color c = map.getColor(p);

                int hex = c.toInteger();
                
                int aarr = (hex >> 16) & 0xFFFF;
                int ggbb = hex & 0xFFFF;

                complex[i] = new Complex(aarr, ggbb);
            }

            return complex;
        }*/
        /*public static void from_frequencyBins(ColorMap map, Point origin, Point bound, Complex [] complex) {
            if (origin.x > bound.x || origin.y > bound.y) {
                throw new IllegalArgumentException("bound must be larger than origin");
            }

            int width = bound.x - origin.x;
            int height = bound.y - origin.y;
            int resolution = width * height;

            if (complex.length < resolution) {
                throw new IllegalArgumentException("complex must have at least the same number of bins as there are pixels");
            }

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Point p = new Point(x,y);
                    System.out.println("Filling " + p.toString());
                    int index = Raster.from_2D(p, width);

                    Complex com = complex[index];
                    int aarr = (int)Math.round(com.getReal());
                    int ggbb = (int)Math.round(com.getImaginary());

                    int argb = (aarr << 16) | ggbb;

                    Color c = new Color(argb);

                    map.setColor(p, c);
                }
            }
        }*/
        public static void fill(ColorMap map, Point origin, Point bound, Color c) {
            for (int x = origin.x; x < bound.x; x++) {
                for (int y = origin.y; y < bound.y; y++) {
                    map.addColor(bound, c);
                }
            }
        }
}
