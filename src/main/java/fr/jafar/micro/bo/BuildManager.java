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

    public BuildManager(Manager manager) {
        this.buildLocationManager = new BuildLocationManager(manager);
        this.sitesOfBo = new ArrayList<>();
    }

    public Optional<String> build() {
        Optional<String> rebuild = this.rebuild();
        if (rebuild.isPresent()) {
            return this.rebuild();
        }
        return buildNext();
    }

    private Optional<String> rebuild() {
        for (int i = 0; i < sitesOfBo.size(); i++) {
            if (!sitesOfBo.get(i).isFriendly()) {
                return Optional.of(rebuild(BuildOrder.BUILD_ORDER.get(i)));
            }
        }
        return Optional.empty();
    }

    private Optional<String> buildNext() {
        if (sitesOfBo.size() < BuildOrder.BUILD_ORDER.size()) {
            BuildRequest buildRequest = BuildOrder.BUILD_ORDER.get(sitesOfBo.size());
            System.err.println(buildRequest);
            return Optional.of(build(buildRequest));
        }
        return Optional.empty();
    }

    private String build(BuildRequest request) {
        request.check();
        Site site = this.buildLocationManager.get(request.getOption());
        this.sitesOfBo.add(site);
        System.err.println(sitesOfBo);
        return build(request, site);
    }

    private String rebuild(BuildRequest request) {
        request.check();
        Site site = this.buildLocationManager.get(request.getOption());
        return build(request, site);
    }

    private String build(BuildRequest request, Site site) {
        if (request.getStructureType() == StructureType.BARRACKS) {
            return String.format("BUILD %d %s-%s", site.getId(), request.getStructureType(), request.getUnitType());
        }
        return String.format("BUILD %d %s", site.getId(), request.getStructureType());
    }

}