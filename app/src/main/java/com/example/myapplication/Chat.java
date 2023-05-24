package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.text.format.DateFormat;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chat extends AppCompatActivity {
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("messages");
    Button button;
    private LinearLayout linearLayout;
    private final List<Item> items = new ArrayList<>();
    private final List<Message> messages = new ArrayList<Message>();

    private final RecyclerView.Adapter adapter2 = new ItemAdapter(this.items);
    private final List<String> sortitems = new ArrayList<>();




    private static int SIGN_IN_CODE;
    private FirebaseListAdapter<Item> adapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_CODE){
            Snackbar.make(linearLayout, "Вы авторизованы", Snackbar.LENGTH_LONG).show();
            displayAllMessages();
        }
        else{
            Snackbar.make(linearLayout, "Вы авторизованы", Snackbar.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        linearLayout = findViewById(R.id.Chat_activity);
        RecyclerView recycler = findViewById(R.id.list_of_messages);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter2);
        button = findViewById(R.id.button);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(Chat.this, MainActivity.class);
            startActivity(intent);
        }
        else
        {Snackbar.make(linearLayout, "Вы авторизованы", Snackbar.LENGTH_LONG).show();
        displayAllMessages();}
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText textField = findViewById(R.id.editText);
            if (textField.getText().toString() == ""){
                return;
            }

            else{
                FirebaseDatabase.getInstance().getReference().push().setValue(
                    new Item(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                            textField.getText().toString()));
            textField.setText("");
            }
        }
    });
    }
    public void add(View view) {
        EditText edit = this.findViewById(R.id.editText);
        this.items.add(new Item(edit.getText().toString()));
        sortitems.add(edit.getText().toString());
        Collections.sort(sortitems);
        edit.setText("");
        adapter2.notifyItemInserted(this.items.size() - 1);
        displayAllMessages();
    }

    private void displayAllMessages() {
        FirebaseListOptions<Item> options =
                new FirebaseListOptions.Builder<Item>()
                        .setQuery(FirebaseDatabase.getInstance().getReference(), Item.class)
                        .setLayout(R.layout.items)
                        .build();
        adapter = new FirebaseListAdapter<Item> ( options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Item model, int position) {
                 items.add(new Item(model.getName()+
                         "\n"+
                         model.getText()+
                         "\n"+
                         DateFormat.format("dd-mm-yyyy HH:mm:ss", model.getTime())));
            }

        };
        adapter2.notifyItemInserted(this.items.size() - 1);
    }


    public void sort(View view) {
        int d = items.size();
        this.items.clear();
        for (int i = 0; i < sortitems.size(); i++) {
            this.items.add(new Item(sortitems.get(i)));
        }
        adapter2.notifyItemRangeChanged(0, d);
    }


    public class ItemAdapter extends RecyclerView.Adapter {
        private final List<Item> items;

        public ItemAdapter(List<Item> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false)) {};
            }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Button name = holder.itemView.findViewById(R.id.name);
            TextView distance = holder.itemView.findViewById(R.id.distance);
            name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    MyDialogFragment myDialogFragment = new MyDialogFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    myDialogFragment.show(transaction, "dialog");
                    return true;
                }
            });
            name.setText(String.format("%s. %s", position, this.items.get(position).getName()));

        }

        @Override
        public int getItemCount() {
            return this.items.size();
        }
    }
}