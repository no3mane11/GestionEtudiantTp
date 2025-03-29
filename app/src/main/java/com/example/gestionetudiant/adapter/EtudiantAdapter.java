package com.example.gestionetudiant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.gestionetudiant.R;
import com.example.gestionetudiant.model.Etudiant;
import java.util.ArrayList;
import java.util.List;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder> {
    private final Context context;
    private List<Etudiant> etudiants;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Etudiant etudiant);
    }

    public EtudiantAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.etudiants = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public EtudiantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_etudiant, parent, false);
        return new EtudiantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EtudiantViewHolder holder, int position) {
        Etudiant etudiant = etudiants.get(position);
        holder.bind(etudiant, listener);
    }

    @Override
    public int getItemCount() {
        return etudiants.size();
    }

    public void updateList(List<Etudiant> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new EtudiantDiffCallback(etudiants, newList));
        etudiants.clear();
        etudiants.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    static class EtudiantViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNomPrenom;
        private final TextView tvDateNaissance;
        private final ImageView imgProfil;

        public EtudiantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomPrenom = itemView.findViewById(R.id.tvNomPrenom);
            tvDateNaissance = itemView.findViewById(R.id.tvDateNaissance);
            imgProfil = itemView.findViewById(R.id.imgProfil);
        }

        public void bind(final Etudiant etudiant, final OnItemClickListener listener) {
            // Afficher le nom et prénom
            tvNomPrenom.setText(String.format("%s %s", etudiant.getNom(), etudiant.getPrenom()));

            // Afficher la date de naissance avec le format
            String dateNaissance = etudiant.getDateNaissance() != null ? etudiant.getDateNaissance() : "Non spécifiée";
            tvDateNaissance.setText(itemView.getContext().getString(R.string.date_naissance_format, dateNaissance));

            // Charger l'image avec Glide
            Glide.with(itemView.getContext())
                    .load(etudiant.getPhotoUri())
                    .placeholder(R.drawable.default_profile)
                    .error(R.drawable.default_profile)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .circleCrop()
                    .into(imgProfil);

            // Gestion du clic sur l'item
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(etudiant);
                }
            });
        }
    }

    static class EtudiantDiffCallback extends DiffUtil.Callback {
        private final List<Etudiant> oldList;
        private final List<Etudiant> newList;

        public EtudiantDiffCallback(List<Etudiant> oldList, List<Etudiant> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }
}