package es.studium.losamigosdeviky.veterinarios;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import es.studium.losamigosdeviky.BDConexion;
import es.studium.losamigosdeviky.MainActivity;
import es.studium.losamigosdeviky.R;
import es.studium.losamigosdeviky.RecyclerViewOnItemClickListener;

public class ConsultaVeterinario extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    List<Veterinario> veterinarios = new ArrayList<>();
    RecyclerView recyclerView;
    Spinner spinnerOrdenarVeterinarios;
    Button btnNuevoVeterinario;
    VeterinariosAdapter adapter;
    FragmentManager fm;
    FragmentTransaction ft;
    AltaVeterinario altaVeterinario;
    ModificacionVeterinario modificacionVeterinario;
    BorradoVeterinario borradoVeterinario;

    public ConsultaVeterinario() {
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
        return inflater.inflate(R.layout.fragment_consulta_veterinario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("apellidos de veterinario");
        spinnerArray.add("especialidad");
        spinnerOrdenarVeterinarios = view.findViewById(R.id.spinnerOrdenarVeterinarios);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerOrdenarVeterinarios.setAdapter(spinnerArrayAdapter);
        spinnerOrdenarVeterinarios.setOnItemSelectedListener(this);
        btnNuevoVeterinario = view.findViewById(R.id.buttonNuevoVeterinario);
        btnNuevoVeterinario.setOnClickListener(this);
        fm = getActivity().getSupportFragmentManager();
        setUpRecyclerView(view);
        fetchVeterinariosData();

        fm.setFragmentResultListener("altaVeterinarioRequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                boolean success = result.getBoolean("operationSuccess");
                Log.d("ConsultaVeterinario", "Fragment result received: " + success);
                if (success) {
                    Log.d("ConsultaVeterinario", "Calling fetchVeterinariosData after successful result");
                    fetchVeterinariosData();
                }
            }
        });

        fm.setFragmentResultListener("modificacionVeterinarioRequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                boolean success = result.getBoolean("operationSuccess");
                Log.d("ConsultaVeterinario", "Fragment result received: " + success);
                if (success) {
                    Log.d("ConsultaVeterinario", "Calling fetchVeterinariosData after successful result");
                    fetchVeterinariosData();
                }
            }
        });

        fm.setFragmentResultListener("borradoVeterinarioRequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                boolean success = result.getBoolean("operationSuccess");
                Log.d("ConsultaVeterinario", "Fragment result received: " + success);
                if (success) {
                    Log.d("ConsultaVeterinario", "Calling fetchVeterinariosData after successful result");
                    fetchVeterinariosData();
                }
            }
        });

        // establecer el título en la barra superior
        if (getActivity() != null) {
            // establecer el color del fondo de la barra superior
            ((MainActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.pink)));
            // establecer el título de la barra superior
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.veterinarios);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchVeterinariosData();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fetchVeterinariosData();
    }

    private void setUpRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewVeterinarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new VeterinariosAdapter(veterinarios, new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (MainActivity.tipoUsuario == 0) {
                    // fragment Modificación
                    Log.d("ConsultaVeterinario", "Showing ModificacionVeterinario dialog");
                    modificacionVeterinario = new ModificacionVeterinario(veterinarios.get(position));
                    modificacionVeterinario.setCancelable(false);
                    modificacionVeterinario.show(getParentFragmentManager(), "ModificacionVeterinario");
                }
            }

            @Override
            public void onLongClick(View v, int position) {
                if (MainActivity.tipoUsuario == 0) {
                    // fragment Borrado
                    Log.d("ConsultaVeterinario", "Showing BorradoVeterinario dialog");
                    borradoVeterinario = new BorradoVeterinario(veterinarios.get(position));
                    borradoVeterinario.setCancelable(false);
                    borradoVeterinario.show(getParentFragmentManager(), "BorradoVeterinario");
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void fetchVeterinariosData() {
        Log.d("ConsultaVeterinario", "Fetching veterinarios data");
        BDConexion.consultarVeterinarios(new VeterinarioCallback() {
            @Override
            public void onResult(final ArrayList<Veterinario> vets) {
                if (vets != null) {
                    Log.d("ConsultaVeterinario", "Data fetched: " + vets.size() + " veterinarios");
                    // Ensure this code runs on the main thread
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                veterinarios.clear();
                                veterinarios.addAll(vets);
                                veterinarios.sort(Comparator.comparing((Veterinario v) -> v.getApellidosVeterinario().toLowerCase()));
                                adapter.notifyDataSetChanged();
                                Log.d("ConsultaVeterinario", "Veterinarios list updated");

                            }
                        });
                    }
                    else {
                        Log.d("ConsultaVeterinario", "No veterinarios data received");
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnNuevoVeterinario.getId()) {
            Log.d("ConsultaVeterinario", "Showing AltaVeterinario dialog");
            altaVeterinario = new AltaVeterinario();
            altaVeterinario.setCancelable(false);
            altaVeterinario.show(getParentFragmentManager(), "AltaVeterinario");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // ordenar por el primer apellido de veterinario
        if (spinnerOrdenarVeterinarios.getSelectedItemPosition() == 0) {
            veterinarios.sort(Comparator.comparing((Veterinario v) -> v.getApellidosVeterinario().toLowerCase()));
            adapter.notifyDataSetChanged();
        }
        // ordenar por la especialidad
        else if (spinnerOrdenarVeterinarios.getSelectedItemPosition() == 1) {
            veterinarios.sort(Comparator.comparing((Veterinario v) -> v.getEspecialidadVeterinario().toLowerCase()));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}