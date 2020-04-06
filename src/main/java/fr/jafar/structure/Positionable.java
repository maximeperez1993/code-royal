package fr.jafar.structure;

public interface Positionable {

    Position getPosition();

    default boolean isBetween(Positionable p1, Positionable p2, int delta) {
        return getPosition().isBetween(p1.getPosition(), p2.getPosition(), delta);
    }


    default double getDistance(Positionable positionable) {
        return getPosition().getDistance(positionable.getPosition());
    }
}
