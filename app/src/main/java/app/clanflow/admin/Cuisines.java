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

public class Cuisines implements Collection {
    Firestore db;

    public Cuisines(Firestore db_) {
        db = db_;
    }

    @Override
    public void Interact() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("~/cuisines> ");
            System.out.flush();
            String input = scanner.nextLine();
            if (input.compareTo("list") == 0) {
                listCuisines();
            }
            if (input.compareTo("add") == 0) {
                addCuisine(scanner);
            }
            if (input.startsWith("q")) {
                break;
            }
        }
    }

    private void addCuisine(Scanner scanner) {
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

    private void listCuisines() {
        ApiFuture<QuerySnapshot> query = db.collection("cuisines").get();

        try {
            // ...
            // query.get() blocks on response
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                System.out.print("Id: " + document.getId());
                System.out.println(" Name: " + document.getString("name"));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
