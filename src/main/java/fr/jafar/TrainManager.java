package fr.jafar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.jafar.site.Site;
import fr.jafar.site.SiteManager;
import fr.jafar.unit.UnitManager;
import fr.jafar.util.PositionableComparator;

public class TrainManager {

	private final SiteManager siteManager;
	private final UnitManager unitManager;

	public TrainManager(SiteManager siteManager, UnitManager unitManager) {
		this.siteManager = siteManager;
		this.unitManager = unitManager;
	}

	public void train(int gold) {
		System.out.println(this.getTrainString(gold));
	}

	private String getTrainString(int gold) {
		if (siteManager.getMySites().isEmpty()) {
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
		List<Site> myReadySites = siteManager.getMyReadySites().stream()
			.sorted(new PositionableComparator(this.unitManager.getHisQueen()))
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
