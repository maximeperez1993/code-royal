package fr.jafar.structure.site;

import fr.jafar.structure.Position;
import fr.jafar.structure.Positionable;
import fr.jafar.structure.Team;
import fr.jafar.structure.unit.UnitType;

import java.util.Scanner;

public class Site implements Positionable {

    private final int id;
    private final Position position;
    private final int radius;
    private SiteState state;

    private Site(int id, Position position, int radius) {
        this.id = id;
        this.position = position;
        this.radius = radius;
    }

    public Team getTeam() {
        return state.getTeam();
    }

    public boolean isNeutral() {
        return this.state.getTeam() == Team.NEUTRAL;
    }

    public boolean isFriendly() {
        return this.state.getTeam() == Team.FRIENDLY;
    }

    public boolean isEnemy() {
        return this.state.getTeam() == Team.ENEMY;
    }

    public boolean isReady() {
        return this.state.isReady();
    }

    public boolean isTraining() {
        return this.state.getRemainTurn() > 0;
    }

    public boolean isBarrack() {
        return this.state.getStructureType() == StructureType.BARRACKS;
    }

    public boolean isKnightBarrack() {
        return isBarrack() && state.getUnitType() == UnitType.KNIGHT;
    }

    public boolean isMine() {
        return this.state.getStructureType() == StructureType.MINE;
    }

    public boolean isUpdatable() {
        if (!isFriendly()) {
            return false;
        }
        if (state.getStructureType() == StructureType.TOWER) {
            return isTowerUpgradable();
        }
        if (state.getStructureType() == StructureType.MINE) {
            return isMineUpgradable();
        }
        return false;
    }

    public boolean hasNoRemainingGold() {
        return this.state.getGold() <= 0;
    }

    public boolean hasRemainingGold() {
        return this.state.getGold() > 0;
    }

    public boolean isTower() {
        return this.state.getStructureType() == StructureType.TOWER;
    }

    public boolean isEnemyTower() {
        return isTower() && isEnemy();
    }

    public boolean isMineUpgradable() {
        return isMine() && state.getRemainTurn() < state.getMaxMineSize();
    }

    public int getMineLevel() {
        return state.getRemainTurn();
    }

    public int getMaxMineSize() {
        return state.getMaxMineSize();
    }

    public boolean isTowerUpgradable() {
        return isTower() && state.getRemainTurn() <= 300;
    }

    public boolean isTowerLowHp() {
        return isTower() && state.getRemainTurn() <= 80;
    }

    public int getId() {
        return this.id;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

    public SiteState getState() {
        return state;
    }

    public int getTowerRange() {
        return state.getTowerRange();
    }

    public boolean isInTowerRange(Positionable p) {
        return (getTowerRange() + getRadius()) * (getTowerRange() + getRadius()) >=
                (p.getPosition().getX() - position.getX()) * (p.getPosition().getX() - position.getX()) +
                        (p.getPosition().getY() - position.getY()) * (p.getPosition().getY() - position.getY());
    }

    public void update(Scanner in) {
        if (in.nextInt() != id) {
            throw new IllegalStateException("id of site not ordered !!");
        }
        this.state = SiteState.read(in);
    }


    public static Site read(Scanner in) {
        return new Site(in.nextInt(), Position.read(in), in.nextInt());
    }

    @Override
    public String toString() {
        return "Site{" +
                "id=" + id +
                ", position=" + position +
                ", radius=" + radius +
                ", state=" + state +
                '}';
    }
}
