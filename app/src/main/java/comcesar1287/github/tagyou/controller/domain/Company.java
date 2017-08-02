package comcesar1287.github.tagyou.controller.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Company implements Parcelable{

    private String name, description, address, phone, site, banner, logo;
    private int quantity;
    private double latitude, longitude;

    public Company(){

    }

    private Company(Parcel p){
        name = p.readString();
        description = p.readString();
        address = p.readString();
        phone = p.readString();
        site = p.readString();
        banner = p.readString();
        quantity = p.readInt();
        latitude = p.readDouble();
        longitude = p.readDouble();
    }

    public static final Parcelable.Creator<Company>
            CREATOR = new Parcelable.Creator<Company>() {

        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(address);
        parcel.writeString(phone);
        parcel.writeString(site);
        parcel.writeString(banner);
        parcel.writeInt(quantity);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
