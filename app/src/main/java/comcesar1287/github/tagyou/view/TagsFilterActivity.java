package comcesar1287.github.tagyou.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.adroitandroid.chipcloud.ChipCloud;

import comcesar1287.github.tagyou.R;

public class TagsFilterActivity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        String result = sharedPreferences.getString(getString(R.string.tags_filter), "");
        if(!result.equals("")){
            openTagsFilterGroupActivity();
        }

        setContentView(R.layout.activity_tags_filter);

        ChipCloud chipCloudAffinity = (ChipCloud) findViewById(R.id.chip_cloud_affinity);

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

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.tags_filter), getString(R.string.done));
                editor.apply();

                openTagsFilterGroupActivity();
                break;
        }
    }

    public void openTagsFilterGroupActivity(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}