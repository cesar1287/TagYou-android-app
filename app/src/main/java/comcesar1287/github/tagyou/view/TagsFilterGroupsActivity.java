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

public class TagsFilterGroupsActivity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        String result = sharedPreferences.getString(getString(R.string.tags_filter_group), "");
        if(!result.equals("")){
            openTagsFilterGroupActivity();
        }

        setContentView(R.layout.activity_tags_filter_groups);

        ChipCloud chipCloud = (ChipCloud) findViewById(R.id.chip_cloud_groups);

        String[] segmentsArray = getResources().getStringArray(R.array.tags_filter_groups);
        chipCloud.addChips(segmentsArray);

        Button btNextSegments = (Button) findViewById(R.id.btNextGroups);
        btNextSegments.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btNextGroups:

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.tags_filter_group), getString(R.string.done));
                editor.apply();

                openTagsFilterGroupActivity();
                break;
        }
    }

    public void openTagsFilterGroupActivity(){
        startActivity(new Intent(this, TagsFilterAffinityActivity.class));
        finish();
    }
}
