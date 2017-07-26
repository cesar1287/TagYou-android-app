package comcesar1287.github.tagyou.controller.domain;

import android.net.Uri;

public class Segment {

    private String name, nameFirebase;
    private Uri banner;

    public String getNameFirebase() {
        return nameFirebase;
    }

    public void setNameFirebase(String nameFirebase) {
        this.nameFirebase = nameFirebase;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getBanner() {
        return banner;
    }

    public void setBanner(Uri banner) {
        this.banner = banner;
    }
}
