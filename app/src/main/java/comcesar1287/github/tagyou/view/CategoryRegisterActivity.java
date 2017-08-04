package comcesar1287.github.tagyou.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.firebase.FirebaseHelper;
import comcesar1287.github.tagyou.controller.util.Utility;

public class CategoryRegisterActivity extends AppCompatActivity {

    String database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_register);
    }

    public void registerPerson(View view) {
        database = FirebaseHelper.FIREBASE_DATABASE_USERS;
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATABASE, database);
        startActivity(intent);
    }


    public void registerCompany(View view) {
        database = FirebaseHelper.FIREBASE_DATABASE_COMPANIES;
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATABASE, database);
        startActivity(intent);
    }
}
