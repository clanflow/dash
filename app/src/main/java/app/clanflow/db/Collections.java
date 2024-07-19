package app.clanflow.db;

import com.google.cloud.firestore.Firestore;

public class Collections {
    Firestore db;
    Cuisines cuisines;
    ItemCategories itemCategories;
    Dishes dishes;
    DishItemCategoryItems dishItemCategoryItems;
    Items items;

    public Collections(Firestore db_) {
        db = db_;
    }

    public void close() throws Exception {
        db.close();
    }

    Firestore db() {
        return db;
    }

    public Cuisines cuisinesCollection() {
        if (cuisines != null) {
            return cuisines;
        }

        cuisines = new Cuisines(this);
        return cuisines;
    }

    public ItemCategories itemCategoriesCollection() {
        if (itemCategories != null) {
            return itemCategories;
        }

        itemCategories = new ItemCategories(this);
        return itemCategories;
    }

    public Dishes dishesCollection() {
        if (dishes != null) {
            return dishes;
        }

        dishes = new Dishes(this);
        return dishes;
    }

    public Items itemsCollection() {
        if (items != null) {
            return items;
        }

        items = new Items(this);
        return items;
    }

    public DishItemCategoryItems dishItemCategoryItemsCollection() {
        if (dishItemCategoryItems != null) {
            return dishItemCategoryItems;
        }

        dishItemCategoryItems = new DishItemCategoryItems(this);
        return dishItemCategoryItems;
    }
}
