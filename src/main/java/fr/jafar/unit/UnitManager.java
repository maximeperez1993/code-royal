package fr.jafar.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UnitManager {

    private final List<Unit> units;

    public UnitManager(List<Unit> units) {
        this.units = units;
    }

    public static UnitManager read(Scanner in) {
        int numSites = in.nextInt();
        List<Unit> units = new ArrayList<>();
        while (numSites-- > 0) {
            units.add(Unit.read(in));
        }
        return new UnitManager(units);
    }
}
