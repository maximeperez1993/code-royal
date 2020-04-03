package fr.jafar.site;

import fr.jafar.api.EnumCG;

import java.util.Scanner;

enum StructureType implements EnumCG {
    NO_STRUCTURE(-1),
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
