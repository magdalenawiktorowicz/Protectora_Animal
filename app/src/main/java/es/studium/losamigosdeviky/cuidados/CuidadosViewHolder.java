package es.studium.losamigosdeviky.cuidados;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.studium.losamigosdeviky.BDConexion;
import es.studium.losamigosdeviky.R;
import es.studium.losamigosdeviky.RecyclerViewOnItemClickListener;
import es.studium.losamigosdeviky.gatos.Gato;
import es.studium.losamigosdeviky.gatos.GatoCallback;
import es.studium.losamigosdeviky.veterinarios.Veterinario;
import es.studium.losamigosdeviky.veterinarios.VeterinarioCallback;


public class CuidadosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView numeroCuidado;
    private final TextView veterinarioFKCuidado;
    private final TextView gatoFKCuidado;
    private final TextView fechaInicioCuidado;
    private final TextView fechaFinCuidado;
    private final TextView descripcionCuidado;
    private final TextView posologiaCuidado;

    private final RecyclerViewOnItemClickListener listener;
    private Context contexto;

    public CuidadosViewHolder(@NonNull View itemView, @NonNull RecyclerViewOnItemClickListener listener) {
        super(itemView);
        contexto = itemView.getContext();
        numeroCuidado = itemView.findViewById(R.id.textViewNumeroCuidado);
        veterinarioFKCuidado = itemView.findViewById(R.id.textViewVeterinarioCuidado);
        gatoFKCuidado = itemView.findViewById(R.id.textViewGatoCuidado);
        fechaInicioCuidado = itemView.findViewById(R.id.textViewFechaInicioCuidado);
        fechaFinCuidado = itemView.findViewById(R.id.textViewFechaFinCuidado);
        descripcionCuidado = itemView.findViewById(R.id.textViewDescripcionCuidado);
        posologiaCuidado = itemView.findViewById(R.id.textViewPosologiaCuidado);
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

    public void bindRow(@NonNull Cuidado cuidado) {
        final Cuidado currentCuidado = cuidado;

        numeroCuidado.setText(contexto.getResources().getString(R.string.cuidado) + " " + cuidado.getIdCuidado());
        fechaInicioCuidado.setText(contexto.getResources().getString(R.string.fechaInicio) + " " + cuidado.getFechaInicioCuidado());
        fechaFinCuidado.setText(contexto.getResources().getString(R.string.fechaFin) + " " + cuidado.getFechaFinCuidado());
        descripcionCuidado.setText(contexto.getResources().getString(R.string.descripcion) + " " + cuidado.getDescripcionCuidado());
        posologiaCuidado.setText(contexto.getResources().getString(R.string.posologia) + " " + cuidado.getPosologiaCuidado());

        BDConexion.consultarVeterinarios(cuidado.getIdVeterinarioFK(), new VeterinarioCallback() {
            @Override
            public void onResult(ArrayList<Veterinario> vets) {
                if (!vets.isEmpty()) {
                    Veterinario veterinario = vets.get(0);
                    String veterinarioData = veterinario.getNombreVeterinario() + " " + veterinario.getApellidosVeterinario() + "\n\t\t\t" + veterinario.getTelefonoVeterinario();

                    // Update the UI with the Veterinario data
                    updateUIWithVeterinarioData(currentCuidado, veterinarioData);
                }
            }
        });

        BDConexion.consultarGatos(cuidado.getIdGatoFK(), new GatoCallback() {
            @Override
            public void onResult(ArrayList<Gato> gatos) {
                if (!gatos.isEmpty()) {
                    Gato gato = gatos.get(0);
                    String gatoData = gato.getNombreGato() + "\n\t\t\t" + gato.getChipGato();

                    // Update the UI with the Gato data
                    updateUIWithGatoData(currentCuidado, gatoData);
                }
            }
        });
    }

    private void updateUIWithVeterinarioData(Cuidado cuidado, String veterinarioData) {
        veterinarioFKCuidado.setText(contexto.getResources().getString(R.string.veterinarioFK) + " " + veterinarioData);
    }

    private void updateUIWithGatoData(Cuidado cuidado, String gatoData) {
        gatoFKCuidado.setText(contexto.getResources().getString(R.string.gatoFK) + " " + gatoData);
    }


    @Override
    public void onClick(View v) {
        listener.onClick(v, getAbsoluteAdapterPosition());
    }
}
