package es.studium.losamigosdeviky.ayuntamientos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import es.studium.losamigosdeviky.BDConexion;
import es.studium.losamigosdeviky.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BorradoAyuntamiento extends DialogFragment implements View.OnClickListener {

    Ayuntamiento ayuntamiento;
    private TextView textViewMensajeConfirmacion;
    private Button btnSi, btnNo;
    private Context context;
    public BorradoAyuntamiento(Ayuntamiento ayuntamiento) {
        this.ayuntamiento = ayuntamiento;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_borrado_ayuntamiento, null);
        context = v.getContext();
        textViewMensajeConfirmacion = v.findViewById(R.id.textViewConfirmacionBorradoAyuntamiento);
        textViewMensajeConfirmacion.setText(textViewMensajeConfirmacion.getText() + ayuntamiento.getNombreAyuntamiento());
        btnSi = v.findViewById(R.id.btnSiBorradoAyuntamiento);
        btnSi.setOnClickListener(this);
        btnNo = v.findViewById(R.id.btnNoBorradoAyuntamiento);
        btnNo.setOnClickListener(this);
        builder.setView(v);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.background);
        return alertDialog;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == btnNo.getId()) {
            dismiss();
        } else if (v.getId() == btnSi.getId()) {
            BDConexion.borrarAyuntamiento(ayuntamiento, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT).show();
                        // Send result
                        if (isAdded()) {
                            sendResult(false);
                        }
                        dismiss();
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
                        dismiss();
                    });
                }
            });
        }
    }

    private void sendResult(boolean success) {
        Bundle result = new Bundle();
        result.putBoolean("operationSuccess", success);
        if (isAdded()) {
            Log.d("BorradoAyuntamiento", "Fragment is added, sending result: " + success);
            getParentFragmentManager().setFragmentResult("borradoAyuntamientoRequestKey", result);
        } else {
            Log.d("BorradoAyuntamiento", "Fragment not added, result not sent");
        }
    }
}