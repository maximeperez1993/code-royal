package fr.jafar.brain.macro;

import fr.jafar.Manager;
import fr.jafar.brain.macro.bo2.BuildManager;
import fr.jafar.brain.macro.bo2.StateInfo;
import fr.jafar.brain.micro.Escaper;
import fr.jafar.structure.Position;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.site.StructureType;

import java.util.Objects;

public class QueenManager {

    private final Manager manager;
    private final BuildManager buildManager;
    private final Escaper escaper;

    public QueenManager(Manager manager) {
        this.manager = manager;
        this.buildManager = new BuildManager(manager);
        this.escaper = new Escaper(manager);
    }

    public void build() {
        System.out.println(Objects.requireNonNull(order(), "ERROR : No order specify"));
    }

    public String order() {
        StateInfo i = new StateInfo(manager);

        if (i.isTouchSiteUpdatable()) {
            return upgrade(i.getTouchedSite());
        }
        if (i.isClosestFreeSiteAtRange()) {
            return build(this.buildManager.build(i.getClosestFreeSite()), i.getClosestFreeSite());
        }
        return build(this.buildManager.build(i.getClosestFreeSite()), i.getClosestFreeSite());


        //return escape();
    }

    /**
     * Upgrade this site
     *
     * @param site
     * @return
     */
    private String upgrade(Site site) {
        System.err.println("Upgrade " + site);
        return build(new BuildRequest().a(site.getState().getStructureType()), site);
    }

    private String escape() {
        System.err.println("Try to escape");
        return move(this.escaper.getEscapePosition());
    }

    private String build(BuildRequest request, Site site) {
        if (request.getStructureType() == StructureType.BARRACKS) {
            return String.format("BUILD %d %s-%s", site.getId(), request.getStructureType(), request.getUnitType());
        }
        return String.format("BUILD %d %s", site.getId(), request.getStructureType());
    }

    private String move(Position position) {
        return String.format("MOVE %d %d", position.getX(), position.getY());
    }
}
