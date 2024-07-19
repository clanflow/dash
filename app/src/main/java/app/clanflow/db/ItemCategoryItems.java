package app.clanflow.db;

import com.google.cloud.firestore.DocumentReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCategoryItems {
    Collections collections;
    DocumentReference ref;
    ItemCategory itemCategory;
    Map<DocumentReference, Item> items;
    boolean modified;

    ItemCategoryItems(Collections collections_, DocumentReference ref_,
                      ItemCategory itemCategory_, List<Item> items_) {
        collections = collections_;
        ref = ref_;
        itemCategory = itemCategory_;
        if (items_ != null) {
            items = new HashMap<DocumentReference, Item>();
            for (Item item : items_) {
                items.put(item.ref(), item);
            }
        }
    }

    public DocumentReference ref() {
        return ref;
    }

    public ItemCategory itemCategory() {
        return itemCategory;
    }

    public List<Item> items() {
        List<Item> itemsList = new ArrayList<Item>();
        if (items != null) {
            for (Map.Entry<DocumentReference, Item> entry : items.entrySet()) {
                itemsList.add(entry.getValue());
            }
        }

        return itemsList;
    }

    public void addItem(Item item) {
        if (items == null) {
            items = new HashMap<DocumentReference, Item>();
        }

        items.put(item.ref(), item);
        modified = true;
    }

    public void print() {
        System.out.print("Id: " + ref.getId() + " name: " + itemCategory.name() + "[");
        if (items != null) {
            for (Map.Entry<DocumentReference, Item> entry : items.entrySet()) {
                System.out.print(entry.getValue().name() + ", ");
            }
        }

        System.out.println("]");

    }

    public void close() {
        if (!modified) {
            return;
        }

        collections.dishItemCategoryItemsCollection().updateDishItemCategoryItems(this);
    }
}
