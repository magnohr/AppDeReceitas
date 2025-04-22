package com.example.appdereceita2.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdereceita2.Model.Ingredientes;
import com.example.appdereceita2.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private List<Ingredientes> postagens;
    private OnDeleteClickListener deleteClickListener;

    // Interface para deletar item
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    // Construtor
    public Adapter(List<Ingredientes> postagens) {
        this.postagens = postagens;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.receita_card, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Ingredientes postagem = postagens.get(position);

        // Preenche os dados da receita
        holder.imagem.setImageResource(postagem.getImage1());
        holder.NomeReceita.setText(postagem.getNomeReceita());
        holder.igredientes1.setText(postagem.getIngre1());
        holder.igredientes2.setText(postagem.getIngre2());
        holder.igredientes3.setText(postagem.getIngre3());
        holder.igredientes4.setText(postagem.getIngre4());
        holder.igredientes5.setText(postagem.getIngre5());
        holder.igredientes6.setText(postagem.getIngre6());
        holder.link.setText(postagem.getLink1());

        // Evento para excluir a receita
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postagens.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imagem, btnDelete;
        TextView NomeReceita, igredientes1, igredientes2, igredientes3,
                igredientes4, igredientes5, igredientes6, link;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imagem = itemView.findViewById(R.id.recipe_image);
            NomeReceita = itemView.findViewById(R.id.recipe_title);
            igredientes1 = itemView.findViewById(R.id.ingredient_1);
            igredientes2 = itemView.findViewById(R.id.ingredient_2);
            igredientes3 = itemView.findViewById(R.id.ingredient_3);
            igredientes4 = itemView.findViewById(R.id.ingredient_4);
            igredientes5 = itemView.findViewById(R.id.ingredient_5);
            igredientes6 = itemView.findViewById(R.id.ingredient_6);
            link = itemView.findViewById(R.id.recipe_link);
            btnDelete = itemView.findViewById(R.id.btn_delete); // ID do botão de excluir no XML
        }
    }
}
