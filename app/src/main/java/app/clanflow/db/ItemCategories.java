package app.clanflow.db;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCategories {
    Collections collections;
    Firestore db;
    Map<DocumentReference, ItemCategory> itemCategories;

    ItemCategories(Collections collections_) {
        collections = collections_;
        db = collections.db();
    }

    private void populateFromBackend() {
        if (itemCategories != null) {
            return;
        }

        itemCategories = new HashMap<DocumentReference, ItemCategory>();
        ApiFuture<QuerySnapshot> query = db.collection("item_categories").get();
        try {
            // ...
            // query.get() blocks on response
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (DocumentSnapshot doc : documents) {
                ItemCategory category = new ItemCategory(doc.getReference(), doc.getString("name"));
                itemCategories.put(doc.getReference(), category);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public List<ItemCategory> list() {
        if (itemCategories == null) {
            populateFromBackend();
        }

        List<ItemCategory> categoryList = new ArrayList<ItemCategory>();
        for (Map.Entry<DocumentReference, ItemCategory> entry : itemCategories.entrySet()) {
            categoryList.add(entry.getValue());
        }
        return categoryList;
    }

    public ItemCategory add(String categoryName) {
        if (itemCategories == null) {
            populateFromBackend();
        }

        Map<String, Object> data = new HashMap<>();
        data.put("name", categoryName);
        ItemCategory category = null;
        try {
            ApiFuture<DocumentReference> docRef = db.collection("item_categories").add(data);
            category = new ItemCategory(docRef.get(), categoryName);
            itemCategories.put(category.ref(), category);
            return category;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return category;
    }

    public ItemCategory lookup(DocumentReference categoryRef) {
        if (itemCategories == null) {
            populateFromBackend();
        }

        return itemCategories.get(categoryRef);
    }
}
