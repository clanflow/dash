package app.clanflow.shell;

import java.util.List;
import java.util.Scanner;

import app.clanflow.admin.Env;
import app.clanflow.db.Cuisine;
import app.clanflow.db.Dish;
import app.clanflow.db.Item;

public class CuisineShell implements Shell {
    Scanner scanner;
    Env env;
    String prefix;
    Cuisine cuisine;
    boolean quit;

    public CuisineShell(Scanner scanner_, Env env_, String prefix_, Cuisine cuisine_) {
        scanner = scanner_;
        env = env_;
        prefix = prefix_ + "/" + cuisine_.name();
        cuisine = cuisine_;
    }

    @Override
    public void interact() {
        while (!quit) {
            Shell.PrintPrompt(prefix);
            String input = scanner.nextLine();
            if (input.compareTo("help") == 0) {
                System.out.println("list: List dishes");
                System.out.println("add: Add new dish");
                System.out.println("pick: Pick a dish");
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
            if (input.compareTo("b") == 0 ||
                input.compareTo("back") == 0) {
                break;
            }
            if (input.startsWith("q")) {
                quit = true;
                break;
            }
        }
    }

    private void pick() {
        int idx = 0;
        List<Dish> list = cuisine.list();
        Shell.PrintDelimiter();
        if (list != null) {
            for (Dish dish : list) {
                System.out.print("[" + idx + "] ");
                dish.print();
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
        DishShell dishShell = env.shells().dishShell(prefix, list.get(index));
        dishShell.interact();
        quit = dishShell.quit();
    }

    private void add() {
        // Add dish
        System.out.print("name: ");
        String dishName = scanner.nextLine();
        Dish dish = env.collections().dishesCollection().add(dishName, cuisine);
        Shell.PrintDelimiter();
        dish.print();
        Shell.PrintDelimiter();
    }

    private void list() {
        List<Dish> dishes = cuisine.list();
        Shell.PrintDelimiter();
        for (Dish dish : dishes) {
            dish.print();
        }
        Shell.PrintDelimiter();
    }

    @Override
    public boolean quit() {
        return quit;
    }

}
