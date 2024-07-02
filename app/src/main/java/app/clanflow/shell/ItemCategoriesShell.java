package app.clanflow.shell;

import app.clanflow.admin.Env;
import app.clanflow.db.ItemCategories;
import app.clanflow.db.ItemCategory;

import java.util.List;
import java.util.Scanner;

public class ItemCategoriesShell implements Shell {
    Scanner scanner;
    Env env;
    String prefix;
    ItemCategories itemCategories;
    boolean quit;
    
    public ItemCategoriesShell(Scanner scanner_, Env env_, String prefix_) {
        scanner = scanner_;
        env = env_;
        prefix = prefix_ + "/item_categories";
        itemCategories = env.collections().itemCategoriesCollection();
        quit = false;
    }

    @Override
    public void interact() {
        while (!quit) {
            Shell.PrintPrompt(prefix);
            String input = scanner.nextLine();
            if (input.compareTo("help") == 0) {
                System.out.println("list: List different categories");
                System.out.println("add: Add new category");
                System.out.println("pick: Pick a category");
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

    private void list() {
        java.util.List<ItemCategory> list = itemCategories.list();
        Shell.PrintDelimiter();
        for (ItemCategory category : list) {
            category.print();
        }
        Shell.PrintDelimiter();
    }

    private void add() {
        // Add cuisine
        System.out.print("name: ");
        String categoryName = scanner.nextLine();
        ItemCategory category = itemCategories.add(categoryName);
        Shell.PrintDelimiter();
        category.print();
        Shell.PrintDelimiter();
    }

    private void pick() {
        int idx = 0;
        List<ItemCategory> list = itemCategories.list();
        Shell.PrintDelimiter();
        if (list != null) {
            for (ItemCategory category : list) {
                System.out.print("[" + idx + "] ");
                category.print();
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
        ItemCategoryShell categoryShell = env.shells().itemCategoryShell(prefix, list.get(index));
        categoryShell.interact();
        quit = categoryShell.quit();
    }

    @Override
    public boolean quit() {
        return quit;
    }
}
