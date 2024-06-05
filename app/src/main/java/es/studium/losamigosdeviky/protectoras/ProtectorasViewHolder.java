package es.studium.losamigosdeviky.protectoras;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import es.studium.losamigosdeviky.R;
import es.studium.losamigosdeviky.RecyclerViewOnItemClickListener;

public class ProtectorasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView nombreProtectora;
    private final TextView direccionProtectora;
    private final TextView telefonoProtectora;
    private final TextView correoProtectora;
    private final RecyclerViewOnItemClickListener listener;
    private Context contexto;

    public ProtectorasViewHolder(@NonNull View itemView, @NonNull RecyclerViewOnItemClickListener listener) {
        super(itemView);
        contexto = itemView.getContext();
        nombreProtectora = itemView.findViewById(R.id.textViewNombreProtectora);
        direccionProtectora = itemView.findViewById(R.id.textViewDireccionProtectora);
        telefonoProtectora = itemView.findViewById(R.id.textViewTelefonoProtectora);
        correoProtectora = itemView.findViewById(R.id.textViewCorreoProtectora);
        this.listener = listener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(v, getAbsoluteAdapterPosition());
                return true;
            }
        });
    }

    public void bindRow(@NonNull Protectora protectora) {
        nombreProtectora.setText(protectora.getNombreProtectora());
        direccionProtectora.setText(contexto.getResources().getString(R.string.direccion) + " " + protectora.getDireccionProtectora() + ", " + protectora.getLocalidadProtectora());
        telefonoProtectora.setText(contexto.getResources().getString(R.string.telefono) + " " + String.valueOf(protectora.getTelefonoProtectora()));
        correoProtectora.setText(contexto.getResources().getString(R.string.correo) + " " + protectora.getCorreoProtectora());
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAbsoluteAdapterPosition());
    }
}
