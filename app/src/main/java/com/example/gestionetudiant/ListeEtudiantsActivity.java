package com.example.gestionetudiant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.gestionetudiant.adapter.EtudiantAdapter;
import com.example.gestionetudiant.model.Etudiant;
import com.example.gestionetudiant.viewmodel.EtudiantViewModel;

public class ListeEtudiantsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EtudiantAdapter etudiantAdapter;
    private EtudiantViewModel etudiantViewModel;
    private TextView tvEmptyView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_etudiants);

        initViews();
        setupRecyclerView();
        setupViewModel();
        setupSwipeRefresh();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewEtudiants);
        tvEmptyView = findViewById(R.id.tvEmptyView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        etudiantAdapter = new EtudiantAdapter(this, this::openEtudiantDetails);
        recyclerView.setAdapter(etudiantAdapter);
    }

    private void setupViewModel() {
        etudiantViewModel = new ViewModelProvider(this).get(EtudiantViewModel.class);
        observeEtudiants();
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            etudiantViewModel.refreshEtudiants();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void observeEtudiants() {
        etudiantViewModel.getAllEtudiants().observe(this, etudiants -> {
            if (etudiants != null) {
                etudiantAdapter.updateList(etudiants);
                updateEmptyView(etudiants.isEmpty());
            }
        });
    }

    private void updateEmptyView(boolean isEmpty) {
        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        tvEmptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    private void openEtudiantDetails(Etudiant etudiant) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("ETUDIANT_ID", etudiant.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        etudiantViewModel.refreshEtudiants();
    }
}