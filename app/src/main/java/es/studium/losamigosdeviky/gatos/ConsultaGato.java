package es.studium.losamigosdeviky.gatos;

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
import es.studium.losamigosdeviky.protectoras.AltaProtectora;
import es.studium.losamigosdeviky.protectoras.BorradoProtectora;
import es.studium.losamigosdeviky.protectoras.ModificacionProtectora;
import es.studium.losamigosdeviky.protectoras.Protectora;

public class ConsultaGato extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    List<Gato> gatos = new ArrayList<>();
    RecyclerView recyclerView;
    Spinner spinnerOrdenarGatos;
    Button btnNuevoGato;
    GatosAdapter adapter;
    FragmentManager fm;
    FragmentTransaction ft;
    AltaGato altaGato;
    ModificacionGato modificacionGato;
    BorradoGato borradoGato;

    public ConsultaGato() {
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
        return inflater.inflate(R.layout.fragment_consulta_gato, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("nombre de gato");
        spinnerArray.add("fecha de nacimiento");
        spinnerArray.add("chip");
        spinnerArray.add("esterilización");
        spinnerArray.add("veterinario");
        spinnerArray.add("colonia");
        spinnerOrdenarGatos = view.findViewById(R.id.spinnerOrdenarGatos);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerOrdenarGatos.setAdapter(spinnerArrayAdapter);
        spinnerOrdenarGatos.setOnItemSelectedListener(this);
        btnNuevoGato = view.findViewById(R.id.buttonNuevoGato);
        btnNuevoGato.setOnClickListener(this);
        fm = getActivity().getSupportFragmentManager();
        setUpRecyclerView(view);
        fetchGatosData();

        fm.setFragmentResultListener("altaGatoRequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                boolean success = result.getBoolean("operationSuccess");
                Log.d("ConsultaGato", "Fragment result received: " + success);
                if (success) {
                    Log.d("ConsultaGato", "Calling fetchGatosData after successful result");
                    fetchGatosData();
                }
            }
        });

        fm.setFragmentResultListener("modificacionGatoRequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                boolean success = result.getBoolean("operationSuccess");
                Log.d("ConsultaGato", "Fragment result received: " + success);
                if (success) {
                    Log.d("ConsultaGato", "Calling fetchGatosData after successful result");
                    fetchGatosData();
                }
            }
        });

        fm.setFragmentResultListener("borradoGatoRequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                boolean success = result.getBoolean("operationSuccess");
                Log.d("ConsultaGato", "Fragment result received: " + success);
                if (success) {
                    Log.d("ConsultaGato", "Calling fetchGatosData after successful result");
                    fetchGatosData();
                }
            }
        });

        // establecer el título en la barra superior
        if (getActivity() != null) {
            // establecer el color del fondo de la barra superior
            ((MainActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.pink)));
            // establecer el título de la barra superior
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.gatos);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchGatosData();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fetchGatosData();
    }

    private void setUpRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewGatos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new GatosAdapter(gatos, new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (MainActivity.tipoUsuario == 0) {
                    // fragment Modificación
//                    Log.d("ConsultaGato", "Showing ModificacionGato dialog");
//                    modificacionGato = new ModificacionGato(gatos.get(position));
//                    modificacionGato.setCancelable(false);
//                    modificacionGato.show(getParentFragmentManager(), "ModificacionGato");
                }
            }

            @Override
            public void onLongClick(View v, int position) {
                if (MainActivity.tipoUsuario == 0) {
                    // fragment Borrado
//                    Log.d("ConsultaGato", "Showing BorradoGato dialog");
//                    borradoGato = new BorradoGato(gatos.get(position));
//                    borradoGato.setCancelable(false);
//                    borradoGato.show(getParentFragmentManager(), "BorradoGato");
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void fetchGatosData() {
        Log.d("ConsultaGato", "Fetching gatos data");
        BDConexion.consultarGatos(new GatoCallback() {
            @Override
            public void onResult(final ArrayList<Gato> gs) {
                if (gs != null) {
                    Log.d("ConsultaGato", "Data fetched: " + gs.size() + " gatos");
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gatos.clear();
                                gatos.addAll(gs);
                                gatos.sort(Comparator.comparing((Gato g) -> g.getNombreGato().toLowerCase()));
                                adapter.notifyDataSetChanged();
                                Log.d("ConsultaGato", "Gatos list updated");
                            }
                        });
                    }
                    else {
                        Log.d("ConsultaGato", "No gatos data received");
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnNuevoGato.getId()) {
//            Log.d("ConsultaGato", "Showing AltaGato dialog");
//            altaGato = new AltaGato();
//            altaGato.setCancelable(false);
//            altaGato.show(getParentFragmentManager(), "AltaProtectora");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // ordenar por el nombre de gato
        if (spinnerOrdenarGatos.getSelectedItemPosition() == 0) {
            gatos.sort(Comparator.comparing((Gato g) -> g.getNombreGato().toLowerCase()));
            adapter.notifyDataSetChanged();
        }
        // ordenar por la fecha de nacimiento
        else if (spinnerOrdenarGatos.getSelectedItemPosition() == 1) {
            gatos.sort(Comparator.comparing((Gato g) -> g.getFechaNacimientoGato()));
            adapter.notifyDataSetChanged();
        }
        // ordenar por el chip
        else if (spinnerOrdenarGatos.getSelectedItemPosition() == 2) {
            gatos.sort(Comparator.comparing((Gato g) -> g.getChipGato()));
            adapter.notifyDataSetChanged();
        }
        // ordenar por esterilización
        else if (spinnerOrdenarGatos.getSelectedItemPosition() == 3) {
            gatos.sort(Comparator.comparing((Gato g) -> g.getEsEsterilizado()));
            adapter.notifyDataSetChanged();
        }
        // ordenar por el veterinario
        else if (spinnerOrdenarGatos.getSelectedItemPosition() == 4) {
            gatos.sort(Comparator.comparing((Gato g) -> g.getIdVeterinarioFK3()));
            adapter.notifyDataSetChanged();
        }
        // ordenar por la colonia
        else if (spinnerOrdenarGatos.getSelectedItemPosition() == 5) {
            gatos.sort(Comparator.comparing((Gato g) -> g.getIdColoniaFK4()));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}