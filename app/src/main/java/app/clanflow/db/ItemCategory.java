package app.clanflow.db;

import java.util.List;

import com.google.cloud.firestore.DocumentReference;

public class ItemCategory {
    Collections collections;
    DocumentReference ref;
    String name;

    ItemCategory(Collections collections_, DocumentReference ref_, String name_) {
        collections = collections_;
        ref = ref_;
        name = name_;
    }

    DocumentReference ref() { return ref; }
    public String name() { return name; }

    public void print() {
        System.out.println("Id: " + ref.getId() + " name: " + name());
    }

    public List<Item> list() {
        return collections.itemsCollection().list(this);
    }
}
