package fr.jafar.brain.macro.bo;

import fr.jafar.brain.macro.BuildRequest;
import fr.jafar.brain.micro.Escaper;
import fr.jafar.brain.micro.PathFinder;
import fr.jafar.info.Manager;
import fr.jafar.structure.Positionable;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.site.StructureType;
import fr.jafar.structure.unit.UnitType;
import fr.jafar.util.comparators.MyComparators;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AggressiveAttitude implements Attitude {

    private final Manager manager;
    private final Escaper escaper;
    private final PathFinder pathFinder;

    public AggressiveAttitude(Manager manager, Escaper escaper) {
        this.manager = manager;
        this.escaper = escaper;
        this.pathFinder = new PathFinder(manager);
    }

    @Override
    public String order(StateInfo i) {
        Optional<Site> hisBarrack = manager.his().barracks()
                .filter(barrack -> barrack.getDistance(manager.my().queen()) < 200)
                .min(MyComparators.distanceFrom(manager.my().queen()));
        if (hisBarrack.isPresent() && !isEnemyTrainingSoldiers()) {
            return pathFinder.goBuild(this.build(hisBarrack.get()).log("Destroy his barrack (distance < 200)"));
        }

        if (i.isTouchSiteUpdatable() && i.getTouchedSite().isMine()) {
            return pathFinder.goBuild(upgrade(i.getTouchedSite()).log("Upgrade mine"));
        }

        if (manager.my().towers().count() >= 5) {
            Optional<Site> towerToReplace = manager.my().safeTowersFrom(manager.his()).filter(Site::hasRemainingGold).findFirst();
            if (towerToReplace.isPresent()) {
                return pathFinder.goBuild(new BuildRequest().a(StructureType.MINE).at(towerToReplace.get()).log("Replace tower because i'm safe"));
            }
        }

        if (i.isTouchSiteUpdatable()) {
            return pathFinder.goBuild(upgrade(i.getTouchedSite()).log("Upgrade tower"));
        }

        return pathFinder.goBuild(this.build(i.getClosestSafeFreeSite()).log("Build closest"));
    }

    @Override
    public BuildRequest build(Site site) {
        if (!hasBarracks()) {
            return new BuildRequest().a(StructureType.BARRACKS).of(UnitType.KNIGHT).at(site);
        }

        if (manager.my().towers().count() < 3 && isEnemyTrainingSoldiers()) {
            return new BuildRequest().a(StructureType.TOWER).at(site).log("Enemy is training soldiers");
        }

        // TODO : if is in our safe zone, maybe it's better to build a mine
        if (site.hasRemainingGold() && manager.my().towers().findAny().isPresent() && !isAtFrontLine(site)) {
            return new BuildRequest().a(StructureType.MINE).at(site);
        }

        return new BuildRequest().a(StructureType.TOWER).at(site);
    }

    private boolean isAtFrontLine(Site site) {
        Positionable danger;
        Optional<Site> hisBarrack = manager.his().trainingKnightBarracks().findFirst();
        if (hisBarrack.isPresent()) {
            danger = hisBarrack.get();
        } else {
            danger = manager.his().queen();
        }

        List<Site> mySitesAndTarget = manager.my().sites().collect(Collectors.toList());
        mySitesAndTarget.add(site);
        mySitesAndTarget.sort(MyComparators.distanceFrom(danger));
        System.err.println(mySitesAndTarget);
        if (mySitesAndTarget.size() < 2) {
            return true;
        }
        return mySitesAndTarget.get(0) == site || mySitesAndTarget.get(1) == site;

    }

    private boolean hasBarracks() {
        return manager.my().barracks().findAny().isPresent();
    }

    private boolean isEnemyTrainingSoldiers() {
        return manager.his().trainingKnightBarracks().findAny().isPresent();
    }
}
