package es.studium.losamigosdeviky.ayuntamientos;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import es.studium.losamigosdeviky.BDConexion;
import es.studium.losamigosdeviky.MainActivity;
import es.studium.losamigosdeviky.R;
import es.studium.losamigosdeviky.RecyclerViewOnItemClickListener;

public class ConsultaAyuntamiento extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    List<Ayuntamiento> ayuntamientos = new ArrayList<>();
    RecyclerView recyclerView;
    Spinner spinnerOrdenarAyuntamientos;
    Button btnNuevoAyuntamiento;

    FragmentManager fm;
    FragmentTransaction ft;
    DialogFragment altaAyuntamiento;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("nombre de ayuntamiento");
        spinnerArray.add("responsable de ayuntamiento");
        spinnerArray.add("localización");
        spinnerOrdenarAyuntamientos = view.findViewById(R.id.spinnerOrdenarAyuntamientos);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerOrdenarAyuntamientos.setAdapter(spinnerArrayAdapter);
        spinnerOrdenarAyuntamientos.setOnItemSelectedListener(this);
        btnNuevoAyuntamiento = view.findViewById(R.id.buttonNuevoAyuntamiento);
        btnNuevoAyuntamiento.setOnClickListener(this);
        fm = getActivity().getSupportFragmentManager();
        setUpRecyclerView(view);
        fetchAyuntamientosData();

        // establecer el título en la barra superior
        if (getActivity() != null) {
            // establecer el color del fondo de la barra superior
            ((MainActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.pink)));
            // establecer el título de la barra superior
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.ayuntamientos);
        }
    }

    private void setUpRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewAyuntamientos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(new AyuntamientosAdapter(ayuntamientos, new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (MainActivity.tipoUsuario == 0) {
                    // fragment Modificación
                    Toast.makeText(getContext(), "short click on " + ayuntamientos.get(position), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLongClick(View v, int position) {
                if (MainActivity.tipoUsuario == 0) {
                    Toast.makeText(getContext(), "long click on " + ayuntamientos.get(position), Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }

    private void fetchAyuntamientosData() {
        BDConexion.consultarAyuntamientos(new AyuntamientoCallback() {
            @Override
            public void onResult(ArrayList<Ayuntamiento> ays) {
                if (ays != null) {
                    ayuntamientos.clear();
                    ayuntamientos.addAll(ays);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onOperacionCorrectaUpdated(boolean resultado) {
                if (resultado) {
                    fetchAyuntamientosData();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == btnNuevoAyuntamiento.getId()) {
            altaAyuntamiento = new AltaAyuntamiento();
            altaAyuntamiento.setCancelable(false);
            altaAyuntamiento.show(getActivity().getSupportFragmentManager(), "AltaAyuntamiento");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // ordenar por el nombre de ayuntamiento
        if (spinnerOrdenarAyuntamientos.getSelectedItemPosition() == 0) {
            ayuntamientos.sort(Comparator.comparing((Ayuntamiento a) -> a.getNombreAyuntamiento().toLowerCase()));
            recyclerView.getAdapter().notifyDataSetChanged();
        }
        // ordenar por el nombre del responsable de ayuntamiento
        else if (spinnerOrdenarAyuntamientos.getSelectedItemPosition() == 1) {
            ayuntamientos.sort(Comparator.comparing((Ayuntamiento a) -> a.getResponsableAyuntamiento().toLowerCase()));
            recyclerView.getAdapter().notifyDataSetChanged();
        }
        // ordenar por la localización, según código postal
        else if (spinnerOrdenarAyuntamientos.getSelectedItemPosition() == 2) {
            ayuntamientos.sort(Comparator.comparing((Ayuntamiento a) -> a.getCpAyuntamiento()));
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}