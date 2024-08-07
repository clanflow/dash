package app.clanflow.db;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

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
            List<QueryDocumentSnapshot> dishDocuments = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot dishDoc : dishDocuments) {
                List<ItemCategoryItems> itemCategoryItemsList = new ArrayList<ItemCategoryItems>();
                ApiFuture<QuerySnapshot> itemsQuery = dishDoc.getReference()
                        .collection("dish_item_category_items").get();
                QuerySnapshot itemsQuerySnapshot = itemsQuery.get();
                List<QueryDocumentSnapshot> itemsDocuments = itemsQuerySnapshot.getDocuments();
                for (QueryDocumentSnapshot itemsDoc : itemsDocuments) {
                    // System.out.println("category: " + itemsDoc.get("category_name"));
                    DocumentReference categoryRef = (DocumentReference) itemsDoc.get("category");
                    ItemCategory itemCategory = collections.itemCategoriesCollection().lookup(categoryRef);
                    List<Item> items = new ArrayList<Item>();
                    List<DocumentReference> itemRefs = (List<DocumentReference>) itemsDoc.get("items");
                    if (itemRefs != null) {
                        for (DocumentReference itemRef : itemRefs) {
                            items.add(collections.itemsCollection().lookup(itemRef));
                        }
                    }

                    ItemCategoryItems itemCategoryItems = new ItemCategoryItems(collections, itemsDoc.getReference(),
                            itemCategory, items);
                    itemCategoryItemsList.add(itemCategoryItems);
                }

                DocumentReference cuisineRef = (DocumentReference) dishDoc.get("cuisine");
                Cuisine cuisine = collections.cuisinesCollection().lookup(cuisineRef);
                Dish dish = new Dish(collections, dishDoc.getReference(),
                        dishDoc.getString("name"), cuisine, itemCategoryItemsList);
                dishes.put(dish.ref(), dish);
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
            e.printStackTrace();
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

        try {
            ApiFuture<WriteResult> future = dish.ref().update(data);
            future.get();
            dishes.put(dish.ref(), dish);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        for (ItemCategoryItems itemCategoryItems : dish.itemCategoryItems()) {
            if (itemCategoryItems.modified()) {
                collections.dishItemCategoryItemsCollection().updateDishItemCategoryItems(itemCategoryItems);
            }
        }
    }
}
