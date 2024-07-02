package app.clanflow.db;

import java.util.List;

import com.google.cloud.firestore.DocumentReference;

public class ItemCategory {
    DocumentReference ref;
    String name;

    ItemCategory(DocumentReference ref_, String name_) {
        ref = ref_;
        name = name_;
    }

    DocumentReference ref() { return ref; }
    public String name() { return name; }

    public void print() {
        System.out.println("Id: " + ref.getId() + " name: " + name());
    }

    public List<Item> list() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'list'");
    }
}
