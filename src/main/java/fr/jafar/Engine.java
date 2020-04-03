package fr.jafar;

import fr.jafar.macro.TrainManager;
import fr.jafar.micro.QueenManager;
import fr.jafar.site.SiteManager;
import fr.jafar.unit.UnitManager;

import java.util.Scanner;

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

		QueenManager queenManager = new QueenManager(siteManager, unitManager);
		queenManager.build();

		TrainManager trainManager = new TrainManager(siteManager, unitManager);
		trainManager.train(gold);
		this.nbTurn++;
	}
}
