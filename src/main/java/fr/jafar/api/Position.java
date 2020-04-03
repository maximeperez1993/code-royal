package fr.jafar.api;

import java.util.Objects;
import java.util.Scanner;

public class Position {

    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double getDistance(Position p) {
        return Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Position read(Scanner scanner) {
        return new Position(scanner.nextInt(), scanner.nextInt());
    }

    @Override
    public String toString() {
        return "("+x+" " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
