package app.clanflow.shell;

import java.util.Scanner;

import app.clanflow.admin.Env;

public class RootShell implements Shell {
    Scanner scanner;
    Env env;
    String prefix;
    boolean quit;

    public RootShell(Scanner scanner_, Env env_) {
        scanner = scanner_;
        env = env_;
        prefix = "~";
    }

    @Override
    public void interact() {
        ShowBanner();

        while (!quit) {
            Shell.PrintPrompt(prefix);
            String input = scanner.nextLine();
            if (input.compareTo("help") == 0) {
                System.out.println("c|cuisines: Cuisines");
                System.out.println("ic|item_categories: Item categories");
                System.out.println("q: Quit");
                continue;
            }
            
            if (input.compareTo("c") == 0 ||
                input.compareTo("cuisines") == 0) {
                CuisinesShell c = env.shells().cuisinesShell(prefix);
                c.interact();
                if (c.quit()) {
                    break;
                }
            }
            if (input.compareTo("ic") == 0 ||
                input.compareTo("item_categories") == 0) {
                ItemCategoriesShell ic = env.shells().itemCategoriesShell(prefix);
                ic.interact();
                if (ic.quit()) {
                    break;
                }
            }
            if (input.compareTo("q") == 0) {
                quit = true;
                break;
            }
        }

        System.out.println(" *** BYE ***");
        Shell.PrintDelimiter();
    }

    private void ShowBanner() {
        System.out.println("*** Welcome to ClanFlow Admin Dashboard ***");
        System.out.println(" Press 'q' to exit.");
        Shell.PrintDelimiter();
    }

    @Override
    public boolean quit() {
        return quit;
    }
}
