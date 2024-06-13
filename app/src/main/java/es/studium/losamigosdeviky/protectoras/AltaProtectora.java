package es.studium.losamigosdeviky.protectoras;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
    Toast toast;

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
                        String nombreProtectora = editTextNombreProtectora.getText().toString();
                        String direccionProtectora = editTextDireccionProtectora.getText().toString();
                        String localidadProtectora = editTextLocalidadProtectora.getText().toString();
                        String telefonoProtectoraStr = editTextTelefonoProtectora.getText().toString();
                        String correoProtectora = editTextCorreoProtectora.getText().toString();

                        if (nombreProtectora.isBlank() || direccionProtectora.isBlank() || localidadProtectora.isBlank() || telefonoProtectoraStr.isBlank() || correoProtectora.isBlank()) {
                            toast = Toast.makeText(context, "Rellena todos los campos.", Toast.LENGTH_SHORT);
                            makeToast();
                            return;
                        }

                        int telefonoProtectora;
                        try {
                            telefonoProtectora = Integer.parseInt(telefonoProtectoraStr);
                        } catch (NumberFormatException e) {
                            toast = Toast.makeText(context, "Introduce valores válidos para el código postal.", Toast.LENGTH_SHORT);
                            makeToast();
                            return;
                        }

                        // DAR DE ALTA + INFORMAR SOBRE EL RESULTADO
                        BDConexion.anadirProtectora(new Protectora(nombreProtectora, direccionProtectora, localidadProtectora, telefonoProtectora, correoProtectora), new Callback() {
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
            Log.d("AltaProtectora", "Fragment is added, sending result: " + success);
            getParentFragmentManager().setFragmentResult("altaProtectoraRequestKey", result);
        } else {
            Log.d("AltaProtectora", "Fragment not added, result not sent");
        }
    }
}