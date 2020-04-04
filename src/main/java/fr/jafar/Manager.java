package fr.jafar;

import fr.jafar.structure.site.SiteManager;
import fr.jafar.structure.unit.UnitManager;
import fr.jafar.util.Finder;

import java.util.Scanner;

public class Manager {

    private final SiteManager siteManager;
    private UnitManager unitManager;


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
        if (this.siteManager.getMyStartSite() == null) {
            this.siteManager.setStartSites(
                    new Finder<>(this.siteManager.getSites()).sortByClosestFrom(unitManager.getMyQueen()).get(),
                    new Finder<>(this.siteManager.getSites()).sortByClosestFrom(unitManager.getHisQueen()).get()
            );
        }
    }
}
