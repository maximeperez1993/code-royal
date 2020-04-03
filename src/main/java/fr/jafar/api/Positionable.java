package fr.jafar.api;

public interface Positionable {

    Position getPosition();

    default double getDistance(Positionable positionable) {
        return getPosition().getDistance(positionable.getPosition());
    }
}
