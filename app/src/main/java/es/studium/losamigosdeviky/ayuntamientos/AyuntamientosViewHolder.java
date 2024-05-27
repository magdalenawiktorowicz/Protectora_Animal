package es.studium.losamigosdeviky.ayuntamientos;

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

    public AyuntamientosViewHolder(@NonNull View itemView, @NonNull RecyclerViewOnItemClickListener listener) {
        super(itemView);
        nombreAyuntamiento = itemView.findViewById(R.id.textViewNombreAyuntamiento);
        telefonoAyuntamiento = itemView.findViewById(R.id.textViewTelefonoAyuntamiento);
        responsableAyuntamiento = itemView.findViewById(R.id.textViewResponsableAyuntamiento);
        direccionAyuntamiento = itemView.findViewById(R.id.textViewDireccionAyuntamiento);
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    public void bindRow(@NonNull Ayuntamiento ayuntamiento) {
        nombreAyuntamiento.setText(ayuntamiento.getNombreAyuntamiento());
        telefonoAyuntamiento.setText(String.valueOf(ayuntamiento.getTelefonoAyuntamiento()));
        responsableAyuntamiento.setText(ayuntamiento.getResponsableAyuntamiento());
        direccionAyuntamiento.setText(ayuntamiento.getDireccionAyuntamiento() + ", " + ayuntamiento.getCpAyuntamiento());
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAbsoluteAdapterPosition());
    }
}
