package com.sf.evento.Services;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.sf.evento.Classes.User;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private FirebaseFirestore db;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */

    @Override
    public void onNewToken(String token) {
        db = FirebaseFirestore.getInstance();
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        User u = new User();
        u.setToken(token);
        u.SaveToken(db);
    }

}
