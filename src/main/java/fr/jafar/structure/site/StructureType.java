package fr.jafar.structure.site;

import fr.jafar.structure.EnumCG;

import java.util.Scanner;

public enum StructureType implements EnumCG {
    NO_STRUCTURE(-1),
    MINE(0),
    TOWER(1),
    BARRACKS(2);

    private final int code;

    StructureType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public static StructureType read(Scanner in) {
        return EnumCG.find(in.nextInt(), values());
    }


}
