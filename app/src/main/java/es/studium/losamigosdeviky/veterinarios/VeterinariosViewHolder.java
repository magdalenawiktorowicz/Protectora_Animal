package es.studium.losamigosdeviky.veterinarios;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import es.studium.losamigosdeviky.R;
import es.studium.losamigosdeviky.RecyclerViewOnItemClickListener;

public class VeterinariosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView nombreVeterinario;
    private final TextView telefonoVeterinario;
    private final TextView especialidadVeterinario;
    private final RecyclerViewOnItemClickListener listener;
    private Context contexto;

    public VeterinariosViewHolder(@NonNull View itemView, @NonNull RecyclerViewOnItemClickListener listener) {
        super(itemView);
        contexto = itemView.getContext();
        nombreVeterinario = itemView.findViewById(R.id.textViewNombreVeterinario);
        telefonoVeterinario = itemView.findViewById(R.id.textViewTelefonoVeterinario);
        especialidadVeterinario = itemView.findViewById(R.id.textViewEspecialidadVeterinario);
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

    public void bindRow(@NonNull Veterinario veterinario) {
        nombreVeterinario.setText(veterinario.getNombreVeterinario() + " " + veterinario.getApellidosVeterinario());
        telefonoVeterinario.setText(contexto.getResources().getString(R.string.telefono) + " " + String.valueOf(veterinario.getTelefonoVeterinario()));
        especialidadVeterinario.setText(contexto.getResources().getString(R.string.especialidad) + " " + veterinario.getEspecialidadVeterinario());
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAbsoluteAdapterPosition());
    }
}
