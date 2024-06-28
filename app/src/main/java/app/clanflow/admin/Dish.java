package app.clanflow.admin;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firestore.v1.Document;
import com.google.firestore.v1.WriteRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Dish extends Doc {
    Firestore db;
    QueryDocumentSnapshot document;

    Dish(Firestore db_, QueryDocumentSnapshot doc_) {
        db = db_;
        document = doc_;
    }

    @Override
    public void Interact(Scanner scanner, String prefix) {
        String newPrefix = prefix + "/" + document.getString("name");
        while (true) {
            System.out.print(newPrefix + "> ");
            System.out.flush();
            String input = scanner.nextLine();
            if (input.compareTo("list") == 0 ||
                    input.compareTo("li") == 0) {
                list();
            }
            if (input.compareTo("add") == 0) {
                add(scanner, newPrefix);
            }
            if (input.startsWith("q")) {
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void add(Scanner scanner, String prefix) {
        try {
            ItemCategories categories = new ItemCategories(db);
            categories.Interact(scanner, prefix);

            if (categories.GetItemsAdded() != null) {
                List<DocumentReference> items = (List<DocumentReference>) document.get("items");
                items.addAll(categories.GetItemsAdded());
                DocumentReference dishRef = document.getReference();
                ApiFuture<WriteResult> future = dishRef.update("items", items);
                WriteResult result = future.get();
                System.out.println("Write result: " + result);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<DocumentSnapshot> listInternal() {
        List<DocumentSnapshot> itemDocs = new ArrayList<DocumentSnapshot>();

        try {
            List<DocumentReference> items = (List<DocumentReference>) document.get("items");
            for (DocumentReference item : items) {
                ApiFuture<DocumentSnapshot> itemDoc = item.get();
                itemDocs.add(itemDoc.get());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return itemDocs;
    }

    private void list() {
        try {
            List<DocumentSnapshot> items = listInternal();
            printDelimiter();
            if (items != null) {
                for (DocumentSnapshot item : items) {
                    System.out.print("Id: " + item.getId());
                    System.out.println(" Name: " + item.getString("name"));
                }
            }
            printDelimiter();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
