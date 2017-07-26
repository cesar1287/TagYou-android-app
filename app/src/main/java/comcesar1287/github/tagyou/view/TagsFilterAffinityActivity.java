package comcesar1287.github.tagyou.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.adroitandroid.chipcloud.ChipCloud;

import comcesar1287.github.tagyou.R;

public class TagsFilterAffinityActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags_filter_affinity);

        ChipCloud chipCloud = (ChipCloud) findViewById(R.id.chip_cloud_affinity);

        String[] segmentsArray = getResources().getStringArray(R.array.tags_filter_affinity);
        chipCloud.addChips(segmentsArray);

        Button btNextSegments = (Button) findViewById(R.id.btNextAffinity);
        btNextSegments.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btNextAffinity:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }
}
