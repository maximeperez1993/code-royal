package fr.jafar.util.comparators;

import fr.jafar.structure.Positionable;

import java.util.Comparator;

public final class MyComparators {


    private MyComparators() {
    }

    public static Comparator<Positionable> distanceFrom(Positionable positionable) {
        return new PositionableComparator(positionable);
    }
}
