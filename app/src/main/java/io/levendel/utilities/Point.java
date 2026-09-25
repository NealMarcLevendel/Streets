package io.levendel.utilities;
import java.util.Objects;

public class Point {
    public int x;
    public int y;
    public Point(int x1, int y1) {
        this.x = x1;
        this.y = y1;
    }
    public String toString() {
        return x + " " + y;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Point)) return false;

        Point other = (Point)obj;

        return Integer.compare(other.x, this.x) == 0 &&
            Integer.compare(other.y, this.y) == 0;
    }
}
