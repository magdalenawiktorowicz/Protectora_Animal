package es.studium.losamigosdeviky.colonias;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.studium.losamigosdeviky.BDConexion;
import es.studium.losamigosdeviky.R;
import es.studium.losamigosdeviky.RecyclerViewOnItemClickListener;
import es.studium.losamigosdeviky.ayuntamientos.Ayuntamiento;
import es.studium.losamigosdeviky.ayuntamientos.AyuntamientoCallback;
import es.studium.losamigosdeviky.protectoras.Protectora;
import es.studium.losamigosdeviky.protectoras.ProtectoraCallback;

public class ColoniasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView nombreColonia;
    private final TextView direccionColonia;
    private final TextView coordinadasGeograficasColonia;
    private final TextView ayuntamientoFKColonia;
    private final TextView protectoraFKColonia;
    private final RecyclerViewOnItemClickListener listener;
    private Context contexto;

    public ColoniasViewHolder(@NonNull View itemView, @NonNull RecyclerViewOnItemClickListener listener) {
        super(itemView);
        contexto = itemView.getContext();
        nombreColonia = itemView.findViewById(R.id.textViewNombreColonia);
        direccionColonia = itemView.findViewById(R.id.textViewDireccionColonia);
        coordinadasGeograficasColonia = itemView.findViewById(R.id.textViewCoordinadasGeograficasColonia);
        ayuntamientoFKColonia = itemView.findViewById(R.id.textViewAyuntamientoFKColonia);
        protectoraFKColonia = itemView.findViewById(R.id.textViewProtectoraFKColonia);

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

    public void bindRow(@NonNull Colonia colonia) {
        final Colonia currentColonia = colonia;

        BDConexion.consultarAyuntamientos(colonia.getIdAyuntamientoFK1(), new AyuntamientoCallback() {
            @Override
            public void onResult(ArrayList<Ayuntamiento> ayuntamientos) {
                if (!ayuntamientos.isEmpty()) {
                    Ayuntamiento ayuntamiento = ayuntamientos.get(0);
                    String ayuntamientoData = ayuntamiento.getNombreAyuntamiento() + "\n\t\t\t" +
                            ayuntamiento.getResponsableAyuntamiento() + "\n\t\t\t" +
                            ayuntamiento.getTelefonoAyuntamiento();

                    // Update the UI with the Ayuntamiento data
                    updateUIWithAyuntamientoData(currentColonia, ayuntamientoData);
                }
            }
        });

        BDConexion.consultarProtectoras(colonia.getIdProtectoraFK2(), new ProtectoraCallback() {
            @Override
            public void onResult(ArrayList<Protectora> protectoras) {
                if (!protectoras.isEmpty()) {
                    Protectora protectora = protectoras.get(0);
                    String protectoraData = protectora.getNombreProtectora() + "\n\t\t\t" +
                            protectora.getCorreoProtectora() + "\n\t\t\t" +
                            protectora.getTelefonoProtectora();

                    // Update the UI with the Protectora data
                    updateUIWithProtectoraData(currentColonia, protectoraData);
                }
            }
        });

        nombreColonia.setText(colonia.getNombreColonia());
        direccionColonia.setText(contexto.getResources().getString(R.string.direccion) + " " + colonia.getDireccionColonia() + ", " + colonia.getCpColonia());
        coordinadasGeograficasColonia.setText(contexto.getResources().getString(R.string.coordinadas) + " " + colonia.getLatitudColonia() + ", " + colonia.getLongitudColonia());
        ayuntamientoFKColonia.setText(contexto.getResources().getString(R.string.ayuntamientoFK) + "...");
        protectoraFKColonia.setText(contexto.getResources().getString(R.string.protectoraFK) + "...");
    }

    private void updateUIWithAyuntamientoData(Colonia colonia, String ayuntamientoData) {
        ayuntamientoFKColonia.setText(contexto.getResources().getString(R.string.ayuntamientoFK) + " " + ayuntamientoData);
    }

    private void updateUIWithProtectoraData(Colonia colonia, String protectoraData) {
        protectoraFKColonia.setText(contexto.getResources().getString(R.string.protectoraFK) + " " + protectoraData);
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAbsoluteAdapterPosition());
    }
}
