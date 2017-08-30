package comcesar1287.github.tagyou.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import comcesar1287.github.tagyou.R;

public class ForgotPasswordFragment extends DialogFragment implements OnCompleteListener, OnFailureListener, DialogInterface.OnClickListener{

    ProgressDialog progressDialog;

    AlertDialog.Builder builder;

    TextInputLayout etEmail;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_forgot_password, null);

         etEmail = (TextInputLayout) view.findViewById(R.id.forgot_email);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.forgot_password_button_send, this)
                .setNegativeButton(R.string.forgot_password_button_cancel, this);
        return builder.create();
    }

    @Override
    public void onComplete(@NonNull Task task) {
        Toast.makeText(getActivity().getApplicationContext(),
                "Email para resetar sua senha enviado com sucesso",
                Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        progressDialog.dismiss();
        if(e instanceof FirebaseAuthInvalidUserException){
            //showToastMessage(getActivity(), "Falha ao enviar o email para recuperar sua conta." +
            //"\n\nConta não registrada");
        }else{
            // showToastMessage(getActivity(), "Erro desconhecido, tente novamente e contate o administrador.");
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i){
            case DialogInterface.BUTTON_POSITIVE:{
                progressDialog = ProgressDialog.show(getActivity(),"",
                        getActivity().getResources().getString(R.string.sending_email), true, false);

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.sendPasswordResetEmail(etEmail.getEditText().getText().toString())
                        .addOnCompleteListener(getActivity(), this)
                        .addOnFailureListener(getActivity(), this);
            }
            break;
            case DialogInterface.BUTTON_NEGATIVE:{
                Toast.makeText(getActivity(),
                        getActivity().getResources().getString(R.string.send_cancelled_email),
                        Toast.LENGTH_SHORT).show();
            }
                break;
        }
    }
}

