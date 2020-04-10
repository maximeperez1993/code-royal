package fr.jafar;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Player {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        Scanner in = new Scanner(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        Player solver = new Player();
        solver.solve(1, in, out);
        out.close();
    }

    public void solve(int testNumber, Scanner in, PrintWriter out) {
        Engine engine = new Engine(in);

        while (true) {
            engine.update(in);
        }
    }

}
