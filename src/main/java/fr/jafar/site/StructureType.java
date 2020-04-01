package fr.jafar.site;

import java.util.Scanner;

enum StructureType {
    NO_STRUCTURE(-1),
    BARRACKS(2);

    private final int code;

    StructureType(int code) {
        this.code = code;
    }

    public static StructureType read(Scanner scanner) {
        int code = scanner.nextInt();
        for (StructureType structureType : StructureType.values()) {
            if (structureType.code == code) {
                return structureType;
            }
        }
        throw new IllegalArgumentException("No structure type found with code " + code);
    }
}
