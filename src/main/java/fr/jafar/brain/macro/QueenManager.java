package fr.jafar.brain.macro;

import fr.jafar.brain.macro.bo.AggressiveAttitude;
import fr.jafar.brain.macro.bo.DefensiveAttitude;
import fr.jafar.brain.macro.bo.EarlyAttitude;
import fr.jafar.brain.macro.bo.StateInfo;
import fr.jafar.brain.micro.Escaper;
import fr.jafar.info.Manager;

import java.util.Objects;

public class QueenManager {

    private final Manager manager;
    private final Escaper escaper;
    private final AggressiveAttitude aggressive;
    private final DefensiveAttitude defensive;
    private final EarlyAttitude early;


    public QueenManager(Manager manager) {
        this.manager = manager;
        this.escaper = new Escaper(manager);
        this.aggressive = new AggressiveAttitude(manager, escaper);
        this.defensive = new DefensiveAttitude(manager, escaper);
        this.early = new EarlyAttitude(manager, escaper);

    }

    public void build() {
        System.out.println(Objects.requireNonNull(order(), "ERROR : No order specify"));
    }

    public String order() {
        StateInfo i = new StateInfo(manager);

        if (!early.isFinish()) {
            System.err.println("Early, following build order");
            return this.early.order(i);
        }
        if (i.isUnderAttack()) {
            System.err.println("Defensive order, We are under attack !");
            return this.defensive.order(i);
        }
        System.err.println("Aggressive order");
        return this.aggressive.order(i);
    }

}
