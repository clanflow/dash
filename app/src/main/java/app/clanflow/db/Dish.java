package app.clanflow.db;

import com.google.cloud.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dish {
    Collections collections;
    DocumentReference ref;
    String name;
    Cuisine cuisine;
    Map<DocumentReference, ItemCategoryItems> itemCategoryItems;
    boolean dishModified;

    Dish(Collections collections_, DocumentReference ref_,
         String name_, Cuisine cuisine_, List<ItemCategoryItems> itemCategoryItemsList_) {
        collections = collections_;
        ref = ref_;
        name = name_;
        cuisine = cuisine_;
        if (itemCategoryItemsList_ != null) {
            itemCategoryItems = new HashMap<DocumentReference, ItemCategoryItems>();
            for (ItemCategoryItems itemCategoryItemsItem : itemCategoryItemsList_) {
                itemCategoryItems.put(itemCategoryItemsItem.ref(), itemCategoryItemsItem);
            }
        }

        dishModified = false;
    }

    public DocumentReference ref() {
        return ref;
    }

    public String name() {
        return name;
    }

    public Cuisine cuisine() {
        return cuisine;
    }

    public List<ItemCategoryItems> itemCategoryItems() {
        List<ItemCategoryItems> itemCategoryItemsList = new ArrayList<ItemCategoryItems>();
        if (itemCategoryItems != null) {
            for (Map.Entry<DocumentReference, ItemCategoryItems> entry : itemCategoryItems.entrySet()) {
                itemCategoryItemsList.add(entry.getValue());
            }
        }

        return itemCategoryItemsList;
    }

    public void addItem(Item item) {
        if (itemCategoryItems == null) {
            itemCategoryItems = new HashMap<DocumentReference, ItemCategoryItems>();
        }

        items.put(item.ref(), item);
        dishModified = true;
    }

    public void print() {
        System.out.println("Id: " + ref.getId() + " name: " + name());
    }

    public void close() {
        if (!dishModified) {
            return;
        }

        collections.dishesCollection().updateDish(this);
    }
}
