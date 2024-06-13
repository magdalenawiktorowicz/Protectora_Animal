package es.studium.losamigosdeviky.gatos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import es.studium.losamigosdeviky.BDConexion;
import es.studium.losamigosdeviky.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class BorradoGato extends DialogFragment implements View.OnClickListener {

    Gato gato;
    private TextView textViewMensajeConfirmacion;
    private Button btnSi, btnNo;
    private Context context;
    Toast toast;

    public BorradoGato(Gato gato) {
        this.gato = gato;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_borrado_gato, null);
        context = v.getContext();
        textViewMensajeConfirmacion = v.findViewById(R.id.textViewConfirmacionBorradoGato);
        textViewMensajeConfirmacion.setText(textViewMensajeConfirmacion.getText() + gato.getNombreGato());
        btnSi = v.findViewById(R.id.btnSiBorradoGato);
        btnSi.setOnClickListener(this);
        btnNo = v.findViewById(R.id.btnNoBorradoGato);
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
            BDConexion.borrarGato(gato, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        toast = Toast.makeText(context, "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT);
                        makeToast();
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
            Log.d("BorradoGato", "Fragment is added, sending result: " + success);
            getParentFragmentManager().setFragmentResult("borradoGatoRequestKey", result);
        } else {
            Log.d("BorradoGato", "Fragment not added, result not sent");
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