package app.clanflow.db;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Cuisines {
    Collections collections;
    Firestore db;
    Map<DocumentReference, Cuisine> cuisines;

    public Cuisines(Collections collections_) {
        collections = collections_;
        db = collections.db();
    }

    public List<Cuisine> list() {
        if (cuisines == null) {
            cuisines = new HashMap<DocumentReference, Cuisine>();
            ApiFuture<QuerySnapshot> query = db.collection("cuisines").get();
            try {
                // ...
                // query.get() blocks on response
                QuerySnapshot querySnapshot = query.get();
                List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
                for (DocumentSnapshot doc : documents) {
                    Cuisine cuisine = new Cuisine(collections, doc.getReference(), doc.getString("name"));
                    cuisines.put(cuisine.ref(), cuisine);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        List<Cuisine> cuisineList = new ArrayList<Cuisine>();
        for (Map.Entry<DocumentReference, Cuisine> entry : cuisines.entrySet()) {
            cuisineList.add(entry.getValue());
        }
        return cuisineList;
    }

    public Cuisine add(String cuisineName) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", cuisineName);
        Cuisine cuisine = null;
        try {
            ApiFuture<DocumentReference> docRef = db.collection("cuisines").add(data);
            cuisine = new Cuisine(collections, docRef.get(), cuisineName);
            cuisines.put(cuisine.ref(), cuisine);
            return cuisine;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return cuisine;
    }

    public Cuisine lookup(DocumentReference cuisineRef) {
        if (cuisines == null) { list(); }
        return cuisines.get(cuisineRef);
    }
}
