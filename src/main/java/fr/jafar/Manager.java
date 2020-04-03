package fr.jafar;

import fr.jafar.site.SiteManager;
import fr.jafar.unit.UnitManager;

import java.util.Scanner;

public class Manager {

    private final SiteManager siteManager;
    private UnitManager unitManager;


    public Manager(Scanner in) {
        this.siteManager = SiteManager.read(in);
    }

    public void update(Scanner in) {
        this.siteManager.update(in);
        this.unitManager = UnitManager.read(in);
    }

    public SiteManager getSiteManager() {
        return siteManager;
    }

    public UnitManager getUnitManager() {
        return unitManager;
    }
}
