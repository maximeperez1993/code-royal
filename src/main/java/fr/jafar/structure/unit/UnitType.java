package fr.jafar.structure.unit;

import fr.jafar.structure.EnumCG;

import java.util.Scanner;

public enum UnitType implements EnumCG {

    QUEEN(-1, 60, 30),
    KNIGHT(0, 100, 20),
    ARCHER(1, 75, 25),
    GIANT(2, 50, 40);

    private final int code;
    private final int maxSteps;
    private final int radius;

    UnitType(int code, int maxSteps, int radius) {
        this.code = code;
        this.maxSteps = maxSteps;
        this.radius = radius;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public int maxSteps() {
        return this.maxSteps;
    }

    public int getRadius() {
        return radius;
    }

    public static UnitType read(Scanner in) {
        return EnumCG.find(in.nextInt(), values());
    }

    public static UnitType findByCode(int code) {
        return EnumCG.find(code, values());
    }


}
