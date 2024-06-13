package es.studium.losamigosdeviky.veterinarios;

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

public class ModificacionVeterinario extends DialogFragment {
    EditText editTextNombreVeterinario, editTextApellidosVeterinario, editTextTelefonoVeterinario, editTextEspecialidadVeterinario;
    Veterinario veterinario;
    Toast toast;
    public ModificacionVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_modificacion_veterinario, null);
        Context context = v.getContext();
        editTextNombreVeterinario = v.findViewById(R.id.editTextModificacionNombreVeterinario);
        editTextNombreVeterinario.setText(veterinario.getNombreVeterinario());
        editTextApellidosVeterinario = v.findViewById(R.id.editTextModificacionApellidosVeterinario);
        editTextApellidosVeterinario.setText(veterinario.getApellidosVeterinario());
        editTextTelefonoVeterinario = v.findViewById(R.id.editTextModificacionTelefonoVeterinario);
        editTextTelefonoVeterinario.setText(String.valueOf(veterinario.getTelefonoVeterinario()));
        editTextEspecialidadVeterinario = v.findViewById(R.id.editTextModificacionEspecialidadVeterinario);
        editTextEspecialidadVeterinario.setText(veterinario.getEspecialidadVeterinario());

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
                        String nombreVeterinarioNuevo = editTextNombreVeterinario.getText().toString();
                        String apellidosVeterinarioNuevo = editTextApellidosVeterinario.getText().toString();
                        String telefonoVeterinarioNuevoStr = editTextTelefonoVeterinario.getText().toString();
                        String especialidadVeterinarioNuevo = editTextEspecialidadVeterinario.getText().toString();

                        if (nombreVeterinarioNuevo.isBlank() || apellidosVeterinarioNuevo.isBlank() || telefonoVeterinarioNuevoStr.isBlank() || especialidadVeterinarioNuevo.isBlank()) {
                            toast = Toast.makeText(context, "Rellena todos los campos.", Toast.LENGTH_SHORT);
                            makeToast();
                            return;
                        }

                        int telefonoVeterinarioNuevo;
                        try {
                            telefonoVeterinarioNuevo = Integer.parseInt(telefonoVeterinarioNuevoStr);
                        } catch (NumberFormatException e) {
                            toast = Toast.makeText(context, "Introduce valores válidos para el teléfono.", Toast.LENGTH_SHORT);
                            makeToast();
                            return;
                        }

                        veterinario.setNombreVeterinario(nombreVeterinarioNuevo);
                        veterinario.setApellidosVeterinario(apellidosVeterinarioNuevo);
                        veterinario.setTelefonoVeterinario(telefonoVeterinarioNuevo);
                        veterinario.setEspecialidadVeterinario(especialidadVeterinarioNuevo);

                        // REALIZAR LA MODIFICACIÓN + INFORMAR SOBRE EL RESULTADO
                        BDConexion.modificarVeterinario(veterinario, new Callback() {
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
            Log.d("ModificacionVeterinario", "Fragment is added, sending result: " + success);
            getParentFragmentManager().setFragmentResult("modificacionVeterinarioRequestKey", result);
        } else {
            Log.d("ModificacionVeterinario", "Fragment not added, result not sent");
        }
    }
}