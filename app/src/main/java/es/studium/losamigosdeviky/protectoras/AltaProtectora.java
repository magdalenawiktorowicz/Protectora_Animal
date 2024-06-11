package es.studium.losamigosdeviky.protectoras;

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

public class AltaProtectora extends DialogFragment {

    EditText editTextNombreProtectora, editTextDireccionProtectora, editTextLocalidadProtectora, editTextTelefonoProtectora, editTextCorreoProtectora;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_alta_protectora, null);
        Context context = v.getContext();
        editTextNombreProtectora = v.findViewById(R.id.editTextAltaNombreProtectora);
        editTextDireccionProtectora = v.findViewById(R.id.editTextAltaDireccionProtectora);
        editTextLocalidadProtectora = v.findViewById(R.id.editTextAltaLocalidadProtectora);
        editTextTelefonoProtectora = v.findViewById(R.id.editTextAltaTelefonoProtectora);
        editTextCorreoProtectora = v.findViewById(R.id.editTextAltaCorreoProtectora);

        builder.setView(v)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!(editTextNombreProtectora.getText().toString().isBlank()) &&
                                !(editTextDireccionProtectora.getText().toString().isBlank()) &&
                                !(editTextLocalidadProtectora.getText().toString().isBlank()) &&
                                !(editTextTelefonoProtectora.getText().toString().isBlank()) &&
                                !(editTextCorreoProtectora.getText().toString().isBlank())) {

                            String nombreProtectora = editTextNombreProtectora.getText().toString();
                            String direccionProtectora = editTextDireccionProtectora.getText().toString();
                            String localidadProtectora = editTextLocalidadProtectora.getText().toString();
                            int telefonoProtectora = Integer.parseInt(editTextTelefonoProtectora.getText().toString());
                            String correoProtectora = editTextCorreoProtectora.getText().toString();

                            // DAR DE ALTA + INFORMAR SOBRE EL RESULTADO
                            BDConexion.anadirProtectora(new Protectora(nombreProtectora, direccionProtectora, localidadProtectora, telefonoProtectora, correoProtectora), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    new Handler(Looper.getMainLooper()).post(() -> {
                                        Toast.makeText(context, "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT).show();
                                        // Send result
                                        if (isAdded()) {
                                            sendResult(false);
                                        }
                                        dialog.dismiss();
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
                                        dialog.dismiss();
                                    });
                                }
                            });
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
            Log.d("AltaProtectora", "Fragment is added, sending result: " + success);
            getParentFragmentManager().setFragmentResult("altaProtectoraRequestKey", result);
        } else {
            Log.d("AltaProtectora", "Fragment not added, result not sent");
        }
    }
}