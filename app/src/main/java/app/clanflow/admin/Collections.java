package app.clanflow.admin;

import com.google.cloud.firestore.Firestore;

public class Collections {
    Firestore db;

    public Collections(Firestore db_) {
        db = db_;
    }
 
    Cuisines getCuisines() {
        return new Cuisines(db);
    }

    ItemCategories getItemCategories() {
        return new ItemCategories(db);
    }
}
