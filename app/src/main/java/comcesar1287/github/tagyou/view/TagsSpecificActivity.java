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
import comcesar1287.github.tagyou.controller.firebase.FirebaseHelper;
import comcesar1287.github.tagyou.controller.util.Utility;
import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipListener;

public class TagsSpecificActivity extends AppCompatActivity implements View.OnClickListener{

    private String tagDb, tagFirebase;

    private String [] array;

    private DatabaseReference mDatabase;

    private Query tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags_specific);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        tagDb = getIntent().getStringExtra(Utility.KEY_CONTENT_EXTRA_TAG);

        getCurrentTag();

        Button btNextSegments = (Button) findViewById(R.id.btNextAffinity);
        btNextSegments.setOnClickListener(this);
    }

    private void getCurrentTag() {
        tag = mDatabase
                .child(FirebaseHelper.FIREBASE_DATABASE_TAGS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(tagDb);

        loadCurrentTag();
    }

    private void loadCurrentTag() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tagFirebase = String.valueOf(dataSnapshot.getValue());
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

        tag.addValueEventListener(valueEventListener);

        tag.addListenerForSingleValueEvent(singleValueEventListener);
    }

    private void setupChipCloud() {
        switch (tagDb){
            case Utility.AFFINITY:
                //To create the same wrapping cloud as previous incarnation use Google's FlexboxLayout:
                FlexboxLayout flexboxAffinity = (FlexboxLayout) findViewById(R.id.flex);

                ChipCloudConfig configAffinity = new ChipCloudConfig()
                        .selectMode(fisk.chipcloud.ChipCloud.SelectMode.multi)
                        .checkedChipColor(getResources().getColor(R.color.black))
                        .checkedTextColor(Color.parseColor("#ffffff"))
                        .uncheckedChipColor(getResources().getColor(R.color.concrete))
                        .uncheckedTextColor(Color.parseColor("#ffffff"))
                        .useInsetPadding(true);

                //Create a new ChipCloud with a Context and ViewGroup:
                ChipCloud chipCloudAffinity = new ChipCloud(this, flexboxAffinity, configAffinity);

                final String[] affinity = getResources().getStringArray(R.array.tags_filter_affinity);
                chipCloudAffinity.addChips(affinity);

                array = new String[affinity.length];

                chipCloudAffinity.setListener(new fisk.chipcloud.ChipListener() {
                    @Override
                    public void chipCheckedChange(int i, boolean b, boolean b1) {
                        if(b){
                            array[i] = affinity[i];
                        }else{
                            array[i] = null;
                        }
                    }
                });

                for(int x = 0; x<affinity.length; x++){
                    if(tagFirebase.contains(affinity[x])){
                        chipCloudAffinity.setChecked(x);
                        array[x] = affinity[x];
                    }
                }
                break;
            case Utility.GROUP:
                //To create the same wrapping cloud as previous incarnation use Google's FlexboxLayout:
                FlexboxLayout flexboxGroups = (FlexboxLayout) findViewById(R.id.flex);

                ChipCloudConfig configGroups = new ChipCloudConfig()
                        .selectMode(fisk.chipcloud.ChipCloud.SelectMode.multi)
                        .checkedChipColor(getResources().getColor(R.color.black))
                        .checkedTextColor(Color.parseColor("#ffffff"))
                        .uncheckedChipColor(getResources().getColor(R.color.concrete))
                        .uncheckedTextColor(Color.parseColor("#ffffff"))
                        .useInsetPadding(true);

                //Create a new ChipCloud with a Context and ViewGroup:
                ChipCloud chipCloudGroups = new ChipCloud(this, flexboxGroups, configGroups);

                final String[] groups = getResources().getStringArray(R.array.tags_filter_groups);
                chipCloudGroups.addChips(groups);

                array = new String[groups.length];

                chipCloudGroups.setListener(new ChipListener() {
                    @Override
                    public void chipCheckedChange(int i, boolean b, boolean b1) {
                        if(b){
                            array[i] = groups[i];
                        }else{
                            array[i] = null;
                        }
                    }
                });

                for(int x = 0; x<groups.length; x++){
                    if(tagFirebase.contains(groups[x])){
                        chipCloudGroups.setChecked(x);
                        array[x] = groups[x];
                    }
                }
                break;
            case Utility.SEGMENT:
                //To create the same wrapping cloud as previous incarnation use Google's FlexboxLayout:
                FlexboxLayout flexboxSegments = (FlexboxLayout) findViewById(R.id.flex);

                ChipCloudConfig configSegments = new ChipCloudConfig()
                        .selectMode(fisk.chipcloud.ChipCloud.SelectMode.multi)
                        .checkedChipColor(getResources().getColor(R.color.black))
                        .checkedTextColor(Color.parseColor("#ffffff"))
                        .uncheckedChipColor(getResources().getColor(R.color.concrete))
                        .uncheckedTextColor(Color.parseColor("#ffffff"))
                        .useInsetPadding(true);

                //Create a new ChipCloud with a Context and ViewGroup:
                ChipCloud chipCloudSegments = new ChipCloud(this, flexboxSegments, configSegments);

                final String[] segments = getResources().getStringArray(R.array.tags_filter_segments);
                chipCloudSegments.addChips(segments);

                array = new String[segments.length];

                chipCloudSegments.setListener(new ChipListener() {
                    @Override
                    public void chipCheckedChange(int i, boolean b, boolean b1) {
                        if(b){
                            array[i] = segments[i];
                        }else{
                            array[i] = null;
                        }
                    }
                });

                for(int x = 0; x<segments.length; x++){
                    if(tagFirebase.contains(segments[x])){
                        chipCloudSegments.setChecked(x);
                        array[x] = segments[x];
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btNextAffinity:
                String tag = "";
                for(String tagItem : array){
                    if(tagItem!=null) {
                        tag += tagItem+";";
                    }
                }

                mDatabase.child(FirebaseHelper.FIREBASE_DATABASE_TAGS)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(tagDb)
                        .setValue(tag);
                Toast.makeText(this, "Tags cadastradas com sucesso", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
