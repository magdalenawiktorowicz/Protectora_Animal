package es.studium.losamigosdeviky.protectoras;

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

public class ModificacionProtectora extends DialogFragment {
    EditText editTextNombreProtectora, editTextDireccionProtectora, editTextLocalidadProtectora, editTextTelefonoProtectora, editTextCorreoProtectora;
    Protectora protectora;
    Toast toast;

    public ModificacionProtectora(Protectora protectora) {
        this.protectora = protectora;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_modificacion_protectora, null);
        Context context = v.getContext();
        editTextNombreProtectora = v.findViewById(R.id.editTextModificacionNombreProtectora);
        editTextNombreProtectora.setText(protectora.getNombreProtectora());
        editTextDireccionProtectora = v.findViewById(R.id.editTextModificacionDireccionProtectora);
        editTextDireccionProtectora.setText(protectora.getDireccionProtectora());
        editTextLocalidadProtectora = v.findViewById(R.id.editTextModificacionLocalidadProtectora);
        editTextLocalidadProtectora.setText(protectora.getLocalidadProtectora());
        editTextTelefonoProtectora = v.findViewById(R.id.editTextModificacionTelefonoProtectora);
        editTextTelefonoProtectora.setText(String.valueOf(protectora.getTelefonoProtectora()));
        editTextCorreoProtectora = v.findViewById(R.id.editTextModificacionCorreoProtectora);
        editTextCorreoProtectora.setText(protectora.getCorreoProtectora());

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
                        String nombreProtectoraNuevo = editTextNombreProtectora.getText().toString();
                        String direccionProtectoraNuevo = editTextDireccionProtectora.getText().toString();
                        String localidadProtectoraNuevo = editTextLocalidadProtectora.getText().toString();
                        String telefonoProtectoraNuevoStr = editTextTelefonoProtectora.getText().toString();
                        String correoProtectoraNuevo = editTextCorreoProtectora.getText().toString();

                        if (nombreProtectoraNuevo.isEmpty() || direccionProtectoraNuevo.isEmpty() || localidadProtectoraNuevo.isEmpty() || telefonoProtectoraNuevoStr.isEmpty() || correoProtectoraNuevo.isEmpty()) {
                            toast = Toast.makeText(context, "Rellena todos los campos.", Toast.LENGTH_SHORT);
                            makeToast();
                            return;
                        }

                        int telefonoProtectoraNuevo;
                        try {
                            telefonoProtectoraNuevo = Integer.parseInt(telefonoProtectoraNuevoStr);
                        } catch (NumberFormatException e) {
                            toast = Toast.makeText(context, "Introduce valores válidos para el código postal.", Toast.LENGTH_SHORT);
                            makeToast();
                            return;
                        }

                        protectora.setNombreProtectora(nombreProtectoraNuevo);
                        protectora.setDireccionProtectora(direccionProtectoraNuevo);
                        protectora.setLocalidadProtectora(localidadProtectoraNuevo);
                        protectora.setTelefonoProtectora(telefonoProtectoraNuevo);
                        protectora.setCorreoProtectora(correoProtectoraNuevo);

                        // REALIZAR LA MODIFICACIÓN + INFORMAR SOBRE EL RESULTADO
                        BDConexion.modificarProtectora(protectora, new Callback() {
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

    private void makeToast() {
        View toastView = toast.getView();
        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
        toastMessage.setTextAppearance(R.style.ToastStyle);
        toastView.setBackground(getResources().getDrawable(R.drawable.toast_shape));
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void sendResult(boolean success) {
        Bundle result = new Bundle();
        result.putBoolean("operationSuccess", success);
        if (isAdded()) {
            Log.d("ModificacionProtectora", "Fragment is added, sending result: " + success);
            getParentFragmentManager().setFragmentResult("modificacionProtectoraRequestKey", result);
        } else {
            Log.d("ModificacionProtectora", "Fragment not added, result not sent");
        }
    }
}























