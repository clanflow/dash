package app.clanflow.admin;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Cuisines extends Collection {
    Firestore db;

    public Cuisines(Firestore db_) {
        db = db_;
    }

    @Override
    public void Interact(Scanner scanner, String prefix) {
        String newPrefix = prefix + "/cuisines";
        while (true) {
            System.out.print(newPrefix + "> ");
            System.out.flush();
            String input = scanner.nextLine();
            if (input.compareTo("help") == 0) {
                System.out.println("list: List different cuisines");
                System.out.println("add: Add new cuisine");
                System.out.println("pick: Pick a cuisine");
                continue;
            }
            if (input.compareTo("list") == 0) {
                list();
            }
            if (input.compareTo("add") == 0) {
                add(scanner, newPrefix);
            }
            if (input.compareTo("pick") == 0) {
                pick(scanner, newPrefix);
            }
            if (input.startsWith("q")) {
                break;
            }
        }
    }

    private void add(Scanner scanner, String prefix) {
        // Add cuisine
        System.out.print("name: ");
        String cuisineName = scanner.nextLine();
        Map<String, Object> data = new HashMap<>();
        data.put("name", cuisineName);
        try {
            ApiFuture<DocumentReference> docRef = db.collection("cuisines").add(data);
            System.out.println("Id: " + docRef.get().getId());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private List<QueryDocumentSnapshot> listInternal() {
        ApiFuture<QuerySnapshot> query = db.collection("cuisines").get();
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

    private void list() {
        try {
            List<QueryDocumentSnapshot> documents = listInternal();
            printDelimiter();
            if (documents != null) {
                for (QueryDocumentSnapshot document : documents) {
                    System.out.print("Id: " + document.getId());
                    System.out.println(" Name: " + document.getString("name"));
                }
            }
            printDelimiter();
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
                    System.out.print("[" + idx + "]");
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
            QueryDocumentSnapshot document = documents.get(index);
            Dishes d = new Dishes(db, document.getReference());
            d.Interact(scanner, prefix + "/" + document.getString("name"));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
