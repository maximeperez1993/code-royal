package fr.jafar.unit;

import java.util.Scanner;

public enum UnitType {

    QUEEN(-1),
    KNIGHT(0),
    ARCHER(1),
    GIANT(2);

    private final int code;

    UnitType(int code) {
        this.code = code;
    }

    public static UnitType read(Scanner in) {
        int code = in.nextInt();
        for (UnitType structureType : UnitType.values()) {
            if (structureType.code == code) {
                return structureType;
            }
        }
        throw new IllegalArgumentException("No UnitType found with code " + code);
    }
}
