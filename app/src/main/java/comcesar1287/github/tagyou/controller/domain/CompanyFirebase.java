package comcesar1287.github.tagyou.controller.domain;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CompanyFirebase {

    public String name, description, email, address, phone, site, banner, logo, hashtag, description_offer, description_desire, social_network;
    public int quantity;
    public double latitude, longitude;

    public CompanyFirebase() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public CompanyFirebase(String name, String description, String email, String address, String phone, String site, String banner,
                           String logo, int quantity, double latitude, double longitude, String hashtag, String description_offer, String description_desire, String social_network) {
        this.name = name;
        this.description = description;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.site = site;
        this.banner = banner;
        this.logo = logo;
        this.quantity = quantity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hashtag = hashtag;
        this.description_offer = description_offer;
        this.description_desire = description_desire;
        this.social_network = social_network;
    }
}
