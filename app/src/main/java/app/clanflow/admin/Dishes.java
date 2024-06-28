package app.clanflow.admin;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Dishes extends Collection {
    Firestore db;
    DocumentReference cuisineRef;

    public Dishes(Firestore db_) {
        this(db_, null);
    }

    public Dishes(Firestore db_, DocumentReference cuisineRef_) {
        db = db_;
        cuisineRef = cuisineRef_;
    }


    @Override
    public void Interact(Scanner scanner, String prefix) {
        String newPrefix = prefix + "/dishes";
        while (true) {
            System.out.print(newPrefix + "> ");
            System.out.flush();
            String input = scanner.nextLine();
            if (input.compareTo("help") == 0) {
                System.out.println("list: List dishes");
                System.out.println("add: Add a new dish");
                System.out.println("pick: Pick a dish");
                continue;
            }
            if (input.compareTo("list") == 0) {
                list();
            }
            if (input.compareTo("add") == 0) {
                add(scanner);
            }
            if (input.compareTo("pick") == 0) {
                pick(scanner, newPrefix);
            }
            if (input.startsWith("q")) {
                break;
            }
        }
    }

    private List<QueryDocumentSnapshot> listInternal() {
        ApiFuture<QuerySnapshot> query = null;

        if (cuisineRef != null) {
            Filter queryFilter = Filter.equalTo("cuisine", cuisineRef);
            query = db.collection("dishes").where(queryFilter).get();
        } else {
            query = db.collection("dishes").get();
        }

        try {
            // ...
            // query.get() blocks on response
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            return documents;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    public void list() {
        try {
            // ...
            // query.get() blocks on response
            List<QueryDocumentSnapshot> documents = listInternal();
            printDelimiter();
            for (QueryDocumentSnapshot document : documents) {
                System.out.print("Id: " + document.getId());
                System.out.println(" Name: " + document.getString("name"));
            }
            printDelimiter();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void add(Scanner scanner) {
        // Add an item.
        System.out.print("name: ");
        String item = scanner.nextLine();
        Map<String, Object> data = new HashMap<>();
        data.put("name", item);
        data.put("cuisine", cuisineRef);

        try {
            ApiFuture<DocumentReference> docRef = db.collection("dishes").add(data);
            System.out.println("Id: " + docRef.get().getId());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void pick(Scanner scanner, String prefix) {
        try {
            int idx = 0;
            List<QueryDocumentSnapshot> documents = listInternal();
            printDelimiter();
            if (documents != null) {
                for (QueryDocumentSnapshot document : documents) {
                    System.out.print("[" + idx + "] ");
                    System.out.print("Id: " + document.getId());
                    System.out.println(" Name: " + document.getString("name"));
                    idx = idx + 1;
                }
            }
            printDelimiter();

            System.out.print("Pick index: ");
            String input = scanner.nextLine();
            if (input.startsWith("q")) {
                return;
            }

            int index = Integer.parseInt(input);
            Dish d = new Dish(db, documents.get(index));
            d.Interact(scanner, prefix);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}