package comcesar1287.github.tagyou.controller.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Utility {

    public static final String LOGIN_SHARED_PREF_NAME = "LoginActivityPreferences";

    public static final String APP_PACKAGE_NAME = "comcesar1287.github.tagyou";

    public static final String URI_PACKAGE = "android.resource://comcesar1287.github.tagyou/";

    private static final int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

    public static final String KEY_CONTENT_EXTRA_COMPANY = "company";
    public static final String KEY_CONTENT_EXTRA_DATABASE = "database";
    public static final String KEY_CONTENT_EXTRA_DATA = "data";
    public static final String KEY_MAP_FRAGMENT = "mainFrag";
    public static final String TAGYOU_PHONE = "(85)00000–0000";

    public static boolean verifyEmptyField(String name, String email, String pass){

        return (name.isEmpty() || email.isEmpty() || pass.isEmpty());
    }

    public static boolean verifyEmptyField(String email, String pass){

        return (email.isEmpty() || pass.isEmpty());
    }

    private static int calcularDigito(String str, int[] peso) {
        int soma = 0;
        for (int indice=str.length()-1, digito; indice >= 0; indice-- ) {
            digito = Integer.parseInt(str.substring(indice,indice+1));
            soma += digito*peso[peso.length-str.length()+indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }

    public static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "");
    }

    public static void callPhone(Activity activity, String phone){

        Uri uri = Uri.parse("tel:" + phone);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);

        activity.startActivity(intent);
    }

    public static TextWatcher insertMask(final String mask, final EditText ediTxt) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String str = unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }


}
