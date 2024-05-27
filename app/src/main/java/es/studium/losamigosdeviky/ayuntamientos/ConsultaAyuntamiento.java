package es.studium.losamigosdeviky.ayuntamientos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.studium.losamigosdeviky.R;

public class ConsultaAyuntamiento extends Fragment {

    public ConsultaAyuntamiento() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_consulta_ayuntamiento, container, false);
    }
}