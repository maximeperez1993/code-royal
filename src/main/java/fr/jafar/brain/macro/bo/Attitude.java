package fr.jafar.brain.macro.bo;

import fr.jafar.brain.macro.BuildRequest;
import fr.jafar.structure.Position;
import fr.jafar.structure.site.Site;

public interface Attitude {

    String order(StateInfo i);

    BuildRequest build(Site site);

    default String upgrade(Site site) {
        return new BuildRequest().a(site.getState().getStructureType())
                .at(site)
                .log("Upgrade")
                .build();
    }

    default String move(Position position) {
        return String.format("MOVE %d %d", position.getX(), position.getY());
    }
}
