package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class ActivityTwo extends AppCompatActivity {
    EditText creating_code_1, creating_code_2;
    private Button exit_btn;
    Button creating_btn_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        creating_btn_1 = (Button) findViewById(R.id.creating_btn_1);
        creating_code_1 = (EditText) findViewById(R.id.creating_code_1);
        creating_code_2 = (EditText) findViewById(R.id.creating_code_2);
        if (creating_code_1 != null && creating_code_2 != null){
            creating_btn_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ActivityThree.class);
                    intent.putExtra("creating_code_1", creating_code_1.getText().toString());
                    intent.putExtra("creating_code_2", creating_code_2.getText().toString());
                    startActivity(intent);

                }
            });
        }
    exit_btn = (Button) findViewById(R.id.exit_button);
    exit_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(ActivityTwo.this, MainActivity.class);
            startActivity(i);
        }
    });
    }
}