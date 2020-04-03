package fr.jafar;

import fr.jafar.macro.TrainManager;
import fr.jafar.micro.QueenManager;

import java.util.Scanner;

public class Engine {

	private final Manager manager;

	public Engine(Scanner in) {
		this.manager = new Manager(in);
	}

	public void update(Scanner in) {
		int gold = in.nextInt();
		int touchedSite = in.nextInt(); // -1 if none
		this.manager.update(in);

		new QueenManager(manager).build();
		new TrainManager(manager).train(gold);
	}
}
