package es.studium.losamigosdeviky.veterinarios;

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

public class AltaVeterinario extends DialogFragment {
    EditText editTextNombreVeterinario, editTextApellidosVeterinario, editTextTelefonoVeterinario, editTextEspecialidadVeterinario;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_alta_veterinario, null);
        Context context = v.getContext();
        editTextNombreVeterinario = v.findViewById(R.id.editTextAltaNombreVeterinario);
        editTextApellidosVeterinario = v.findViewById(R.id.editTextAltaApellidosVeterinario);
        editTextTelefonoVeterinario = v.findViewById(R.id.editTextAltaTelefonoVeterinario);
        editTextEspecialidadVeterinario = v.findViewById(R.id.editTextAltaEspecialidadVeterinario);

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
                        String nombreVeterinario = editTextNombreVeterinario.getText().toString();
                        String apellidosVeterinario = editTextApellidosVeterinario.getText().toString();
                        String telefonoVeterinarioStr = editTextTelefonoVeterinario.getText().toString();
                        String especialidadVeterinario = editTextEspecialidadVeterinario.getText().toString();

                        if (nombreVeterinario.isEmpty() || apellidosVeterinario.isEmpty() || telefonoVeterinarioStr.isEmpty() || especialidadVeterinario.isEmpty()) {
                            Toast.makeText(context, "Rellena todos los campos.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int telefonoVeterinario;
                        try {
                            telefonoVeterinario = Integer.parseInt(telefonoVeterinarioStr);
                        } catch (NumberFormatException e) {
                            Toast.makeText(context, "Introduce valores válidos para el teléfono.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // DAR DE ALTA + INFORMAR SOBRE EL RESULTADO
                        BDConexion.anadirVeterinario(new Veterinario(nombreVeterinario, apellidosVeterinario, telefonoVeterinario, especialidadVeterinario), new Callback() {
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
            Log.d("AltaVeterinario", "Fragment is added, sending result: " + success);
            getParentFragmentManager().setFragmentResult("altaVeterinarioRequestKey", result);
        } else {
            Log.d("AltaVeterinario", "Fragment not added, result not sent");
        }
    }
}