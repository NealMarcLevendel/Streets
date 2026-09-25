package io.levendel.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Tile {
    
    public final Point origin;
    
    public Tile (Point origin) {
        this.origin = origin;
    }
    public String toString() {
        return origin.x + " " + origin.y;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.origin.x, this.origin.y);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Tile)) return false;

        Tile other = (Tile)obj;

        return Integer.compare(other.origin.x, this.origin.x) == 0 &&
            Integer.compare(other.origin.y, this.origin.y) == 0;
    }

}
