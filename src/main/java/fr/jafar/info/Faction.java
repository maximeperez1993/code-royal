package fr.jafar.info;

import fr.jafar.structure.Team;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.unit.Unit;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Faction {

    private final Team team;
    private final Unit queen;
    private final List<Unit> knights;

    private final List<Site> sites;
    private final List<Site> barracks;
    private final List<Site> readyBarracks;
    private final List<Site> trainingKnightBarracks;
    private final List<Site> mines;
    private final List<Site> towers;

    public Faction(Team team, List<Site> allSites, List<Unit> allUnits) {
        this.team = team;

        List<Unit> units = allUnits.stream().filter(this::isInTeam).collect(Collectors.toList());
        this.queen = units.stream().filter(Unit::isQueen).findFirst().orElse(null);
        this.knights = units.stream().filter(Unit::isKnight).collect(Collectors.toList());

        this.sites = allSites.stream().filter(this::isInTeam).collect(Collectors.toList());
        this.barracks = sites.stream().filter(Site::isBarrack).collect(Collectors.toList());
        this.readyBarracks = barracks.stream().filter(Site::isReady).collect(Collectors.toList());
        this.trainingKnightBarracks = barracks.stream().filter(Site::isKnightBarrack).filter(Site::isTraining).collect(Collectors.toList());
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

    public Stream<Site> mines() {
        return mines.stream();
    }

    public Stream<Site> towers() {
        return towers.stream();
    }

    private boolean isInTeam(Unit unit) {
        return unit.getTeam() == this.team;
    }

    private boolean isInTeam(Site site) {
        return site.getTeam() == this.team;
    }
}
