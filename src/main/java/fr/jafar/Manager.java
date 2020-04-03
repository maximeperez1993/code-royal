package fr.jafar;

import fr.jafar.api.Finder;
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
        if (this.siteManager.getMyStartSite() == null) {
            this.siteManager.setStartSites(
                    new Finder<>(this.siteManager.getSites()).sortByClosestFrom(unitManager.getMyQueen()).get(),
                    new Finder<>(this.siteManager.getSites()).sortByClosestFrom(unitManager.getHisQueen()).get()
            );
        }

    }

    public SiteManager getSiteManager() {
        return siteManager;
    }

    public UnitManager getUnitManager() {
        return unitManager;
    }
}
