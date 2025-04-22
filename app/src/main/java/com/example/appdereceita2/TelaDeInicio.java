package com.example.appdereceita2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appdereceita2.Activity.MainActivity;
import com.example.appdereceita2.ui.home.HomeFragment;

public class TelaDeInicio extends AppCompatActivity {
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_de_inicio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Inicia a MainActivity após o carregamento
                Intent intent = new Intent(TelaDeInicio.this, MainActivity.class);
                startActivity(intent);
                finish(); // Fecha a tela de carregamento para não voltar ao pressionar "Voltar"
            }
        }, 500); // 3000ms = 3 segundos
    }
}