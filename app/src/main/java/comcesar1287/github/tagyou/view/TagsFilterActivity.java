package comcesar1287.github.tagyou.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.domain.Tags;
import comcesar1287.github.tagyou.controller.firebase.FirebaseHelper;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipListener;

public class TagsFilterActivity extends AppCompatActivity implements View.OnClickListener{

    String [] arrayAffinity, arrayGroups, arraySegments;


    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tags_filter);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //To create the same wrapping cloud as previous incarnation use Google's FlexboxLayout:
        FlexboxLayout flexboxAffinity = (FlexboxLayout) findViewById(R.id.flex_affinity);

        ChipCloudConfig configAffinity = new ChipCloudConfig()
                .selectMode(fisk.chipcloud.ChipCloud.SelectMode.multi)
                .checkedChipColor(getResources().getColor(R.color.black))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(getResources().getColor(R.color.concrete))
                .uncheckedTextColor(Color.parseColor("#ffffff"))
                .useInsetPadding(true);

        //Create a new ChipCloud with a Context and ViewGroup:
        fisk.chipcloud.ChipCloud chipCloudAffinity = new fisk.chipcloud.ChipCloud(this, flexboxAffinity, configAffinity);

        final String[] affinity = getResources().getStringArray(R.array.tags_filter_affinity);
        chipCloudAffinity.addChips(affinity);

        arrayAffinity = new String[affinity.length];

        chipCloudAffinity.setListener(new fisk.chipcloud.ChipListener() {
            @Override
            public void chipCheckedChange(int i, boolean b, boolean b1) {
                if(b){
                    arrayAffinity[i] = affinity[i];
                }else{
                    arrayAffinity[i] = null;
                }
            }
        });

        //To create the same wrapping cloud as previous incarnation use Google's FlexboxLayout:
        FlexboxLayout flexboxGroups = (FlexboxLayout) findViewById(R.id.flex_groups);

        ChipCloudConfig configGroups = new ChipCloudConfig()
                .selectMode(fisk.chipcloud.ChipCloud.SelectMode.multi)
                .checkedChipColor(getResources().getColor(R.color.black))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(getResources().getColor(R.color.concrete))
                .uncheckedTextColor(Color.parseColor("#ffffff"))
                .useInsetPadding(true);

        //Create a new ChipCloud with a Context and ViewGroup:
        fisk.chipcloud.ChipCloud chipCloudGroups = new fisk.chipcloud.ChipCloud(this, flexboxGroups, configGroups);

        final String[] groups = getResources().getStringArray(R.array.tags_filter_groups);
        chipCloudGroups.addChips(groups);

        arrayGroups = new String[groups.length];

        chipCloudGroups.setListener(new ChipListener() {
            @Override
            public void chipCheckedChange(int i, boolean b, boolean b1) {
                if(b){
                    arrayGroups[i] = groups[i];
                }else{
                    arrayGroups[i] = null;
                }
            }
        });

        //To create the same wrapping cloud as previous incarnation use Google's FlexboxLayout:
        FlexboxLayout flexboxSegments = (FlexboxLayout) findViewById(R.id.flex_segments);

        ChipCloudConfig configSegments = new ChipCloudConfig()
                .selectMode(fisk.chipcloud.ChipCloud.SelectMode.multi)
                .checkedChipColor(getResources().getColor(R.color.black))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(getResources().getColor(R.color.concrete))
                .uncheckedTextColor(Color.parseColor("#ffffff"))
                .useInsetPadding(true);

        //Create a new ChipCloud with a Context and ViewGroup:
        fisk.chipcloud.ChipCloud chipCloudSegments = new fisk.chipcloud.ChipCloud(this, flexboxSegments, configSegments);

        final String[] segments = getResources().getStringArray(R.array.tags_filter_segments);
        chipCloudSegments.addChips(segments);

        arraySegments = new String[segments.length];

        chipCloudSegments.setListener(new ChipListener() {
            @Override
            public void chipCheckedChange(int i, boolean b, boolean b1) {
                if(b){
                    arraySegments[i] = segments[i];
                }else{
                    arraySegments[i] = null;
                }
            }
        });

        Button btNextSegments = (Button) findViewById(R.id.btNextAffinity);
        btNextSegments.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btNextAffinity:
                String affinity = "", groups = "", segments = "";
                for(String affinityItem : arrayAffinity){
                    if(affinityItem!=null) {
                         affinity += affinityItem+";";
                    }
                }

                for(String groupsItem : arrayGroups){
                    if(groupsItem!=null) {
                        groups += groupsItem+";";
                    }
                }

                for(String segmentsItem : arraySegments){
                    if(segmentsItem!=null) {
                        segments += segmentsItem+";";
                    }
                }

                Tags tags = new Tags(affinity, groups, segments);

                mDatabase.child(FirebaseHelper.FIREBASE_DATABASE_TAGS)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(tags);
                Toast.makeText(this, "Tags cadastradas com sucesso", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
