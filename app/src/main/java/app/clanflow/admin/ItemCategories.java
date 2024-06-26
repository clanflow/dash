package app.clanflow.admin;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import io.grpc.netty.shaded.io.netty.util.internal.IntegerHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ItemCategories extends Collection {
    Firestore db;

    public ItemCategories(Firestore db_) {
        db = db_;
    }

    @Override
    public void Interact() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("~/item_categories> ");
            System.out.flush();
            String input = scanner.nextLine();
            if (input.compareTo("list") == 0) {
                list();
            }
            if (input.compareTo("add") == 0) {
                add(scanner);
            }
            if (input.compareTo("pick") == 0) {
                pick(scanner);
            }
            if (input.startsWith("q")) {
                break;
            }
        }
    }

    private void add(Scanner scanner) {
        // Add cuisine
        System.out.print("name: ");
        String itemCategory = scanner.nextLine();
        Map<String, Object> data = new HashMap<>();
        data.put("name", itemCategory);
        try {
            ApiFuture<DocumentReference> docRef = db.collection("item_categories").add(data);
            System.out.println("Id: " + docRef.get().getId());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private List<QueryDocumentSnapshot> listInternal() {
        ApiFuture<QuerySnapshot> query = db.collection("item_categories").get();
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

    private void pick(Scanner scanner) {
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
            interact(scanner, documents.get(index));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void interact(Scanner scanner, QueryDocumentSnapshot document) {
        DocumentReference ref = document.getReference();
        Items i = new Items(db);
        while (true) {
            System.out.print("~/item_categories/" + document.getString("name") + "> ");
            System.out.flush();
            String input = scanner.nextLine();
            if (input.compareTo("list_items") == 0) {
                i.list(ref);
            }
            if (input.compareTo("add") == 0) {
                i.add(scanner, ref);
            }
            if (input.startsWith("q")) {
                break;
            }
        }
    }
}
