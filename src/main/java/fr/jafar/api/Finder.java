package fr.jafar.api;

import fr.jafar.util.PositionableComparator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Finder<T extends Positionable> {
    private Stream<T> stream;


    public Finder(List<T> positionables) {
        this.stream = positionables.stream();
    }


    public Finder<T> sortByFarthestFrom(Positionable positionable) {
        this.stream = this.stream.sorted(new PositionableComparator(positionable));
        return this;
    }

    public Optional<T> getOptional() {
        return this.stream.findFirst();
    }

    public T get() {
        return this.getOptional().orElseThrow(IllegalStateException::new);
    }
}
