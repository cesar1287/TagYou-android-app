package comcesar1287.github.tagyou.controller.domain;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CompanyFirebase {

    public String name, description, address, phone, site, banner, logo, hashtag;
    public int quantity;
    public double latitude, longitude;

    public CompanyFirebase() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public CompanyFirebase(String name, String description, String address, String phone, String site, String banner,
                           String logo, int quantity, double latitude, double longitude, String hashtag) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.site = site;
        this.banner = banner;
        this.logo = logo;
        this.quantity = quantity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hashtag = hashtag;
    }
}
