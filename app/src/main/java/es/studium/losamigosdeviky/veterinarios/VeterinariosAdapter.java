package es.studium.losamigosdeviky.veterinarios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.studium.losamigosdeviky.R;
import es.studium.losamigosdeviky.RecyclerViewOnItemClickListener;

public class VeterinariosAdapter extends RecyclerView.Adapter<VeterinariosViewHolder> {
    private List<Veterinario> data;
    private final RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    public VeterinariosAdapter(@NonNull List<Veterinario> data, @NonNull RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.data = data;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
    }
    @NonNull
    @Override
    public VeterinariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_veterinarios, parent, false);
        return new VeterinariosViewHolder(row, recyclerViewOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VeterinariosViewHolder holder, int position) {
        holder.bindRow(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
