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

public class Items extends Collection {
    Firestore db;

    public Items(Firestore db_) {
        db = db_;
    }

    @Override
    public void Interact() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Interact'");
    }

    public void add(Scanner scanner, DocumentReference categoryRef) {
        // Add an item.
        System.out.print("name: ");
        String item = scanner.nextLine();
        Map<String, Object> data = new HashMap<>();
        data.put("name", item);
        data.put("category", categoryRef);

        try {
            ApiFuture<DocumentReference> docRef = db.collection("items").add(data);
            System.out.println("Id: " + docRef.get().getId());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void list(DocumentReference categoryRef) {
        Filter queryFilter = Filter.equalTo("category", categoryRef);
        ApiFuture<QuerySnapshot> query = db.collection("items").where(queryFilter).get();

        try {
            // ...
            // query.get() blocks on response
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
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
}