package fr.jafar.util;

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
}
