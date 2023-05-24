package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Properties;

public class MainActivity extends AppCompatActivity {
    Button btn1;
    Button btn_2;
    DBHelperMain dbHelper;
    Dialog dialog;
    private GoogleSignInClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new Dialog(MainActivity.this);
        EditText autorization_code_1 = findViewById(R.id.autorization_code_1);
        btn_2 = findViewById(R.id.btn_2);
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this, options);
        btn1 = findViewById(R.id.btn_1);
        String user_code = autorization_code_1.getText().toString();
        dbHelper = new DBHelperMain(this);
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showCustomDialog();
                Intent i = client.getSignInIntent();
                startActivityForResult(i, 1234);
            }
        });
    }

    ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    private void showCustomDialog() {
        dialog.setContentView(R.layout.regdialog);
        CardView cardView = new CardView(dialog.getContext());
        cardView.setRadius(17F);
        Window window = dialog.getWindow();
        window.setLayout(930, 1000);
        window.setGravity(Gravity.TOP);
        window.setClipToOutline(true);
        Resources res = getResources();
        Drawable shape = ResourcesCompat.getDrawable(res, R.drawable.button_top_1, getTheme());
        window.setBackgroundDrawable(shape);
        dialog.setCancelable(true);
        EditText pasreg1 = dialog.findViewById(R.id.pass_dial_1);
        EditText pasreg2 = dialog.findViewById(R.id.pass_dial_2);
        EditText pasreg3 = dialog.findViewById(R.id.pass_dial_3);
        EditText pasreg4 = dialog.findViewById(R.id.pass_dial_4);
        Button btn_2 = dialog.findViewById(R.id.reg_button_1);
//        btn_2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String pasreg1_str = pasreg1.getText().toString();
//                String pasreg2_str = pasreg2.getText().toString();
//                String pasreq3_str = pasreg3.getText().toString();
//                String pasreq4_str = pasreg4.getText().toString();
//                if (pasreg1_str.equals(pasreg2_str) & (pasreg3.length() != 0 & pasreg4.length() != 0)) {
//                    if (pasreg1_str.length() < 8) {
//                        Toast.makeText(getApplicationContext(), "Пароль должен состоять из восьми или более символов", Toast.LENGTH_LONG).show();
//                    }
//                    if (pasreg1_str.length() >= 8 & getMaxCharInSubstring(pasreg1_str) > 2) {
//                        Toast.makeText(getApplicationContext(), "Ненадежный пароль", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), "На вашу почту был отправлен код подтверждения", Toast.LENGTH_LONG).show();
//                        Intent i = client.getSignInIntent();
//                        startActivityForResult(i, 1234);
//                    }
//                }
//                 else {
//                    if (!pasreg1_str.equals(pasreg2_str) & (pasreg3.length() != 0 & pasreg4.length() != 0)) {
//                        Toast.makeText(getApplicationContext(), "Ваши пароли не совпадают", Toast.LENGTH_LONG).show();
//                    }
//                    if (pasreg1_str.equals(pasreg2_str) & (pasreg3.length() == 0 || pasreg4.length() == 0)) {
//                        Toast.makeText(getApplicationContext(), "Заполните все поля", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });
//        dialog.show();
    }

    public static int getMaxCharInSubstring(String inputString) {
        int countCharsInSubString = 0;
        int countCharsInSubStringTemp = 0;

        for (int i = 0; i < inputString.length() - 1; ++i) {
            if (inputString.charAt(i) == inputString.charAt(i + 1)) {
                countCharsInSubStringTemp += 1;
            } else if (countCharsInSubStringTemp > countCharsInSubString) {
                countCharsInSubString = countCharsInSubStringTemp;
                countCharsInSubStringTemp = 0;
            }
        }
        if (countCharsInSubStringTemp > countCharsInSubString) {
            return (countCharsInSubStringTemp + 1);
        } else return (countCharsInSubString + 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(getApplicationContext(), ActivityTwo.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            Intent intent = new Intent(this, ActivityTwo.class);
            startActivity(intent);
        }
    }
}


