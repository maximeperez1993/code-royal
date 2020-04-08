package fr.jafar.structure.site;

import fr.jafar.structure.Team;
import fr.jafar.structure.unit.UnitType;

import java.util.Scanner;

public class SiteState {

    private final int gold;
    private final int maxMineSize;
    private final StructureType structureType;
    private final Team team;
    private final int remainTurn;
    private final int param2; // Quand il n'y a pas de bâtiment construit ou si c'est une mine: -1,  Si c'est une tour, son rayon de portée //Si c'est une caserne, le type d'armée qu'elle produit 0 pour une caserne de chevaliers, 1 pour une caserne d'archers, 2 pour une caserne de géants.

    public SiteState(int gold, int maxMineSize, StructureType structureType, Team team, int remainTurn, int param2) {
        this.gold = gold;
        this.maxMineSize = maxMineSize;
        this.structureType = structureType;
        this.team = team;
        this.remainTurn = remainTurn;
        this.param2 = param2;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isReady() {
        return remainTurn == 0;
    }

    public int getRemainTurn() {
        return remainTurn;
    }

    public int getGold() {
        return gold;
    }

    public int getMaxMineSize() {
        return maxMineSize;
    }

    public UnitType getUnitType() {
        return UnitType.findByCode(param2);
    }

    public int getTowerRange() {
        return param2;
    }

    public StructureType getStructureType() {
        return structureType;
    }

    public int getParam2() {
        return param2;
    }

    public static SiteState read(Scanner in) {
        return new SiteState(in.nextInt(), in.nextInt(), StructureType.read(in), Team.read(in), in.nextInt(), in.nextInt());
    }

    public static SiteState empty() {
        return new SiteState(0, 0, StructureType.NO_STRUCTURE, Team.NEUTRAL, 0, 0);
    }

    @Override
    public String toString() {
        return "SiteState{" +
                "gold=" + gold +
                ", maxMineSize=" + maxMineSize +
                ", structureType=" + structureType +
                ", team=" + team +
                ", remainTurn=" + remainTurn +
                ", param2=" + param2 +
                '}';
    }
}
