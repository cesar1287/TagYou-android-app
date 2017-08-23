package comcesar1287.github.tagyou.view;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.util.Utility;


public class ContactUsActivity extends AppCompatActivity {

    TextInputLayout nome, telefone, cidade, mensagem;

    TextWatcher phoneMask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        ActionBar actionBar = getSupportActionBar();

        nome = (TextInputLayout)findViewById(R.id.contactName);
        telefone = (TextInputLayout)findViewById(R.id.contactPhone);
        cidade = (TextInputLayout)findViewById(R.id.contactCity);
        mensagem = (TextInputLayout)findViewById(R.id.contactMessage);

        setupFieldMasks();
    }

    public void attemptLogin(){
        String valNome, valTelefone, valCidade, valMensagem;

        boolean allFieldsFilled = true;
        boolean allFilledRight = true;

        valNome = nome.getEditText().getText().toString();
        valTelefone = telefone.getEditText().getText().toString();
        valCidade = cidade.getEditText().getText().toString();
        valMensagem = mensagem.getEditText().getText().toString();

        if(valNome.equals("")){
            allFieldsFilled = false;
            nome.setError("Campo obrigatório");
        }else{
            nome.setErrorEnabled(false);
        }

        if(valTelefone.equals("")){
            allFieldsFilled = false;
            allFilledRight = false;
            telefone.setError("Campo obrigatório");
        }else{
            telefone.setErrorEnabled(false);
        }

        if(valCidade.equals("")){
            allFieldsFilled = false;
            cidade.setError("Campo obrigatório");
        }else{
            cidade.setErrorEnabled(false);
        }

        if(valMensagem.equals("")){
            allFieldsFilled = false;
            mensagem.setError("Campo obrigatório");
        }else{
            mensagem.setErrorEnabled(false);
        }


        if(allFieldsFilled) {
            if (valTelefone.length() < 14) {
                allFilledRight = false;
                telefone.setError("Telefone inválido");
            } else {
                telefone.setErrorEnabled(false);
            }
        }

        if(allFieldsFilled && allFilledRight) {
            messageEmail();
            finish();
        }
    }

    public StringBuilder messageEmail(){
        String nomeRemetente = nome.getEditText().getText().toString();
        String telefoneRemetente = telefone.getEditText().getText().toString();
        String cidadeRementente = cidade.getEditText().getText().toString();
        String mensagemRemetente = mensagem.getEditText().getText().toString();

        StringBuilder sb = new StringBuilder();
        sb.append("Nome: ").append(nomeRemetente).append("\n");
        sb.append("Telefone: ").append(telefoneRemetente).append("\n");
        sb.append("Cidade: ").append(cidadeRementente).append("\n");
        sb.append("\n\n\n");
        sb.append("Mensagem: ").append(mensagemRemetente).append("\n");

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[] {"contato.eagora@outlook.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Fale Conosco - Eagora");
        email.putExtra(Intent.EXTRA_TEXT, sb.toString());
        email.setType("plain/text");
        startActivity(Intent.createChooser(email, "Enviando Email..."));

        return sb;
    }

    public void enviarEmail(View view){

        attemptLogin();
    }

    public void setupFieldMasks(){
        phoneMask = Utility.insertMask(getResources().getString(R.string.phone_mask), telefone.getEditText());
        telefone.getEditText().addTextChangedListener(phoneMask);
    }

    public void callEagora(View view) {

        String phone = Utility.TAGYOU_PHONE;

        Utility.callPhone(this, phone);
    }
}