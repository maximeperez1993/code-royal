package fr.jafar.unit;

import java.util.Scanner;

import fr.jafar.Team;
import fr.jafar.site.Site;
import fr.jafar.util.Position;
import fr.jafar.util.Positionable;

public class Unit implements Positionable {

    private final Position position;
    private final Team team;
    private final UnitType unitType;
    private final int health;

    public Unit(Position position, Team team, UnitType unitType, int health) {
        this.position = position;
        this.team = team;
        this.unitType = unitType;
        this.health = health;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public void build(Site site) {
        System.out.println(String.format("BUILD %d BARRACKS-KNIGHT", site.getId()));
    }

    public boolean isMyQueen() {
        return unitType == UnitType.QUEEN && team == Team.FRIENDLY;
    }

    public boolean isHisQueen() {
        return unitType == UnitType.QUEEN && team == Team.ENEMY;
    }

    public boolean isMySoldier(){
        return unitType != UnitType.QUEEN && team == Team.FRIENDLY;
    }

    public boolean isHisSoldier(){
        return unitType != UnitType.QUEEN && team == Team.ENEMY;
    }

    public static Unit read(Scanner in) {
        return new Unit(Position.read(in), Team.read(in), UnitType.read(in), in.nextInt());
    }
}
