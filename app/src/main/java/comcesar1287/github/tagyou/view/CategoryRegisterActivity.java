package comcesar1287.github.tagyou.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.firebase.FirebaseHelper;
import comcesar1287.github.tagyou.controller.util.Utility;

public class CategoryRegisterActivity extends AppCompatActivity implements View.OnClickListener{

    String database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_register);

        Button btCategoryPerson = (Button) findViewById(R.id.btCategoryPerson);
        btCategoryPerson.setOnClickListener(this);
        Button btCategoryCompany = (Button) findViewById(R.id.btCategoryCompany);
        btCategoryCompany.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        Intent intent = new Intent(this, SignWithActivity.class);

        switch (id){
            case R.id.btCategoryPerson:
                database = FirebaseHelper.FIREBASE_DATABASE_USERS;
                intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATABASE, database);
                startActivity(intent);
                finish();
                break;
            case R.id.btCategoryCompany:
                database = FirebaseHelper.FIREBASE_DATABASE_COMPANIES;
                intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATABASE, database);
                startActivity(intent);
                finish();
                break;
        }
    }
}
