package fr.jafar.brain.macro.bo;

import fr.jafar.brain.macro.BuildRequest;
import fr.jafar.brain.micro.Escaper;
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

    public DefensiveAttitude(Manager manager, Escaper escaper) {
        this.manager = manager;
        this.escaper = escaper;
    }

    @Override
    public String order(StateInfo i) {
        if (hasTimeToBuild(i.getClosestFreeSite())) {
            return this.build(i.getClosestFreeSite()).log("Has time to build to closest free site").build();
        }

        Unit hisCloserKnight = manager.his().knightCloserOf(manager.my().queen()).orElseThrow(IllegalStateException::new);
        System.err.println("HisCloserKnight=" + hisCloserKnight);
        long nbTowersBetweenKnightAndMe = manager.my().towers().filter(tower -> tower.isBetween(hisCloserKnight, manager.my().queen(), tower.getTowerRange())).count();

        if (nbTowersBetweenKnightAndMe > 3) {
            Optional<Site> safeTower = getSafeTower();
            if (safeTower.isPresent()) {
                return new BuildRequest().a(StructureType.MINE).at(safeTower.get()).log("Should have enough tower to def, let's build a mine at backlane").build();
            }
        }

        if (i.isCloserSoldierUnder(2000)) {
            Optional<Site> freeAndSafeSite = getFreeAndSafeSite();
            if (freeAndSafeSite.isPresent()) {
                return this.build(freeAndSafeSite.get()).log("Build to free & safe site").build();
            }
            Optional<Site> safeTower = getSafeTower();
            if (safeTower.isPresent()) {
                Site safeTowerSite = safeTower.get();
                if (safeTowerSite.isBetween(hisCloserKnight, manager.my().queen(), 100)) {
                    return this.build(safeTowerSite).log("Build to next safe tower " + safeTowerSite).build();
                }
                Position behindTower = behindSite(safeTowerSite, hisCloserKnight);
                System.err.println("Try to go behind safe tower " + safeTowerSite + " at " + behindTower);
                return move(behindTower);
            }
            Site closerTower = manager.my().towers().max(MyComparators.distanceFrom(hisCloserKnight)).get();
            Position behindTower = behindSite(closerTower, hisCloserKnight);
            System.err.println("Try to go behind closer tower " + closerTower + " at " + behindTower);
            return move(behindTower);
        }
        if (i.isTouchSiteTowerLowHp()) {
            return upgrade(i.getTouchedSite());
        }
        return this.build(i.getClosestFreeSite()).log("Soldiers far away, we can try to build").build();
    }

    private Position behindSite(Site site, Unit hisCloserKnight) {
        return escaper.getEscapePositions(site).stream()
                .filter(safePosition -> site.getPosition().isBetween(hisCloserKnight.getPosition(), safePosition, site.getRadius()))
                .max(MyComparators.distanceFrom(hisCloserKnight))
                .get();
    }

    private boolean hasTimeToBuild(Site closestFreeSite) {
        Unit hisKnight = manager.his().knightCloserOf(closestFreeSite).get();
        double hisDistance = closestFreeSite.getDistance(hisKnight);
        double myDistance = closestFreeSite.getDistance(manager.my().queen());
        //return hisDistance > myDistance / 0.6;
        return hisDistance * 0.7 > myDistance;
    }

    private Optional<Site> getFreeAndSafeSite() {
        Unit hisKnight = manager.his().knightCloserOf(manager.my().queen()).get();
        return manager.free().sites()
                .filter(site -> manager.my().towers().anyMatch(tower -> tower.getPosition().isBetween(site.getPosition(), hisKnight, 100)))
                .filter(this::hasTimeToBuild)
                .min(MyComparators.distanceFrom(manager.my().queen()));
    }

    private Optional<Site> getSafeTower() {
        Unit hisKnight = manager.his().knightCloserOf(manager.my().queen()).get();
        return manager.my().towers()
                .filter(towerSafe -> manager.my().towers().anyMatch(tower -> tower.isBetween(towerSafe, hisKnight, 100)))
                .filter(this::hasTimeToBuild)
                .max(MyComparators.distanceFrom(manager.my().queen()));
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
