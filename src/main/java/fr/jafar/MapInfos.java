package fr.jafar;

import fr.jafar.util.Position;

import java.util.Arrays;
import java.util.List;

public final class MapInfos {
    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1000;

    public static final Position TOP = new Position(WIDTH / 2, 0);
    public static final Position TOP_RIGHT = new Position(WIDTH, 0);
    public static final Position RIGHT = new Position(WIDTH, HEIGHT / 2);
    public static final Position BOTTOM_RIGHT = new Position(WIDTH, HEIGHT);
    public static final Position BOTTOM = new Position(WIDTH / 2, HEIGHT);
    public static final Position BOTTOM_LEFT = new Position(0, HEIGHT);
    public static final Position LEFT = new Position(0, HEIGHT / 2);
    public static final Position TOP_LEFT = new Position(0, 0);

    public static final Position MIDDLE = new Position(WIDTH/2, HEIGHT/2);

    public static final List<Position> CARDINALS = Arrays.asList(TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, TOP_LEFT);

    private MapInfos() {
        throw new IllegalStateException("Can't instantiate MapInfos utils class");
    }
}
