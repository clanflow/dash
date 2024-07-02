package app.clanflow.db;

import com.google.cloud.firestore.DocumentReference;

public class Item {
    DocumentReference ref;
    String name;
    ItemCategory itemCategory;

    Item(DocumentReference ref_, String name_, ItemCategory itemCategory_) {
        ref = ref_;
        name = name_;
        itemCategory = itemCategory_;
    }

    public DocumentReference ref() {
        return ref;
    }

    public String name() {
        return name;
    }

    public ItemCategory itemCategory() {
        return itemCategory;
    }

    public void print() {
        System.out.println("Id: " + ref.getId() + " category: "
                + itemCategory.name() + " name: " + name());
    }

}
