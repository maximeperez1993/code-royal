package fr.jafar.brain.macro.bo;

import fr.jafar.info.Manager;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.unit.Unit;
import fr.jafar.util.Finder;

import java.util.stream.Collectors;

public class StateInfo {

    private final Manager manager;
    private final Unit myQueen;
    private final Site touchedSite;
    private final Site closestFreeSite;

    public StateInfo(Manager manager) {
        this.manager = manager;
        this.myQueen = this.manager.my().queen();
        this.touchedSite = this.manager.getTouchedSite().orElse(null);
        this.closestFreeSite = new Finder<>(this.manager.free().sites())
                .sortByClosestFrom(this.myQueen)
                .filterSafeFromEnemyTowerRange(manager.his().towers().collect(Collectors.toList())).getOptional()
                .orElseGet(() -> new Finder<>(this.manager.my().sites()).sortByClosestFrom(this.myQueen).get());
    }

    public boolean isTouchSiteUpdatable() {
        return this.touchedSite != null && this.touchedSite.isUpdatable();
    }

    public boolean isTouchSiteTowerLowHp() {
        return this.touchedSite != null && this.touchedSite.isFriendly() && this.touchedSite.isTowerLowHp();
    }

    public boolean isClosestFreeSiteAtRange() {
        return this.closestFreeSite != null && this.closestFreeSite.getDistance(myQueen) <= 60;
    }


    public Site getTouchedSite() {
        return touchedSite;
    }

    public Site getClosestFreeSite() {
        return this.closestFreeSite;
    }

    public boolean isSitesAllTaken() {
        return this.closestFreeSite == null;
    }

    public boolean isUnderAttack() {
        return this.manager.his().knights().findAny().isPresent();
    }

    public boolean isCloserSoldierUnder(int distance) {
        return this.manager.his().knights().anyMatch(soldier -> soldier.getDistance(myQueen) < distance);
    }

    public boolean isCloserSoldierMoreThan(int distance) {
        return this.manager.his().knights().anyMatch(soldier -> soldier.getDistance(myQueen) > distance);
    }
}
