package app.clanflow.db;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DishItemCategoryItems {
    Collections collections;
    Firestore db;

    public DishItemCategoryItems(Collections collections_) {
        collections = collections_;
        db = collections.db();
    }

    public ItemCategoryItems add(Dish dish, ItemCategory itemCategory, Item item) {
        Map<String, Object> data = new HashMap<>();
        data.put("category", itemCategory.ref());
        data.put("category_name", itemCategory.name());
        List<DocumentReference> items = new ArrayList<DocumentReference>();
        List<String> itemNames = new ArrayList<String>();

        items.add(item.ref());
        itemNames.add(item.name());

        data.put("items", items);
        data.put("item_names", itemNames);

        try {
            ApiFuture<DocumentReference> docRef = dish.ref().collection("dish_item_category_items").add(data);
            docRef.get();
            List<Item> itemsList = new ArrayList<Item>();
            itemsList.add(item);
            return new ItemCategoryItems(collections, docRef.get(), itemCategory, itemsList);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public void updateDishItemCategoryItems(ItemCategoryItems itemCategoryItems) {
        Map<String, Object> data = new HashMap<>();
        data.put("category", itemCategoryItems.itemCategory.ref());
        data.put("category_name", itemCategoryItems.itemCategory.name());
        List<DocumentReference> items = new ArrayList<DocumentReference>();
        List<String> itemNames = new ArrayList<String>();

        for (Item item : itemCategoryItems.items()) {
            items.add(item.ref());
            itemNames.add(item.name());
        }

        data.put("items", items);
        data.put("item_names", itemNames);

        try {
            ApiFuture<WriteResult> future = itemCategoryItems.ref().update(data);
            future.get();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
