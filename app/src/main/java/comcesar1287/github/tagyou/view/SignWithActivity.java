package comcesar1287.github.tagyou.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.domain.UserFacebook;
import comcesar1287.github.tagyou.controller.firebase.FirebaseHelper;
import comcesar1287.github.tagyou.controller.util.Utility;

public class SignWithActivity extends AppCompatActivity {

    private CallbackManager callbackManager;

    private FirebaseAuth mAuth;

    private ProgressDialog dialog;

    private UserFacebook userFacebook;

    private DatabaseReference mDatabase;

    private String database;

    private boolean isRegistryComplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        database = getIntent().getStringExtra(Utility.KEY_CONTENT_EXTRA_DATABASE);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //verifyUserIsLogged();

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_sign_with);

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Collections.singletonList("email"));
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
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
                    public void onError(FacebookException exception) {
                        Toast.makeText(SignWithActivity.this, R.string.error_facebook_login_unknown_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void verifyUserIsLogged(){

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            getUserDataFacebook();
            Intent intent = new Intent(SignWithActivity.this, CategoryRegisterActivity.class);
            intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATA, userFacebook);
            startActivity(intent);
            finish();
        }
    }

    public void checkIfUserRegistryIsComplete(){

        mDatabase.child(database).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    if(child.getKey().equals(mAuth.getCurrentUser().getUid())){
                        isRegistryComplete = true;
                    }
                }

                if(!isRegistryComplete) {
                    getUserDataFacebook();
                }

                dialog.dismiss();

                Intent intent;
                if(isRegistryComplete){
                    intent = new Intent(SignWithActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    if(database.equals(FirebaseHelper.FIREBASE_DATABASE_USERS)) {
                        intent = new Intent(SignWithActivity.this, RegisterPersonActivity.class);
                        intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATABASE, database);
                        intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATA, userFacebook);
                        startActivity(intent);
                        finish();
                    }else{
                        intent = new Intent(SignWithActivity.this, RegisterCompanyActivity.class);
                        intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATABASE, database);
                        intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATA, userFacebook);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void getUserDataFacebook(){
        FirebaseUser user = mAuth.getCurrentUser();
        userFacebook = new UserFacebook();
        userFacebook.setName(user.getDisplayName());
        userFacebook.setEmail(user.getEmail());
        userFacebook.setProfilePic(user.getPhotoUrl().toString());
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
                        //TODO
                    }
                })
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            checkIfUserRegistryIsComplete();
                        }
                    }
                });
    }
}
