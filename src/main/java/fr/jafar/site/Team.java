package fr.jafar.site;

import java.util.Scanner;

enum Team {

    NEUTRAL(-1),
    FRIENDLY(0),
    ENEMY(1);

    private final int code;

    Team(int code) {
        this.code = code;
    }

    public static Team read(Scanner scanner) {
        int code = scanner.nextInt();
        for (Team team : Team.values()) {
            if (team.code == code) {
                return team;
            }
        }
        throw new IllegalArgumentException("No Team found with code " + code);
    }
}
