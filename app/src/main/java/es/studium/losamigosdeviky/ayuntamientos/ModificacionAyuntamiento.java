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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
    Toast toast;

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
                .setPositiveButton(R.string.aceptar, null)
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.background);

        // Override onClick for the positive button
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nombreAyuntamientoNuevo = editTextNombreAyuntamiento.getText().toString();
                        String telefonoAyuntamientoNuevoStr = editTextTelefonoAyuntamiento.getText().toString();
                        String responsableAyuntamientoNuevo = editTextResponsableAyuntamiento.getText().toString();
                        String direccionAyuntamientoNuevo = editTextDireccionAyuntamiento.getText().toString();
                        String cpAyuntamientoNuevoStr = editTextCpAyuntamiento.getText().toString();

                        // Check if any field is empty
                        if (nombreAyuntamientoNuevo.isEmpty() || telefonoAyuntamientoNuevoStr.isEmpty() ||
                                responsableAyuntamientoNuevo.isEmpty() || direccionAyuntamientoNuevo.isEmpty() ||
                                cpAyuntamientoNuevoStr.isEmpty()) {
                            toast = Toast.makeText(context, "Rellena todos los campos.", Toast.LENGTH_SHORT);
                            makeToast();
                            return;
                        }

                        // Validate and parse integers
                        int telefonoAyuntamientoNuevo;
                        int cpAyuntamientoNuevo;
                        try {
                            telefonoAyuntamientoNuevo = Integer.parseInt(telefonoAyuntamientoNuevoStr);
                            cpAyuntamientoNuevo = Integer.parseInt(cpAyuntamientoNuevoStr);
                        } catch (NumberFormatException e) {
                            toast = Toast.makeText(context, "Introduce valores válidos para el teléfono y el código postal.", Toast.LENGTH_SHORT);
                            makeToast();
                            return;
                        }

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
            Log.d("ModificacionAyuntamiento", "Fragment is added, sending result: " + success);
            getParentFragmentManager().setFragmentResult("modificacionAyuntamientoRequestKey", result);
        } else {
            Log.d("ModificacionAyuntamiento", "Fragment not added, result not sent");
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
}