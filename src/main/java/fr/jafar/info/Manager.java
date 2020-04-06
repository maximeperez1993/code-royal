package fr.jafar.info;

import fr.jafar.structure.Team;
import fr.jafar.structure.site.SiteManager;
import fr.jafar.structure.unit.UnitManager;

import java.util.Scanner;

public class Manager {

    private final SiteManager siteManager;
    private UnitManager unitManager;

    private int startHp;

    private Faction my;
    private Faction his;
    private Faction free;

    public Manager(Scanner in) {
        this.siteManager = SiteManager.read(in);
    }

    public SiteManager getSiteManager() {
        return siteManager;
    }

    public UnitManager getUnitManager() {
        return unitManager;
    }

    public void update(Scanner in) {
        this.siteManager.update(in);
        this.unitManager = UnitManager.read(in);

        this.startHp = this.unitManager.getMyQueen().getHealth();
        this.my = new Faction(siteManager, unitManager, Team.FRIENDLY);
        this.his = new Faction(siteManager, unitManager, Team.ENEMY);
        this.free = new Faction(siteManager, unitManager, Team.NEUTRAL);
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
}
