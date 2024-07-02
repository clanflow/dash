package app.clanflow.db;

import com.google.cloud.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class Dish {
    Collections collections;
    DocumentReference ref;
    String name;
    Cuisine cuisine;
    List<Item> items;

    Dish(Collections collections_, DocumentReference ref_, String name_, Cuisine cuisine_, List<Item> items_) {
        collections = collections_;
        ref = ref_;
        name = name_;
        cuisine = cuisine_;
        items = items_;
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
        return items;
    }

    public void addItem(Item item) {
        if (items == null) {
            items = new ArrayList<Item>();
        }

        // ...
    }

    public void print() {
        System.out.println("Id: " + ref.getId() + " name: " + name());
    }
}
