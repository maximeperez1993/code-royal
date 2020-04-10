package fr.jafar;

import java.util.Scanner;

public class Player {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Engine engine = new Engine(in);

        while (true) {
            engine.update(in);
        }
    }

}
