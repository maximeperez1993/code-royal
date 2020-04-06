package fr.jafar.structure;

import java.util.Objects;
import java.util.Scanner;

public class Position implements Positionable {

    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position position) {
        this.x = position.x;
        this.y = position.y;
    }

    public double getDistance(Position p) {
        return Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
    }

    public Position add(Position other) {
        return new Position(x + other.x, y + other.y);
    }

    public Position sub(Position other) {
        return new Position(x - other.x, y - other.y);
    }

    public boolean isBetween(Position p1, Position p2, int delta) {
        return x > Math.min(p1.x, p2.x) - delta &&
                x < Math.max(p1.x, p2.x) + delta &&
                y > Math.min(p1.y, p2.y) - delta &&
                y < Math.max(p1.y, p2.y) + delta;
    }

    public Position resize(int newLength) {
        double length = Math.sqrt(x * x + y * y);
        return new Position((int) (x / length) * newLength, (int) (y / length) * newLength);
    }

    public Position moveTo(Position other, int maxSteps) {
        if (getDistance(other) < maxSteps) {
            return new Position(this);
        }
        return this.add(other.sub(this).resize(maxSteps));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int getRadius() {
        return 0;
    }

    public static Position read(Scanner scanner) {
        return new Position(scanner.nextInt(), scanner.nextInt());
    }

    @Override
    public Position getPosition() {
        return this;
    }

    @Override
    public String toString() {
        return "(" + x + " " + y + ")";
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
