package fr.jafar.site;

import fr.jafar.api.Team;

import java.util.Scanner;

public class SiteState {

    private final int gold;
    private final int maxMineSize;
    private final StructureType structureType;
    private final Team team;
    private final int remainTurn;
    private final int param2;

    private SiteState(int gold, int maxMineSize, StructureType structureType, Team team, int remainTurn, int param2) {
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
