package fr.jafar;

import fr.jafar.site.SiteManager;
import fr.jafar.unit.Unit;

import java.io.PrintWriter;
import java.util.Scanner;

public class Task {

    public void solve(int testNumber, Scanner in, PrintWriter out) {
        SiteManager siteManager = SiteManager.read(in);

        // game loop
        while (true) {
            int gold = in.nextInt();
            int touchedSite = in.nextInt(); // -1 if none
            siteManager.update(in);
            int numUnits = in.nextInt();
            for (int i = 0; i < numUnits; i++) {
                Unit unit = Unit.read(in);
            }

            System.out.println("WAIT");
            System.out.println("TRAIN");
        }
    }
}
