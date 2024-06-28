package app.clanflow.admin;

import java.io.IOException;
import java.util.Scanner;

public class Shell {
    Collections collections;

    public Shell(Collections c) {
        collections = c;
    }

    public void Run() throws IOException {
        ShowBanner();
        Scanner scanner = new Scanner(System.in);
        String prefix = "~";
        while (true) {
            System.out.print(prefix + "> ");
            System.out.flush();
            String input = scanner.nextLine();
            if (input.compareTo("help") == 0) {
                System.out.println("c|cuisines: Cuisines");
                System.out.println("ic|item_categories: Item categories");
                continue;
            }
            
            if (input.compareTo("c") == 0 ||
                input.compareTo("cuisines") == 0) {
                Cuisines c = collections.getCuisines();
                c.Interact(scanner, prefix);
            }
            if (input.compareTo("ic") == 0 ||
            input.compareTo("item_categories") == 0) {
                ItemCategories c = collections.getItemCategories();
                c.Interact(scanner, prefix);
            }
            if (input.startsWith("q")) {
                break;
            }
        }

        System.out.println(" *** BYE ***");
        scanner.close();
    }

    private void ShowBanner() {
        System.out.println("*** Welcome to ClanFlow Admin Dashboard ***");
        System.out.println(" Press 'q' to exit.");
        System.out.println("*******************************************");
        System.out.flush();
        System.err.flush();
    }
}
