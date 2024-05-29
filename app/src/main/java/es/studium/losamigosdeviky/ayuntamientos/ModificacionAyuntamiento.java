package es.studium.losamigosdeviky.ayuntamientos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import es.studium.losamigosdeviky.BDConexion;
import es.studium.losamigosdeviky.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ModificacionAyuntamiento extends DialogFragment {
    EditText editTextNombreAyuntamiento, editTextTelefonoAyuntamiento, editTextResponsableAyuntamiento, editTextDireccionAyuntamiento, editTextCpAyuntamiento;
    Ayuntamiento ayuntamiento;

    public ModificacionAyuntamiento(Ayuntamiento ayuntamiento) {
        this.ayuntamiento = ayuntamiento;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_modificacion_ayuntamiento, null);
        Context context = v.getContext();
        editTextNombreAyuntamiento = v.findViewById(R.id.editTextModificacionNombreAyuntamiento);
        editTextNombreAyuntamiento.setText(ayuntamiento.getNombreAyuntamiento());
        editTextTelefonoAyuntamiento = v.findViewById(R.id.editTextModificacionTelefonoAyuntamiento);
        editTextTelefonoAyuntamiento.setText(String.valueOf(ayuntamiento.getTelefonoAyuntamiento()));
        editTextResponsableAyuntamiento = v.findViewById(R.id.editTextModificacionResponsableAyuntamiento);
        editTextResponsableAyuntamiento.setText(ayuntamiento.getResponsableAyuntamiento());
        editTextDireccionAyuntamiento = v.findViewById(R.id.editTextModificacionDireccionAyuntamiento);
        editTextDireccionAyuntamiento.setText(ayuntamiento.getDireccionAyuntamiento());
        editTextCpAyuntamiento = v.findViewById(R.id.editTextModificacionCpAyuntamiento);
        editTextCpAyuntamiento.setText(String.valueOf(ayuntamiento.getCpAyuntamiento()));

        builder.setView(v)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nombreAyuntamientoNuevo = editTextNombreAyuntamiento.getText().toString();
                        int telefonoAyuntamientoNuevo = Integer.parseInt(editTextTelefonoAyuntamiento.getText().toString());
                        String responsableAyuntamientoNuevo = editTextResponsableAyuntamiento.getText().toString();
                        String direccionAyuntamientoNuevo = editTextDireccionAyuntamiento.getText().toString();
                        int cpAyuntamientoNuevo = Integer.parseInt(editTextCpAyuntamiento.getText().toString());

                        if (!(nombreAyuntamientoNuevo.isBlank()) &&
                                !(String.valueOf(telefonoAyuntamientoNuevo).isBlank()) &&
                                !(responsableAyuntamientoNuevo.isBlank()) &&
                                !(direccionAyuntamientoNuevo.isBlank()) &&
                                !(String.valueOf(cpAyuntamientoNuevo).isBlank())) {

                            ayuntamiento.setNombreAyuntamiento(nombreAyuntamientoNuevo);
                            ayuntamiento.setTelefonoAyuntamiento(telefonoAyuntamientoNuevo);
                            ayuntamiento.setResponsableAyuntamiento(responsableAyuntamientoNuevo);
                            ayuntamiento.setDireccionAyuntamiento(direccionAyuntamientoNuevo);
                            ayuntamiento.setCpAyuntamiento(cpAyuntamientoNuevo);

                            // REALIZAR LA MODIFICACIÓN + INFORMAR SOBRE EL RESULTADO
                            BDConexion.modificarAyuntamiento(ayuntamiento, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    new Handler(Looper.getMainLooper()).post(() -> {
                                        Toast.makeText(context, "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT).show();
                                        // Send result
                                        if (isAdded()) {
                                            sendResult(false);
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    new Handler(Looper.getMainLooper()).post(() -> {
                                        if (response.code() == 200) {
                                            if (isAdded()) {
                                                sendResult(true);
                                            }
                                            Toast.makeText(context, "La operación se ha realizado correctamente.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Send result
                                            if (isAdded()) {
                                                sendResult(false);
                                            }
                                            Toast.makeText(context, "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        } else {
                            Toast.makeText(context, "Rellena todos los campos.", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.background);
        return alertDialog;
    }

    private void sendResult(boolean success) {
        Bundle result = new Bundle();
        result.putBoolean("operationSuccess", success);
        if (isAdded()) {
            Log.d("ModificacionAyuntamiento", "Fragment is added, sending result: " + success);
            getParentFragmentManager().setFragmentResult("modificacionAyuntamientoRequestKey", result);
        } else {
            Log.d("ModificacionAyuntamiento", "Fragment not added, result not sent");
        }
    }
}