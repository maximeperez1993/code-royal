package fr.jafar;

import java.util.Scanner;

public class Position {

    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Position read(Scanner scanner){
        return new Position(scanner.nextInt(), scanner.nextInt());
    }
}