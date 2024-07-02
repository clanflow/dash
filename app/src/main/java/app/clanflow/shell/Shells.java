package app.clanflow.shell;

import java.util.Scanner;

import app.clanflow.admin.Env;
import app.clanflow.db.Cuisine;
import app.clanflow.db.Dish;
import app.clanflow.db.ItemCategory;

public class Shells {
    Scanner scanner;
    Env env;
    RootShell shell;
    CuisinesShell cuisinesShell;
    ItemCategoriesShell itemCategoriesShell;

    public Shells(Scanner scanner_, Env env_) {
        scanner = scanner_;
        env = env_;
    }

    public RootShell rootShell() {
        if (shell != null) { return shell; }
        shell = new RootShell(scanner, env);
        return shell;
    }

    public CuisinesShell cuisinesShell(String prefix) {
        if (cuisinesShell != null) { return cuisinesShell; }
        cuisinesShell = new CuisinesShell(scanner, env, prefix);
        return cuisinesShell;
    }

    public CuisineShell cuisineShell(String prefix, Cuisine cuisine) {
        return new CuisineShell(scanner, env, prefix, cuisine);
    }

    public ItemCategoriesShell itemCategoriesShell(String prefix) {
        if (itemCategoriesShell != null) { return itemCategoriesShell; }
        itemCategoriesShell = new ItemCategoriesShell(scanner, env, prefix);
        return itemCategoriesShell;
    }

    public DishShell dishShell(String prefix, Dish dish) {
        return new DishShell(scanner, env, prefix, dish);
    }

    public ItemCategoryShell itemCategoryShell(String prefix, ItemCategory category) {
        return new ItemCategoryShell(scanner, env, prefix, category);
    }
}
