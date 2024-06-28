package app.clanflow.admin;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ItemCategories extends Collection {
    Firestore db;
    List<DocumentReference> addedCuisines;
    List<DocumentReference> addedItems;

    public ItemCategories(Firestore db_) {
        db = db_;
        addedCuisines = null;
        addedItems = null;
    }

    public List<DocumentReference> GetItemsAdded() {
        return addedItems;
    }

    public List<DocumentReference> GetCuisinesAdded() {
        return addedCuisines;
    }

    @Override
    public void Interact(Scanner scanner, String prefix) {
        String newPrefix = prefix + "/item_categories";
        while (true) {
            System.out.print(prefix + "/item_categories> ");
            System.out.flush();
            String input = scanner.nextLine();
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
        String itemCategory = scanner.nextLine();
        Map<String, Object> data = new HashMap<>();
        data.put("name", itemCategory);
        try {
            ApiFuture<DocumentReference> docRef = db.collection("item_categories").add(data);
            if (addedCuisines == null) {
                addedCuisines = new ArrayList<DocumentReference>();
            }

            DocumentReference doc = docRef.get();
            addedCuisines.add(doc);
            System.out.println("Id: " + doc.getId());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public List<QueryDocumentSnapshot> List() {
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
            List<QueryDocumentSnapshot> documents = List();
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
            List<QueryDocumentSnapshot> documents = List();
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
            interact(scanner, prefix, documents.get(index));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void interact(Scanner scanner, String prefix, QueryDocumentSnapshot document) {
        DocumentReference ref = document.getReference();
        String newPrefix = prefix + "/" + document.getString("name");
        Items i = new Items(db);
        while (true) {
            System.out.print(newPrefix + "> ");
            System.out.flush();
            String input = scanner.nextLine();
            if (input.compareTo("list_items") == 0 ||
                input.compareTo("li") == 0) {
                i.List(ref);
            }
            if (input.compareTo("add") == 0) {
                i.add(scanner, newPrefix, ref);
            }
            if (input.startsWith("q")) {
                break;
            }
        }

        List<DocumentReference> newItems = i.GetItemsAdded();
        if (newItems != null) {
            if (addedItems == null) {
                addedItems = new ArrayList<DocumentReference>();
            }

            addedItems.addAll(newItems);
        }
    }
}
