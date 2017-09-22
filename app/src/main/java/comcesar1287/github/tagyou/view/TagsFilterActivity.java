package comcesar1287.github.tagyou.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.domain.Tag;
import comcesar1287.github.tagyou.controller.domain.Tags;
import comcesar1287.github.tagyou.controller.firebase.FirebaseHelper;
import comcesar1287.github.tagyou.controller.util.Utility;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipListener;

public class TagsFilterActivity extends AppCompatActivity implements View.OnClickListener{

    String [] arrayAffinity, arrayGroups, arraySegments;

    private DatabaseReference mDatabase;

    private Query tags;

    private Tag tag = new Tag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tags_filter);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        getCurrentTags();

        Button btNextSegments = (Button) findViewById(R.id.btNextAffinity);
        btNextSegments.setOnClickListener(this);
    }

    private void getCurrentTags() {
        tags = mDatabase
                .child(FirebaseHelper.FIREBASE_DATABASE_TAGS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        loadCurrentTag();
    }

    private void loadCurrentTag() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tag.setAffinity((String)dataSnapshot.child(Utility.AFFINITY).getValue());
                tag.setGroup((String)dataSnapshot.child(Utility.GROUP).getValue());
                tag.setSegment((String)dataSnapshot.child(Utility.SEGMENT).getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*dialog.dismiss();
                Toasty.error(PartnerCategoryActivity.this, getResources().getString(R.string.error_loading_partners), Toast.LENGTH_SHORT, true).show();
                finish();*/
            }
        };

        ValueEventListener singleValueEventListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                setupChipCloud();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*dialog.dismiss();
                Toasty.error(PartnerCategoryActivity.this, getResources().getString(R.string.error_loading_partners), Toast.LENGTH_SHORT, true).show();
                finish();*/
            }
        };

        tags.addValueEventListener(valueEventListener);

        tags.addListenerForSingleValueEvent(singleValueEventListener);
    }

    private void setupChipCloud() {
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

        for(int x = 0; x<affinity.length; x++){
            if(tag.getAffinity().contains(affinity[x])){
                chipCloudAffinity.setChecked(x);
                arrayAffinity[x] = affinity[x];
            }
        }

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

        for(int x = 0; x<groups.length; x++){
            if(tag.getGroup().contains(groups[x])){
                chipCloudGroups.setChecked(x);
                arrayGroups[x] = groups[x];
            }
        }

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

        for(int x = 0; x<segments.length; x++){
            if(tag.getSegment().contains(segments[x])){
                chipCloudSegments.setChecked(x);
                arraySegments[x] = segments[x];
            }
        }
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
