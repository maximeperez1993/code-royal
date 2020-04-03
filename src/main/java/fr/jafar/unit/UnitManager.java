package fr.jafar.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UnitManager {

    private final List<Unit> units;
    private final Unit myQueen;
    private final Unit hisQueen;

    public UnitManager(List<Unit> units) {
        this.units = units;
        this.myQueen = units.stream().filter(Unit::isMyQueen).findFirst().orElseThrow(() -> new IllegalStateException("No Friendly Queen"));
        this.hisQueen = units.stream().filter(Unit::isHisQueen).findFirst().orElseThrow(() -> new IllegalStateException("No Enemy Queen"));
    }

    public Unit getMyQueen() {
        return myQueen;
    }

    public Unit getHisQueen() {
        return hisQueen;
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
