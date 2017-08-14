package comcesar1287.github.tagyou.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.adroitandroid.chipcloud.ChipCloud;

import comcesar1287.github.tagyou.R;

public class TagsFilterActivity extends AppCompatActivity implements View.OnClickListener{

    ChipCloud chipCloudAffinity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tags_filter);

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
                break;
        }
    }
}
