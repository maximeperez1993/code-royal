package fr.jafar.micro.bo;

import fr.jafar.site.StructureType;
import fr.jafar.unit.UnitType;

public class BuildRequest {

    enum Option {AGGRESSIVE, PASSIVE;}

    private StructureType structureType;
    private UnitType unitType;
    private Option option;

    public BuildRequest() {
    }

    public BuildRequest a(StructureType structureType) {
        this.structureType = structureType;
        return this;
    }

    public BuildRequest of(UnitType type) {
        this.unitType = type;
        return this;
    }

    public BuildRequest in(Option option) {
        this.option = option;
        return this;
    }

    public void check() {
        if (this.option == null) {
            throw new IllegalStateException("Can't build without option");
        }
        if (this.structureType == StructureType.NO_STRUCTURE) {
            throw new IllegalStateException("Can't build NO_STRUCTURE");
        }
        if (this.structureType == StructureType.BARRACKS && this.unitType == null) {
            throw new IllegalStateException("Can't make a BARRACKS with no UnitType");
        }
        if (this.structureType == StructureType.TOWER && this.unitType != null) {
            throw new IllegalStateException("Can't make a TOWER with UnitType");
        }
    }

    public StructureType getStructureType() {
        return structureType;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public Option getOption() {
        return option;
    }
}
