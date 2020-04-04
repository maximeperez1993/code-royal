package fr.jafar.structure.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UnitManager {

    private final List<Unit> units;

    private final Unit myQueen;
    private final Unit hisQueen;

    private final List<Unit> mySoldiers;
    private final List<Unit> hisSoldiers;

    public UnitManager(List<Unit> units) {
        this.units = units;
        this.myQueen = units.stream().filter(Unit::isMyQueen).findFirst().orElseThrow(() -> new IllegalStateException("No Friendly Queen"));
        this.hisQueen = units.stream().filter(Unit::isHisQueen).findFirst().orElseThrow(() -> new IllegalStateException("No Enemy Queen"));

        this.mySoldiers = units.stream().filter(Unit::isMySoldier).collect(Collectors.toList());
        this.hisSoldiers = units.stream().filter(Unit::isHisSoldier).collect(Collectors.toList());
    }

    public Unit getMyQueen() {
        return myQueen;
    }

    public Unit getHisQueen() {
        return hisQueen;
    }

    public List<Unit> getMySoldiers() {
        return mySoldiers;
    }

    public List<Unit> getHisSoldiers() {
        return hisSoldiers;
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
