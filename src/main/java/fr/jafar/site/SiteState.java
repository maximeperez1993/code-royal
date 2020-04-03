package fr.jafar.site;

import fr.jafar.api.Team;

import java.util.Scanner;

class SiteState {

    private final int ignore1;
    private final int ignore2;
    private final StructureType structureType;
    private final Team team;
    private final int remainTurn;
    private final int param2;

    private SiteState(int ignore1, int ignore2, StructureType structureType, Team team, int remainTurn, int param2) {
        this.ignore1 = ignore1;
        this.ignore2 = ignore2;
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

    public int getIgnore1() {
        return ignore1;
    }

    public int getIgnore2() {
        return ignore2;
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

}
