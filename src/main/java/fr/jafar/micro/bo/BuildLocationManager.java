package fr.jafar.micro.bo;

import fr.jafar.Manager;
import fr.jafar.api.Finder;
import fr.jafar.site.Site;
import fr.jafar.site.SiteManager;
import fr.jafar.util.MapInfos;

public class BuildLocationManager {

    private final Manager manager;
    private final SiteManager siteManager;

    public BuildLocationManager(Manager manager) {
        this.manager = manager;
        this.siteManager = manager.getSiteManager();
    }

    public Site get(BuildRequest.Option option) {
        if (option == BuildRequest.Option.FRONTIER) {
            return getAggressive();
        }
        if (option == BuildRequest.Option.CLOSEST) {
            return getClosest();
        }
        return getPassive();
    }

    private Site getAggressive() {
        return new Finder<>(siteManager.getNeutralSites()).sortByClosestFrom(MapInfos.MIDDLE).get();
    }

    private Site getClosest() {
        return new Finder<>(siteManager.getNeutralSites()).sortByClosestFrom(manager.getUnitManager().getMyQueen()).get();
    }

    private Site getPassive() {
        return new Finder<>(siteManager.getNeutralSites()).sortByClosestFrom(siteManager.getMyStartSite()).get();
    }

}
