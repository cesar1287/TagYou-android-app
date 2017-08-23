package comcesar1287.github.tagyou.view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.util.Utility;

public class IndicationPersonActivity extends AppCompatActivity {

    TextView tvCode, tvPhone;
    Button btnWpp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indication_person);

        tvCode = (TextView) findViewById(R.id.code_indication);
        tvCode.setText(FirebaseAuth.getInstance().getCurrentUser().getUid().substring(0,5));

    }

    public void teste(View view) {
        String text = textCode();
        indicationCode(text);
    }

    public void indicationCode(String textCode) {


        Intent sendIntent = new Intent("android.intent.action.MAIN");
        sendIntent.setAction(Intent.ACTION_SEND);

        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, textCode);
        sendIntent.setPackage("com.whatsapp");
        startActivity(sendIntent);

    }

    public String textCode(){
        String code = (String) tvCode.getText();
        String text = "CÓDIGO: " + code + "\nUse o código de indicação e aproveite todos os nosso benefícios";

        return text;
    }


}
