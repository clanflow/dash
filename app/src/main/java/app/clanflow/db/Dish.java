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
    Map<DocumentReference, Item> items;
    boolean dishModified;

    Dish(Collections collections_, DocumentReference ref_, String name_, Cuisine cuisine_, List<Item> items_) {
        collections = collections_;
        ref = ref_;
        name = name_;
        cuisine = cuisine_;
        if (items_ != null) {
            items = new HashMap<DocumentReference, Item>();
            for (Item item : items_) {
                items.put(item.ref(), item);
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

    public List<Item> items() {
        List<Item> itemsList = new ArrayList<Item>();
        for (Map.Entry<DocumentReference, Item> entry : items.entrySet()) {
            itemsList.add(entry.getValue());
        }

        return itemsList;
    }

    public void addItem(Item item) {
        if (items == null) {
            items = new HashMap<DocumentReference, Item>();
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
