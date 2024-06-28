package app.clanflow.admin;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Items extends Collection {
    Firestore db;
    List<DocumentReference> addedItems;

    public Items(Firestore db_) {
        db = db_;
    }

    public List<DocumentReference> GetItemsAdded() {
        return addedItems;
    }

    @Override
    public void Interact(Scanner scanner, String prefix) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Interact'");
    }

    public void add(Scanner scanner, String prefix, DocumentReference categoryRef) {
        // Add an item.
        System.out.print("name: ");
        String item = scanner.nextLine();
        Map<String, Object> data = new HashMap<>();
        data.put("name", item);
        data.put("category", categoryRef);

        try {
            ApiFuture<DocumentReference> docRef = db.collection("items").add(data);
            if (addedItems == null) {
                addedItems = new ArrayList<DocumentReference>();
            }

            DocumentReference doc = docRef.get();
            addedItems.add(doc);
            System.out.println("Id: " + doc.getId());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public List<QueryDocumentSnapshot> List(DocumentReference categoryRef) {
        Filter queryFilter = Filter.equalTo("category", categoryRef);
        ApiFuture<QuerySnapshot> query = db.collection("items").where(queryFilter).get();
        List<QueryDocumentSnapshot> items = new ArrayList<QueryDocumentSnapshot>();

        try {
            // ...
            // query.get() blocks on response
            QuerySnapshot querySnapshot = query.get();
            items = querySnapshot.getDocuments();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return items;
    }

    public void ListAndPrint(DocumentReference categoryRef) {
        List<QueryDocumentSnapshot> items = List(categoryRef);

        try {
            printDelimiter();
            for (QueryDocumentSnapshot item : items) {
                System.out.print("Id: " + item.getId());
                System.out.println(" Name: " + item.getString("name"));
            }
            printDelimiter();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}