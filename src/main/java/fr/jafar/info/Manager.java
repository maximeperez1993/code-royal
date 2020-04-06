package fr.jafar.info;

import fr.jafar.structure.Team;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.unit.Unit;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Manager {

    private final List<Site> sites;

    private Site touchedSite;
    private int startHp = 0;

    private Faction my;
    private Faction his;
    private Faction free;

    public Manager(Scanner in) {
        this.sites = InfoReader.readSites(in);
    }

    public void update(Scanner in) {
        this.touchedSite = InfoReader.readTouchedSite(in, this.sites);
        InfoReader.readSitesStates(in, this.sites);
        List<Unit> units = InfoReader.readUnits(in);

        this.my = new Faction(Team.FRIENDLY, this.sites, units);
        this.his = new Faction(Team.ENEMY, this.sites, units);
        this.free = new Faction(Team.NEUTRAL, this.sites, units);
        this.startHp = startHp == 0 ? my.queen().getHealth() : startHp;
    }

    public Faction my() {
        return my;
    }

    public Faction his() {
        return his;
    }

    public Faction free() {
        return free;
    }

    public int getStartHp() {
        return startHp;
    }

    public Optional<Site> getTouchedSite() {
        return Optional.ofNullable(this.touchedSite);
    }
}
