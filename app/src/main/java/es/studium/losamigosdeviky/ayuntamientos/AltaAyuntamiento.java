package es.studium.losamigosdeviky.ayuntamientos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.io.IOException;

import es.studium.losamigosdeviky.BDConexion;
import es.studium.losamigosdeviky.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AltaAyuntamiento extends DialogFragment {
    EditText editTextNombreAyuntamiento, editTextTelefonoAyuntamiento, editTextResponsableAyuntamiento, editTextDireccionAyuntamiento, editTextCpAyuntamiento;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_alta_ayuntamiento, null);
        Context context = v.getContext();
        editTextNombreAyuntamiento = v.findViewById(R.id.editTextAltaNombreAyuntamiento);
        editTextTelefonoAyuntamiento = v.findViewById(R.id.editTextAltaTelefonoAyuntamiento);
        editTextResponsableAyuntamiento = v.findViewById(R.id.editTextAltaResponsableAyuntamiento);
        editTextDireccionAyuntamiento = v.findViewById(R.id.editTextAltaDireccionAyuntamiento);
        editTextCpAyuntamiento = v.findViewById(R.id.editTextAltaCpAyuntamiento);

        builder.setView(v)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!(editTextNombreAyuntamiento.getText().toString().isBlank()) &&
                                !(editTextTelefonoAyuntamiento.getText().toString().isBlank()) &&
                                !(editTextResponsableAyuntamiento.getText().toString().isBlank()) &&
                                !(editTextDireccionAyuntamiento.getText().toString().isBlank()) &&
                                !(editTextCpAyuntamiento.getText().toString().isBlank())) {

                            String nombreAyuntamiento = editTextNombreAyuntamiento.getText().toString();
                            int telefonoAyuntamiento = Integer.parseInt(editTextTelefonoAyuntamiento.getText().toString());
                            String responsableAyuntamiento = editTextResponsableAyuntamiento.getText().toString();
                            String direccionAyuntamiento = editTextDireccionAyuntamiento.getText().toString();
                            int cpAyuntamiento = Integer.parseInt(editTextCpAyuntamiento.getText().toString());

                            // DAR DE ALTA + INFORMAR SOBRE EL RESULTADO
                            BDConexion.anadirAyuntamiento(new Ayuntamiento(nombreAyuntamiento, telefonoAyuntamiento, responsableAyuntamiento, direccionAyuntamiento, cpAyuntamiento), new Callback() {
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
                                            Toast.makeText(context, "La operación se ha realizado correctamente.", Toast.LENGTH_SHORT).show();
                                            if (isAdded()) {
                                                sendResult(true);
                                            }
                                        } else {
                                            Toast.makeText(context, "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT).show();
                                            // Send result
                                            if (isAdded()) {
                                                sendResult(false);
                                            }
                                        }
                                    });
                                }
                            });
                            dismiss();
                        } else {
                            Toast.makeText(context, "Rellena todos los campos.", Toast.LENGTH_SHORT).show();

                        }
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
            getParentFragmentManager().setFragmentResult("altaAyuntamientoRequestKey", result);
        }
    }
}
