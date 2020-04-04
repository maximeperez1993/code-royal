package fr.jafar.brain.macro;

import fr.jafar.Manager;
import fr.jafar.brain.macro.bo.BuildManager;
import fr.jafar.brain.micro.Escaper;
import fr.jafar.structure.Position;

import java.util.Optional;

public class QueenManager {

    private final BuildManager buildManager;
    private final Escaper escaper;

    public QueenManager(Manager manager) {
        this.buildManager = new BuildManager(manager);
        this.escaper = new Escaper(manager);
    }

    public void build() {
        Optional<String> toBuild = this.buildManager.build();

        if (toBuild.isPresent()) {
            System.out.println(toBuild.get());
            return;
        }
        System.err.println("Try to escape");
        System.out.println(move(this.escaper.getEscapePosition()));
    }

    private String move(Position position) {
        return String.format("MOVE %d %d", position.getX(), position.getY());
    }
}
