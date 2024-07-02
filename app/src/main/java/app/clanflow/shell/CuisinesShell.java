package app.clanflow.shell;

import app.clanflow.admin.Env;
import app.clanflow.db.Cuisine;
import app.clanflow.db.Cuisines;

import java.util.List;
import java.util.Scanner;

public class CuisinesShell implements Shell {
    Scanner scanner;
    Env env;
    String prefix;
    Cuisines cuisines;
    boolean quit;

    public CuisinesShell(Scanner scanner_, Env env_, String prefix_) {
        scanner = scanner_;
        env = env_;
        prefix = prefix_ + "/cuisines";
        cuisines = env.collections().cuisinesCollection();
        quit = false;
    }

    public void interact() {
        while (!quit) {
            Shell.PrintPrompt(prefix);
            String input = scanner.nextLine();
            if (input.compareTo("help") == 0) {
                System.out.println("list: List different cuisines");
                System.out.println("add: Add new cuisine");
                System.out.println("pick: Pick a cuisine");
                System.out.println("b|back: Go back");
                System.out.println("q: Quit");
                continue;
            }
            if (input.compareTo("list") == 0) {
                list();
            }
            if (input.compareTo("add") == 0) {
                add();
            }
            if (input.compareTo("pick") == 0) {
                pick();
            }
            if (input.compareTo("back") == 0) {
                break;
            }
            if (input.compareTo("q") == 0) {
                quit = true;
                break;
            }
        }
    }

    @Override
    public boolean quit() { return quit; }

    private void add() {
        // Add cuisine
        System.out.print("name: ");
        String cuisineName = scanner.nextLine();
        Cuisine cuisine = cuisines.add(cuisineName);
        Shell.PrintDelimiter();
        cuisine.print();
        Shell.PrintDelimiter();
    }

    private void list() {
        List<Cuisine> list = cuisines.list();
        Shell.PrintDelimiter();
        for (Cuisine cuisine : list) {
            cuisine.print();
        }
        Shell.PrintDelimiter();
    }

    private void pick() {
        int idx = 0;
        List<Cuisine> list = cuisines.list();
        Shell.PrintDelimiter();
        if (list != null) {
            for (Cuisine cuisine : list) {
                System.out.print("[" + idx + "] ");
                cuisine.print();
                idx = idx + 1;
            }
        }
        Shell.PrintDelimiter();

        System.out.print("Pick index: ");
        String input = scanner.nextLine();
        if (input.startsWith("q")) {
            return;
        }

        int index = Integer.parseInt(input);
        CuisineShell cuisineShell = env.shells().cuisineShell(prefix, list.get(index));
        cuisineShell.interact();
        quit = cuisineShell.quit();
    }
}
