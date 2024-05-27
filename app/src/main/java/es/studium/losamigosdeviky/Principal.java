package es.studium.losamigosdeviky;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import es.studium.losamigosdeviky.ayuntamientos.ConsultaAyuntamiento;

public class Principal extends Fragment implements View.OnClickListener {

    ImageButton imageBtnHelp;
    Button btnAyuntamientos, btnProtectoras, btnColonias, btnGatos, btnVeterinarios, btnCuidados, btnBorrarCredenciales;

    FragmentManager fm;
    FragmentTransaction ft;
    Fragment consultaAyuntamiento;

    public Principal() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_principal, container, false);

        Toast.makeText(getContext(), "" + MainActivity.tipoUsuario, Toast.LENGTH_SHORT).show();
        imageBtnHelp = v.findViewById(R.id.imageBtnHelp);
        imageBtnHelp.setOnClickListener(this);
        btnAyuntamientos = v.findViewById(R.id.btnAyuntamientos);
        btnAyuntamientos.setOnClickListener(this);
        btnProtectoras = v.findViewById(R.id.btnProtectoras);
        btnProtectoras.setOnClickListener(this);
        btnColonias = v.findViewById(R.id.btnColonias);
        btnColonias.setOnClickListener(this);
        btnGatos = v.findViewById(R.id.btnGatos);
        btnGatos.setOnClickListener(this);
        btnVeterinarios = v.findViewById(R.id.btnVeterinarios);
        btnVeterinarios.setOnClickListener(this);
        btnCuidados = v.findViewById(R.id.btnCuidados);
        btnCuidados.setOnClickListener(this);
        btnBorrarCredenciales = v.findViewById(R.id.btnBorrarCredenciales);
        btnBorrarCredenciales.setOnClickListener(this);
        fm = getActivity().getSupportFragmentManager();

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnAyuntamientos.getId()) {
            consultaAyuntamiento = fm.findFragmentByTag("consultaAyuntamiento");
            if (consultaAyuntamiento == null) {
                consultaAyuntamiento = new ConsultaAyuntamiento();
                ft = fm.beginTransaction();
                ft.replace(R.id.container, consultaAyuntamiento, "consultaAyuntamiento")
                        .addToBackStack(null)
                        .commit();
            }
        }
    }
}