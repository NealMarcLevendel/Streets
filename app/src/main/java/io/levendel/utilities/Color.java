package io.levendel.utilities;
import java.io.IOException;
import java.util.Objects;

public class Color {
    public static final Color BLACK = new Color(0xFF000000);

    public byte alpha;
    public byte red;
    public byte green;
    public byte blue;

    public double alphaMax;
    public double redMax;
    public double greenMax;
    public double blueMax;

    public double alphaActual;
    public double redActual;
    public double greenActual;
    public double blueActual;

    public Color(int hex) {
        this.alpha = (byte)((hex >> 24) & 0xFF); // Extract Alpha (AA)
        this.red = (byte)((hex >> 16) & 0xFF); // Extract Red (RR)
        this.green = (byte)((hex >> 8) & 0xFF); // Extract Green (GG)
        this.blue = (byte)((hex) & 0xFF); // Extract Blue (BB)
    }
    public Color(byte a, byte r, byte g, byte b) {
        this.alpha = a;
        this.red = r;
        this.green = g;
        this.blue = b;
    }
    public Color(int a, int r, int g, int b) {
        if (a > 255 || a < 0 || r > 255 || r < 0 || b > 255 || b < 0 || g > 255 || g < 0) {
            throw new IllegalArgumentException("Numbers must be between 0-255");
        }
        this.alpha = (byte)a;
        this.red = (byte)r;
        this.green = (byte)g;
        this.blue = (byte)b;
    }
    public Color(int h, int s, int l) {
        float C = (1 - Math.abs(2 * l - 1) * s);
        float X = C * (1 - Math.abs((h / 60)%2-1));
        float m = l - C / 2;

        float r = 0, g = 0, b = 0;

        if (s == 0) {
            r = g = b = l;
        } else {
            if (h >= 0 && h < 60) {
                r = C;
                g = X;
                b = 0;
            } else if (h >= 60 && h < 120) {
                r = 0;
                g = C;
                b = 0;
            } else if (h >= 120 && h < 180) {
                r = 0;
                g = C;
                b = 0;
            } else if (h >= 180 && h < 240) {
                r = 0;
                g = X;
                b = C;
            } else if (h >= 240 && h < 300) {
                r = X;
                g = 0;
                b = C;
            } else if (h >= 300 && h < 360) {
                r = C;
                g = 0;
                b = X;
            }
        }
        
        r = (r+m)*255;
        g = (g+m)*255;
        b = (b+m)*255;

        this.alpha = (byte)255;
        this.red = (byte)Math.round(r);
        this.green = (byte)Math.round(g);
        this.blue = (byte)Math.round(b);
    }
    public Color (double a, double r, double g, double b, double aMax, double rMax, double gMax, double bMax) {
        this.alphaActual = a;
        this.redActual = r;
        this.greenActual = g;
        this.blueActual = b;
        this.alphaMax = aMax;
        this.redMax = rMax;
        this.blueMax = bMax;
        this.greenMax = gMax;

        this.recalibrate(255, 255, 255, 255);
    }
    public Color copy() {
        return new Color(this.alpha, this.red, this.green, this.blue);
    }
    public void recalibrate(double a, double r, double g, double b) {
        if (a > this.alphaMax) {
            this.alphaMax = a;
            this.alpha = (byte)((this.alphaActual / a) * 255);
        }
        if (r > this.redMax) {
            this.redMax = r;
            this.red = (byte)((this.redActual / r) * 255);
        }
        if (g > this.greenMax) {
            this.greenMax = g;
            this.alpha = (byte)((this.greenActual / g) * 255);
        }
        if (b > this.blueMax) {
            this.blueMax = b;
            this.alpha = (byte)((this.blueActual / b) * 255);
        }
    }
    public void add(Color f) {
        int falpha = f.alpha & 0xFF;
        int fred = f.red & 0xFF;
        int fgreen = f.green & 0xFF;
        int fblue = f.blue & 0xFF;

        int balpha = this.alpha & 0xFF;
        int bred = this.red & 0xFF;
        int bgreen = this.green & 0xFF;
        int bblue = this.blue & 0xFF;

        int newAlpha = falpha + balpha * (255 - falpha)/255;
        int newRed = (fred * falpha + bred * (255 - falpha))/newAlpha;
        int newGreen = (fgreen * falpha + bgreen * (255 - falpha))/newAlpha;
        int newBlue = (fblue * falpha + bblue * (255 - falpha))/newAlpha;

        this.alpha = (byte)(newAlpha & 0xFF);
        this.red = (byte)(newRed & 0xFF);
        this.green = (byte)(newGreen & 0xFF);
        this.blue = (byte)(newBlue & 0xFF);
    }
    public int toInteger() {
        //System.out.println((alpha & 0xFF) + " " + (red & 0xFF) + " " + (green & 0xFF) + " " + (blue & 0xFF) + " hex " + Integer.toHexString(((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF)));
        return ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF);
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.alpha, this.red, this.green, this.blue);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Color)) return false;

        Color other = (Color)obj;

        return Byte.compare(other.alpha, this.alpha) == 0 &&
            Byte.compare(other.red, this.red) == 0 &&
            Byte.compare(other.green, this.green) == 0 &&
            Byte.compare(other.blue, this.blue) == 0;
    }
}