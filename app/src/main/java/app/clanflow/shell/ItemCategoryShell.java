package app.clanflow.shell;

import app.clanflow.admin.Env;
import app.clanflow.db.Item;
import app.clanflow.db.ItemCategory;

import java.util.List;
import java.util.Scanner;

public class ItemCategoryShell implements Shell {
    Scanner scanner;
    Env env;
    String prefix;
    ItemCategory category;
    boolean quit;

    ItemCategoryShell(Scanner scanner_, Env env_, String prefix_, ItemCategory category_) {
        scanner = scanner_;
        env = env_;
        prefix = prefix_ + "/" + category_.name();
        category = category_;
    }

    @Override
    public void interact() {
        while (!quit) {
            Shell.PrintPrompt(prefix);
            String input = scanner.nextLine();
            if (input.compareTo("help") == 0) {
                System.out.println("list: List items");
                System.out.println("add: Add new item");
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

    private void add() {
        // Add cuisine
        System.out.print("name: ");
        String itemName = scanner.nextLine();
        Item item = env.collections().itemsCollection().add(itemName, category);
        Shell.PrintDelimiter();
        item.print();
        Shell.PrintDelimiter();
    }

    private void list() {
        List<Item> items = category.list();
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
