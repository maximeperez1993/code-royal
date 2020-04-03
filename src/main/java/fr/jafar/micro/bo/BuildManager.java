package fr.jafar.micro.bo;

import fr.jafar.Manager;
import fr.jafar.site.Site;
import fr.jafar.site.StructureType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BuildManager {

    private final BuildLocationManager buildLocationManager;
    private final List<Site> sitesOfBo;
    private int buildOrderIndex = 0;

    public BuildManager(Manager manager) {
        this.buildLocationManager = new BuildLocationManager(manager);
        this.sitesOfBo = new ArrayList<>();
    }

    public Optional<String> build() {
        return this.rebuild().or(this::buildNext);
    }

    private Optional<String> rebuild() {
        for (int i = 0; i < buildOrderIndex; i++) {
            if (!sitesOfBo.get(i).isFriendly()) {
                return Optional.of(build(BuildOrder.BUILD_ORDER.get(i)));
            }
        }
        return Optional.empty();
    }

    private Optional<String> buildNext() {
        if (buildOrderIndex < BuildOrder.BUILD_ORDER.size()) {
            return Optional.of(build(BuildOrder.BUILD_ORDER.get(buildOrderIndex++)));
        }
        return Optional.empty();
    }

    private String build(BuildRequest request) {
        request.check();
        Site site = this.buildLocationManager.get(request.getOption());
        this.sitesOfBo.add(site);
        if (request.getStructureType() == StructureType.BARRACKS) {
            return String.format("BUILD %d %s-%s", site.getId(), request.getStructureType(), request.getUnitType());
        }
        return String.format("BUILD %d %s", site.getId(), request.getStructureType());
    }

}