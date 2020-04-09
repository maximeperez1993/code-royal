package fr.jafar.brain.macro.bo;

import fr.jafar.brain.macro.BuildRequest;
import fr.jafar.brain.micro.Escaper;
import fr.jafar.brain.micro.PathFinder;
import fr.jafar.info.Manager;
import fr.jafar.structure.Position;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.site.StructureType;
import fr.jafar.structure.unit.Unit;
import fr.jafar.structure.unit.UnitType;
import fr.jafar.util.comparators.MyComparators;

import java.util.Optional;

public class DefensiveAttitude implements Attitude {

    private final Manager manager;
    private final Escaper escaper;
    private final PathFinder pathFinder;

    public DefensiveAttitude(Manager manager, Escaper escaper) {
        this.manager = manager;
        this.escaper = escaper;
        this.pathFinder = new PathFinder(manager);
    }

    @Override
    public String order(StateInfo i) {
        long nbTowers = manager.my().towers().count();
        if (nbTowers < 3) {
            return pathFinder.goBuild(this.build(i.getClosestFreeSite()).log("Not enough tower to def (" + nbTowers + "), need to build one now"));
        }

        if (hasTimeToBuild(i.getClosestSafeFreeSite())) {
            return pathFinder.goBuild(this.build(i.getClosestSafeFreeSite()).log("Has time to build to closest free site"));
        }

        Unit hisCloserKnight = manager.his().knightCloserOf(manager.my().queen()).orElseThrow(IllegalStateException::new);
        System.err.println("HisCloserKnight=" + hisCloserKnight);


        if (isEnoughSafeToBuildMine(i, hisCloserKnight)) {
            Optional<Site> safeTower = manager.my().mostSafeTowerFrom(manager.his());
            if (safeTower.isPresent() && safeTower.get().hasRemainingGold()) {
                return pathFinder.goBuild(
                        new BuildRequest().a(StructureType.MINE).at(safeTower.get()).log("Should have enough tower to def, let's build a mine at backlane"));
            }
        }


        Optional<Site> freeAndSafeSite = getFreeAndSafeSite();
        if (freeAndSafeSite.isPresent()) {
            return pathFinder.goBuild(this.build(freeAndSafeSite.get()).log("Build to free & safe site"));
        }
        Optional<Site> safeTower = manager.my().mostSafeTowerFrom(manager.his());
        if (safeTower.isPresent()) {
            Site safeTowerSite = safeTower.get();
            if (safeTowerSite.isBetween(hisCloserKnight, manager.my().queen(), safeTowerSite.getRadius()) && safeTowerSite.getHealth() < 790) {
                return pathFinder.goBuild(this.build(safeTowerSite).log("Build to next safe tower " + safeTowerSite));
            }
            Position behindTower = escaper.behindSite(safeTowerSite, manager.my().getDangerMeanUnit(manager.his()));
            System.err.println("Try to go behind safe tower " + safeTowerSite + " at " + behindTower);
            return pathFinder.goTo(behindTower);
        }
        Site closerTower = manager.my().towers().max(MyComparators.distanceFrom(hisCloserKnight)).get();
        Position behindTower = escaper.behindSite(closerTower, manager.my().getDangerMeanUnit(manager.his()));
        System.err.println("Try to go behind closer tower " + closerTower + " at " + behindTower);
        return pathFinder.goTo(behindTower);
    }

    private boolean isEnoughSafeToBuildMine(StateInfo i, Unit hisCloserKnight) {
        long nbTowersBetweenKnightAndMe = manager.my().towersThatProtect(manager.my().queen(), hisCloserKnight).count();
        int maxDistance = 550;
        if (nbTowersBetweenKnightAndMe > 7) {
            maxDistance = 500;
        }
        if (nbTowersBetweenKnightAndMe > 8) {
            maxDistance = 450;
        }

        return nbTowersBetweenKnightAndMe > 4 && !i.isCloserSoldierUnder(maxDistance);
    }

    private boolean hasTimeToBuild(Site closestFreeSite) {
        Unit hisKnight = manager.his().knightCloserOf(closestFreeSite).get();
        double hisDistanceWithMe = manager.my().queen().getDistance(hisKnight);
        double hisDistance = closestFreeSite.getDistance(hisKnight);
        double myDistance = closestFreeSite.getDistance(manager.my().queen());
        // 0.6 <=> knight speed
        double factor = 0.6 - 0.2;
        return hisDistance * factor > myDistance &&
                hisDistanceWithMe * factor > myDistance;
    }

    private Optional<Site> getFreeAndSafeSite() {
        Unit hisKnight = manager.his().knightCloserOf(manager.my().queen()).get();
        return manager.free().sites()
                .filter(site -> manager.my().towersThatProtect(site, hisKnight).findAny().isPresent())
                .filter(this::hasTimeToBuild)
                .min(MyComparators.distanceFrom(manager.my().queen()));
    }

    @Override
    public BuildRequest build(Site site) {
        if (!hasBarracks() && hasTower()) {
            return new BuildRequest().a(StructureType.BARRACKS).of(UnitType.KNIGHT).at(site);
        }
        return new BuildRequest().a(StructureType.TOWER).at(site);
    }

    private boolean hasBarracks() {
        return manager.my().barracks().findAny().isPresent();
    }

    private boolean hasTower() {
        return !manager.my().towers().findAny().isPresent();
    }
}
