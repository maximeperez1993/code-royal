package fr.jafar.info;

import fr.jafar.structure.Team;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.site.SiteManager;
import fr.jafar.structure.unit.Unit;
import fr.jafar.structure.unit.UnitManager;

import java.util.List;
import java.util.stream.Collectors;

public class Faction {

    private final Team team;
    private final Unit queen;
    private final List<Unit> knights;

    private final List<Site> sites;
    private final List<Site> barracks;
    private final List<Site> readyBarracks;
    private final List<Site> mines;
    private final List<Site> towers;

    public Faction(SiteManager siteManager, UnitManager unitManager, Team team) {
        this.team = team;
        List<Unit> units = unitManager.getUnits().stream().filter(this::isInTeam).collect(Collectors.toList());
        this.queen = units.stream().filter(Unit::isQueen).findFirst().orElseThrow(() -> new IllegalStateException("No Queen " + team));
        this.knights = units.stream().filter(Unit::isKnight).collect(Collectors.toList());

        this.sites = siteManager.getSites().stream().filter(this::isInTeam).collect(Collectors.toList());
        this.barracks = sites.stream().filter(Site::isBarrack).collect(Collectors.toList());
        this.readyBarracks = barracks.stream().filter(Site::isReady).collect(Collectors.toList());
        this.mines = sites.stream().filter(Site::isMine).collect(Collectors.toList());
        this.towers = sites.stream().filter(Site::isTower).collect(Collectors.toList());
    }

    public Unit queen() {
        return queen;
    }

    public List<Unit> knights() {
        return knights;
    }

    public List<Site> sites() {
        return sites;
    }

    public List<Site> barracks() {
        return barracks;
    }

    public List<Site> readyBarracks() {
        return readyBarracks;
    }

    public List<Site> mines() {
        return mines;
    }

    public List<Site> towers() {
        return towers;
    }

    private boolean isInTeam(Unit unit) {
        return unit.getTeam() == this.team;
    }

    private boolean isInTeam(Site site) {
        return site.getTeam() == this.team;
    }
}
