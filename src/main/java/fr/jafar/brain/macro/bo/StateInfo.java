package fr.jafar.brain.macro.bo;

import fr.jafar.Manager;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.unit.Unit;
import fr.jafar.util.Finder;

import java.util.List;

public class StateInfo {

    private final Manager manager;
    private final Unit myQueen;
    private final List<Site> neutralSites;
    private final Site touchedSite;
    private final Site closestFreeSite;


    public StateInfo(Manager manager) {
        this.manager = manager;
        this.myQueen = this.manager.getUnitManager().getMyQueen();
        this.neutralSites = this.manager.getSiteManager().getNeutralSites();
        this.touchedSite = this.manager.getSiteManager().getTouchedSite().orElse(null);
        this.closestFreeSite = new Finder<>(this.manager.getSiteManager().getNeutralSites()).sortByClosestFrom(this.myQueen).getOptional().orElse(null);
    }

    public boolean isTouchSiteUpdatable() {
        return this.touchedSite != null && this.touchedSite.isUpdatable();
    }

    public boolean isClosestFreeSiteAtRange() {
        return this.closestFreeSite != null && this.closestFreeSite.getDistance(myQueen) <= 60;
    }

    public Site getTouchedSite() {
        return touchedSite;
    }

    public Site getClosestFreeSite() {
        return closestFreeSite;
    }

    public boolean isUnderAttack() {
        return this.manager.getUnitManager().getHisSoldiers().stream().anyMatch(soldier -> soldier.getDistance(myQueen) < 500);
    }
}
