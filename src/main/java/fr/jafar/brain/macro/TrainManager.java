package fr.jafar.brain.macro;

import fr.jafar.info.Manager;
import fr.jafar.structure.site.Site;
import fr.jafar.util.comparators.PositionableComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TrainManager {

    private final Manager manager;
    private boolean shouldTrain = false;

    public TrainManager(Manager manager) {
        this.manager = manager;
    }

    public void train(int gold) {
        System.out.println(this.getTrainString(gold));
    }

    private String getTrainString(int gold) {
        shouldTrain = shouldTrain(gold);
        if (shouldTrain && manager.my().readyBarracks().count() > 0) {
            return train(getSitesToTrain(gold));
        }
        return "TRAIN";
    }

    private boolean shouldTrain(int gold) {
        if (gold < 80) {
            return false;
        }
        if (gold >= 160 - manager.my().currentIncome()) {
            return true;
        }
        return shouldTrain;
    }

    private List<Site> getSitesToTrain(int gold) {
        int cost = 0;
        List<Site> sitesToTrain = new ArrayList<>();
        List<Site> myReadySites = manager.my().readyBarracks()
                .sorted(new PositionableComparator(this.manager.his().queen()))
                .collect(Collectors.toList());

        for (Site site : myReadySites) {
            if (cost + 80 <= gold) {
                sitesToTrain.add(site);
                cost += 80;
            }
        }
        return sitesToTrain;
    }

    private String train(List<Site> barracks) {
        return "TRAIN " + barracks.stream()
                .map(Site::getId)
                .map(Object::toString)
                .collect(Collectors.joining(" "));
    }
}
