package comcesar1287.github.tagyou.controller.firebase;

import com.google.firebase.database.DatabaseReference;

import comcesar1287.github.tagyou.controller.domain.Company;
import comcesar1287.github.tagyou.controller.domain.CompanyFirebase;
import comcesar1287.github.tagyou.controller.domain.User;

public class FirebaseHelper {

    public static final String FIREBASE_DATABASE_USERS = "users";
    public static final String FIREBASE_DATABASE_COMPANIES = "companies";
    public static final String FIREBASE_DATABASE_TAGS = "tags";

    public static void writeNewUser(DatabaseReference mDatabase, String userId, String name,
                                    String email, String birth, String sex, String phone, String profile_pic,
                                    String hashtag, String description_offer, String description_desire, String social_network) {

        User user = new User(name, email, birth, phone, sex, profile_pic, hashtag, description_offer, description_desire, social_network);

        mDatabase.child(FIREBASE_DATABASE_USERS).child(userId).setValue(user);
    }

    public static void writeNewCompany(DatabaseReference mDatabase, String userId, String name, String description,
                                    String email, String address, String phone, String site, String banner, String logo, int quantity,
                                       double latitude, double longitude, String hashtag) {

        CompanyFirebase company = new CompanyFirebase(name, description, email, address, phone, site, banner, logo, quantity,
                latitude, longitude, hashtag);

        mDatabase.child(FIREBASE_DATABASE_COMPANIES).child(userId).setValue(company);
    }
}
