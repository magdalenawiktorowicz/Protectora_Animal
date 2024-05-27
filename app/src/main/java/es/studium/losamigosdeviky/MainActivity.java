package es.studium.losamigosdeviky;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static int tipoUsuario;
    ImageButton imageBtnHelp;
    Button btnAyuntamientos, btnProtectoras, btnColonias, btnGatos, btnVeterinarios, btnCuidados, btnBorrarCredenciales;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "" + MainActivity.tipoUsuario, Toast.LENGTH_SHORT).show();
        imageBtnHelp = findViewById(R.id.imageBtnHelp);
        imageBtnHelp.setOnClickListener(this);
        btnAyuntamientos = findViewById(R.id.btnAyuntamientos);
        btnAyuntamientos.setOnClickListener(this);
        btnProtectoras = findViewById(R.id.btnProtectoras);
        btnProtectoras.setOnClickListener(this);
        btnColonias = findViewById(R.id.btnColonias);
        btnColonias.setOnClickListener(this);
        btnGatos = findViewById(R.id.btnGatos);
        btnGatos.setOnClickListener(this);
        btnVeterinarios = findViewById(R.id.btnVeterinarios);
        btnVeterinarios.setOnClickListener(this);
        btnCuidados = findViewById(R.id.btnCuidados);
        btnCuidados.setOnClickListener(this);
        btnBorrarCredenciales = findViewById(R.id.btnBorrarCredenciales);
        btnBorrarCredenciales.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TO BE CONTINUED...
    }
}