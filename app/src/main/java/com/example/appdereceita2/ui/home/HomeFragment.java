package com.example.appdereceita2.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdereceita2.Adapter.Adapter;
import com.example.appdereceita2.Model.Ingredientes;
import com.example.appdereceita2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;  // Código de solicitação para permissões
    private static final int REQUEST_CODE_PICK_IMAGE = 2; // Código de solicitação para escolher imagem
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ArrayList<Ingredientes> postagens = new ArrayList<>();
    private Adapter adapter;
    private Uri selectedImageUri; // Variável para armazenar a URI da imagem selecionada

    private static final String PREFS_NAME = "MeuAppPrefs";
    private static final String KEY_LISTA = "lista_receitas";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 🔹 Verifique se a permissão foi concedida para gravar no armazenamento externo
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Se não foi concedida, solicite a permissão
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.floatingActionButton);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        // 🔹 Carrega os dados salvos
        postagens = carregarLista();

        adapter = new Adapter(postagens);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        // 🔹 Remove um item ao clicar no botão de deletar
        adapter.setOnDeleteClickListener(position -> {
            postagens.remove(position);
            adapter.notifyItemRemoved(position);
            salvarLista(); // Salva a lista após a remoção
        });

        // 🔹 Abre o diálogo para adicionar uma nova receita
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
        Button btnSelectImage = dialogView.findViewById(R.id.btnSelectImage);
        ImageView imgSelected = dialogView.findViewById(R.id.imgSelected);

        AlertDialog dialog = builder.create();

        // Quando o botão de selecionar imagem for clicado
        btnSelectImage.setOnClickListener(v -> abrirGaleriaImagem());

        // Salvar os dados e adicionar a receita
        btnSalvar.setOnClickListener(v -> {
            String nome = edtNome.getText().toString().trim();
            String descricao = edtDescricao.getText().toString().trim();
            String link = edtLink.getText().toString().trim();

            // Verificação dos campos obrigatórios
            if (nome.isEmpty() || descricao.isEmpty()) {
                Toast.makeText(getContext(), "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show();
            } else {
                // Verificação do link
                if (!link.isEmpty() && !isValidUrl(link)) {
                    Toast.makeText(getContext(), "Link inválido. Insira um link válido.", Toast.LENGTH_SHORT).show();
                    return; // Não continua se o link for inválido
                }

                // Se uma imagem foi selecionada, usa a URI dela
                String imagem = (selectedImageUri != null) ? selectedImageUri.toString() : "default_image";

                // 🔹 Adiciona a nova receita à lista
                postagens.add(new Ingredientes(
                        R.drawable.rr, // Imagem padrão (pode ser modificada para imagem do armazenamento)
                        nome,
                        descricao,
                        "Passo 1", "Passo 2", "Passo 3", "Passo 4", "Dica especial",
                        link.isEmpty() ? "Sem link" : link // Se o link estiver vazio, coloca "Sem link"
                ));

                adapter.notifyItemInserted(postagens.size() - 1);
                salvarLista(); // 🔹 Salva a lista no SharedPreferences
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void abrirGaleriaImagem() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    // Manipula o resultado da escolha da imagem
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == getActivity().RESULT_OK && data != null) {
            selectedImageUri = data.getData(); // Pega a URI da imagem escolhida

            // Exibe a imagem selecionada no ImageView
            ImageView imgSelected = getView().findViewById(R.id.imgSelected);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);
                imgSelected.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Função para verificar se o link é válido
    private boolean isValidUrl(String url) {
        // Regex melhorada para verificar o formato de uma URL (http, https, ftp)
        return url.matches("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$");
    }

    // 🔹 Método para salvar a lista no SharedPreferences
    private void salvarLista() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(postagens); // Converte a lista em JSON
        editor.putString(KEY_LISTA, json);
        editor.apply(); // Salva os dados
    }

    // 🔹 Método para carregar a lista salva do SharedPreferences
    private ArrayList<Ingredientes> carregarLista() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(KEY_LISTA, null);

        if (json == null) {
            return new ArrayList<>(); // Retorna lista vazia se não houver dados salvos
        }

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Ingredientes>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // 🔹 Manipulação do resultado da solicitação de permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permissão concedida!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permissão negada! Não será possível salvar imagens.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
