package fr.jafar.brain.macro.bo;

import fr.jafar.brain.macro.BuildRequest;
import fr.jafar.brain.micro.Escaper;
import fr.jafar.info.Manager;
import fr.jafar.structure.Position;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.site.StructureType;
import fr.jafar.structure.unit.Unit;
import fr.jafar.structure.unit.UnitType;

import java.util.List;
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
        if (i.isCloserSoldierUnder(2000)) {
            Optional<Site> freeAndSafeSite = getFreeAndSafeSite();
            if (freeAndSafeSite.isPresent()) {
                return this.build(freeAndSafeSite.get()).log("Build to free & safe site").build();
            }
            Optional<Site> safeTower = getSafeTower();
            if (safeTower.isPresent()) {
                Site safeTowerSite = safeTower.get();
                Unit hisSoldier = manager.getUnitManager().getHisSoldiers().stream().min((o1, o2) -> (int) (o1.getDistance(manager.getUnitManager().getMyQueen()) - o2.getDistance(manager.getUnitManager().getMyQueen()))).get();
                if (safeTowerSite.getPosition().isBetween(hisSoldier.getPosition(), manager.getUnitManager().getMyQueen().getPosition(), 100)) {
                    return this.build(safeTowerSite).log("Build to next safe tower").build();
                }
                Position behindTower = escaper.getEscapePositions(safeTowerSite).stream()
                        .filter(safePosition -> safeTowerSite.getPosition().isBetween(hisSoldier.getPosition(), safePosition, 100))
                        .findFirst()
                        .get();
                System.err.println("Try to go behind tower at " + behindTower);
                return move(behindTower);
            }
            return this.build(i.getClosestFreeSite()).log("Build to closer site").build();
        }
        if (i.isTouchSiteTowerLowHp()) {
            return upgrade(i.getTouchedSite());
        }
        return this.build(i.getClosestFreeSite()).log("Soldiers far away, we can try to build").build();
    }

    private boolean hasTimeToBuild(Site closestFreeSite) {
        Unit hisSoldier = manager.getUnitManager().getHisSoldiers().stream().min((o1, o2) -> (int) (o1.getDistance(manager.getUnitManager().getMyQueen()) - o2.getDistance(manager.getUnitManager().getMyQueen()))).get();
        double hisDistance = closestFreeSite.getDistance(hisSoldier);
        double myDistance = closestFreeSite.getDistance(manager.getUnitManager().getMyQueen());
        return hisDistance * 0.9 > myDistance;
    }

    private Optional<Site> getFreeAndSafeSite() {
        Position hisSoldier = manager.getUnitManager().getHisSoldiers().get(0).getPosition();
        List<Site> myTowers = manager.getSiteManager().getMyTowers();
        return manager.getSiteManager().getNeutralSites().stream()
                .filter(site -> myTowers.stream().anyMatch(tower -> tower.getPosition().isBetween(site.getPosition(), hisSoldier, 100)))
                .filter(this::hasTimeToBuild)
                .min((o1, o2) -> (int) (o1.getDistance(manager.getUnitManager().getMyQueen()) - o2.getDistance(manager.getUnitManager().getMyQueen())));
    }

    private Optional<Site> getSafeTower() {
        Position hisSoldier = manager.getUnitManager().getHisSoldiers().get(0).getPosition();
        List<Site> myTowers = manager.getSiteManager().getMyTowers();
        return myTowers.stream()
                .filter(site -> myTowers.stream().anyMatch(tower -> tower.getPosition().isBetween(site.getPosition(), hisSoldier, 100)))
                .filter(this::hasTimeToBuild)
                .max((o1, o2) -> (int) (o1.getDistance(manager.getUnitManager().getMyQueen()) - o2.getDistance(manager.getUnitManager().getMyQueen())));
    }


    @Override
    public BuildRequest build(Site site) {
        if (!hasBarracks() && hasTower()) {
            return new BuildRequest().a(StructureType.BARRACKS).of(UnitType.KNIGHT).at(site);
        }
        return new BuildRequest().a(StructureType.TOWER).at(site);
    }

    private boolean hasBarracks() {
        return manager.getSiteManager().getMySites().stream().anyMatch(Site::isBarrack);
    }

    private boolean hasTower() {
        return manager.getSiteManager().getMySites().stream().anyMatch(Site::isTower);
    }
}
