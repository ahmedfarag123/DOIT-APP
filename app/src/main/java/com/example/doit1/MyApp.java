package com.example.doit1;

import android.app.Application;
import android.app.Application;
import android.util.Log;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;



public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey("AIzaSyA_WsTwE1FnNCbejXY8DlXKLPuSVnvXHK4") // 🔹 Replace with your Firebase API Key
                .setApplicationId("1:856498537637:android:c1a76c96da2c3bb252f94c") // 🔹 Replace with your App ID
                .setDatabaseUrl("https://do-it-d4171-default-rtdb.firebaseio.com") // 🔹 Use your database URL
                .setProjectId("do-it-d4171") // 🔹 Use your Project ID
                .build();

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this, options);
            Log.d("Firebase", "✅ Firebase Initialized Manually");
        }
    }
}



