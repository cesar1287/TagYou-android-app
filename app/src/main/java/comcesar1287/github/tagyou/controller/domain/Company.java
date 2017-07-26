package comcesar1287.github.tagyou.controller.domain;

import android.net.Uri;

public class Company {

    private String name;
    private Uri banner;

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
