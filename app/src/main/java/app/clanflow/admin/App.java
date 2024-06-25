package app.clanflow.admin;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello gradle Java World!!!");
        System.out.flush();

        try {
            FileInputStream serviceAccount = new FileInputStream("./ServiceAccountKey.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp defaultApp = FirebaseApp.initializeApp(options);
            Firestore db = FirestoreClient.getFirestore(defaultApp);

            Collections collections = new Collections(db);
            Shell shell = new Shell(collections);
            shell.Run();
/*
            ApiFuture<QuerySnapshot> query = db.collection("users").get();
            // ...
            // query.get() blocks on response
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                System.out.println("User: " + document.getId());
                System.out.println("Name: " + document.getString("display_name"));
                System.out.println("Email: " + document.getString("email"));
            }

            
            // Add Indian cuisine
            Map<String, Object> data = new HashMap<>();
            data.put("name", "Mexican");
            ApiFuture<DocumentReference> docRef = db.collection("cuisines").add(data);
            // ...
            // result.get() blocks on response
            System.out.println("Update time : " + docRef.get().getId());
*/
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        System.out.println("Done !!!");
    }

    public String getGreeting() {
        return "Hello World!";
    }
}
