package fr.jafar.brain.macro.bo;

import fr.jafar.brain.macro.BuildRequest;
import fr.jafar.structure.site.Site;

public interface BuildManager {

    BuildRequest build(Site site);
}
