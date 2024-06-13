package es.studium.losamigosdeviky.protectoras;

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
public class BorradoProtectora extends DialogFragment implements View.OnClickListener {
    Protectora protectora;
    private TextView textViewMensajeConfirmacion;
    private Button btnSi, btnNo;
    private Context context;
    Toast toast;

    public BorradoProtectora(Protectora protectora) {
        this.protectora = protectora;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_borrado_protectora, null);
        context = v.getContext();
        textViewMensajeConfirmacion = v.findViewById(R.id.textViewConfirmacionBorradoProtectora);
        textViewMensajeConfirmacion.setText(textViewMensajeConfirmacion.getText() + protectora.getNombreProtectora());
        btnSi = v.findViewById(R.id.btnSiBorradoProtectora);
        btnSi.setOnClickListener(this);
        btnNo = v.findViewById(R.id.btnNoBorradoProtectora);
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
            BDConexion.borrarProtectora(protectora, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        toast = Toast.makeText(context, "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT);
                        makeToast();
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
            Log.d("BorradoProtectora", "Fragment is added, sending result: " + success);
            getParentFragmentManager().setFragmentResult("borradoProtectoraRequestKey", result);
        } else {
            Log.d("BorradoProtectora", "Fragment not added, result not sent");
        }
    }
}