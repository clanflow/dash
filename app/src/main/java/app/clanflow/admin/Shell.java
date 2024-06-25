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
        while (true) {
            System.out.print("> ");
            System.out.flush();
            String input = scanner.nextLine();
            System.out.println("Hello, " + input + "!");
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
