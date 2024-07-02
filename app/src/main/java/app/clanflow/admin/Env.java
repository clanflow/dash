package app.clanflow.admin;

import java.util.Scanner;

import com.google.cloud.firestore.Firestore;

import app.clanflow.db.Collections;
import app.clanflow.shell.Shells;

public class Env {
    Collections collections;
    Shells shells;

    Env(Firestore db_, Scanner scanner_) {
        collections = new Collections(db_);
        shells = new Shells(scanner_, this);
    }

    void teardown() throws Exception {
        collections.close();
        shells.close();
    }

    public Collections collections() {
        return collections;
    }

    public Shells shells() {
        return shells;
    }
}
