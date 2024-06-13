package es.studium.losamigosdeviky.colonias;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.studium.losamigosdeviky.BDConexion;
import es.studium.losamigosdeviky.R;
import es.studium.losamigosdeviky.ayuntamientos.Ayuntamiento;
import es.studium.losamigosdeviky.ayuntamientos.AyuntamientoCallback;
import es.studium.losamigosdeviky.protectoras.Protectora;
import es.studium.losamigosdeviky.protectoras.ProtectoraCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AltaColonia extends DialogFragment implements AdapterView.OnItemSelectedListener {
    EditText editTextNombreColonia, editTextCpColonia, editTextLatitudColonia, editTextLongitudColonia, editTextDireccionColonia;
    Spinner spinnerAyuntamientoFKColonia, spinnerProtectoraFKColonia;
    private List<Ayuntamiento> ayuntamientos = new ArrayList<>();
    private List<Protectora> protectoras = new ArrayList<>();
    Toast toast;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_alta_colonia, null);
        Context context = v.getContext();
        editTextNombreColonia = v.findViewById(R.id.editTextAltaNombreColonia);
        editTextCpColonia = v.findViewById(R.id.editTextAltaCpColonia);
        editTextLatitudColonia = v.findViewById(R.id.editTextAltaLatitudColonia);
        editTextLongitudColonia = v.findViewById(R.id.editTextAltaLongitudColonia);
        editTextDireccionColonia = v.findViewById(R.id.editTextAltaDireccionColonia);

        spinnerAyuntamientoFKColonia = v.findViewById(R.id.spinnerAltaAyuntamientoFKColonia);
        spinnerProtectoraFKColonia = v.findViewById(R.id.spinnerAltaProtectoraFKColonia);

        // Set up Ayuntamiento Spinner
        BDConexion.consultarAyuntamientos(new AyuntamientoCallback() {
            @Override
            public void onResult(ArrayList<Ayuntamiento> ays) {
                if (ays != null && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        ayuntamientos.addAll(ays);
                        List<String> spinnerArrayAyuntamientos = new ArrayList<>();
                        spinnerArrayAyuntamientos.add("Selecciona el ayuntamiento...");
                        for (Ayuntamiento a : ayuntamientos) {
                            spinnerArrayAyuntamientos.add(a.getNombreAyuntamiento());
                        }
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerArrayAyuntamientos);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        spinnerAyuntamientoFKColonia.setAdapter(spinnerArrayAdapter);
                    });
                }
            }
        });

        // Set up Protectora Spinner
        BDConexion.consultarProtectoras(new ProtectoraCallback() {
            @Override
            public void onResult(ArrayList<Protectora> pros) {
                if (pros != null && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        protectoras.addAll(pros);
                        List<String> spinnerArrayProtectoras = new ArrayList<>();
                        spinnerArrayProtectoras.add("Selecciona la protectora...");
                        for (Protectora p : protectoras) {
                            spinnerArrayProtectoras.add(p.getNombreProtectora());
                        }
                        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerArrayProtectoras);
                        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        spinnerProtectoraFKColonia.setAdapter(spinnerArrayAdapter2);
                    });
                }
            }
        });

        spinnerAyuntamientoFKColonia.setOnItemSelectedListener(this);
        spinnerProtectoraFKColonia.setOnItemSelectedListener(this);

        builder.setView(v)
                .setPositiveButton(R.string.aceptar, null)
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.background);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nombreColonia = editTextNombreColonia.getText().toString();
                        String cpColoniaStr = editTextCpColonia.getText().toString();
                        String latitudColonia = editTextLatitudColonia.getText().toString();
                        String longitudColonia = editTextLongitudColonia.getText().toString();
                        String direccionColonia = editTextDireccionColonia.getText().toString();
                        String ayuntamientoNombre = spinnerAyuntamientoFKColonia.getSelectedItem().toString();
                        String protectoraNombre = spinnerProtectoraFKColonia.getSelectedItem().toString();
                        int ayuntamientoFK = ayuntamientos.stream()
                                .filter(ay -> ay.getNombreAyuntamiento().equals(ayuntamientoNombre))
                                .map(Ayuntamiento::getIdAyuntamiento)
                                .findFirst()
                                .orElse(-1);
                        int protectoraFK = protectoras.stream()
                                .filter(pr -> pr.getNombreProtectora().equals(protectoraNombre))
                                .map(Protectora::getIdProtectora)
                                .findFirst()
                                .orElse(-1);

                        if (ayuntamientoFK == -1 || protectoraFK == -1 || nombreColonia.isEmpty() || cpColoniaStr.isEmpty() || latitudColonia.isEmpty() || longitudColonia.isEmpty() || direccionColonia.isEmpty()) {
                            toast = Toast.makeText(context, "Rellena todos los campos.", Toast.LENGTH_SHORT);
                            makeToast();
                            return;
                        }

                        int cpColonia;
                        try {
                            cpColonia = Integer.parseInt(cpColoniaStr);
                        } catch (NumberFormatException e) {
                            toast = Toast.makeText(context, "Introduce valores válidos para el código postal.", Toast.LENGTH_SHORT);
                            makeToast();
                            return;
                        }

                        // DAR DE ALTA + INFORMAR SOBRE EL RESULTADO
                        BDConexion.anadirColonia(new Colonia(nombreColonia, cpColonia, latitudColonia, longitudColonia, direccionColonia, ayuntamientoFK, protectoraFK), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    toast = Toast.makeText(context, "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT);
                                    makeToast();
                                    // Send result
                                    if (isAdded()) {
                                        sendResult(false);
                                    }
                                    alertDialog.dismiss();
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    if (response.code() == 200) {
                                        if (isAdded()) {
                                            sendResult(true);
                                        }
                                        toast = Toast.makeText(context, "La operación se ha realizado correctamente.", Toast.LENGTH_SHORT);
                                        makeToast();
                                    } else {
                                        // Send result
                                        if (isAdded()) {
                                            sendResult(false);
                                        }
                                        toast = Toast.makeText(context, "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT);
                                        makeToast();
                                    }
                                    alertDialog.dismiss();
                                });
                            }
                        });
                    }
                });
            }
        });

        return alertDialog;
    }

    private void sendResult(boolean success) {
        Bundle result = new Bundle();
        result.putBoolean("operationSuccess", success);
        if (isAdded()) {
            getParentFragmentManager().setFragmentResult("altaColoniaRequestKey", result);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}