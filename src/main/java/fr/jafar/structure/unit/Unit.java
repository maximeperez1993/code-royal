package fr.jafar.structure.unit;

import java.util.Scanner;

import fr.jafar.structure.Position;
import fr.jafar.structure.Positionable;
import fr.jafar.structure.Team;
import fr.jafar.structure.site.Site;

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

	/**
	 * (r1+r2)² >= (x1-x2)² + (y1-y2)²
	 *
	 * @param element
	 * @return
	 */
	public boolean isInCollision(Positionable element) {
		Position p = element.getPosition();
		return (getRadius() + element.getRadius()) * (getRadius() + element.getRadius()) >=
			(p.getX() - position.getX()) * (p.getX() - position.getX()) +
				(p.getY() - position.getY()) * (p.getY() - position.getY());
	}

	public UnitType getUnitType() {
		return unitType;
	}

	public Team getTeam() {
		return team;
	}

	public boolean isMyQueen() {
		return unitType == UnitType.QUEEN && team == Team.FRIENDLY;
	}

	public boolean isQueen() {
		return unitType == UnitType.QUEEN;
	}

	public boolean isKnight() {
		return unitType == UnitType.KNIGHT;
	}

	public boolean isHisQueen() {
		return unitType == UnitType.QUEEN && team == Team.ENEMY;
	}

	public boolean isMySoldier() {
		return unitType != UnitType.QUEEN && team == Team.FRIENDLY;
	}

	public boolean isHisSoldier() {
		return unitType != UnitType.QUEEN && team == Team.ENEMY;
	}

	public int getMaxSteps() {
		return unitType.maxSteps();
	}

	@Override
	public int getRadius() {
		return unitType.getRadius();
	}

	public int getHealth() {
		return health;
	}

	public static Unit read(Scanner in) {
		return new Unit(Position.read(in), Team.read(in), UnitType.read(in), in.nextInt());
	}

	@Override
	public String toString() {
		return "Unit{" +
			"position=" + position +
			", team=" + team +
			", unitType=" + unitType +
			", health=" + health +
			'}';
	}

	public static class Builder {

		private Position position;
		private Team team;
		private UnitType unitType;
		private int health;

		public Builder(Unit unit) {
			this.position = unit.position;
			this.team = unit.team;
			this.unitType = unit.unitType;
			this.health = unit.health;
		}

		public Builder position(Position position) {
			this.position = position;
			return this;
		}

		public Builder team(Team team) {
			this.team = team;
			return this;
		}

		public Builder unitType(UnitType unitType) {
			this.unitType = unitType;
			return this;
		}

		public Builder health(int health) {
			this.health = health;
			return this;
		}

		public Unit build() {
			return new Unit(new Position(this.position), this.team, this.unitType, this.health);
		}
	}
}
