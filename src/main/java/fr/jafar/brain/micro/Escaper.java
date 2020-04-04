package fr.jafar.brain.micro;

import fr.jafar.Manager;
import fr.jafar.structure.Position;
import fr.jafar.structure.unit.Unit;
import fr.jafar.util.MapInfos;

import java.util.ArrayList;
import java.util.List;

/*
 TODO :
  - take care about collisions with sites
  - take care about mine destruction
  - take care about enemy towers
  - take care about collisions with soldiers
  - best of the best : simulation
 */
public class Escaper {

    private final Manager manager;

    public Escaper(Manager manager) {
        this.manager = manager;
    }

    public Position getEscapePosition() {
        return this.getFarthestPosition(this.getEscapePositions());
    }

    public Position getFarthestPosition(List<Position> positions) {
        return positions.stream()
                .max((p1, p2) -> (int) (sumDistance(p1) - sumDistance(p2)))
                .orElseThrow(IllegalStateException::new);
    }

    private double sumDistance(Position position) {
        return this.manager.getUnitManager().getHisSoldiers().stream()
                .mapToDouble(soldier -> position.getDistance(soldier.getPosition()))
                .sum();
    }

    private List<Position> getEscapePositions() {
        Unit myQueen = this.manager.getUnitManager().getMyQueen();
        Position myQueenPosition = myQueen.getPosition();
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < 360; i += 10) {
            int x = (int) (60 * Math.cos(i));
            int y = (int) (60 * Math.sin(i));
            Position position = myQueenPosition.add(new Position(x, y));
            if (MapInfos.isOnMap(position)) {
                positions.add(position);
            }
        }
        return positions;
    }

}
