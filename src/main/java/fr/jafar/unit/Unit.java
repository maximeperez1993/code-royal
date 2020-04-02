package fr.jafar.unit;

import fr.jafar.Position;

import java.util.Scanner;

public class Unit {

    private final Position position;
    private final int owner ;
    private final UnitType unitType; // -1 = QUEEN, 0 = KNIGHT, 1 = ARCHER
    private final int health;

    private Unit(Position position, int owner, UnitType unitType, int health) {
        this.position = position;
        this.owner = owner;
        this.unitType = unitType;
        this.health = health;
    }

    public static Unit read(Scanner in){
        return new Unit(Position.read(in), in.nextInt(), UnitType.read(in), in.nextInt());
    }
}
