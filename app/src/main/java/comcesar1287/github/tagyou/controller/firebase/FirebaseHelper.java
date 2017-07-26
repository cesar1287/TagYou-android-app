package comcesar1287.github.tagyou.controller.firebase;

import com.google.firebase.database.DatabaseReference;

import comcesar1287.github.tagyou.controller.domain.User;

public class FirebaseHelper {

    public static final String FIREBASE_DATABASE_USERS = "users";

    public static void writeNewUser(DatabaseReference mDatabase, String userId, String name, String email, String birth, String sex, String phone, String plan, String profile_pic) {

        User user = new User(name, email, birth, phone, sex, plan, profile_pic);

        mDatabase.child(FIREBASE_DATABASE_USERS).child(userId).setValue(user);
    }
}