package app.clanflow.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.DocumentReference;

public class Cuisine {
    Collections collections;
    DocumentReference ref;
    String name;
    Map<DocumentReference, Dish> dishes;

    public Cuisine(Collections collections_, DocumentReference ref_, String name_) {
        collections = collections_;
        ref = ref_;
        name = name_;
    }

    public DocumentReference ref() {
        return ref;
    }

    public String name() {
        return name;
    }

    public void print() {
        System.out.println("Id: " + ref.getId() + " name: " + name());
    }

    private void populateFromBackend() {
        if (dishes != null) {
            return;
        }

        List<Dish> dishList = collections.dishesCollection().list(this);
        dishes = new HashMap<DocumentReference, Dish>();

        for (Dish dish : dishList) {
            dishes.put(dish.ref(), dish);
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

    public void add(Dish dish) {
        dishes.put(dish.ref(), dish);
    }
}
