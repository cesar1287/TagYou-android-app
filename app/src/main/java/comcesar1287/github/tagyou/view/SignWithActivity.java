package comcesar1287.github.tagyou.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.domain.CompanyFirebase;
import comcesar1287.github.tagyou.controller.domain.User;
import comcesar1287.github.tagyou.controller.firebase.FirebaseHelper;
import comcesar1287.github.tagyou.controller.util.Utility;

public class SignWithActivity extends AppCompatActivity implements View.OnClickListener, FacebookCallback<LoginResult>{

    private CallbackManager callbackManager;

    private FirebaseAuth mAuth;

    private ProgressDialog dialog;

    private DatabaseReference mDatabase;

    private String database;

    private String Uid, name , email, profile_pic, password;

    private EditText etEmail, etPassword;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = getIntent().getStringExtra(Utility.KEY_CONTENT_EXTRA_DATABASE);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_sign_with);

        etEmail = (EditText) findViewById(R.id.email_login);
        etPassword = (EditText) findViewById(R.id.senha_login);

        Button btLogin = (Button) findViewById(R.id.sign_with_login);
        btLogin.setOnClickListener(this);

        Button btRegister = (Button) findViewById(R.id.sign_with_register);
        btRegister.setOnClickListener(this);

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Collections.singletonList("email"));
        loginButton.registerCallback(callbackManager, this);
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        dialog = ProgressDialog.show(SignWithActivity.this,"",
                SignWithActivity.this.getResources().getString(R.string.processing_login), true, false);

        handleFacebookAccessToken(loginResult.getAccessToken());
    }

    @Override
    public void onCancel() {
        Toast.makeText(SignWithActivity.this, R.string.error_facebook_login_canceled, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(FacebookException error) {
        Toast.makeText(SignWithActivity.this, R.string.error_facebook_login_unknown_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.sign_with_login:
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                attemptLogin();
                break;
            case R.id.sign_with_register:
                Intent intent;
                intent = new Intent(SignWithActivity.this, SignUpActivity.class);
                intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATABASE, database);
                startActivity(intent);
                break;
        }
    }

    private void attemptLogin() {
        if(!Utility.verifyEmptyField(email, password)){
            dialog = ProgressDialog.show(SignWithActivity.this,"",
                    SignWithActivity.this.getResources().getString(R.string.processing_login), true, false);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnFailureListener(SignWithActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            if(e instanceof FirebaseAuthInvalidUserException){
                                Toast.makeText(SignWithActivity.this,
                                        getResources().getString(R.string.error_invalid_user),
                                        Toast.LENGTH_SHORT).show();
                            }else if(e instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(SignWithActivity.this,
                                        getResources().getString(R.string.error_user_password_incorrect),
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SignWithActivity.this,
                                        getResources().getString(R.string.error_unknown_error),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnCompleteListener(SignWithActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                if(user !=null && user.isEmailVerified()) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(SignWithActivity.this, MainActivity.class);
                                    intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATABASE, database);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    dialog.dismiss();
                                    Toast.makeText(SignWithActivity.this,
                                            R.string.error_email_not_confirmed,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }else{
            Toast.makeText(SignWithActivity.this,
                    getResources().getString(R.string.error_all_fields_required),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, CategoryRegisterActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnFailureListener(SignWithActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        if(e instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(SignWithActivity.this, R.string.error_failed_signin_email_exists,
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(SignWithActivity.this, R.string.error_unknown_error,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnSuccessListener(SignWithActivity.this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();

                        FirebaseUser user = mAuth.getCurrentUser();

                        finishLogin(user, database);
                    }
                })
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Intent intent = new Intent(SignWithActivity.this, MainActivity.class);
                            intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATABASE, database);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    public void finishLogin(FirebaseUser user, final String database){

        Uid = user.getUid();
        name = user.getDisplayName();
        email = ((user.getEmail()==null) ? "none" : user.getEmail());
        if(profile_pic!=null) {
            profile_pic = user.getPhotoUrl().toString();
        }

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
                                FirebaseHelper.writeNewUser(mDatabase, Uid, name, email, "", "", "", profile_pic, "");
                            }

                        }else{
                            // Get company value
                            CompanyFirebase companyFirebase = dataSnapshot.getValue(CompanyFirebase.class);

                            // [START_EXCLUDE]
                            if (companyFirebase == null) {
                                FirebaseHelper.writeNewCompany(mDatabase, Uid, name, "", email, "", "", "",
                                        "", profile_pic, (int) (Math.random() * 10), 40.233, -40.223, "");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(SignWithActivity.this,
                                getResources().getString(R.string.error_signin),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}