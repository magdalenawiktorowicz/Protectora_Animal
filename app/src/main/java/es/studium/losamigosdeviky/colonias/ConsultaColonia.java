package es.studium.losamigosdeviky.colonias;

import android.content.Context;
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
import android.util.Log;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import es.studium.losamigosdeviky.BDConexion;
import es.studium.losamigosdeviky.MainActivity;
import es.studium.losamigosdeviky.R;
import es.studium.losamigosdeviky.RecyclerViewOnItemClickListener;

public class ConsultaColonia extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    List<Colonia> colonias = new ArrayList<>();
    RecyclerView recyclerView;
    Spinner spinnerOrdenarColonias;
    Button btnNuevaColonia;
    ColoniasAdapter adapter;
    FragmentManager fm;
    FragmentTransaction ft;
    AltaColonia altaColonia;
    ModificacionColonia modificacionColonia;
    BorradoColonia borradoColonia;

    public ConsultaColonia() {
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
        return inflater.inflate(R.layout.fragment_consulta_colonia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("nombre de colonia");
        spinnerArray.add("localización");
        spinnerArray.add("ayuntamiento");
        spinnerArray.add("protectora");
        spinnerOrdenarColonias = view.findViewById(R.id.spinnerOrdenarColonias);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerOrdenarColonias.setAdapter(spinnerArrayAdapter);
        spinnerOrdenarColonias.setOnItemSelectedListener(this);
        btnNuevaColonia = view.findViewById(R.id.buttonNuevoColonia);
        btnNuevaColonia.setOnClickListener(this);
        fm = getActivity().getSupportFragmentManager();
        setUpRecyclerView(view);
        fetchColoniasData();

        fm.setFragmentResultListener("altaColoniaRequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                boolean success = result.getBoolean("operationSuccess");
                Log.d("ConsultaColonia", "Fragment result received: " + success);
                if (success) {
                    Log.d("ConsultaColonia", "Calling fetchColoniasData after successful result");
                    fetchColoniasData();
                }
            }
        });

        fm.setFragmentResultListener("modificacionColoniaRequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                boolean success = result.getBoolean("operationSuccess");
                Log.d("ConsultaColonia", "Fragment result received: " + success);
                if (success) {
                    Log.d("ConsultaColonia", "Calling fetchColoniasData after successful result");
                    fetchColoniasData();
                }
            }
        });

        fm.setFragmentResultListener("borradoColoniaRequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                boolean success = result.getBoolean("operationSuccess");
                Log.d("ConsultaColonia", "Fragment result received: " + success);
                if (success) {
                    Log.d("ConsultaColonia", "Calling fetchColoniasData after successful result");
                    fetchColoniasData();
                }
            }
        });

        // establecer el título en la barra superior
        if (getActivity() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.colonias);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchColoniasData();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fetchColoniasData();
    }

    private void setUpRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewColonias);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new ColoniasAdapter(colonias, new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (MainActivity.tipoUsuario == 0) {
                    // fragment Modificación
                    Log.d("ConsultaColonia", "Showing ModificacionColonia dialog");
                    modificacionColonia = new ModificacionColonia(colonias.get(position));
                    modificacionColonia.setCancelable(false);
                    modificacionColonia.show(getParentFragmentManager(), "ModificacionColonia");
                }
            }

            @Override
            public void onLongClick(View v, int position) {
                if (MainActivity.tipoUsuario == 0) {
                    // fragment Borrado
                    Log.d("ConsultaColonia", "Showing BorradoColonia dialog");
                    borradoColonia = new BorradoColonia(colonias.get(position));
                    borradoColonia.setCancelable(false);
                    borradoColonia.show(getParentFragmentManager(), "BorradoColonia");
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void fetchColoniasData() {
        Log.d("ConsultaColonia", "Fetching Colonias data");
        BDConexion.consultarColonias(new ColoniaCallback() {
            @Override
            public void onResult(final ArrayList<Colonia> cols) {
                if (cols != null) {
                    Log.d("ConsultaColonia", "Data fetched: " + cols.size() + " colonias");
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                colonias.clear();
                                colonias.addAll(cols);
                                colonias.sort(Comparator.comparing((Colonia c) -> c.getNombreColonia().toLowerCase()));
                                adapter.notifyDataSetChanged();
                                Log.d("ConsultaColonia", "Colonias list updated");

                            }
                        });
                    }
                    else {
                        Log.d("ConsultaColonia", "No colonias data received");
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnNuevaColonia.getId()) {
            Log.d("ConsultaColonia", "Showing AltaColonia dialog");
            altaColonia = new AltaColonia();
            altaColonia.setCancelable(false);
            altaColonia.show(getParentFragmentManager(), "AltaColonia");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // ordenar por el nombre de ayuntamiento
        if (spinnerOrdenarColonias.getSelectedItemPosition() == 0) {
            colonias.sort(Comparator.comparing((Colonia c) -> c.getNombreColonia().toLowerCase()));
            adapter.notifyDataSetChanged();
        }
        // ordenar por la localización
        else if(spinnerOrdenarColonias.getSelectedItemPosition() == 1) {
            colonias.sort(Comparator.comparing((Colonia c) -> c.getCpColonia()));
            adapter.notifyDataSetChanged();
        }
        // ordenar por el ayuntamiento
        else if (spinnerOrdenarColonias.getSelectedItemPosition() == 2) {
            colonias.sort(Comparator.comparing((Colonia c) -> c.getIdAyuntamientoFK1()));
            adapter.notifyDataSetChanged();
        }
        // ordenar por la protectora
        else if (spinnerOrdenarColonias.getSelectedItemPosition() == 3) {
            colonias.sort(Comparator.comparing((Colonia c) -> c.getIdProtectoraFK2()));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}