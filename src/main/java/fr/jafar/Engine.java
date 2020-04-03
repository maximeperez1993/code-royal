package fr.jafar;

import java.util.Scanner;

import fr.jafar.site.SiteManager;
import fr.jafar.unit.UnitManager;

public class Engine {

	private final SiteManager siteManager;
	private int nbTurn = 0;

	public Engine(Scanner in) {
		this.siteManager = SiteManager.read(in);
	}

	public void update(Scanner in) {
		int gold = in.nextInt();
		int touchedSite = in.nextInt(); // -1 if none
		siteManager.update(in);
		UnitManager unitManager = UnitManager.read(in);

		BuildManager buildManager = new BuildManager(siteManager, unitManager);
		buildManager.build();

		TrainManager trainManager = new TrainManager(siteManager, unitManager);
		trainManager.train(gold);
		this.nbTurn++;
	}
}
