package fr.jafar;

import java.io.PrintWriter;
import java.util.Scanner;

import fr.jafar.site.SiteManager;

public class Task {

	public void solve(int testNumber, Scanner ine, PrintWriter out) {
		Scanner in = new Scanner(System.in);
		SiteManager siteManager = SiteManager.read(in);

		// game loop
		while (true) {
			int gold = in.nextInt();
			int touchedSite = in.nextInt(); // -1 if none
			siteManager.update(in);
			int numUnits = in.nextInt();
			for (int i = 0; i < numUnits; i++) {
				int x = in.nextInt();
				int y = in.nextInt();
				int owner = in.nextInt();
				int unitType = in.nextInt(); // -1 = QUEEN, 0 = KNIGHT, 1 = ARCHER
				int health = in.nextInt();
			}

			// Write an action using System.out.println()
			// To debug: System.err.println("Debug messages...");

			// First line: A valid queen action
			// Second line: A set of training instructions
			System.out.println("WAIT");
			System.out.println("TRAIN");
		}
	}
}
