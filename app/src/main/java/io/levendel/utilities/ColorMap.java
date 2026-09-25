package io.levendel.utilities;
import java.util.HashMap;

public class ColorMap {
    private final HashMap<Point, Color> map = new HashMap<>();

    public ColorMap () {

    }
    public ColorMap copy () {
        ColorMap cm = new ColorMap();
        for (Point p : map.keySet()) {
            Color c = this.getColor(p).copy();
            cm.setColor(p, c);
        }
        return cm;
    }
    public void setColor(Point key, Color value) {
        map.put(key, value);
    }
    public void addColor(Point key, Color value) {
        Color c = getColor(key);
        c.add(value);
        map.put(key, c);
    }
    public Color getColor(Point key) {
        if (!map.containsKey(key)) {
            return new Color(0x00000000);
        }
        return map.get(key);
    }
    public void clear() {
        map.clear();
    }
}
