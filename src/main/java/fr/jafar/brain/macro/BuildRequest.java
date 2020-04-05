package fr.jafar.brain.macro;

import fr.jafar.structure.site.Site;
import fr.jafar.structure.site.StructureType;
import fr.jafar.structure.unit.UnitType;

public class BuildRequest {

    public enum Option {FRONTIER, SAFE_BASE, CLOSEST;}

    private StructureType structureType;
    private UnitType unitType;
    private Site site;
    private String log;

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

    public BuildRequest at(Site site) {
        this.site = site;
        return this;
    }

    public BuildRequest log(String log) {
        this.log = this.log == null ? log : this.log + ", " + log;
        return this;
    }

    public String build() {
        this.check();
        System.err.println(String.format("[%s] : %s", this.log, this));
        if (StructureType.BARRACKS == structureType) {
            return String.format("BUILD %d %s-%s", site.getId(), structureType, unitType);
        }
        return String.format("BUILD %d %s", site.getId(), structureType);
    }

    private void check() {
        if (this.site == null) {
            throw new IllegalStateException("Can't build without target");
        }
        if (this.log == null) {
            throw new IllegalStateException("Need to log every decision");
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

    public Site getSite() {
        return site;
    }

    @Override
    public String toString() {
        return "BuildRequest{" +
                "structureType=" + structureType +
                ", unitType=" + unitType +
                ", site=" + site +
                '}';
    }
}
