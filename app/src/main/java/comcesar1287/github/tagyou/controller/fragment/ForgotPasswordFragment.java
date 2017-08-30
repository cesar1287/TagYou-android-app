package comcesar1287.github.tagyou.controller.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import comcesar1287.github.tagyou.R;

public class ForgotPasswordFragment extends DialogFragment {



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.fragment_forgot_password, null))
                // Add action buttons
                .setPositiveButton(R.string.forgot_password_button_send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //FirebaseAuth.getInstance().sendPasswordResetEmail()
                    }
                })
                .setNegativeButton(R.string.forgot_password_button_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "Recuperação de email cancelada", Toast.LENGTH_SHORT).show();
                    }
                });
        return builder.create();
    }
}

