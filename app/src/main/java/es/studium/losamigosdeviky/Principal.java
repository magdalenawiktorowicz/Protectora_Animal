package es.studium.losamigosdeviky;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import es.studium.losamigosdeviky.ayuntamientos.ConsultaAyuntamiento;
import es.studium.losamigosdeviky.colonias.ConsultaColonia;
import es.studium.losamigosdeviky.cuidados.ConsultaCuidado;
import es.studium.losamigosdeviky.gatos.ConsultaGato;
import es.studium.losamigosdeviky.protectoras.ConsultaProtectora;
import es.studium.losamigosdeviky.veterinarios.ConsultaVeterinario;

public class Principal extends Fragment implements View.OnClickListener {

    ImageButton imageBtnHelp;
    Button btnAyuntamientos, btnProtectoras, btnColonias, btnGatos, btnVeterinarios, btnCuidados, btnBorrarCredenciales;

    FragmentManager fm;
    FragmentTransaction ft;
    Fragment consultaAyuntamiento, consultaProtectora, consultaColonia, consultaVeterinario, consultaGato, consultaCuidado;
    SharedPreferences sharedPreferences;
    Toast toast;

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

        sharedPreferences = getContext().getSharedPreferences("LoginCredenciales", Context.MODE_PRIVATE);
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
        btnBorrarCredenciales.setEnabled(false);
        if (sharedPreferences.getBoolean("credencialesGuardadas", false)) {
            btnBorrarCredenciales.setEnabled(true);
        }
        fm = getActivity().getSupportFragmentManager();

        if (getActivity() != null) {
            // establecer el color del fondo de la barra superior
            ((MainActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_lighter)));
            // establecer el título de la barra superior
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        }

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
        } else if (v.getId() == btnProtectoras.getId()) {
            consultaProtectora = fm.findFragmentByTag("consultaProtectora");
            if (consultaProtectora == null) {
                consultaProtectora = new ConsultaProtectora();
                ft = fm.beginTransaction();
                ft.replace(R.id.container, consultaProtectora, "consultaProtectora")
                        .addToBackStack(null)
                        .commit();
            }
        } else if (v.getId() == btnColonias.getId()) {
            consultaColonia = fm.findFragmentByTag("consultaColonia");
            if (consultaColonia == null) {
                consultaColonia = new ConsultaColonia();
                ft = fm.beginTransaction();
                ft.replace(R.id.container, consultaColonia, "consultaColonia")
                        .addToBackStack(null)
                        .commit();
            }
        } else if (v.getId() == btnGatos.getId()) {
            consultaGato = fm.findFragmentByTag("consultaGato");
            if (consultaGato == null) {
                consultaGato = new ConsultaGato();
                ft = fm.beginTransaction();
                ft.replace(R.id.container, consultaGato, "consultaGato")
                        .addToBackStack(null)
                        .commit();
            }

        } else if (v.getId() == btnVeterinarios.getId()) {
            consultaVeterinario = fm.findFragmentByTag("consultaVeterinario");
            if (consultaVeterinario == null) {
                consultaVeterinario = new ConsultaVeterinario();
                ft = fm.beginTransaction();
                ft.replace(R.id.container, consultaVeterinario, "consultaVeterinario")
                        .addToBackStack(null)
                        .commit();
            }
        } else if (v.getId() == btnCuidados.getId()) {
            consultaCuidado = fm.findFragmentByTag("consultaCuidado");
            if (consultaCuidado == null) {
                consultaCuidado = new ConsultaCuidado();
                ft = fm.beginTransaction();
                ft.replace(R.id.container, consultaCuidado, "consultaCuidado")
                        .addToBackStack(null)
                        .commit();
            }

        } else if (v.getId() == imageBtnHelp.getId()) {
            toast = Toast.makeText(getContext(), "Selecciona la sección \nque quieres gestionar.", Toast.LENGTH_SHORT);
            makeToast();

        } else if (v.getId() == btnBorrarCredenciales.getId()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            btnBorrarCredenciales.setEnabled(false);
            toast = Toast.makeText(getContext(), "Credenciales borradas", Toast.LENGTH_SHORT);
            makeToast();
        }
    }

    private void makeToast() {
        View toastView = toast.getView();
        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
        toastMessage.setTextAppearance(R.style.ToastStyle);
        toastView.setBackground(getResources().getDrawable(R.drawable.toast_shape));
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}