package comcesar1287.github.tagyou.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.domain.CompanyFirebase;
import comcesar1287.github.tagyou.controller.domain.UserFacebook;
import comcesar1287.github.tagyou.controller.firebase.FirebaseHelper;
import comcesar1287.github.tagyou.controller.util.Utility;

public class EditCompanyActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    private String Uid, name , email, database, phone, description, address, site, banner,
            logo, hashtag, city, street, number;

    private int quantity;

    private double latitude, longitude;

    private SharedPreferences sp;

    private TextInputLayout etName, etEmail, etHashtag, etDescription, etCity, etStreet, etNumber, etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company);

        /*UserFacebook userFacebook = (UserFacebook) getIntent().getSerializableExtra(Utility.KEY_CONTENT_EXTRA_DATA);

        final ImageView ivPhoto = (ImageView) findViewById(R.id.register_photo);
        Glide.with(this).load(Uri.parse(userFacebook.getProfilePic()))
                .asBitmap().into(new BitmapImageViewTarget(ivPhoto) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivPhoto.setImageDrawable(circularBitmapDrawable);
            }
        });

        etName = (TextInputLayout) findViewById(R.id.register_name);
        etName.getEditText().setText(userFacebook.getName());

        etEmail = (TextInputLayout) findViewById(R.id.register_email);
        etEmail.getEditText().setText(userFacebook.getEmail());

        etHashtag = (TextInputLayout) findViewById(R.id.register_hashtag);
        etDescription = (TextInputLayout) findViewById(R.id.register_description);
        etCity = (TextInputLayout) findViewById(R.id.register_city);
        etStreet = (TextInputLayout) findViewById(R.id.register_street);
        etNumber = (TextInputLayout) findViewById(R.id.register_number);
        etPhone = (TextInputLayout) findViewById(R.id.register_phone);

        Button advance = (Button) findViewById(R.id.advance);
        advance.setOnClickListener(this);

        database = getIntent().getStringExtra(Utility.KEY_CONTENT_EXTRA_DATABASE);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        setupFieldMasks();*/
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id){
            case R.id.advance:
                attemptLogin();
                break;
        }
    }

    public void setupFieldMasks(){
        TextWatcher phoneMask = Utility.insertMask(getResources().getString(R.string.phone_mask), etPhone.getEditText());
        etPhone.getEditText().addTextChangedListener(phoneMask);
    }

    public void attemptLogin(){

        boolean allFieldsFilled = true;
        boolean allFilledRight = true;

        name = etName.getEditText().getText().toString();
        email = etEmail.getEditText().getText().toString();
        phone = etPhone.getEditText().getText().toString();
        hashtag = etHashtag.getEditText().getText().toString();
        description = etDescription.getEditText().getText().toString();
        city = etCity.getEditText().getText().toString();
        street = etStreet.getEditText().getText().toString();
        number = etNumber.getEditText().getText().toString();

        if(name.equals("")){
            allFieldsFilled = false;
            etName.setError("Campo obrigatório");
        }else{
            etName.setErrorEnabled(false);
        }

        if(phone.equals("")){
            allFieldsFilled = false;
            etPhone.setError("Campo obrigatório");
        }else{
            etPhone.setErrorEnabled(false);
        }

        if(email.equals("")){
            allFieldsFilled = false;
            etEmail.setError("Campo obrigatório");
        }else{
            etEmail.setErrorEnabled(false);
        }

        if(description.equals("")){
            allFieldsFilled = false;
            etDescription.setError("Campo obrigatório");
        }else{
            etDescription.setErrorEnabled(false);
        }

        if(city.equals("")){
            allFieldsFilled = false;
            etCity.setError("Campo obrigatório");
        }else{
            etCity.setErrorEnabled(false);
        }

        if(street.equals("")){
            allFieldsFilled = false;
            etStreet.setError("Campo obrigatório");
        }else{
            etStreet.setErrorEnabled(false);
        }

        if(number.equals("")){
            allFieldsFilled = false;
            etNumber.setError("Campo obrigatório");
        }else{
            etNumber.setErrorEnabled(false);
        }

        if(allFieldsFilled) {

            if (phone.length() < 14) {
                allFilledRight = false;
                etPhone.setError("Telefone inválido");
            } else {
                etPhone.setErrorEnabled(false);
            }
        }

        if(allFieldsFilled && allFilledRight) {
            FirebaseUser user = mAuth.getCurrentUser();
            finishLogin(user, database);
            Toast.makeText(this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPreferences = getSharedPreferences(Utility.LOGIN_SHARED_PREF_NAME, MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.registry), getResources().getString(R.string.done));
            editor.apply();

            startActivity(new Intent(this, TagsFilterActivity.class));
            finish();
        }
    }

    public void finishLogin(FirebaseUser user, final String database){

        Uid = user.getUid();
        logo = user.getPhotoUrl().toString();
        address = street+", "+number+", "+city;

        mDatabase.child(database).child(Uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        CompanyFirebase companyFirebase = dataSnapshot.getValue(CompanyFirebase.class);

                        // [START_EXCLUDE]
                        if (companyFirebase == null) {

                            FirebaseHelper.writeNewCompany(mDatabase, Uid, name, description, email ,address, phone, "" ,
                                    "", logo, (int)(Math.random()*10), 40.233, -40.223, hashtag);

                            /*sp = getSharedPreferences(Utility.LOGIN_SHARED_PREF_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();

                            editor.putString("id", Uid);
                            editor.putString("name", name);
                            editor.putString("email", email);
                            editor.putString("profile_pic", profile_pic);
                            editor.putString("hashtag", hashtag);
                            editor.apply();*/
                        } else {

                            /*sp = getSharedPreferences(Utility.LOGIN_SHARED_PREF_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();

                            editor.putString("id", Uid);
                            editor.putString("name", name);
                            editor.putString("email", email);
                            editor.putString("profile_pic", profile_pic);
                            editor.putString("phone", user.phone);
                            editor.putString("birth", user.birth);
                            editor.putString("sex", user.sex);
                            editor.putString("hashtag", hashtag);
                            editor.apply();*/
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(EditCompanyActivity.this, R.string.error_signin, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
