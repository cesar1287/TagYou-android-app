package comcesar1287.github.tagyou.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.InputStream;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.domain.CompanyFirebase;
import comcesar1287.github.tagyou.controller.domain.User;
import comcesar1287.github.tagyou.controller.firebase.FirebaseHelper;
import comcesar1287.github.tagyou.controller.util.Utility;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.resource;
import static comcesar1287.github.tagyou.R.id.person;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    String Uid, name , email, password, database;

    EditText etName, etEmail, etPassword;

    private ProgressDialog dialog;

    Button btCreate;

    SharedPreferences sp;

    CircleImageView photoProfile;
    String local = "";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int RESULT_LOAD_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        database = getIntent().getStringExtra(Utility.KEY_CONTENT_EXTRA_DATABASE);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        etName = (EditText) findViewById(R.id.sign_up_name);
        etEmail = (EditText) findViewById(R.id.sign_up_email);
        etPassword = (EditText) findViewById(R.id.sign_up_password);

        btCreate = (Button) findViewById(R.id.sign_up_button_register);
        btCreate.setOnClickListener(this);



}




    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch(id){
            case R.id.sign_up_button_register:
                name = etName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                createUser();
                break;
        }
    }

    public void createUser(){
        if(!Utility.verifyEmptyField(name, email, password)) {
            dialog = ProgressDialog.show(SignUpActivity.this,"",
                    SignUpActivity.this.getResources().getString(R.string.sign_up_user), true, false);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnFailureListener(SignUpActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            if (e instanceof FirebaseAuthWeakPasswordException) {
                                Toast.makeText(SignUpActivity.this,
                                        getResources().getString(R.string.error_password_too_small),
                                        Toast.LENGTH_SHORT).show();
                                etPassword.setText("");
                                etPassword.requestFocus();
                            } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(SignUpActivity.this,
                                        getResources().getString(R.string.error_invalid_email),
                                        Toast.LENGTH_SHORT).show();
                                etEmail.setText("");
                                etEmail.requestFocus();
                            } else if (e instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(SignUpActivity.this,
                                        getResources().getString(R.string.error_failed_signin_email_exists),
                                        Toast.LENGTH_LONG).show();
                                etEmail.setText("");
                                etEmail.requestFocus();
                            } else {
                                Toast.makeText(SignUpActivity.this,
                                        getResources().getString(R.string.error_unknown_error),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this,
                                        getResources().getString(R.string.user_created_successfully),
                                        Toast.LENGTH_LONG).show();

                                final FirebaseUser user = mAuth.getCurrentUser();


                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();

                                if(user!=null) {
                                    user.updateProfile(profileUpdates);

                                    user.sendEmailVerification()
                                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SignUpActivity.this,
                                                                getString(R.string.send_email_confirmation) + " " + user.getEmail(),
                                                                Toast.LENGTH_LONG).show();
                                                        finishLogin(user, database);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(SignUpActivity.this,
                                                                R.string.error_failed_send_email_confirmation,
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(SignUpActivity.this, new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SignUpActivity.this,
                                                            R.string.error_failed_send_email_confirmation,
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        }
                    });
        }else{
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.error_all_fields_required), Toast.LENGTH_SHORT).show();
        }
    }

    public void finishLogin(final FirebaseUser user, final String database){

        Uid = user.getUid();

        mDatabase.child(database).child(Uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(Utility.KEY_CONTENT_EXTRA_DATABASE, database);
                        editor.apply();
                        if (database.equals(FirebaseHelper.FIREBASE_DATABASE_USERS)) {
                            // Get user value
                            User user = dataSnapshot.getValue(User.class);

                            // [START_EXCLUDE]
                            if (user == null) {

                                FirebaseHelper.writeNewUser(mDatabase, Uid, name, email, "", "", "", "", "");
                            }
                        }else{
                            // Get user value
                            CompanyFirebase companyFirebase = dataSnapshot.getValue(CompanyFirebase.class);

                            // [START_EXCLUDE]
                            if (companyFirebase == null) {

                                FirebaseHelper.writeNewCompany(mDatabase, Uid, name, "", email, "", "", "",
                                        "", "", (int) (Math.random() * 10), 40.233, -40.223, "");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(SignUpActivity.this, R.string.error_signin, Toast.LENGTH_LONG).show();
                    }
                });
    }



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

        photoProfile = (CircleImageView) findViewById(R.id.sign_up_image_profile);

        if (local == "camera" && reqCode == REQUEST_IMAGE_CAPTURE) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photoProfile.setImageBitmap(imageBitmap);
        }

        else if (local == "galeria" && reqCode == RESULT_LOAD_IMG) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                photoProfile.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }



}