package fr.jafar.util;

import fr.jafar.structure.Positionable;
import fr.jafar.structure.site.Site;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Finder<T extends Positionable> {
    private Stream<T> stream;


    public Finder(List<T> positionables) {
        this.stream = positionables.stream();
    }

    public Finder(Stream<T> steam) {
        this.stream = steam;
    }


    public Finder<T> sortByFarthestFrom(Positionable origin) {
        this.stream = this.stream.sorted((p1, p2) -> (int) (origin.getDistance(p2) - origin.getDistance(p1)));
        return this;
    }

    public Finder<T> sortByClosestFrom(Positionable origin) {
        this.stream = this.stream.sorted((p1, p2) -> (int) (origin.getDistance(p1) - origin.getDistance(p2)));
        return this;
    }

    public Finder<T> filterSafeFromEnemyTowerRange(List<Site> hisTowers) {
        this.stream = this.stream.filter(t -> hisTowers.stream().noneMatch(tower -> tower.isInTowerRange(t)));
        return this;
    }

    public Optional<T> getOptional() {
        return this.stream.findFirst();
    }

    public T get() {
        return this.getOptional().orElseThrow(IllegalStateException::new);
    }
}
