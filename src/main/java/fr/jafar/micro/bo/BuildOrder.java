package fr.jafar.micro.bo;

import fr.jafar.unit.UnitType;

import java.util.Arrays;
import java.util.List;

import static fr.jafar.micro.bo.BuildRequest.Option.AGGRESSIVE;
import static fr.jafar.micro.bo.BuildRequest.Option.PASSIVE;
import static fr.jafar.site.StructureType.*;

public class BuildOrder {

    static final List<BuildRequest> BUILD_ORDER = Arrays.asList(
            new BuildRequest().a(BARRACKS).of(UnitType.KNIGHT).in(PASSIVE),
            new BuildRequest().a(MINE).in(PASSIVE),
            new BuildRequest().a(BARRACKS).of(UnitType.KNIGHT).in(AGGRESSIVE),
            new BuildRequest().a(TOWER).in(PASSIVE),
            new BuildRequest().a(MINE).in(PASSIVE),
            new BuildRequest().a(TOWER).in(PASSIVE),
            new BuildRequest().a(TOWER).in(PASSIVE),
            new BuildRequest().a(TOWER).in(PASSIVE),
            new BuildRequest().a(TOWER).in(PASSIVE)
    );


}
