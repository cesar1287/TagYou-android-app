package comcesar1287.github.tagyou.view;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import comcesar1287.github.tagyou.R;

public class IndicationPersonActivity extends AppCompatActivity {

    TextView tvCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indication_person);

        tvCode = (TextView) findViewById(R.id.code_indication);
        tvCode.setText(FirebaseAuth.getInstance().getCurrentUser().getUid().substring(0,5));
    }
}
