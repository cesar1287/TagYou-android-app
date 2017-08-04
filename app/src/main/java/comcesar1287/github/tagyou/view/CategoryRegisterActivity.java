package comcesar1287.github.tagyou.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.domain.UserFacebook;
import comcesar1287.github.tagyou.controller.firebase.FirebaseHelper;
import comcesar1287.github.tagyou.controller.util.Utility;

public class CategoryRegisterActivity extends AppCompatActivity {

    String database;

    UserFacebook userFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_register);

        userFacebook = (UserFacebook) getIntent().getSerializableExtra(Utility.KEY_CONTENT_EXTRA_DATA);
    }

    public void registerPerson(View view) {
        database = FirebaseHelper.FIREBASE_DATABASE_USERS;
        Intent intent = new Intent(this, RegisterPersonActivity.class);
        intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATABASE, database);
        intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATA, userFacebook);
        startActivity(intent);
        finish();
    }


    public void registerCompany(View view) {
        database = FirebaseHelper.FIREBASE_DATABASE_COMPANIES;
        Intent intent = new Intent(this, RegisterCompanyActivity.class);
        intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATABASE, database);
        intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATA, userFacebook);
        startActivity(intent);
        finish();
    }
}
