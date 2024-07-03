package app.clanflow.db;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Items {
    Collections collections;
    Firestore db;
    Map<DocumentReference, Item> items;

    Items(Collections collections_) {
        collections = collections_;
        db = collections.db();
    }

    private void populateFromBackend() {
        if (items != null) {
            return;
        }

        items = new HashMap<DocumentReference, Item>();
        ApiFuture<QuerySnapshot> query = db.collection("items").get();
        try {
            // ...
            // query.get() blocks on response
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                DocumentReference categoryRef = (DocumentReference) doc.get("category");
                ItemCategory category = collections.itemCategoriesCollection().lookup(categoryRef);
                Item item = new Item(doc.getReference(), doc.getString("name"),
                        category);
                items.put(item.ref(), item);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Item> list(ItemCategory category) {
        if (items == null) {
            populateFromBackend();
        }

        List<Item> itemList = new ArrayList<Item>();
        for (Map.Entry<DocumentReference, Item> entry : items.entrySet()) {
            if (category != null) {
                if (entry.getValue().itemCategory().ref() == category.ref()) {
                    itemList.add(entry.getValue());
                }
            }
        }
        return itemList;
    }

    public Item add(String itemName, ItemCategory category) {
        if (items == null) {
            populateFromBackend();
        }

        Map<String, Object> data = new HashMap<>();
        data.put("name", itemName);
        data.put("category", category.ref());
        Item item = null;

        try {
            ApiFuture<DocumentReference> docRef = db.collection("items").add(data);
            item = new Item(docRef.get(), itemName, category);
            items.put(item.ref(), item);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return item;
    }

    public Item lookup(DocumentReference itemRef) {
        if (items == null) {
            populateFromBackend();
        }

        return items.get(itemRef);
    }

    public List<Item> list(String itemName) {
        if (items == null) {
            populateFromBackend();
        }

        List<Item> itemList = new ArrayList<Item>();
        for (Map.Entry<DocumentReference, Item> entry : items.entrySet()) {
            if (entry.getValue().name().toLowerCase().contains(itemName.toLowerCase())) {
                itemList.add(entry.getValue());
            }
        }
        return itemList;
    }
}
