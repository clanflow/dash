package app.clanflow.db;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firestore.v1.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Dishes {
    Collections collections;
    Firestore db;
    Map<DocumentReference, Dish> dishes;

    Dishes(Collections collections_) {
        collections = collections_;
        db = collections.db();
    }

    @SuppressWarnings("unchecked")
    private void populateFromBackend() {
        if (dishes != null) {
            return;
        }

        dishes = new HashMap<DocumentReference, Dish>();
        ApiFuture<QuerySnapshot> query = db.collection("dishes").get();
        try {
            // ...
            // query.get() blocks on response
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                List<Item> items = new ArrayList<Item>();
                for (DocumentReference itemRef : (List<DocumentReference>) doc.get("items")) {
                    items.add(collections.itemsCollection().lookup(itemRef));
                }

                DocumentReference cuisineRef = (DocumentReference) doc.get("cuisine");
                Cuisine cuisine = collections.cuisinesCollection().lookup(cuisineRef);
                Dish dish = new Dish(collections, doc.getReference(),
                                     doc.getString("name"), cuisine, items);
                dishes.put(dish.ref(), dish);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
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

    public List<Dish> list(Cuisine cuisine) {
        if (dishes == null) {
            populateFromBackend();
        }

        List<Dish> dishList = new ArrayList<Dish>();
        for (Map.Entry<DocumentReference, Dish> entry : dishes.entrySet()) {
            if (cuisine != null) {
                if (entry.getValue().cuisine().ref() == cuisine.ref()) {
                    dishList.add(entry.getValue());
                }
            }
        }
        return dishList;
    }

    public Dish add(String dishName, Cuisine cuisine) {
        if (dishes == null) {
            populateFromBackend();
        }
        Map<String, Object> data = new HashMap<>();
        data.put("name", dishName);
        data.put("cuisine", cuisine.ref());
        Dish dish = null;

        try {
            ApiFuture<DocumentReference> docRef = db.collection("dishes").add(data);
            dish = new Dish(collections, docRef.get(), dishName, cuisine, null);
            dishes.put(dish.ref(), dish);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return dish;
    }

    public Dish lookup(DocumentReference dishRef) {
        if (dishes == null) {
            populateFromBackend();
        }
        return dishes.get(dishRef);
    }

    public void updateDish(Dish dish) {
        if (dishes == null) {
            populateFromBackend();
        }
        Map<String, Object> data = new HashMap<>();
        data.put("name", dish.name());
        data.put("cuisine", dish.cuisine().ref());
        List<DocumentReference> items = new ArrayList<DocumentReference>();
        for (Item item : dish.items()) {
            items.add(item.ref());
        }
        data.put("items", items);

        try {
            ApiFuture<WriteResult> future = dish.ref().update(data);
            future.get();
            dishes.put(dish.ref(), dish);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
