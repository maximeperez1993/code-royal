package fr.jafar.brain.macro.bo;

import fr.jafar.brain.macro.BuildRequest;
import fr.jafar.structure.unit.UnitType;

import java.util.Arrays;
import java.util.List;

import static fr.jafar.brain.macro.BuildRequest.Option.CLOSEST;
import static fr.jafar.brain.macro.BuildRequest.Option.SAFE_BASE;
import static fr.jafar.structure.site.StructureType.*;

public class BuildOrder {

    static final List<BuildRequest> BUILD_ORDER = Arrays.asList(
            new BuildRequest().a(MINE).in(CLOSEST),
            new BuildRequest().a(MINE).in(CLOSEST),
            new BuildRequest().a(MINE).in(CLOSEST),
            new BuildRequest().a(TOWER).in(CLOSEST),
            new BuildRequest().a(BARRACKS).of(UnitType.KNIGHT).in(CLOSEST),
            new BuildRequest().a(MINE).in(SAFE_BASE),
            new BuildRequest().a(MINE).in(SAFE_BASE),
            new BuildRequest().a(MINE).in(SAFE_BASE)
    );


}
