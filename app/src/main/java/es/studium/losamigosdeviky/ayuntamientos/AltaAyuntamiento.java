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
                        String nombreAyuntamiento = editTextNombreAyuntamiento.getText().toString();
                        String telefonoAyuntamientoStr = editTextTelefonoAyuntamiento.getText().toString();
                        String responsableAyuntamiento = editTextResponsableAyuntamiento.getText().toString();
                        String direccionAyuntamiento = editTextDireccionAyuntamiento.getText().toString();
                        String cpAyuntamientoStr = editTextCpAyuntamiento.getText().toString();

                        if (nombreAyuntamiento.isEmpty() || telefonoAyuntamientoStr.isEmpty() ||
                                responsableAyuntamiento.isEmpty() || direccionAyuntamiento.isEmpty() ||
                                cpAyuntamientoStr.isEmpty()) {
                            Toast.makeText(context, "Rellena todos los campos.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Validate and parse integers
                        int telefonoAyuntamiento;
                        int cpAyuntamiento;
                        try {
                            telefonoAyuntamiento = Integer.parseInt(telefonoAyuntamientoStr);
                            cpAyuntamiento = Integer.parseInt(cpAyuntamientoStr);
                        } catch (NumberFormatException e) {
                            Toast.makeText(context, "Introduce valores válidos para el teléfono y el código postal.", Toast.LENGTH_SHORT).show();
                            return;
                        }

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
                                        Toast.makeText(context, "La operación se ha realizado correctamente.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Send result
                                        if (isAdded()) {
                                            sendResult(false);
                                        }
                                        Toast.makeText(context, "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT).show();
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
            Log.d("AltaAyuntamiento", "Fragment is added, sending result: " + success);
            getParentFragmentManager().setFragmentResult("altaAyuntamientoRequestKey", result);
        } else {
            Log.d("AltaAyuntamiento", "Fragment not added, result not sent");
        }
    }
}