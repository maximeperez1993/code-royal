package fr.jafar.site;

import fr.jafar.api.Position;
import fr.jafar.api.Positionable;
import fr.jafar.api.Team;

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

	public boolean isMine() {
		return this.state.getStructureType() == StructureType.MINE;
	}

	public int getId() {
		return this.id;
	}

	@Override
	public Position getPosition() {
		return this.position;
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

}
