package fr.jafar;

import fr.jafar.unit.Unit;
import fr.jafar.unit.UnitManager;
import fr.jafar.util.Position;

import java.util.List;

public class Escaper {

    private final Unit myQueen;
    private final UnitManager unitManager;

    public Escaper(Unit myQueen, UnitManager unitManager) {
        this.myQueen = myQueen;
        this.unitManager = unitManager;
    }

    public Position getFarthestPosition(List<Position> positions) {
        return positions.stream()
                .max((p1, p2) -> (int) (sumDistance(p1) - sumDistance(p2)))
                .orElseThrow(IllegalStateException::new);
    }

    private double sumDistance(Position position) {
        return this.unitManager.getHisSoldiers().stream()
                .mapToDouble(unit -> position.getDistance(unit.getPosition()))
                .sum();
    }

}
