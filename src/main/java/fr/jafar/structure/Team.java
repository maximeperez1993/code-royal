package fr.jafar.structure;

import java.util.Scanner;

public enum Team implements EnumCG {

    NEUTRAL(-1),
    FRIENDLY(0),
    ENEMY(1);

    private final int code;

    Team(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public static Team read(Scanner in) {
        return EnumCG.find(in.nextInt(), values());
    }
}
