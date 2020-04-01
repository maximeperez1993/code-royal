package fr.jafar.site;

import fr.jafar.Position;

import java.util.Scanner;

public class Site {

    private final int id;
    private final Position position;
    private final int radius;
    private SiteState state;

    private Site(int id, Position position, int radius) {
        this.id = id;
        this.position = position;
        this.radius = radius;
    }

    public void update(Scanner in) {
        this.state = SiteState.read(in);
    }

    public static Site read(Scanner scanner) {
        return new Site(scanner.nextInt(), Position.read(scanner), scanner.nextInt());
    }
}
