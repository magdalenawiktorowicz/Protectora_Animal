package es.studium.losamigosdeviky.cuidados;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import es.studium.losamigosdeviky.BDConexion;
import es.studium.losamigosdeviky.MainActivity;
import es.studium.losamigosdeviky.R;
import es.studium.losamigosdeviky.RecyclerViewOnItemClickListener;
import es.studium.losamigosdeviky.gatos.AltaGato;
import es.studium.losamigosdeviky.gatos.BorradoGato;
import es.studium.losamigosdeviky.gatos.Gato;
import es.studium.losamigosdeviky.gatos.ModificacionGato;

public class ConsultaCuidado extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    List<Cuidado> cuidados = new ArrayList<>();
    RecyclerView recyclerView;
    Spinner spinnerOrdenarCuidados;
    Button btnNuevoCuidado;
    CuidadosAdapter adapter;
    FragmentManager fm;
    FragmentTransaction ft;
    AltaCuidado altaCuidado;
    ModificacionCuidado modificacionCuidado;
    BorradoCuidado borradoCuidado;

    public ConsultaCuidado() {
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
        return inflater.inflate(R.layout.fragment_consulta_cuidado, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("número id de cuidado");
        spinnerArray.add("veterinario");
        spinnerArray.add("gato");
        spinnerOrdenarCuidados = view.findViewById(R.id.spinnerOrdenarCuidados);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerOrdenarCuidados.setAdapter(spinnerArrayAdapter);
        spinnerOrdenarCuidados.setOnItemSelectedListener(this);
        btnNuevoCuidado = view.findViewById(R.id.buttonNuevoCuidado);
        btnNuevoCuidado.setOnClickListener(this);
        fm = getActivity().getSupportFragmentManager();
        setUpRecyclerView(view);
        fetchCuidadosData();

        fm.setFragmentResultListener("altaCuidadoRequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                boolean success = result.getBoolean("operationSuccess");
                Log.d("ConsultaCuidado", "Fragment result received: " + success);
                if (success) {
                    Log.d("ConsultaCuidado", "Calling fetchCuidadosData after successful result");
                    fetchCuidadosData();
                }
            }
        });

        fm.setFragmentResultListener("modificacionCuidadoRequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                boolean success = result.getBoolean("operationSuccess");
                Log.d("ConsultaCuidado", "Fragment result received: " + success);
                if (success) {
                    Log.d("ConsultaCuidado", "Calling fetchCuidadosData after successful result");
                    fetchCuidadosData();
                }
            }
        });

        fm.setFragmentResultListener("borradoCuidadoRequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                boolean success = result.getBoolean("operationSuccess");
                Log.d("ConsultaCuidado", "Fragment result received: " + success);
                if (success) {
                    Log.d("ConsultaCuidado", "Calling fetchCuidadosData after successful result");
                    fetchCuidadosData();
                }
            }
        });

        // establecer el título en la barra superior
        if (getActivity() != null) {
            // establecer el título de la barra superior
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.cuidados);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchCuidadosData();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fetchCuidadosData();
    }

    private void setUpRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewCuidados);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new CuidadosAdapter(cuidados, new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (MainActivity.tipoUsuario == 0) {
                    // fragment Modificación
                    Log.d("ConsultaCuidado", "Showing ModificacionCuidado dialog");
                    modificacionCuidado = new ModificacionCuidado(cuidados.get(position));
                    modificacionCuidado.setCancelable(false);
                    modificacionCuidado.show(getParentFragmentManager(), "ModificacionCuidado");
                }
            }

            @Override
            public void onLongClick(View v, int position) {
                if (MainActivity.tipoUsuario == 0) {
                    // fragment Borrado
                    Log.d("ConsultaCuidado", "Showing BorradoCuidado dialog");
                    borradoCuidado = new BorradoCuidado(cuidados.get(position));
                    borradoCuidado.setCancelable(false);
                    borradoCuidado.show(getParentFragmentManager(), "BorradoCuidado");
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void fetchCuidadosData() {
        BDConexion.consultarCuidados(new CuidadoCallback() {
            @Override
            public void onResult(ArrayList<Cuidado> cuis) {
                if (cuis != null) {
                    Log.d("ConsultaCuidado", "Data fetched: " + cuis.size() + "cuidados");
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cuidados.clear();
                                cuidados.addAll(cuis);
                                cuidados.sort(Comparator.comparing(Cuidado::getIdCuidado));
                                adapter.notifyDataSetChanged();
                                Log.d("ConsultaCuidado", "Cuidados list updated");
                            }
                        });
                    }
                    else {
                        Log.d("ConsultaCuidado", "No cuidados data received");
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnNuevoCuidado.getId()) {
            Log.d("ConsultaCuidado", "Showing AltaCuidado dialog");
            altaCuidado = new AltaCuidado();
            altaCuidado.setCancelable(false);
            altaCuidado.show(getParentFragmentManager(), "AltaCuidado");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // ordenar por el número id de cuidado
        if (spinnerOrdenarCuidados.getSelectedItemPosition() == 0) {
            cuidados.sort(Comparator.comparing(Cuidado::getIdCuidado));
            adapter.notifyDataSetChanged();
        }
        // ordenar por el veterinario
        else if (spinnerOrdenarCuidados.getSelectedItemPosition() == 1) {
            cuidados.sort(Comparator.comparing(Cuidado::getIdVeterinarioFK));
            adapter.notifyDataSetChanged();
        }
        // ordenar por el gato
        else if (spinnerOrdenarCuidados.getSelectedItemPosition() == 2) {
            cuidados.sort(Comparator.comparing(c -> c.getIdGatoFK()));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}