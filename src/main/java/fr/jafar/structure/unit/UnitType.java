package fr.jafar.structure.unit;

import fr.jafar.structure.EnumCG;

import java.util.Scanner;

public enum UnitType implements EnumCG {

    QUEEN(-1),
    KNIGHT(0),
    ARCHER(1),
    GIANT(2);

    private final int code;

    UnitType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public static UnitType read(Scanner in) {
        return EnumCG.find(in.nextInt(), values());
    }
}
