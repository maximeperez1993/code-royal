package fr.jafar.info;

import fr.jafar.structure.Position;
import fr.jafar.structure.Positionable;
import fr.jafar.structure.Team;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.unit.Unit;
import fr.jafar.util.MapInfos;
import fr.jafar.util.comparators.MyComparators;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Faction {

    private static final Map<Team, Position> START_POSITIONS = new HashMap<>();

    private final Team team;
    private final Unit queen;
    private final List<Unit> knights;

    private final List<Site> sites;
    private final List<Site> barracks;
    private final List<Site> knightBarracks;
    private final List<Site> readyBarracks;
    private final List<Site> trainingKnightBarracks;
    private final List<Site> mines;
    private final List<Site> towers;

    public Faction(Team team, List<Site> allSites, List<Unit> allUnits) {
        this.team = team;

        List<Unit> units = allUnits.stream().filter(this::isInTeam).collect(Collectors.toList());
        this.queen = units.stream().filter(Unit::isQueen).findFirst().orElse(null);
        this.knights = units.stream().filter(Unit::isKnight).collect(Collectors.toList());

        START_POSITIONS.computeIfAbsent(team, (k) -> setStartPosition(this.queen));
        this.sites = allSites.stream().filter(this::isInTeam).collect(Collectors.toList());
        this.barracks = sites.stream().filter(Site::isBarrack).collect(Collectors.toList());
        this.knightBarracks = barracks.stream().filter(Site::isKnightBarrack).collect(Collectors.toList());
        this.readyBarracks = barracks.stream().filter(Site::isReady).collect(Collectors.toList());
        this.trainingKnightBarracks = knightBarracks.stream().filter(Site::isTraining).collect(Collectors.toList());
        this.mines = sites.stream().filter(Site::isMine).collect(Collectors.toList());
        this.towers = sites.stream().filter(Site::isTower).collect(Collectors.toList());
    }

    public Unit queen() {
        return queen;
    }

    public Stream<Unit> knights() {
        return knights.stream();
    }

    public Stream<Site> sites() {
        return sites.stream();
    }

    public Stream<Site> barracks() {
        return barracks.stream();
    }

    public Stream<Site> readyBarracks() {
        return readyBarracks.stream();
    }

    public Stream<Site> trainingKnightBarracks() {
        return trainingKnightBarracks.stream();
    }

    public Stream<Site> knightBarracks() {
        return knightBarracks.stream();
    }

    public Stream<Site> mines() {
        return mines.stream();
    }

    public Stream<Site> towers() {
        return towers.stream();
    }

    public Stream<Site> towersThatProtect(Positionable elementToProtect, Positionable from) {
        return towers.stream().filter(tower -> tower.isBetween(elementToProtect, from, tower.getRadius() + tower.getTowerRange() + from.getRadius()));
    }

    public Optional<Site> mostSafeTowerFrom(Faction other) {
        Optional<Positionable> optionalDanger = getDanger(other);
        if (!optionalDanger.isPresent()) {
            return towers().max(Comparator.comparingDouble(tower -> tower.getDistance(startPosition())));
        }
        Positionable danger = optionalDanger.get();
        long max = towers().peek(tower -> System.err.println("tower:" + tower.getId() + " protected by " + towersThatProtect(tower, danger).count() + "towers"))
                .mapToLong(tower -> towersThatProtect(tower, danger).count())
                .max().orElse(0);
        return towers()
                .filter(tower -> towersThatProtect(tower, danger).count() == max)
                .max(Comparator.comparingDouble(tower -> tower.getDistance(danger)));
    }

    public Stream<Site> safeTowersFrom(Faction other) {
        Optional<Positionable> danger = getDanger(other);
        if (danger.isPresent()) {
            return towers().sorted(Comparator.comparingLong(tower -> towersThatProtect(tower, danger.get()).count()));
        }
        return towers().sorted(Comparator.comparingDouble(tower -> tower.getDistance(startPosition())));
    }

    private Optional<Positionable> getDanger(Faction other) {
        Optional<Site> barrack = other.knightBarrackCloserOf(queen());
        if (barrack.isPresent()) {
            return Optional.of(barrack.get());
        }
        Optional<Unit> knight = other.knightCloserOf(queen());
        if (knight.isPresent()) {
            return Optional.of(knight.get());
        }
        return Optional.empty();
    }


    public Optional<Unit> knightCloserOf(Positionable positionable) {
        return knights.stream().min(MyComparators.distanceFrom(positionable));
    }

    public Optional<Site> knightBarrackCloserOf(Positionable positionable) {
        return knightBarracks().min(MyComparators.distanceFrom(positionable));
    }

    public int currentIncome() {
        return mines.stream().mapToInt(Site::getMineLevel).sum();
    }

    public Position startPosition() {
        return START_POSITIONS.get(team);
    }

    private boolean isInTeam(Unit unit) {
        return unit.getTeam() == this.team;
    }

    private boolean isInTeam(Site site) {
        return site.getTeam() == this.team;
    }

    private static Position setStartPosition(Unit queen) {
        if (queen == null) {
            return null;
        }
        if (queen.getDistance(MapInfos.TOP_LEFT) < queen.getDistance(MapInfos.BOTTOM_RIGHT)) {
            return MapInfos.TOP_LEFT;
        }
        return MapInfos.TOP_RIGHT;
    }
}
