package fr.jafar;

import fr.jafar.brain.macro.QueenManager;
import fr.jafar.brain.macro.TrainManager;
import fr.jafar.info.Manager;

import java.util.Scanner;

public class Engine {

    private final Manager manager;
    private final QueenManager queenManager;
    private final TrainManager trainManager;

    public Engine(Scanner in) {
        this.manager = new Manager(in);
        this.queenManager = new QueenManager(manager);
        this.trainManager = new TrainManager(manager);
    }

    public void update(Scanner in) {
        int gold = in.nextInt();
        this.manager.update(in);
        this.queenManager.build();
        this.trainManager.train(gold);
    }
}
