package comcesar1287.github.tagyou.view;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.domain.CompanyFirebase;
import comcesar1287.github.tagyou.controller.domain.User;
import comcesar1287.github.tagyou.controller.firebase.FirebaseHelper;
import comcesar1287.github.tagyou.controller.util.Utility;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    String Uid, name , email, password, database;

    SharedPreferences sp;

    EditText etName, etEmail, etPassword;

    private ProgressDialog dialog;

    Button btCreate;

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
                    SignUpActivity.this.getResources().getString(R.string.creating_user), true, false);
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
                    .addOnSuccessListener(SignUpActivity.this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(SignUpActivity.this,
                                    getResources().getString(R.string.user_created_successfully),
                                    Toast.LENGTH_SHORT).show();

                            FirebaseAuth mAuth = FirebaseAuth.getInstance();

                            FirebaseUser user = mAuth.getCurrentUser();

                            finishLogin(user, database);
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
                                finish();
                            }
                        }
                    });
        }else{
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.error_all_fields_required), Toast.LENGTH_SHORT).show();
        }
    }

    public void finishLogin(FirebaseUser user, final String database){

        Uid = user.getUid();

        mDatabase.child(database).child(Uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (database.equals(FirebaseHelper.FIREBASE_DATABASE_USERS)) {
                            // Get user value
                            User user = dataSnapshot.getValue(User.class);

                            // [START_EXCLUDE]
                            if (user == null) {

                                FirebaseHelper.writeNewUser(mDatabase, Uid, name, email, "", "", "", "", "");

                                sp = getSharedPreferences(Utility.LOGIN_SHARED_PREF_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();

                                editor.putString(Utility.KEY_CONTENT_EXTRA_DATABASE, database);
                                editor.putString("id", Uid);
                                editor.putString("name", name);
                                editor.putString("email", email);
                                editor.apply();
                            } else {

                                sp = getSharedPreferences(Utility.LOGIN_SHARED_PREF_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();

                                editor.putString(Utility.KEY_CONTENT_EXTRA_DATABASE, database);
                                editor.putString("id", Uid);
                                editor.putString("name", name);
                                editor.putString("email", email);
                                editor.putString("profile_pic", user.profile_pic);
                                editor.putString("phone", user.phone);
                                editor.putString("birth", user.birth);
                                editor.putString("sex", user.sex);
                                editor.apply();
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
}