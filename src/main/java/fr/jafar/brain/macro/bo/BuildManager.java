package fr.jafar.brain.macro.bo;

import fr.jafar.Manager;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.site.StructureType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BuildManager {

    private final BuildLocationManager buildLocationManager;
    private final List<Site> sitesOfBo;

    public BuildManager(Manager manager) {
        this.buildLocationManager = new BuildLocationManager(manager);
        this.sitesOfBo = new ArrayList<>();
    }

    public Optional<String> build() {
        Optional<String> upgradeNext = this.upgradeNext();
        if (upgradeNext.isPresent()) {
            return upgradeNext;
        }

        Optional<String> rebuild = this.rebuild();
        if (rebuild.isPresent()) {
            return rebuild;
        }

        Optional<String> buildNext = this.buildNext();
        if (buildNext.isPresent()) {
            return buildNext;
        }


        return Optional.empty();
    }

    private Optional<String> rebuild() {
        for (int i = 0; i < sitesOfBo.size(); i++) {
            Site site = sitesOfBo.get(i);
            BuildRequest buildRequest = BuildOrder.BUILD_ORDER.get(i);
            if (!site.isFriendly() && !site.isEnemyTower()) {
                if (isTryingToMineAnEmptySpot(site, buildRequest)) {
                    buildRequest.a(StructureType.TOWER);
                    System.err.println("Replace by a tower:" + buildRequest + " " + site);
                    return Optional.of(build(buildRequest, site));
                }

                System.err.println("Rebuild:" + buildRequest + " " + site);
                return Optional.of(build(buildRequest, site));
            }
        }
        return Optional.empty();
    }

    private boolean isTryingToMineAnEmptySpot(Site site, BuildRequest buildRequest) {
        return buildRequest.getStructureType() == StructureType.MINE && site.hasNoRemainingGold();
    }

    private Optional<String> buildNext() {
        if (sitesOfBo.size() < BuildOrder.BUILD_ORDER.size()) {
            BuildRequest buildRequest = BuildOrder.BUILD_ORDER.get(sitesOfBo.size());
            System.err.println("Build next:" + buildRequest);
            return Optional.of(build(buildRequest));
        }
        return Optional.empty();
    }

    private Optional<String> upgradeNext() {
        for (int i = 0; i < sitesOfBo.size(); i++) {
            Site site = sitesOfBo.get(i);
            BuildRequest buildRequest = BuildOrder.BUILD_ORDER.get(i);
            if (site.isMineUpgradable()) {
                System.err.println("Upgrade:" + buildRequest + " " + site);
                return Optional.of(build(buildRequest, site));
            }

            if (site.isTowerUpgradable()) {
                System.err.println("Upgrade:" + buildRequest);
                return Optional.of(build(buildRequest, site));
            }
        }
        return Optional.empty();
    }

    private String build(BuildRequest request) {
        request.check();
        Site site = this.buildLocationManager.get(request.getOption());
        this.sitesOfBo.add(site);
        return build(request, site);
    }


    private String build(BuildRequest request, Site site) {
        if (request.getStructureType() == StructureType.BARRACKS) {
            return String.format("BUILD %d %s-%s", site.getId(), request.getStructureType(), request.getUnitType());
        }
        return String.format("BUILD %d %s", site.getId(), request.getStructureType());
    }

}