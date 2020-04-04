package fr.jafar.brain.micro;

import fr.jafar.structure.Position;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.unit.Unit;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SimulationManager {
    private final Unit myQueen;
    private final Set<Unit> hisSoldiers;
    private final Set<Site> sites;
    private final Escaper escaper;

    public SimulationManager(Unit myQueen, Set<Unit> hisSoldiers, Set<Site> sites, Escaper escaper) {
        this.myQueen = myQueen;
        this.hisSoldiers = hisSoldiers;
        this.sites = sites;
        this.escaper = escaper;
    }

    public int compute(int depth) {
        List<Position> myQueenPossibleMoves = this.escaper.getEscapePositions();
        return myQueenPossibleMoves.stream()
                .mapToInt(move -> this.computeMyMove(moveUnit(myQueen, move), this.hisSoldiers, depth))
                .max()
                .orElseThrow(IllegalStateException::new);
    }

    private int computeMyMove(Unit myQueen, Set<Unit> hisSoldiers, int depth) {
        if (depth == 0) {
            return myQueen.getHealth();
        }
        return computeHisMove(myQueen, hisSoldiers, depth);
    }

    public int computeHisMove(Unit myQueenSim, Set<Unit> hisSoldiersSim, int depth) {
        Set<Unit> newHisSoldiersSim = hisSoldiersSim.stream()
                .map(soldier -> attack(soldier, myQueenSim))
                .collect(Collectors.toSet());

        int healthLost = (int) newHisSoldiersSim.stream()
                .filter(soldier -> soldier.getDistance(myQueenSim) < 2)
                .count();

        Unit newMyQueenSim = new Unit.Builder(myQueenSim).health(myQueenSim.getHealth() - healthLost).build();
        List<Position> myQueenPossibleMoves = this.escaper.getEscapePositions();
        return myQueenPossibleMoves.stream()
                .mapToInt(move -> this.computeMyMove(moveUnit(newMyQueenSim, move), newHisSoldiersSim, depth - 1))
                .max()
                .orElseThrow(IllegalStateException::new);
    }

    private Unit attack(Unit soldier, Unit myQueen) {
        if (soldier.getDistance(myQueen) < soldier.maxSteps()) {
            return moveUnit(soldier, soldier.getPosition().moveTo(myQueen.getPosition(), soldier.maxSteps() - myQueen.getRadius()));
        }
        return moveUnit(soldier, soldier.getPosition().moveTo(myQueen.getPosition(), soldier.maxSteps()));
    }

    private Unit moveUnit(Unit unit, Position move) {
        return new Unit.Builder(unit).position(move).build();
    }


}
