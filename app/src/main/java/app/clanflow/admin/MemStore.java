package app.clanflow.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;

public class MemStore {
    Map<String, DocumentReference> catNameRefMap;
    Map<DocumentReference, String> catRefNameMap;
    Map<String, Map<String, DocumentReference>> items;

    public void CacheItems(Firestore db) {
        if (items != null) {
            return;
        }

        catNameRefMap = new HashMap<String, DocumentReference>();
        catRefNameMap = new HashMap<DocumentReference, String>();
        items = new HashMap<String, Map<String, DocumentReference>>();

        ItemCategories c = new ItemCategories(db);
        Items i = new Items(db);
        List<QueryDocumentSnapshot> categories = c.List();

        for (QueryDocumentSnapshot category : categories) {
            DocumentReference categoryRef = category.getReference();
            catNameRefMap.put(category.getString("name"), categoryRef);
            catRefNameMap.put(categoryRef, category.getString("name"));

            List<QueryDocumentSnapshot> categoryItems = i.List(categoryRef);
            Map<String, DocumentReference> itemMap = new HashMap<String, DocumentReference>();
            for (QueryDocumentSnapshot item : categoryItems) {
                itemMap.put(item.getString("name"), item.getReference());
            }

            items.put(category.getString("name"), itemMap);
        }
    }

    public void CacheCategory(String category, DocumentReference categoryRef) {
        catNameRefMap.put(category, categoryRef);
        catRefNameMap.put(categoryRef, category);
        items.put(category, new HashMap<String, DocumentReference>());
    }

    public void CacheItem(DocumentReference categoryRef, String item, DocumentReference itemRef) {
        String category = catRefNameMap.get(categoryRef);
        items.get(category).put(item, itemRef);
    }
}
