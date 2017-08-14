package comcesar1287.github.tagyou.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.adroitandroid.chipcloud.ChipCloud;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.firebase.FirebaseHelper;

public class TagsFilterActivity extends AppCompatActivity implements View.OnClickListener{

    ChipCloud chipCloudAffinity;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tags_filter);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        chipCloudAffinity = (ChipCloud) findViewById(R.id.chip_cloud_affinity);

        String[] affinityArray = getResources().getStringArray(R.array.tags_filter_affinity);
        chipCloudAffinity.addChips(affinityArray);

        ChipCloud chipCloudGroups = (ChipCloud) findViewById(R.id.chip_cloud_groups);

        String[] groupsArray = getResources().getStringArray(R.array.tags_filter_groups);
        chipCloudGroups.addChips(groupsArray);

        ChipCloud chipCloudSegments = (ChipCloud) findViewById(R.id.chip_cloud_segments);

        String[] segmentsArray = getResources().getStringArray(R.array.tags_filter_segments);
        chipCloudSegments.addChips(segmentsArray);

        Button btNextSegments = (Button) findViewById(R.id.btNextAffinity);
        btNextSegments.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btNextAffinity:
                mDatabase.child(FirebaseHelper.FIREBASE_DATABASE_TAGS)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("teste")
                        .setValue("teste");
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
