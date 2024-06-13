package es.studium.losamigosdeviky.cuidados;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.studium.losamigosdeviky.BDConexion;
import es.studium.losamigosdeviky.R;
import es.studium.losamigosdeviky.gatos.Gato;
import es.studium.losamigosdeviky.gatos.GatoCallback;
import es.studium.losamigosdeviky.veterinarios.Veterinario;
import es.studium.losamigosdeviky.veterinarios.VeterinarioCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AltaCuidado extends DialogFragment implements AdapterView.OnItemSelectedListener {
    EditText editTextFechaInicioCuidado, editTextFechaFinCuidado, editTextDescripcionCuidado, editTextPosologiaCuidado;
    Spinner spinnerGatoFKCuidado, spinnerVeterinarioFKCuidado;
    private List<Gato> gatos = new ArrayList<>();
    private List<Veterinario> veterinarios = new ArrayList<>();
    Toast toast;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_alta_cuidado, null);
        Context context = v.getContext();
        editTextFechaInicioCuidado = v.findViewById(R.id.editTextAltaFechaInicioCuidado);
        editTextFechaFinCuidado = v.findViewById(R.id.editTextAltaFechaFinCuidado);
        editTextDescripcionCuidado = v.findViewById(R.id.editTextAltaDescripcionCuidado);
        editTextPosologiaCuidado = v.findViewById(R.id.editTextAltaPosologiaCuidado);
        spinnerGatoFKCuidado = v.findViewById(R.id.spinnerAltaGatoFKCuidado);
        spinnerVeterinarioFKCuidado = v.findViewById(R.id.spinnerAltaVeterinarioFKCuidado);

        // Set up Gato Spinner
        BDConexion.consultarGatos(new GatoCallback() {
            @Override
            public void onResult(ArrayList<Gato> gs) {
                if (gs != null && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        gatos.addAll(gs);
                        List<String> spinnerArrayGatos = new ArrayList<>();
                        spinnerArrayGatos.add("Selecciona el gato...");
                        for (Gato g : gatos) {
                            spinnerArrayGatos.add(g.getNombreGato());
                        }
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerArrayGatos);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        spinnerGatoFKCuidado.setAdapter(spinnerArrayAdapter);
                    });
                }
            }
        });

        // Set up Veterinario Spinner
        BDConexion.consultarVeterinarios(new VeterinarioCallback() {
            @Override
            public void onResult(ArrayList<Veterinario> vets) {
                if (vets != null && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        veterinarios.addAll(vets);
                        List<String> spinnerArrayVeterinarios = new ArrayList<>();
                        spinnerArrayVeterinarios.add("Selecciona el veterinario...");
                        for (Veterinario v : veterinarios) {
                            spinnerArrayVeterinarios.add(v.getNombreVeterinario() + " " + v.getApellidosVeterinario());
                        }
                        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerArrayVeterinarios);
                        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        spinnerVeterinarioFKCuidado.setAdapter(spinnerArrayAdapter2);
                    });
                }
            }
        });
        spinnerGatoFKCuidado.setOnItemSelectedListener(this);
        spinnerVeterinarioFKCuidado.setOnItemSelectedListener(this);

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
                        String fechaInicioStr = editTextFechaInicioCuidado.getText().toString();
                        String fechaFinStr = editTextFechaFinCuidado.getText().toString();
                        String descripcionCuidado = editTextDescripcionCuidado.getText().toString();
                        String posologiaCuidado = editTextPosologiaCuidado.getText().toString();

                        if (fechaInicioStr.isBlank() || fechaFinStr.isBlank() || descripcionCuidado.isBlank() || posologiaCuidado.isBlank() || (spinnerGatoFKCuidado.getSelectedItemPosition() == 0) || (spinnerVeterinarioFKCuidado.getSelectedItemPosition() == 0)) {
                            toast = Toast.makeText(context, "Rellena todos los campos.", Toast.LENGTH_SHORT);
                            makeToast();
                            return;
                        }

                        if (!comprobarFecha(fechaInicioStr) || !comprobarFecha(fechaFinStr)) {
                            toast = Toast.makeText(context, "Fechas incorrectas.", Toast.LENGTH_SHORT);
                            makeToast();
                            return;
                        }

                        String[] fi = fechaInicioStr.split("-");
                        LocalDate fechaInicioCuidado = LocalDate.of(Integer.parseInt(fi[0]), Integer.parseInt(fi[1]), Integer.parseInt(fi[2]));
                        String[] ff = fechaFinStr.split("-");
                        LocalDate fechaFinCuidado = LocalDate.of(Integer.parseInt(ff[0]), Integer.parseInt(ff[1]), Integer.parseInt(ff[2]));

                        String nombreGato = spinnerGatoFKCuidado.getSelectedItem().toString();
                        int gatoFKCuidado = gatos.stream()
                                .filter(g -> g.getNombreGato().equals(nombreGato))
                                .map(Gato::getIdGato)
                                .findFirst()
                                .orElse(-1);

                        String nombreVeterinario = spinnerVeterinarioFKCuidado.getSelectedItem().toString();
                        int veterinarioFKCuidado = veterinarios.stream()
                                .filter(ve -> (ve.getNombreVeterinario() + " " + ve.getApellidosVeterinario()).equals(nombreVeterinario))
                                .map(Veterinario::getIdVeterinario)
                                .findFirst()
                                .orElse(-1);

                        Cuidado cuidado = new Cuidado(fechaInicioCuidado, fechaFinCuidado, descripcionCuidado, posologiaCuidado, gatoFKCuidado, veterinarioFKCuidado);

                        // DAR DE ALTA + INFORMAR SOBRE EL RESULTADO
                        BDConexion.anadirCuidado(cuidado, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    toast = Toast.makeText(context, "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT);
                                    makeToast();
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

    private boolean comprobarFecha(String string) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(string);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void sendResult(boolean success) {
        Bundle result = new Bundle();
        result.putBoolean("operationSuccess", success);
        if (isAdded()) {
            getParentFragmentManager().setFragmentResult("altaCuidadoRequestKey", result);
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