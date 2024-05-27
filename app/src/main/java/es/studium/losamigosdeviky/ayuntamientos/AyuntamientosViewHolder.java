package es.studium.losamigosdeviky.ayuntamientos;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import es.studium.losamigosdeviky.R;
import es.studium.losamigosdeviky.RecyclerViewOnItemClickListener;

public class AyuntamientosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView nombreAyuntamiento;
    private final TextView telefonoAyuntamiento;
    private final TextView responsableAyuntamiento;
    private final TextView direccionAyuntamiento;
    private final RecyclerViewOnItemClickListener listener;
    private Context contexto;

    public AyuntamientosViewHolder(@NonNull View itemView, @NonNull RecyclerViewOnItemClickListener listener) {
        super(itemView);
        contexto = itemView.getContext();
        nombreAyuntamiento = itemView.findViewById(R.id.textViewNombreAyuntamiento);
        telefonoAyuntamiento = itemView.findViewById(R.id.textViewTelefonoAyuntamiento);
        responsableAyuntamiento = itemView.findViewById(R.id.textViewResponsableAyuntamiento);
        direccionAyuntamiento = itemView.findViewById(R.id.textViewDireccionAyuntamiento);
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

    public void bindRow(@NonNull Ayuntamiento ayuntamiento) {
        nombreAyuntamiento.setText(ayuntamiento.getNombreAyuntamiento());
        telefonoAyuntamiento.setText(contexto.getResources().getString(R.string.telefono) + " " + String.valueOf(ayuntamiento.getTelefonoAyuntamiento()));
        responsableAyuntamiento.setText(contexto.getResources().getString(R.string.responsable) + " " +ayuntamiento.getResponsableAyuntamiento());
        direccionAyuntamiento.setText(contexto.getResources().getString(R.string.direccion) + " " +ayuntamiento.getDireccionAyuntamiento() + ", " + ayuntamiento.getCpAyuntamiento());
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAbsoluteAdapterPosition());
    }
}
