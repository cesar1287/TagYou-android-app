package comcesar1287.github.tagyou.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.InputStream;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.domain.Person;
import comcesar1287.github.tagyou.controller.domain.User;
import comcesar1287.github.tagyou.controller.firebase.FirebaseHelper;
import comcesar1287.github.tagyou.controller.util.Utility;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditPersonActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    Query personQuery;

    ValueEventListener valueEventListener;
    ValueEventListener singleValueEventListener;

    private String name, email, phone, birth, sex, hashtag;

    private ImageView ivPhoto;

    private TextInputLayout etName, etEmail, etPhone, etBirth, etHashtag;

    private Spinner spinnerSex;

    private ProgressDialog dialog;

    Person person;
    //String local = "";
    //static final int RESULT_LOAD_IMG = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        dialog = ProgressDialog.show(this,"", "Carregando dados do usuário...", true, false);

        personQuery = mDatabase
                .child(FirebaseHelper.FIREBASE_DATABASE_USERS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        loadPerson();

        ivPhoto = (ImageView) findViewById(R.id.register_photo);

        spinnerSex = (Spinner)findViewById(R.id.register_spinner_sexo);
        ArrayAdapter<String> spinnerCountShoesArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.sexo));
        spinnerSex.setAdapter(spinnerCountShoesArrayAdapter);

        etName = (TextInputLayout) findViewById(R.id.register_name);
        etEmail = (TextInputLayout) findViewById(R.id.register_email);
        etBirth = (TextInputLayout) findViewById(R.id.register_date);
        etPhone = (TextInputLayout) findViewById(R.id.register_phone);
        etHashtag = (TextInputLayout) findViewById(R.id.register_hashtag);

        Button advance = (Button) findViewById(R.id.advance);
        advance.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        setupFieldMasks();
    }

    private void loadPerson() {

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                person = new Person();
                person.setName(user.name);
                person.setEmail(user.email);
                person.setProfilePic(user.profile_pic);
                person.setPhone(user.phone);
                person.setBirth(user.birth);
                person.setHashtag(user.hashtag);
                person.setSex(user.sex);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
                //Toasty.error(PartnerCategoryActivity.this, getResources().getString(R.string.error_loading_partners), Toast.LENGTH_SHORT, true).show();
                finish();
            }
        };

        singleValueEventListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialog.dismiss();
                loadInformationInForm();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
                //Toasty.error(PartnerCategoryActivity.this, getResources().getString(R.string.error_loading_partners), Toast.LENGTH_SHORT, true).show();
                finish();
            }
        };

        personQuery.addValueEventListener(valueEventListener);

        personQuery.addListenerForSingleValueEvent(singleValueEventListener);
    }

    private void loadInformationInForm() {

        Glide.with(this).load(Uri.parse(person.getProfilePic()))
                .asBitmap().into(new BitmapImageViewTarget(ivPhoto) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivPhoto.setImageDrawable(circularBitmapDrawable);
            }
        });

        etName.getEditText().setText(person.getName());
        etEmail.getEditText().setText(person.getEmail());
        etBirth.getEditText().setText(person.getBirth());
        etPhone.getEditText().setText(person.getPhone());



        switch (person.getSex()) {
            case "Feminino":
                spinnerSex.setSelection(1);
                break;
            case "Masculino":
                spinnerSex.setSelection(2);
                break;
            default:
                spinnerSex.setSelection(0);
                break;
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id){
            case R.id.advance:
                editCadastre();
                break;
        }
    }

    public void setupFieldMasks(){
        TextWatcher phoneMask = Utility.insertMask(getResources().getString(R.string.phone_mask), etPhone.getEditText());
        etPhone.getEditText().addTextChangedListener(phoneMask);

        TextWatcher birthMask = Utility.insertMask(getResources().getString(R.string.birth_mask), etBirth.getEditText());
        etBirth.getEditText().addTextChangedListener(birthMask);
    }

    public void editCadastre(){

        boolean allFieldsFilled = true;
        boolean allFilledRight = true;

        name = etName.getEditText().getText().toString();
        email = etEmail.getEditText().getText().toString();
        birth = etBirth.getEditText().getText().toString();
        phone = etPhone.getEditText().getText().toString();
        sex = spinnerSex.getSelectedItem().toString();
        hashtag = etHashtag.getEditText().getText().toString();

        if(name.equals("")){
            allFieldsFilled = false;
            etName.setError("Campo obrigatório");
        }else{
            etName.setErrorEnabled(false);
        }

//        if(sex.equals("Selecione")){
//            allFieldsFilled = false;
//            Toast.makeText(this, "Campo sexo é obrigatório", Toast.LENGTH_SHORT).show();
//        }

//        if(phone.equals("")){
//            allFieldsFilled = false;
//            etPhone.setError("Campo obrigatório");
//        }else{
//            etPhone.setErrorEnabled(false);
//        }

        if(email.equals("")){
            allFieldsFilled = false;
            etEmail.setError("Campo obrigatório");
        }else{
            etEmail.setErrorEnabled(false);
        }

//        if(birth.equals("")){
//            allFieldsFilled = false;
//            etBirth.setError("Campo obrigatório");
//        }else{
//            etBirth.setErrorEnabled(false);
//        }

//        if(allFieldsFilled) {
//
//            if (phone.length() < 14) {
//                allFilledRight = false;
//                etPhone.setError("Telefone inválido");
//            } else {
//                etPhone.setErrorEnabled(false);
//            }
//
//            if (birth.length() < 10) {
//                allFilledRight = false;
//                etBirth.setError("Data de nascimento inválida");
//            } else {
//                etBirth.setErrorEnabled(false);
//            }
//        }

        if(allFieldsFilled && allFilledRight) {
            FirebaseUser user = mAuth.getCurrentUser();
            finishLogin(user);
            Toast.makeText(this, "Editado com sucesso", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void finishLogin(FirebaseUser user){

        String uid = user.getUid();
        String profile_pic = user.getPhotoUrl().toString();

        FirebaseHelper.writeNewUser(mDatabase, uid, name, email, birth, sex, phone, profile_pic, hashtag);
    }

    /*public void changePhoto(View view) {

        selectImage();
    }

    public void selectImage() {
        local = "galeria";
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }


    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

       if (local == "galeria" && reqCode == RESULT_LOAD_IMG) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ivPhoto.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


    }*/
}
