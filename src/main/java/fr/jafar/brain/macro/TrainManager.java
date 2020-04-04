package fr.jafar.brain.macro;

import fr.jafar.Manager;
import fr.jafar.structure.site.Site;
import fr.jafar.util.PositionableComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TrainManager {

    private final Manager manager;

    public TrainManager(Manager manager) {
        this.manager = manager;
    }

    public void train(int gold) {
        System.out.println(this.getTrainString(gold));
    }

    private String getTrainString(int gold) {
        if (manager.getSiteManager().getMySites().isEmpty()) {
            return "TRAIN";
        }
        List<Site> sitesToTrain = this.getSitesToTrain(gold);
        if (sitesToTrain.isEmpty()) {
            return "TRAIN";
        }
        return "TRAIN " + sitesToTrain.stream()
                .map(Site::getId)
                .map(Object::toString)
                .collect(Collectors.joining(" "));
    }

    private List<Site> getSitesToTrain(int gold) {
        int cost = 0;
        List<Site> sitesToTrain = new ArrayList<>();
        List<Site> myReadySites = manager.getSiteManager().getMyReadySites().stream()
                .sorted(new PositionableComparator(this.manager.getUnitManager().getHisQueen()))
                .collect(Collectors.toList());

        for (Site site : myReadySites) {
            if (cost + 80 <= gold) {
                sitesToTrain.add(site);
                cost += 80;
            }
        }
        return sitesToTrain;
    }
}
