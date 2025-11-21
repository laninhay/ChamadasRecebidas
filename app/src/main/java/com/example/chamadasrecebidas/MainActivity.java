package com.example.chamadasrecebidas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView textView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<IncomingNumber> arrayList = new ArrayList<>();
    private RecyclerAdapter recyclerAdapter;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        textView = findViewById(R.id.textView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerAdapter = new RecyclerAdapter(arrayList, new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(IncomingNumber item) {
                // Quando clicar, chama o método que mostra o diálogo
                showDeleteDialog(item);
            }
        });

        recyclerView.setAdapter(recyclerAdapter);
        readFromDB();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                readFromDB();
            }
        };
    }

    private void showDeleteDialog(final IncomingNumber item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remover Chamada");
        builder.setMessage("Deseja realmente remover o número " + item.getNumber() + " da lista?");

        // Botão SIM
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 1. Abrir conexão com o banco
                DbHelper dbHelper = new DbHelper(MainActivity.this);
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                // 2. Deletar usando o método que criamos no passo 1
                dbHelper.deleteNumber(item.getId(), database);

                // 3. Fechar banco
                dbHelper.close();

                // 4. Atualizar a lista na tela
                readFromDB();
            }
        });

        // Botão NÃO (apenas fecha o diálogo)
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        // Exibir o diálogo
        builder.create().show();
    }

    private void readFromDB() {
        arrayList.clear();
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.readNumber(database);

        if (cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                String number = cursor.getString(cursor.getColumnIndex(DbContract.INCOMING_NUMBER));
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                arrayList.add(new IncomingNumber(id, number));
            }
            cursor.close();
            dbHelper.close();
            recyclerAdapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(DbContract.UPDATE_UI_FILTER));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}