package fr.jafar.structure;

public interface Positionable {

    Position getPosition();

    default double getDistance(Positionable positionable) {
        return getPosition().getDistance(positionable.getPosition());
    }
}
