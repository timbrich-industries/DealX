package com.example.myapplication;

import androidx.annotation.NonNull;
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
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ActivityThree extends AppCompatActivity {
    private final List<String> sortitems = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();
    Button btn_s;
    Button btn_delete;
    private final RecyclerView.Adapter adapter = new ItemAdapter(this.items);
    Button btn_next;

    @Override
            protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        RecyclerView recycler = findViewById(R.id.recycler);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (items.size() != 0) {
                    items.remove(items.size() - 1);
                    adapter.notifyItemRemoved(items.size());
                }
            }
        });
        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityThree.this, Chat.class);
                startActivity(intent);
            }
        });
    }
    public void add(View view) {
        EditText edit = this.findViewById(R.id.editText);
        this.items.add(new Item(edit.getText().toString()));
        sortitems.add(edit.getText().toString());
        Collections.sort(sortitems);
        edit.setText("");
        adapter.notifyItemInserted(this.items.size() - 1);
    }
    public void sort(View view) {
        int d = items.size();
        this.items.clear();
        for (int i = 0; i < sortitems.size(); i++) {
            this.items.add(new Item(sortitems.get(i)));
        }
        adapter.notifyItemRangeChanged(0, d);
    }




    private final class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final List<Item> items;

        public ItemAdapter(List l) {
            this.items = l;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int index) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false)) {
            };
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int index) {
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
            name.setText(String.format("%s. %s", index, this.items.get(index).getName()));
        }

        @Override
        public int getItemCount() {
            return this.items.size();
        }
    }
}
