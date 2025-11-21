package com.example.chamadasrecebidas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<IncomingNumber> arrayList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(IncomingNumber item);
    }

    public RecyclerAdapter(ArrayList<IncomingNumber> lista, OnItemClickListener listener){
        this.arrayList = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        IncomingNumber currentItem = arrayList.get(position);

        holder.id.setText(Integer.toString(currentItem.getId()));
        holder.number.setText(currentItem.getNumber());

        // 4. Configurar o clique curto no item inteiro (itemView)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Passa o item clicado de volta para a MainActivity
                listener.onItemClick(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, number;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.txtId);
            number = itemView.findViewById(R.id.txtNumber);
        }
    }
}