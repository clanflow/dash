package app.clanflow.shell;

import app.clanflow.admin.Env;
import app.clanflow.db.Dish;
import app.clanflow.db.Item;

import java.util.List;
import java.util.Scanner;

public class DishShell implements Shell {
    Scanner scanner;
    Env env;
    String prefix;
    Dish dish;
    boolean quit;

    DishShell(Scanner scanner_, Env env_, String prefix_, Dish dish_) {
        scanner = scanner_;
        env = env_;
        prefix = prefix_ + "/" + dish_.name();
        dish = dish_;
    }

    @Override
    public void interact() {
        while (!quit) {
            Shell.PrintPrompt(prefix);
            String input = scanner.nextLine();
            if (input.compareTo("help") == 0) {
                System.out.println("list: List items");
                System.out.println("add: Add new item");
                System.out.println("back: Go back");
                System.out.println("q: Quit");
                continue;
            }
            if (input.compareTo("list") == 0) {
                list();
            }
            if (input.compareTo("add") == 0) {
                add();
            }
            if (input.compareTo("back") == 0) {
                break;
            }
            if (input.startsWith("q")) {
                quit = true;
                break;
            }
        }
    }

    private void add() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    private void list() {
        List<Item> items = dish.items();
        Shell.PrintDelimiter();
        for (Item item : items) {
            item.print();
        }
        Shell.PrintDelimiter();
    }

    @Override
    public boolean quit() {
        return quit;
    }

}
