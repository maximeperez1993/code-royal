package fr.jafar.micro.bo;

import fr.jafar.unit.UnitType;

import java.util.Arrays;
import java.util.List;

import static fr.jafar.micro.bo.BuildRequest.Option.*;
import static fr.jafar.site.StructureType.*;

public class BuildOrder {

    static final List<BuildRequest> BUILD_ORDER = Arrays.asList(
            new BuildRequest().a(BARRACKS).of(UnitType.KNIGHT).in(SAFE_BASE),
            new BuildRequest().a(MINE).in(SAFE_BASE),
            new BuildRequest().a(BARRACKS).of(UnitType.KNIGHT).in(FRONTIER),
            new BuildRequest().a(TOWER).in(CLOSEST),
            new BuildRequest().a(TOWER).in(SAFE_BASE),
            new BuildRequest().a(MINE).in(SAFE_BASE),
            new BuildRequest().a(TOWER).in(SAFE_BASE),
            new BuildRequest().a(TOWER).in(SAFE_BASE),
            new BuildRequest().a(TOWER).in(SAFE_BASE),
            new BuildRequest().a(TOWER).in(SAFE_BASE)
    );


}
