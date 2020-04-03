package fr.jafar.micro;

import fr.jafar.Manager;
import fr.jafar.site.Site;
import fr.jafar.site.StructureType;
import fr.jafar.unit.UnitType;

public class Builder {

    private final Manager manager;

    private StructureType structureType;
    private UnitType unitType;
    private Site site;

    public Builder(Manager manager) {
        this.manager = manager;
    }

    public Builder a(StructureType structureType) {
        this.structureType = structureType;
        return this;
    }

    public Builder of(UnitType type) {
        this.unitType = type;
        return this;
    }

    public Builder at(Site site) {
        this.site = site;
        return this;
    }

    public void build() {
        check();
        if (this.structureType == StructureType.BARRACKS) {
            System.out.println(String.format("BUILD %d %s-%s", this.site.getId(), this.structureType.name(), this.unitType.name()));
        } else {
            System.out.println(String.format("BUILD %d %s", this.site.getId(), this.structureType.name()));
        }
        reset();
    }

    private void reset() {
        this.structureType = null;
        this.unitType = null;
        this.site = null;
    }

    private void check() {
        if (this.site == null) {
            throw new IllegalStateException("Can't build without target (site=null)");
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

}
