package fr.jafar.brain.micro;

import java.util.ArrayList;
import java.util.List;

import fr.jafar.info.Manager;
import fr.jafar.structure.Position;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.unit.Unit;
import fr.jafar.structure.unit.UnitType;
import fr.jafar.util.MapInfos;

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
        return this.manager.his().knights()
                .mapToDouble(soldier -> position.getDistance(soldier.getPosition()))
                .sum();
    }

    public List<Position> getEscapePositions() {
        return getEscapePositions(this.manager.my().queen());
    }

    public List<Position> getEscapePositions(Unit unit) {
        Position myQueenPosition = unit.getPosition();
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < 360; i += 10) {
            int x = (int) (unit.getMaxSteps() * Math.cos(i));
            int y = (int) (unit.getMaxSteps() * Math.sin(i));
            Position position = myQueenPosition.add(new Position(x, y));
            if (MapInfos.isOnMap(position)) {
                positions.add(position);
            }
        }
        return positions;
    }

    public List<Position> getEscapePositions(Site site) {
        Position sitePosition = site.getPosition();
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < 360; i += 2) {
            int x = (int)(site.getRadius() + UnitType.QUEEN.getRadius() * Math.cos(i));
            int y = (int)(site.getRadius() + UnitType.QUEEN.getRadius() * Math.sin(i));
            Position position = sitePosition.add(new Position(x, y));
            if (MapInfos.isOnMap(position)) {
                positions.add(position);
            }
        }
        return positions;
    }

}
