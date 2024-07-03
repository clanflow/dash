package app.clanflow.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

public class Cuisine {
    Collections collections;
    DocumentReference ref;
    String name;
    Map<DocumentReference, Dish> dishes;

    public Cuisine(Collections collections_, DocumentReference ref_, String name_) {
        collections = collections_;
        ref = ref_;
        name = name_;
    }

    public DocumentReference ref() {
        return ref;
    }

    public String name() {
        return name;
    }

    public void print() {
        System.out.println("Id: " + ref.getId() + " name: " + name());
    }

    @SuppressWarnings("unchecked")
    private void populateFromBackend() {
        if (dishes != null) {
            return;
        }

        Firestore db = ref.getFirestore();
        Filter filter = Filter.equalTo("cuisine", ref);
        ApiFuture<QuerySnapshot> query = db.collection("dishes").where(filter).get();
        dishes = new HashMap<DocumentReference, Dish>();

        try {
            // ...
            // query.get() blocks on response
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (DocumentSnapshot doc : documents) {
                List<Item> items = new ArrayList<Item>();
                Object itemsObject = doc.get("items");
                if (itemsObject != null) {
                    for (DocumentReference itemRef : (List<DocumentReference>) doc.get("items")) {
                        items.add(collections.itemsCollection().lookup(itemRef));
                    }
                }

                dishes.put(doc.getReference(), new Dish(collections, doc.getReference(),
                        doc.getString("name"), this, items));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Dish> list() {
        if (dishes == null) {
            populateFromBackend();
        }

        List<Dish> dishList = new ArrayList<Dish>();
        for (Map.Entry<DocumentReference, Dish> entry : dishes.entrySet()) {
            dishList.add(entry.getValue());
        }

        return dishList;
    }
}
