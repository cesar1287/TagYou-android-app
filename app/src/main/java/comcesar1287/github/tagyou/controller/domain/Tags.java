package comcesar1287.github.tagyou.controller.domain;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Tags {

    public String affinity, group, segment;

    public Tags() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Tags(String affinity, String group, String segment) {
        this.affinity = affinity;
        this.group = group;
        this.segment = segment;
    }
}
