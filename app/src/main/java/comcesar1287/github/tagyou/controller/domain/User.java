package comcesar1287.github.tagyou.controller.domain;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String name, email, birth, phone, sex, profile_pic, hashtag, description_offer, description_desire, social_network;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, String birth, String phone, String sex, String profile_pic,
                String hashtag, String description_offer, String description_desire, String social_network) {
        this.name = name;
        this.email = email;
        this.birth = birth;
        this.phone = phone;
        this.sex = sex;
        this.profile_pic = profile_pic;
        this.hashtag = hashtag;
        this.description_offer = description_offer;
        this.description_desire = description_desire;
        this.social_network = social_network;
    }
}
