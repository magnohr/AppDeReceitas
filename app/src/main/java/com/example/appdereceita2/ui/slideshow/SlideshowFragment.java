package com.example.appdereceita2.ui.slideshow;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdereceita2.Adapter.Adapter;
import com.example.appdereceita2.Model.Ingredientes;
import com.example.appdereceita2.R;
import com.example.appdereceita2.databinding.FragmentSlideshowBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ArrayList<Ingredientes> postagens = new ArrayList<>();
    private Adapter adapter;

    private FragmentSlideshowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        View view = inflater.inflate(R.layout.fragment_slideshow, container, false);


        recyclerView = view.findViewById(R.id.recyclerView4);
        fab = view.findViewById(R.id.floatingActionButton3);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new Adapter(postagens);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        // Ao clicar no FAB, abre o AlertDialog para inserir a receita
        fab.setOnClickListener(v -> mostrarDialogoAdicionarReceita());


        return view;
    }

    private void mostrarDialogoAdicionarReceita() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_adicionar_receita, null);
        builder.setView(dialogView);

        EditText edtNome = dialogView.findViewById(R.id.edtNome);
        EditText edtDescricao = dialogView.findViewById(R.id.edtDescricao);
        EditText edtLink = dialogView.findViewById(R.id.edtLink);
        Button btnSalvar = dialogView.findViewById(R.id.btnSalvar);

        AlertDialog dialog = builder.create();

        // Salvar a receita quando o usuário clicar em "Salvar"
        btnSalvar.setOnClickListener(v -> {
            String nome = edtNome.getText().toString().trim();
            String descricao = edtDescricao.getText().toString().trim();
            String link = edtLink.getText().toString().trim();

            if (nome.isEmpty() || descricao.isEmpty()) {
                Toast.makeText(getContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            } else {
                postagens.add(new Ingredientes(
                        R.drawable.c, // IMAGEM PADRÃO
                        nome,
                        descricao,
                        "Passo 1", "Passo 2", "Passo 3", "Passo 4", "Dica especial",
                        link
                ));
                adapter.notifyItemInserted(postagens.size() - 1);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}