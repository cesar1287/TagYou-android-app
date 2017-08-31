package comcesar1287.github.tagyou.view;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import comcesar1287.github.tagyou.R;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, OnCompleteListener, OnFailureListener{

    ProgressDialog progressDialog;

    TextInputLayout etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = (TextInputLayout) findViewById(R.id.forgot_email);

        Button btCancel = (Button) findViewById(R.id.forgot_password_button_cancel);
        btCancel.setOnClickListener(this);

        Button btSend = (Button) findViewById(R.id.forgot_password_button_send);
        btSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.forgot_password_button_cancel:
                Toast.makeText(this,
                        getResources().getString(R.string.send_cancelled_email),
                        Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.forgot_password_button_send:
                if(!etEmail.getEditText().getText().toString().equals("")) {
                    etEmail.setErrorEnabled(false);
                    progressDialog = ProgressDialog.show(this, "",
                            getResources().getString(R.string.processing), true, false);

                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.sendPasswordResetEmail(etEmail.getEditText().getText().toString())
                            .addOnCompleteListener(this, this)
                            .addOnFailureListener(this, this);
                }else{
                    etEmail.setError("Campo obrigatório");
                    etEmail.setErrorEnabled(true);
                }
                break;
        }
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if(task.isSuccessful()){
            progressDialog.dismiss();
            Toast.makeText(this,
                    getResources().getString(R.string.email_successfully_sent),
                    Toast.LENGTH_LONG).show();
        }
        finish();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        progressDialog.dismiss();
        if(e instanceof FirebaseAuthInvalidUserException){
            Toast.makeText(this, getResources().getString(R.string.error_failed_send_email_to_reset), Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, getResources().getString(R.string.error_unknown_error), Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
