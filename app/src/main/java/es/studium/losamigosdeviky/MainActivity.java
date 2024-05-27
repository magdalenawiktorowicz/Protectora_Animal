package es.studium.losamigosdeviky;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import es.studium.losamigosdeviky.ayuntamientos.ConsultaAyuntamiento;

public class MainActivity extends AppCompatActivity {

    public static int tipoUsuario;
    FragmentManager fm = getSupportFragmentManager();
    Fragment principal;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        principal = fm.findFragmentById(R.id.principal);
        if (principal == null) {
            ft = fm.beginTransaction();
            ft.replace(R.id.container, new Principal(), "principal");
            ft.commit();
        }
    }

}