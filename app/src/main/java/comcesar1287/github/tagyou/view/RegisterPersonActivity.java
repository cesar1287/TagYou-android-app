package comcesar1287.github.tagyou.view;

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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import comcesar1287.github.tagyou.controller.domain.User;
import comcesar1287.github.tagyou.controller.domain.UserFacebook;
import comcesar1287.github.tagyou.controller.firebase.FirebaseHelper;
import comcesar1287.github.tagyou.controller.util.Utility;

public class RegisterPersonActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    private String Uid, name , email, profile_pic, database, phone, birth;

    private SharedPreferences sp;

    private TextInputLayout etName, etEmail, etPhone, etBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_person);

        Spinner spinnerCountShoes = (Spinner)findViewById(R.id.register_spinner_sexo);
        ArrayAdapter<String> spinnerCountShoesArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.sexo));
        spinnerCountShoes.setAdapter(spinnerCountShoesArrayAdapter);

        UserFacebook userFacebook = (UserFacebook) getIntent().getSerializableExtra(Utility.KEY_CONTENT_EXTRA_DATA);

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

        etBirth = (TextInputLayout) findViewById(R.id.register_date);

        etPhone = (TextInputLayout) findViewById(R.id.register_phone);

        Button advance = (Button) findViewById(R.id.advance);
        advance.setOnClickListener(this);

        database = getIntent().getStringExtra(Utility.KEY_CONTENT_EXTRA_DATABASE);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        setupFieldMasks();
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

        TextWatcher birthMask = Utility.insertMask(getResources().getString(R.string.birth_mask), etBirth.getEditText());
        etBirth.getEditText().addTextChangedListener(birthMask);
    }

    public void attemptLogin(){
        String name, email, birth, phone;

        boolean allFieldsFilled = true;
        boolean allFilledRight = true;

        name = etName.getEditText().getText().toString();
        email = etEmail.getEditText().getText().toString();
        birth = etBirth.getEditText().getText().toString();
        phone = etPhone.getEditText().getText().toString();

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

        if(birth.equals("")){
            allFieldsFilled = false;
            etBirth.setError("Campo obrigatório");
        }else{
            etBirth.setErrorEnabled(false);
        }

        if(allFieldsFilled) {

            if (phone.length() < 14) {
                allFilledRight = false;
                etPhone.setError("Telefone inválido");
            } else {
                etPhone.setErrorEnabled(false);
            }

            if (birth.length() < 10) {
                allFilledRight = false;
                etBirth.setError("Data de nascimento inválida");
            } else {
                etBirth.setErrorEnabled(false);
            }
        }

        if(allFieldsFilled && allFilledRight) {
            FirebaseUser user = mAuth.getCurrentUser();
            finishLogin(user, database);
            Toast.makeText(this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        }
        FirebaseUser user = mAuth.getCurrentUser();
        finishLogin(user, database);
    }

    public void finishLogin(FirebaseUser user, final String database){

        Uid = user.getUid();
        name = user.getDisplayName();
        email = user.getEmail();
        profile_pic = user.getPhotoUrl().toString();
        phone = etPhone.getEditText().getText().toString();
        birth = etBirth.getEditText().getText().toString();

        mDatabase.child(database).child(Uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {

                            FirebaseHelper.writeNewUser(mDatabase, Uid, name, email, birth, "", phone, profile_pic);

                            sp = getSharedPreferences(Utility.LOGIN_SHARED_PREF_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();

                            editor.putString("id", Uid);
                            editor.putString("name", name);
                            editor.putString("email", email);
                            editor.putString("profile_pic", profile_pic);
                            editor.apply();
                        } else {

                            sp = getSharedPreferences(Utility.LOGIN_SHARED_PREF_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();

                            editor.putString("id", Uid);
                            editor.putString("name", name);
                            editor.putString("email", email);
                            editor.putString("profile_pic", profile_pic);
                            editor.putString("phone", user.phone);
                            editor.putString("birth", user.birth);
                            editor.putString("sex", user.sex);
                            editor.apply();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(RegisterPersonActivity.this, R.string.error_signin, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
