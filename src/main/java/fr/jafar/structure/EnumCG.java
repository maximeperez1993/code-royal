package fr.jafar.structure;

public interface EnumCG {

    int getCode();

    static <T extends EnumCG> T find(int code, T[] enumCGs) {
        for (T enumCG : enumCGs) {
            if (enumCG.getCode() == code) {
                return enumCG;
            }
        }
        throw new IllegalArgumentException("No " + enumCGs[0].getClass().getName() + " found with code " + code);
    }
}
