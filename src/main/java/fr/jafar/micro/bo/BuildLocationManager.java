package fr.jafar.micro.bo;

import fr.jafar.Manager;
import fr.jafar.api.Finder;
import fr.jafar.site.Site;
import fr.jafar.site.SiteManager;
import fr.jafar.util.MapInfos;

public class BuildLocationManager {

    private final SiteManager siteManager;

    public BuildLocationManager(Manager manager) {
        this.siteManager = manager.getSiteManager();
    }

    public Site get(BuildRequest.Option option) {
        if (option == BuildRequest.Option.AGGRESSIVE) {
            return getAggressive();
        }
        return getPassive();
    }

    private Site getAggressive() {
        return new Finder<>(siteManager.getNeutralSites()).sortByFarthestFrom(MapInfos.MIDDLE).get();
    }

    private Site getPassive() {
        return new Finder<>(siteManager.getNeutralSites()).sortByFarthestFrom(siteManager.getMyStartSite()).get();
    }
}
