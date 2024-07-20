package app.clanflow.shell;

import app.clanflow.admin.Env;
import app.clanflow.db.Dish;
import app.clanflow.db.Item;
import app.clanflow.db.ItemCategory;
import app.clanflow.db.ItemCategoryItems;
import io.grpc.netty.shaded.io.netty.util.internal.IntegerHolder;

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

        dish.close();
    }

    private void add() {
        System.out.print("name: ");
        String itemName = scanner.nextLine();
        List<Item> similarItems = env.collections().itemsCollection().list(itemName);
        if (similarItems != null && similarItems.size() > 0) {
            Shell.PrintDelimiter();
            int idx = 0;
            for (Item item : similarItems) {
                System.out.print("[" + idx + "] ");
                item.print();
                idx = idx + 1;
            }
            Shell.PrintDelimiter();

            System.out.print("Pick index(s: skip): ");
            String input = scanner.nextLine();
            if (input.compareTo("s") != 0) {
                int pickedIndex = Integer.parseInt(input);
                if (pickedIndex >= 0 && pickedIndex < similarItems.size()) {
                    Item item = similarItems.get(pickedIndex);
                    dish.addItem(item);
                    System.out.print("Added: ");
                    item.print();
                    return;
                }
            }
        }

        List<ItemCategory> categories = env.collections().itemCategoriesCollection().list();
        int idx = 0;
        for (ItemCategory category : categories) {
            System.out.print("[" + idx + "] ");
            category.print();
            idx = idx + 1;
        }

        System.out.print("Pick index(s: skip): ");
        String input = scanner.nextLine();
        if (input.compareTo("s") == 0) {
            return;
        }

        int pickedIndex = Integer.parseInt(input);
        Item item = env.collections().itemsCollection().add(itemName, categories.get(pickedIndex));
        dish.addItem(item);
        System.out.print("Added: ");
        item.print();
    }

    private void list() {
        List<ItemCategoryItems> itemCategoryItemsList = dish.itemCategoryItems();
        Shell.PrintDelimiter();
        for (ItemCategoryItems itemCategoryItems : itemCategoryItemsList) {
            itemCategoryItems.print();
        }
        Shell.PrintDelimiter();
    }

    @Override
    public boolean quit() {
        return quit;
    }

}
