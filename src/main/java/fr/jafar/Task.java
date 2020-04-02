package fr.jafar;

import java.io.PrintWriter;
import java.util.Scanner;

public class Task {

    public void solve(int testNumber, Scanner in, PrintWriter out) {
        Engine engine = new Engine(in);

        while (true) {
            engine.update(in);
        }
    }
}
