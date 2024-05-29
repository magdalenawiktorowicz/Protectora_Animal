package es.studium.losamigosdeviky.protectoras;

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

public class ConsultaProtectora extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    List<Protectora> protectoras = new ArrayList<>();
    RecyclerView recyclerView;
    Spinner spinnerOrdenarProtectoras;
    Button btnNuevaProtectora;
    ProtectorasAdapter adapter;
    FragmentManager fm;
    FragmentTransaction ft;
    //AltaProtectora altaProtectora;
    //ModificacionProtectora modificacionProtectora;
    //BorradoProtectora borradoProtectora;

    public ConsultaProtectora() {
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
        return inflater.inflate(R.layout.fragment_consulta_protectora, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("nombre de protectora");
        spinnerArray.add("localidad");
        spinnerOrdenarProtectoras = view.findViewById(R.id.spinnerOrdenarProtectoras);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerOrdenarProtectoras.setAdapter(spinnerArrayAdapter);
        spinnerOrdenarProtectoras.setOnItemSelectedListener(this);
        btnNuevaProtectora = view.findViewById(R.id.buttonNuevaProtectora);
        btnNuevaProtectora.setOnClickListener(this);
        fm = getActivity().getSupportFragmentManager();
        setUpRecyclerView(view);
        fetchProtectorasData();

        fm.setFragmentResultListener("altaProtectoraRequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                boolean success = result.getBoolean("operationSuccess");
                Log.d("ConsultaProtectora", "Fragment result received: " + success);
                if (success) {
                    Log.d("ConsultaProtectora", "Calling fetchProtectorasData after successful result");
                    fetchProtectorasData();
                }
            }
        });

        fm.setFragmentResultListener("modificacionProtectoraRequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                boolean success = result.getBoolean("operationSuccess");
                Log.d("ConsultaProtectora", "Fragment result received: " + success);
                if (success) {
                    Log.d("ConsultaProtectora", "Calling fetchProtectorasData after successful result");
                    fetchProtectorasData();
                }
            }
        });

        fm.setFragmentResultListener("borradoProtectoraRequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                boolean success = result.getBoolean("operationSuccess");
                Log.d("ConsultaProtectora", "Fragment result received: " + success);
                if (success) {
                    Log.d("ConsultaProtectora", "Calling fetchProtectorasData after successful result");
                    fetchProtectorasData();
                }
            }
        });

        // establecer el título en la barra superior
        if (getActivity() != null) {
            // establecer el color del fondo de la barra superior
            ((MainActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.pink)));
            // establecer el título de la barra superior
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.protectoras);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchProtectorasData();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fetchProtectorasData();
    }

    private void setUpRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewProtectoras);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new ProtectorasAdapter(protectoras, new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (MainActivity.tipoUsuario == 0) {
                    // fragment Modificación
                    Log.d("ConsultaProtectora", "Showing ModificacionProtectora dialog");
                    //modificacionProtectora = new ModificacionProtectora(protectoras.get(position));
                    //modificacionProtectora.setCancelable(false);
                    //modificacionProtectora.show(getParentFragmentManager(), "ModificacionProtectora");
                }
            }

            @Override
            public void onLongClick(View v, int position) {
                if (MainActivity.tipoUsuario == 0) {
                    // fragment Borrado
                    Log.d("ConsultaProtectora", "Showing BorradoProtectora dialog");
                    //borradoProtectora = new BorradoProtectora(protectoras.get(position));
                    //borradoProtectora.setCancelable(false);
                    //borradoProtectora.show(getParentFragmentManager(), "BorradoProtectora");
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void fetchProtectorasData() {
        Log.d("ConsultaProtectora", "Fetching protectoras data");
        BDConexion.consultarProtectoras(new ProtectoraCallback() {
            @Override
            public void onResult(final ArrayList<Protectora> pros) {
                if (pros != null) {
                    Log.d("ConsultaProtectora", "Data fetched: " + pros.size() + " protectoras");
                    // Ensure this code runs on the main thread
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                protectoras.clear();
                                protectoras.addAll(pros);
                                protectoras.sort(Comparator.comparing((Protectora p) -> p.getNombreProtectora().toLowerCase()));
                                adapter.notifyDataSetChanged();
                                Log.d("ConsultaProtectora", "Protectoras list updated");
                            }
                        });
                    }
                    else {
                        Log.d("ConsultaProtectora", "No protectoras data received");
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnNuevaProtectora.getId()) {
            Log.d("ConsultaProtectora", "Showing AltaProtectora dialog");
            //altaProtectora = new AltaProtectora();
            //altaProtectora.setCancelable(false);
            //altaProtectora.show(getParentFragmentManager(), "AltaProtectora");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // ordenar por el nombre de protectora
        if (spinnerOrdenarProtectoras.getSelectedItemPosition() == 0) {
            protectoras.sort(Comparator.comparing((Protectora p) -> p.getNombreProtectora().toLowerCase()));
            adapter.notifyDataSetChanged();
        }
        // ordenar por la localidad
        else if (spinnerOrdenarProtectoras.getSelectedItemPosition() == 1) {
            protectoras.sort(Comparator.comparing((Protectora p) -> p.getLocalidadProtectora().toLowerCase()));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}