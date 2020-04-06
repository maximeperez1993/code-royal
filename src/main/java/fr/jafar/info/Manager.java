package fr.jafar.info;

import fr.jafar.structure.Team;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.site.SiteManager;
import fr.jafar.structure.unit.UnitManager;

import java.util.Optional;
import java.util.Scanner;

public class Manager {

    private final SiteManager siteManager;

    private int startHp = 0;

    private Faction my;
    private Faction his;
    private Faction free;

    public Manager(Scanner in) {
        this.siteManager = SiteManager.read(in);
    }

    public void update(Scanner in) {
        this.siteManager.update(in);
        UnitManager unitManager = UnitManager.read(in);

        this.my = new Faction(siteManager, unitManager, Team.FRIENDLY);
        this.his = new Faction(siteManager, unitManager, Team.ENEMY);
        this.free = new Faction(siteManager, unitManager, Team.NEUTRAL);
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
        return siteManager.getTouchedSite();
    }
}
