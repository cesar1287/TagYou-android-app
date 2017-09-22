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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import comcesar1287.github.tagyou.controller.domain.Company;
import comcesar1287.github.tagyou.controller.domain.CompanyFirebase;
import comcesar1287.github.tagyou.controller.firebase.FirebaseHelper;
import comcesar1287.github.tagyou.controller.util.Utility;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditCompanyActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    private String Uid, name , email, phone, description, address, site, banner,
            logo, hashtag, city, street, number;

    private int quantity;

    private double latitude, longitude;

    private Query companyQuery;

    ValueEventListener valueEventListener, singleValueEventListener;

    private TextInputLayout etName, etEmail, etHashtag, etDescription, etCity, etStreet, etNumber, etPhone;

    private ImageView ivPhoto;

    private ProgressDialog dialog;

    private Company company;

    private Spinner spinnerSegment;

    private EditText etDescriptionOne, etDescriptiontwo, etSocialNetwork;

    //String local = "";
    //static final int RESULT_LOAD_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        dialog = ProgressDialog.show(this,"", "Carregando dados do empresa...", true, false);

        companyQuery = mDatabase
                .child(FirebaseHelper.FIREBASE_DATABASE_COMPANIES)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        loadCompany();

        ivPhoto = (ImageView) findViewById(R.id.register_photo);
        etName = (TextInputLayout) findViewById(R.id.register_name);
        etEmail = (TextInputLayout) findViewById(R.id.register_email);
        etHashtag = (TextInputLayout) findViewById(R.id.register_hashtag);
        etDescription = (TextInputLayout) findViewById(R.id.register_description);
        etCity = (TextInputLayout) findViewById(R.id.register_city);
        etStreet = (TextInputLayout) findViewById(R.id.register_street);
        etNumber = (TextInputLayout) findViewById(R.id.register_number);
        etPhone = (TextInputLayout) findViewById(R.id.register_phone);

        spinnerSegment = (Spinner)findViewById(R.id.register_spinner_segment);
        ArrayAdapter<String> spinnerCountShoesArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.segment));
        spinnerSegment.setAdapter(spinnerCountShoesArrayAdapter);

        Button advance = (Button) findViewById(R.id.advance);
        advance.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        setupFieldMasks();
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

    private void loadCompany() {

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CompanyFirebase companyFirebase = dataSnapshot.getValue(CompanyFirebase.class);

                company = new Company();
                company.setName(companyFirebase.name);
                company.setDescription(companyFirebase.description);
                company.setEmail(companyFirebase.email);
                company.setAddress(companyFirebase.address);
                company.setPhone(companyFirebase.phone);
                company.setSite(companyFirebase.site);
                company.setBanner(companyFirebase.banner);
                company.setLogo(companyFirebase.logo);
                company.setHashtag(companyFirebase.hashtag);
                company.setQuantity(companyFirebase.quantity);
                company.setLatitude(companyFirebase.latitude);
                company.setLongitude(companyFirebase.longitude);
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

        companyQuery.addValueEventListener(valueEventListener);

        companyQuery.addListenerForSingleValueEvent(singleValueEventListener);
    }

    private void loadInformationInForm() {

        Glide.with(this).load(Uri.parse(company.getLogo()))
                .asBitmap().into(new BitmapImageViewTarget(ivPhoto) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivPhoto.setImageDrawable(circularBitmapDrawable);
            }
        });

        etName.getEditText().setText(company.getName());
        etEmail.getEditText().setText(company.getEmail());
        etHashtag.getEditText().setText(company.getHashtag());
        etDescription.getEditText().setText(company.getDescription());
        //etCity.getEditText().setText(company.get);
        //etStreet = (TextInputLayout) findViewById(R.id.register_street);
        //etNumber = (TextInputLayout) findViewById(R.id.register_number);
        etPhone.getEditText().setText(company.getPhone());
    }

    public void setupFieldMasks(){
        TextWatcher phoneMask = Utility.insertMask(getResources().getString(R.string.phone_mask), etPhone.getEditText());
        etPhone.getEditText().addTextChangedListener(phoneMask);
    }

    public void editCadastre(){

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

//        if(description.equals("")){
//            allFieldsFilled = false;
//            etDescription.setError("Campo obrigatório");
//        }else{
//            etDescription.setErrorEnabled(false);
//        }

//        if(city.equals("")){
//            allFieldsFilled = false;
//            etCity.setError("Campo obrigatório");
//        }else{
//            etCity.setErrorEnabled(false);
//        }
//
//        if(street.equals("")){
//            allFieldsFilled = false;
//            etStreet.setError("Campo obrigatório");
//        }else{
//            etStreet.setErrorEnabled(false);
//        }
//
//        if(number.equals("")){
//            allFieldsFilled = false;
//            etNumber.setError("Campo obrigatório");
//        }else{
//            etNumber.setErrorEnabled(false);
//        }

//        if(allFieldsFilled) {
//
//            if (phone.length() < 14) {
//                allFilledRight = false;
//                etPhone.setError("Telefone inválido");
//            } else {
//                etPhone.setErrorEnabled(false);
//            }
//        }

        if(allFieldsFilled && allFilledRight) {
            FirebaseUser user = mAuth.getCurrentUser();
            finishLogin(user);
            Toast.makeText(this, "Editado com sucesso", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, TagsFilterActivity.class));
            finish();
        }
    }

    public void finishLogin(FirebaseUser user){

        Uid = user.getUid();
        logo = user.getPhotoUrl().toString();
        address = street+", "+number+", "+city;

        FirebaseHelper.writeNewCompany(mDatabase, Uid, name, description, email ,address, phone, "" ,
                "", logo, (int)(Math.random()*10), 40.233, -40.223, hashtag);
    }

    /*
    public void changePhoto(View view) {

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
