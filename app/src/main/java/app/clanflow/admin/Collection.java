package app.clanflow.admin;

import java.util.Scanner;

public abstract class Collection {
    public abstract void Interact(Scanner scanner, String prefix);

    void printDelimiter() {
        System.out.println("================================================");
    }
}
